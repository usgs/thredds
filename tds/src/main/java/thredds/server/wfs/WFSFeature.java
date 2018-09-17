package thredds.server.wfs;

/**
 * A simple container for a WFS Feature Type
 * 
 * @author wchen@usgs.gov
 *
 */
public class WFSFeature {
	private final String name;
	private final String title;
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
	public String getTitle() {
		return title;
	}
	
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
		this.name = name;
		this.title = title;
		fileDSName = null;
	}
}
