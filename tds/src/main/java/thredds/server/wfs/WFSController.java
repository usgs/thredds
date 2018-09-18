package thredds.server.wfs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ucar.nc2.Variable;
import ucar.nc2.Attribute;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Controller for WFS Simple Geometry Web Service
 * 
 * @author wchen@usgs.gov
 *
 */
@Controller
@RequestMapping("/wfs")
public class WFSController extends HttpServlet {

	/**
	 * Processes GetCapabilities requests.
	 * 
	 * @param out
	 * @return
	 */
	private void getCapabilities(PrintWriter out, HttpServletRequest hsreq) {
		WFSGetCapabilitiesWriter gcdw = new WFSGetCapabilitiesWriter(out);
		gcdw.startXML();
		gcdw.setServer(hsreq.getScheme() + "://" + hsreq.getServerName() + ":" + hsreq.getServerPort() + "/thredds/wfs");
		gcdw.addOperation(WFSRequestType.GetCapabilities); gcdw.addOperation(WFSRequestType.DescribeFeatureType); gcdw.addOperation(WFSRequestType.GetFeature);
		gcdw.writeOperations();
		gcdw.addFeature(new WFSFeature("hru_soil_moist", "HRU Soil Moisture"));
		gcdw.writeFeatureTypes();
		gcdw.finishXML();
	}


	private void describeFeatureType(PrintWriter out, HttpServletRequest hsreq) {
		WFSDescribeFeatureTypeWriter dftw = new WFSDescribeFeatureTypeWriter(out);
		dftw.startXML();
		dftw.setServer(hsreq.getScheme() + "://" + hsreq.getServerName() + ":" + hsreq.getServerPort() + "/thredds/wfs");
		try {

			NetcdfDataset data = NetcdfDataset.openDataset("../cdm/src/test/data/dataset/SimpleGeos/hru_soil_moist_3hru_5timestep.nc");
			Variable hru_test = data.findVariable("hru_soil_moist");

			ArrayList<WFSFeatureAttribute> attributes = new ArrayList<>();

			for (Attribute attr : hru_test.getAttributes()){
				attributes.add(new WFSFeatureAttribute(attr.getShortName(), attr.getDataType().toString())); //short name vs long name?
			}

			dftw.addFeature(new WFSFeature("hru_soil_moist", "HRU Soil Moisture", "simplegeom",attributes));
			dftw.writeFeatures();
			dftw.finishXML();

		}
		catch(IOException e) {
		}
	}

	
	/**
	 * Checks request parameters for errors.
	 * Will send back an XML Exception if any errors are encountered.
	 * 
	 * @param request parameter value
	 * @param version parameter value
	 * @param service parameter value
	 * @return an ExceptionWriter if any errors occurred or null if none occurred
	 */
	private WFSExceptionWriter checkParametersForError(String request, String version, String service) {
		// The SERVICE parameter is required. If not specified, is an error (throw exception through XML).
		if(service != null) {
			// For the WFS servlet it must be WFS if not, write out an InvalidParameterValue exception.
			if(!service.equalsIgnoreCase("WFS")) {
					return new WFSExceptionWriter("WFS Server error. SERVICE parameter must be of value WFS.", "service", "InvalidParameterValue");
			}
			
		}
					
		else {
			return new WFSExceptionWriter("WFS server error. SERVICE parameter is required.", "request", "MissingParameterValue");
		}
		
		// The REQUEST Parameter is required. If not specified, is an error (throw exception through XML).
		if(request != null) {
			
			// Only go through version checks if NOT a Get Capabilities request, the VERSION parameter is required for all operations EXCEPT GetCapabilities section 7.6.25 of WFS 2.0 Interface Standard
			if(!request.equalsIgnoreCase(WFSRequestType.GetCapabilities.toString())) {
			
				if(version != null ) {
					// If the version is not failed report exception VersionNegotiationFailed, from OGC Web Services Common Standard section 7.4.1
					
					// Get each part
					String[] versionParts = version.split("\\.");
					
						for(int ind = 0; ind < versionParts.length; ind++) {
						
						// Check if number will throw NumberFormatException if not.
						try {
							Integer.valueOf(versionParts[ind]);
						}
						
						/* Version parameters are only allowed to consist of numbers and periods. If this is not the case then
						 * It qualifies for InvalidParameterException
						 */
						catch (NumberFormatException excep){
							return new WFSExceptionWriter("WFS server error. VERSION parameter consists of invalid characters.", "version", "InvalidParameterValue");
						}
					}
					
					
					/* Now the version parts are all constructed from the parameter
					 * Analyze for correctness. 
					 */
					boolean validVersion = false;
					
					
					// If just number 2 is specified, assume 2.0.0, pass the check
					if(versionParts.length == 1) if(versionParts[0].equals("2")) validVersion = true;
					
					// Two version parts specified, make sure it's 2.0
					if(versionParts.length >= 2) if(versionParts[0].equals("2") && versionParts[1].equals("0")) validVersion = true;
					
					/* Another exception VersionNegotiationFailed is specified by OGC Web Services Common
					 * for version mismatches. If the version check failed print this exception
					 */
					if(!validVersion){
						return new WFSExceptionWriter("WFS Server error. Version requested is not supported.", null, "VersionNegotiationFailed");
					}
				}
		
				else {
					return new WFSExceptionWriter("WFS server error. VERSION parameter is required.", "request", "MissingParameterValue");

				}
			}
			
			WFSRequestType reqToProc = WFSRequestType.getWFSRequestType(request);
			if(reqToProc == null) return new WFSExceptionWriter("WFS server error. REQUEST parameter is not valid. Possible values: GetCapabilities, "
					+ "DescribeFeatureType, GetFeature", "request", "InvalidParameterValue");
			
		}
		
		else{
			return new WFSExceptionWriter("WFS server error. REQUEST parameter is required.", "request", "MissingParameterValue");
		}
		
		return null;
	}
	
	/**
	 * A handler for WFS based HTTP requests that sends to other request handlers
	 * to handle the request.
	 *
	 * Servlet Path: /wfs/{request}
	 *
	 */
	@RequestMapping("**")
	public void httpHandler(HttpServletRequest hsreq, HttpServletResponse hsres) {
		try {
			
			PrintWriter wr = hsres.getWriter();
			List<String> paramNames = new LinkedList<String>();
			Enumeration<String> paramNamesE = hsreq.getParameterNames();
			while(paramNamesE.hasMoreElements()) paramNames.add(paramNamesE.nextElement());
			
			// Prepare parameters
			String request = null;
			String version = null;
			String service = null;
			
			/* Look for parameter names to assign values
			 * in order to avoid casing issues with parameter names (such as a mismatch between reQUEST and request and REQUEST).
			 */
			for(String paramName : paramNames) {
				if(paramName.equalsIgnoreCase("REQUEST")) {
					request = hsreq.getParameter(paramName);
				}
				
				if(paramName.equalsIgnoreCase("VERSION")) {
					version = hsreq.getParameter(paramName);
				}
				
				
				if(paramName.equalsIgnoreCase("SERVICE")) {
					service = hsreq.getParameter(paramName);
				}
			}
			
			WFSExceptionWriter paramError = checkParametersForError(request, version, service);
			
			// If parameter checks all pass launch the request
			if(paramError == null) {
				
				WFSRequestType reqToProc = WFSRequestType.getWFSRequestType(request);
				
				switch(reqToProc) {
					case GetCapabilities:
						getCapabilities(wr, hsreq);
					break;
					
					case DescribeFeatureType:
						describeFeatureType(wr, hsreq);
						
					break;
					
					case GetFeature:
						
					break;
					
					default:
				}	
				
			}
			
			// Parameter checks did not all pass, print the error and return
			else {
				paramError.write(hsres);
				return;
			}
		}
		
		catch(IOException io) {
			throw new RuntimeException("ERROR: retrieval of writer failed", io);
		}
	}
}
