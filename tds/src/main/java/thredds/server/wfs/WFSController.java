package thredds.server.wfs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import thredds.core.TdsRequestedDataset;

import java.io.File;
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
	 * Processes getCapabilities requests.
	 * 
	 * @param out
	 * @return
	 */
	private void getCapabilities(PrintWriter out) {
		WFSDataWriter gcdw = new WFSDataWriter(out);
		gcdw.startXML();
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
					getCapabilities(wr);
				}
			}
			
			else{
				wr.append("ncWFS on THREDDS");
			}
		}
		
		catch(IOException io) {
			
		}
	}
}
