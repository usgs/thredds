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
<<<<<<< HEAD
	private String fileDSName;
	
	/**
	 * Gets the name of this WFS feature.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the title of this WFS feature.
	 * 
	 * @return
	 */
=======
	private final String type;
	private final ArrayList<WFSFeatureAttribute> attributes;

	public String getName() {
		return name;
	}
>>>>>>> ec4f5dcffd8b0e8b193aadf515d2d6a5d9b57c99
	public String getTitle() {
		return title;
	}
	public String getType() { return type; }
	public ArrayList<WFSFeatureAttribute> getAttributes() {return attributes;}
	
	/**
	 * Gets the file / dataset name from which the WFS feature was retrieved.
	 * 
	 * The File DS name is used for the namespace of the feature type.
	 * 
	 * @return
	 */
	public String getFileDSName() {
		return fileDSName;
	}
	
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
<<<<<<< HEAD
		fileDSName = null;
=======
		this.type = type;
		this.attributes = attributes;
	}

	public void addAttribute(WFSFeatureAttribute attribute) {
		attributes.add(attribute);
>>>>>>> ec4f5dcffd8b0e8b193aadf515d2d6a5d9b57c99
	}
}
