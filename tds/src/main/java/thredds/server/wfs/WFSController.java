package thredds.server.wfs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import thredds.server.wfs.WFSRequestType;

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
			
			// The SERVICE parameter is required. If not specified, is an error (throw exception through XML).
			if(service != null) {

				// For the WFS servlet it must be WFS
				if(!service.equalsIgnoreCase("WFS")) {
					hsres.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
				
			}
			
			else {
				WFSExceptionWriter owsExcept = new WFSExceptionWriter("WFS server error. SERVICE parameter is required.", "request", "MissingParameterValue", hsres);
				owsExcept.write();
				return;
			}
			
			// The VERSION parameter is required for all operations EXCEPT GetCapabilities §7.6.25 of WFS 2.0 Interface Standard
			if(request != null){
				
				// Only go through version checks if NOT a Get Capabilities request
				if(!request.equalsIgnoreCase(WFSRequestType.GetCapabilities.toString())) {
				
					if(version != null ) {
						
					}
			
					else {
						WFSExceptionWriter owsExcept = new WFSExceptionWriter("WFS server error. VERSION parameter is required.", "request", "MissingParameterValue", hsres);
						owsExcept.write();
						return;
					}
				}
			}
			
			// The REQUEST Parameter is required. If not specified, is an error (throw exception through XML).
			if(request != null) {
				if(request.equalsIgnoreCase(WFSRequestType.GetCapabilities.toString())) {
					getCapabilities(wr, hsreq);
				}
			}
			
			else{
				WFSExceptionWriter owsExcept = new WFSExceptionWriter("WFS server error. REQUEST parameter is required.", "request", "MissingParameterValue", hsres);
				owsExcept.write();
				return;
			}
		}
		
		catch(IOException io) {
			throw new RuntimeException("ERROR: retrieval of writer failed", io);
		}
	}
}
