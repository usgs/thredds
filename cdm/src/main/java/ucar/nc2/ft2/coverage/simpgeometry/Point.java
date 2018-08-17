package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.ma2.Array;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Generic interface for a Simple Geometry Point.
 * 
 * @author wchen@usgs.gov
 *
 */
public interface Point {

	/**
	 * Get the data associated with this point
	 * 
	 * @return data
	 */
	public Array getData();
	
	/**
	 * Return the x coordinate for the point.
	 * 
	 * @return x of the point
	 */
	public double getX();
	
	/**
	 * Return the y coordinate for the point
	 * 
	 * @ return y of the point
	 */
	public double getY();
	/**
	 * Retrieves the next point within a multipoint if any
	 * 
	 * @return next point if it exists, null if not
	 */
	public CFPoint getNext();
	
	/**
	 * Retrieves the previous point within a multipoint if any
	 * 
	 * @return previous point if it exists null if not
	 */
	public CFPoint getPrev();

	/**
	 * Given a dataset, construct a point from the variable which holds points
	 * and the index as given.
	 * 
	 * @param dataset Where the point variable resides
	 * @param variable Which holds point information
	 * @param index for Indexing within the polygon variable
	 * 
	 * @return the constructed Point with associated data
	 */
	public Point setupPoint(NetcdfDataset dataset, Variable variable, int index);
}
