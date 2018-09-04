package thredds.server.wfs;

/**
 * Enum representing WFS Request Types as specified by the OGC.
 * 
 * @author wchen@usgs.gov
 *
 */
public enum WFSRequestType {
	
	GetCapabilities("GetCapabilities", 0),
	GetFeature("GetFeature", 1),
	DescribeFeatureType("DescribeFeatureType", 2);
	
	String requestType;
	int number;
	
	WFSRequestType(String requestType, int identifier){
		this.requestType = requestType;
		this.number = identifier;
	}
	
	public String toString() {
		return requestType;
	}
	
	public int toID()
	{
		return this.number;
	}
}
