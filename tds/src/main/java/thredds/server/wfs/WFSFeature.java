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

	public String getName() {
		return name;
	}
	public String getTitle() {
		return title;
	}
	public String getType() { return type; }
	public ArrayList<WFSFeatureAttribute> getAttributes() {return attributes;}
	
	/**
	 * Creates a new WFSFeature of the given name and type.
	 * 
	 * @param name - name of the feature
	 * @param title - title of the feature
	 */
	public WFSFeature(String name, String title) {
		this(name, title, null, null);
	}

	public WFSFeature(String name, String title, String type, ArrayList<WFSFeatureAttribute> attributes) {
		this.name = name;
		this.title = title;
		this.type = type;
		this.attributes = attributes;
	}

	public void addAttribute(WFSFeatureAttribute attribute) {
		attributes.add(attribute);
	}
}
