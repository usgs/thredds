package ucar.nc2.ft2.coverage.simpgeometry;


import java.util.List;
import ucar.ma2.Array;

/**
 * Generic interface for a Simple Geometry Polygon.
 * 
 * @author wchen@usgs.gov
 *
 */
public interface Polygon {

	/**
	 * Get the list of points which constitute the polygon.
	 * 
	 * @return points
	 */
	public List<CFPoint> getPoints();

	/**
	 * Get the data associated with this Polygon
	 * 
	 * @return data
	 */
	public Array getData();
	
	/**
	 * Get the next polygon in the sequence of multi-polygons
	 * 
	 * @return next polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getNext();
	
	/**
	 * Get the previous polygon in the sequence of multi-polygons
	 * 
	 * @return previous polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getPrev();
	
	/**
	 * Get this polygon's interior ring.
	 * 
	 * @return previous interior ring as a polygon if any, otherwise null
	 */
	public CFPolygon getInteriorRing();
	
	/**
	 * Add a point to this polygon's points list
	 * 
	 */
	public void addPoint(double x, double y);
	
	/**
	 * Set the data associated with this Polygon
	 * 
	 * @param data - array of data to set to
	 */
	public void setData(Array data);
	
	/**
	 * Sets the next polygon which make up the multipolygon which this polygon is a part of.
	 * Automatically connects the other polygon to this polygon as well.
	 */
	public void setNext(CFPolygon next);
	
	/**
	 * Sets the previous polygon which makes up the multipolygon which this polygon is a part of.
	 * Automatically connect the other polygon to this polygon as well.
	 */
	public void setPrev(CFPolygon prev);
	
	/**
	 *  Simply sets the interior ring of the polygon.
	 * 
	 */
	public void setInteriorRing(CFPolygon interior);

}
