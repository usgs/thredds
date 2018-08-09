package ucar.nc2.ft2.coverage.simpgeo;

/**
 * A CF 1.8 compliant Point
 * for use with Simple Geometries.
 * Can also represent multipoints.
 * 
 * @author wchen@usgs.gov
 *
 */
public class CFPoint {

	private double x;	// x coordinate
	private double y;	// y coordinate
	private CFPoint next;	// next element in a multipoint
	private CFPoint prev;	// previous element in a multipoint
	
	/**
	 * Return the x coordinate for the point.
	 * 
	 * @return x of the point
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Return the y coordinate for the point
	 * 
	 * @ return y of the point
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Retrieves the next point within a multipoint if any
	 * 
	 * @return next point if it exists, null if not
	 */
	public CFPoint getNext()
	{
		return next;
	}
	
	/**
	 * Retrieves the previous point within a multipoint if any
	 * 
	 * @return previous point if it exists null if not
	 */
	public CFPoint getPrev()
	{
		return prev;
	}
	
	/**
	 *  Sets the next point in a multipoint
	 * 
	 */
	protected void setNext(CFPoint next)
	{
		this.next = next;
	}
	
	/**
	 * 
	 *  Set the previous point in a multipoint
	 * 
	 */
	protected void setPrev(CFPoint prev)
	{
		this.prev = prev;
	}
	
	/**
	 * Construct a new IMMUTABLE point from specified parameters
	 * The construction will automatically connect in related parts of a Multipoint - just specify any constituents
	 * of a multipoint as next or prev.
	 * 
	 * @param x - the x coordinate of the point
	 * @param y - the y coordinate of the point
	 * @param next - next point if part of a multipoint
	 * @param prev - previous point if part of a multipoint
	 */
	public CFPoint(int x, int y, CFPoint next, CFPoint prev)
	{
		this.next = next;
		this.prev = prev;
		
		// Create links automatically
		if(next != null)
		{
			next.setPrev(this);
		}
		
		if(prev != null)
		{
			prev.setNext(this);
		}
		
		this.x = x;
		this.y = y;
	}
}
