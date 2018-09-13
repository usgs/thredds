package thredds.server.wfs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

	private void describeFeatureType(PrintWriter out, HttpServletRequest hsreq) {
		WFSDescribeFeatureTypeWriter dftw = new WFSDescribeFeatureTypeWriter(out);
		dftw.startXML();
		dftw.setServer(hsreq.getScheme() + "://" + hsreq.getServerName() + ":" + hsreq.getServerPort() + "/thredds/wfs");
		ArrayList<WFSFeatureAttribute> attributes = new ArrayList<>();
		attributes.add(new WFSFeatureAttribute("catchments_geometry_container", "int"));
		attributes.add(new WFSFeatureAttribute("hruid", "int"));
		attributes.add(new WFSFeatureAttribute("lat", "double"));
		attributes.add(new WFSFeatureAttribute("lon", "double"));
		attributes.add(new WFSFeatureAttribute("catchments_area", "double"));
		attributes.add(new WFSFeatureAttribute("catchments_perimeter", "double"));
		attributes.add(new WFSFeatureAttribute("catchments_veght", "double"));
		attributes.add(new WFSFeatureAttribute("catchments_cov", "double"));
		dftw.addFeature(new WFSFeature("hru_soil_moist", "HRU Soil Moisture", "simplegeom",attributes));
		dftw.writeFeatures();
		dftw.finishXML();
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
			String request = hsreq.getParameter("REQUEST");
			
			// The REQUEST Parameter is required. If not specified, is an error (throw exception through XML).
			if(request != null) {
				if(request.equals(WFSRequestType.GetCapabilities.toString())) {
					getCapabilities(wr, hsreq);
				}

				else if (request.equals(WFSRequestType.DescribeFeatureType.toString())) {
					describeFeatureType(wr, hsreq);
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
