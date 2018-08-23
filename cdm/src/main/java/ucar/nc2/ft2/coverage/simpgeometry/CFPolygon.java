package ucar.nc2.ft2.coverage.simpgeometry;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;
import ucar.nc2.constants.CF;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * A CF 1.8 compliant Polygon
 * for use with Simple Geometries.
 * Can also represent multipolygons.
 * 
 * @author wchen@usgs.gov
 *
 */
public class CFPolygon implements Polygon  {

	private List<CFPoint> points;	// a list of the constitutent points of the Polygon, connected in ascending order as in the CF convention
	private CFPolygon next;	// if non-null, next refers to the next line part of a multi-polygon
	private CFPolygon prev;	// if non-null, prev refers to the previous line part of a multi-polygon
	private boolean isInteriorRing; // true is an interior ring polygon otherwise false
	private Array data;	// data array associated with the polygon
	
	/**
	 * Get the geometry the data is associated with.
	 * 
	 */
	
	public CFGEOMETRY getGeometryType() {
		return CFGEOMETRY.CFPOLYGON;
	}
	
	/**
	 * Get the list of points which constitute the polygon.
	 * 
	 * @return points
	 */
	public List<CFPoint> getPoints() {
		return points;
	}

	/**
	 * Get the data associated with this Polygon
	 * 
	 * @return data
	 */
	public Array getData() {
		return data;
	}
	
	/**
	 * Get the next polygon in the sequence of multi-polygons
	 * 
	 * @return next polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getNext() {
		return next;
	}
	
	/**
	 * Get the previous polygon in the sequence of multi-polygons
	 * 
	 * @return previous polygon in the same multipolygon if any, otherwise null
	 */
	public CFPolygon getPrev() {
		return prev;
	}
	
	/**
	 * Get whether or not this polygon is an interior ring
	 * 
	 * @return true if an interior ring, false if not
	 */
	public boolean getInteriorRing() {
		return isInteriorRing;
	}
	
	/**
	 * Add a point to this polygon's points list
	 * 
	 */
	public void addPoint(double x, double y) {
		CFPoint ptPrev = null;
		
		if(points.size() > 0) {
			ptPrev = points.get(points.size() - 1);
		}
		
		this.points.add(new CFPoint(x, y, ptPrev, null, null));
	}
	
	/**
	 * Set the data associated with this Polygon
	 * 
	 * @param data - array of data to set to
	 */
	public void setData(Array data) {
		this.data = data;
	}
	
	/**
	 * Sets the next polygon which make up the multipolygon which this polygon is a part of.
	 * Automatically connects the other polygon to this polygon as well.
	 */
	public void setNext(CFPolygon next) {
		this.next = next;
		
		if(next != null) {
			next.setPrevOnce(this);
		}
	}
	
	private void setNextOnce(CFPolygon next) {
		this.next = next;
	}

	/**
	 * Sets the previous polygon which makes up the multipolygon which this polygon is a part of.
	 * Automatically connect the other polygon to this polygon as well.
	 */
	public void setPrev(CFPolygon prev) {
		this.prev = prev;
		
		if(prev != null) {
			prev.setNextOnce(this);
		}
	}
	
	private void setPrevOnce(CFPolygon prev) {
		this.prev = prev;
	}
	
	/**
	 *  Sets whether or not this polygon is an interior ring.
	 * 
	 */
	public void setInteriorRing(boolean interior) {
		this.isInteriorRing = interior;
	}
	
	/**
	 * Given a dataset, variable and index, automatically sets up a previously constructed polygon.
	 * If the specified polygon is not found in the dataset, returns null
	 * 
	 * @param dataset which the variable is a part of
	 * @param polyvar the variable which has a geometry attribute
	 * @param index of the polygon within the variable
	 * 
	 */
	public Polygon setupPolygon(NetcdfDataset dataset, Variable polyvar, int index)
	{
		this.points.clear();
		Array xPts = null;
		Array yPts = null;
		Variable nodeCounts = null;
		Variable partNodeCounts = null;
		Variable interiorRings = null;

		List<CoordinateAxis> axes = dataset.getCoordinateAxes();
		CoordinateAxis x = null; CoordinateAxis y = null;
		
		String[] nodeCoords = polyvar.findAttributeIgnoreCase(CF.NODE_COORDINATES).getStringValue().split(" ");
		
		// Look for x and y
		
		for(CoordinateAxis ax : axes){
			
			if(ax.getFullName().equals(nodeCoords[0])) x = ax;
			if(ax.getFullName().equals(nodeCoords[1])) y = ax;
		}
		
		// Affirm node counts
		String nodeCoStr = polyvar.findAttValueIgnoreCase(CF.NODE_COUNT, "");
		
		if(!nodeCoStr.equals("")) {
			nodeCounts = dataset.findVariable(nodeCoStr);
		}
		
		else return null;
		
		// Affirm part node counts
		String pNodeCoStr = polyvar.findAttValueIgnoreCase(CF.PART_NODE_COUNT, "");
		
		if(!pNodeCoStr.equals("")) {
			partNodeCounts = dataset.findVariable(pNodeCoStr);
		}
		
		// Affirm interior rings
		String interiorRingsStr = polyvar.findAttValueIgnoreCase(CF.PART_NODE_COUNT, "");
				
		if(!interiorRingsStr.equals("")) {
				interiorRings = dataset.findVariable(interiorRingsStr);
		}
		
		SimpleGeometryIndexFinder indexFinder = new SimpleGeometryIndexFinder(nodeCounts);
		
		//Get beginning and ending indicies for this polygon
		int lower = indexFinder.getBeginning(index);
		int upper = indexFinder.getEnd(index);

		
		try {
			
			xPts = x.read( lower + ":" + upper ).reduce();
			yPts = y.read( lower + ":" + upper ).reduce(); 

			IndexIterator itrX = xPts.getIndexIterator();
			IndexIterator itrY = yPts.getIndexIterator();
			
			// No multipolygons just read in the whole thing
			if(partNodeCounts == null) {
				
				this.next = null;
				this.prev = null;
				this.isInteriorRing = false;
				
				// x and y should have the same shape, will add some handling on this
				while(itrX.hasNext()) {
					this.addPoint(itrX.getDoubleNext(), itrY.getDoubleNext());
				}
	
				this.setData(polyvar.read(":," + index).reduce());
			}
			
			// If there are multipolygons then take the upper and lower of it and divy it up
			else {
				
				CFPolygon tail = this;
				Array pnc = partNodeCounts.read();
				Array ir = null;
				IndexIterator pncItr = pnc.getIndexIterator();
				
				if(interiorRings != null) ir = interiorRings.read();
				
				// In part node count search for the right index to begin looking for "part node counts"
				int pncInd = 0;
				int pncEnd = 0;
				while(pncEnd < lower)
				{
					pncEnd += pncItr.getIntNext();
					pncInd++;
				}
				
				// Now the index is found, use part node count and the index to find each part node count of each individual part
				while(lower < upper) {
					
					int smaller = pnc.getInt(pncInd);
					
					// Set interior ring if needed
					if(interiorRings != null)
					{
						int interiorRingValue = ir.getInt(pncInd);
						
						switch(interiorRingValue) {
						
							case 0:
								this.setInteriorRing(false);
								break;
								
							case 1:
								this.setInteriorRing(true);
								break;
								
							// will handle default case
						}
						
					} else this.isInteriorRing = false;
					
					while(smaller > 0) {
						tail.addPoint(itrX.getDoubleNext(), itrY.getDoubleNext());
						smaller--;
					}
					
					// Set data of each
					tail.setData(polyvar.read(":," + index));
					lower += tail.getPoints().size();
					pncInd++;
					tail.setNext(new CFPolygon());
					tail = tail.getNext();
				}
				
				//Clean up
				tail = tail.getPrev();
				if(tail != null) tail.setNext(null);
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
			return null;
		
		} catch (InvalidRangeException e) {
			e.printStackTrace();
			return null;
		}
		
		return this;
	}
	
	
	/**
	 * Constructs an empty polygon with nothing in it using an Array List.
	 */
	public CFPolygon() {
		this.points = new ArrayList<CFPoint>();
		this.next = null;
		this.prev = null;
		this.isInteriorRing = false;
		this.data = null;
	}
	
	/**
	 * Constructs a new polygon whose points constitute the points passed in.
	 * 
	 * @param points which make up the Polygon
	 */
	public CFPolygon(List<CFPoint> points) {
		this.points = points;
		this.next = null;
		this.prev = null;
		this.isInteriorRing = false;
		this.data = null;
	}
}
