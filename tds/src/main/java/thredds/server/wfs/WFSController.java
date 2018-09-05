package thredds.server.wfs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;

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
			String request = hsreq.getParameter("REQUEST");
			
			// 
			if(request != null) {
				if(request.equals(WFSRequestType.GetCapabilities.toString())) {
					getCapabilities(wr, hsreq);
				}
			}
			
			else{
				wr.append("ncWFS on THREDDS - ");
				wr.append(hsreq.getScheme() + "://" + hsreq.getServerName() + ":" + hsreq.getServerPort() + "/thredds/wfs");
			}
		}
		
		catch(IOException io) {
			
		}
	}
}
