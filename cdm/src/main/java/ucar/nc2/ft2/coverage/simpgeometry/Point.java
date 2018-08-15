package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.ma2.Array;

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

}
