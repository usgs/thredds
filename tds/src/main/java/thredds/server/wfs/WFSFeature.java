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

	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
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
	}
}
