package ucar.nc2.ft2.coverage.simpgeo;

import java.util.List;

/**
 * A CF 1.8 compliant Polygon
 * for use with Simple Geometries.
 * Can also represent multipolygons.
 * 
 * @author wchen@usgs.gov
 *
 */
public class CFPolygon  {

	private List<CFPoint> points;	// a list of the constitutent points of the Polygon, connected in ascending order as in the CF convention
	private CFPolygon next;	// if non-null, next refers to the next line part of a multi-polygon
	private CFPolygon prev;	// if non-null, prev refers to the previous line part of a multi-polygon
	private CFPolygon interior_ring; // the polygon that makes up an interior ring, if any
	
	/**
	 * Get the list of points which constitute the polygon.
	 * 
	 * @return points
	 */
	public List<CFPoint> getPoints()
	{
		return points;
	}
	
	/**
	 * Get the next polygon in the sequence of multi-polygons
	 * 
	 * @return next polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getNext()
	{
		return next;
	}
	
	/**
	 * Get the previous polygon in the sequence of multi-polygons
	 * 
	 * @return previous polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getPrev()
	{
		return prev;
	}
	
	/**
	 * Get this polygon's interior ring.
	 * 
	 * @return previous interior ring as a polygon if any, otherwise null
	 */
	public CFPolygon getInteriorRing()
	{
		return interior_ring;
	}
	
	/**
	 * Add a point to this polygon's points list
	 * 
	 */
	public void addPoint(int x, int y)
	{
		CFPoint pt_prev = null;
		
		if(points.size() > 0)
		{
			pt_prev = points.get(points.size() - 1);
		}
		
		this.points.add(new CFPoint(x, y, null, pt_prev));
	}
	
	/**
	 * Sets the next polygon which make up the multipolygon which this line is a part of.
	 * Automatically connects the other polygon to this polygon as well.
	 */
	public void setNext(CFPolygon next)
	{
		this.next = next;
		
		if(next != null)
		{
			next.setPrev(this);
		}
	}

	/**
	 * Sets the previous polygon which makes up the multipolygon which this line is a part of.
	 * Automatically connect the other polygon to this polygon as well.
	 */
	public void setPrev(CFPolygon prev)
	{
		this.prev = prev;
		
		if(prev != null)
		{
			prev.setNext(this);
		}
	}
	
	/**
	 * Constructs an empty polygon with nothing in it.
	 */
	public CFPolygon()
	{
		this.points = null;
		this.next = null;
		this.prev = null;
		this.interior_ring = null;
	}
	
	/**
	 * Constructs a new polygon whose points constitute the points passed in.
	 */
	public CFPolygon(List<CFPoint> points)
	{
		this.points = points;
	}
}
