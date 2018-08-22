package ucar.nc2.ft2.coverage.simpgeometry;


import java.util.List;
import ucar.ma2.Array;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Generic interface for a Simple Geometry Polygon.
 * 
 * @author wchen@usgs.gov
 *
 */
public interface Polygon extends SimpleGeometry{

	/**
	 * Get the list of points which constitute the polygon.
	 * 
	 * @return points Points which constitute the polygon
	 */
	public List<CFPoint> getPoints();

	/**
	 * Get the data associated with this Polygon
	 * 
	 * @return data Data series associated with the Polygon
	 */
	public Array getData();
	
	/**
	 * Get the next polygon in the sequence of multi-polygons
	 * 
	 * @return Next polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getNext();
	
	/**
	 * Get the previous polygon in the sequence of multi-polygons
	 * 
	 * @return Previous polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getPrev();
	
	/**
	 * Get this polygon's interior ring.
	 * 
	 * @return Previous interior ring as a polygon if any, otherwise null
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
	 * @param data Array of data to set to
	 */
	public void setData(Array data);
	
	/**
	 * Sets the next polygon which make up the multipolygon which this polygon is a part of.
	 * Automatically connects the other polygon to this polygon as well.
	 * 
	 * @param next Polygon to set
	 */
	public void setNext(CFPolygon next);
	
	/**
	 * Sets the previous polygon which makes up the multipolygon which this polygon is a part of.
	 * Automatically connect the other polygon to this polygon as well.
	 * 
	 * @param prev Polygon to set
	 */
	public void setPrev(CFPolygon prev);
	
	/**
	 *  Simply sets the interior ring of the polygon.
	 * 
	 * @param interior Ring which is a polygon itself
	 */
	public void setInteriorRing(CFPolygon interior);
	
	/**
	 * Given a dataset, construct a polygon from the variable which holds polygons
	 * and the index as given.
	 * 
	 * @param dataset Where the polygon variable resides
	 * @param variable Which holds polygon information
	 * @param index for Indexing within the polygon variable
	 * 
	 * @return the constructed Polygon with associated data
	 */
	public Polygon setupPolygon(NetcdfDataset dataset, Variable variable, int index);

}
