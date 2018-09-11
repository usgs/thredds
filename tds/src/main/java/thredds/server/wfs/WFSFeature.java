package thredds.server.wfs;

import java.util.ArrayList;

/**
 * A simple container for a WFS Feature Type
 * 
 * @author wchen@usgs.gov
 *
 */
public class WFSFeature {

	private final String name;
	private final String title;
	private final String type;
	private final ArrayList<WFSFeatureAttribute> attributes;
	private final int catchments_geometry_container;
	private final int hruid;
	private final double lat;
	private final double lon;
	private final double catchments_area;
	private final double catchments_perimeter;
	private final double catchments_veght;
	private final double catchments_cov;


	public String getName() {
		return name;
	}
	public String getTitle() {
		return title;
	}
	public String getType() { return type; }
	public ArrayList<WFSFeatureAttribute> getAttributes() {return attributes;}
	public int getCatchments_geometry_container() { return catchments_geometry_container; }
	public int getHruid() { return hruid; }
	public double getLat() { return lat; }
	public double getLon() { return lon; }
	public double getCatchments_area() { return catchments_area; }
	public double getCatchments_perimeter() { return catchments_perimeter; }
	public double getCatchments_veght() { return catchments_veght; }
	public double getCatchments_cov() { return catchments_cov; }
	
	/**
	 * Creates a new WFSFeature of the given name and type.
	 * 
	 * @param name - name of the feature
	 * @param title - title of the feature
	 */
	public WFSFeature(String name, String title) {
		this(name, title, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public WFSFeature(String name, String title, String type, ArrayList<WFSFeatureAttribute> attributes,
					  int catchments_geometry_container, int hruid, double lat, double lon, double catchments_area,
					  double catchments_perimeter, double catchments_veght, double catchments_cov) {
		this.name = name;
		this.title = title;
		this.type = type;
		this.attributes = attributes;
		this.catchments_geometry_container = catchments_geometry_container;
		this.hruid = hruid;
		this.lat = lat;
		this.lon = lon;
		this.catchments_area = catchments_area;
		this.catchments_perimeter = catchments_perimeter;
		this.catchments_veght = catchments_veght;
		this.catchments_cov = catchments_cov;
	}
}
