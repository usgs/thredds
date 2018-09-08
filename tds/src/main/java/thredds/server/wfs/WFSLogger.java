package thredds.server.wfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A logger specifically used for logging events related to the WFS servlet.
 * 
 * @author wchen@usgs.gov
 *
 */
public class WFSLogger {

	static private final Logger wfsLog = LoggerFactory.getLogger(WFSController.class);
	
	/**
	 * Gets the WFS SLF4J logger
	 * 
	 * @return the WFS SLF4J Logger
	 */
	static public Logger getLogger() {
		return wfsLog;
	}
	
	/**
	 * Log an error to the WFS log
	 * 
	 * @param error message
	 */
	static public void logError(String error) {
		if(error != null) wfsLog.error(error);
	}
	
	/**
	 * Log some general information to the WFS log
	 * 
	 * @param info message
	 */
	static public void logInfo(String info) {
		if(info != null) wfsLog.info(info);
	}
}
