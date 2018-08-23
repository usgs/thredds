package ucar.nc2.ft2.coverage.simpgeometry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;
import ucar.nc2.constants.CF;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * A CF 1.8 compliant Line
 * for use with Simple Geometries. Can also
 * represent Multilines.
 * 
 * @author wchen@usgs.gov
 *
 */
public class CFLine implements Line {

	private List<CFPoint> points;	// a list of the constitutent points of the Line, connected in ascending order as in the CF convention
	private CFLine next;	// if non-null, next refers to the next line part of a multi-line
	private CFLine prev;	// if non-null, prev refers to the previous line part of a multi-line	
	private Array data;		// data associated with the line

	/**
	 * Add a point to the end of the line. 
	 *
	 */
	public void addPoint(double x, double y) {
		CFPoint ptPrev = null;
		
		if(points.size() > 0) {
			ptPrev = points.get(points.size() - 1);
		}
		
		this.points.add(new CFPoint(x, y, (CFPoint) ptPrev, null, null));
	}
	
	/**
	 * Returns the list of points which make up this line
	 * 
	 * @return points - the collection of points that make up this line
	 */
	public List<CFPoint> getPoints() {
		return points;
	}
	
	/**
	 * Get the data associated with this line
	 * 
	 * @return data
	 */
	public Array getData() {
		return data;
	}
	
	/**
	 * If part of a multiline, returns the next line within that line
	 * if it is present.
	 * 
	 * @return next line if present, null if not
	 */
	public CFLine getNext() {
		return next;
	}
	
	/**
	 * If part of a multiline, returns the previous line within that line
	 * if it is present
	 * 
	 * @return previous line if present, null if not
	 */
	public CFLine getPrev() {
		return prev;
	}

	/**
	 * Set the data associated with this Line
	 * 
	 * @param data - array of data to set to
	 */
	public void setData(Array data) {
		this.data = data;
	}
	
	/**
	 * Sets the next line which make up the multiline which this line is a part of.
	 * Automatically connects the other line to this line as well.
	 */
	public void setNext(CFLine next) {
		this.next = next;
		
		if(next != null) {
			next.setPrevOnce(this);
		}
	}
	
	private void setNextOnce(CFLine next) {
		this.next = next;
	}
	

	/**
	 * Sets the previous line which makes up the multiline which this line is a part of.
	 * Automatically connect the other line to this line as well.
	 */
	public void setPrev(CFLine prev) {
		this.prev = prev;
		
		if(prev != null) {
			prev.setNextOnce(this);
		}
	}
	
	private void setPrevOnce(CFLine prev) {
		this.prev = prev;
	}
	
	/**
	 * Given a dataset, variable, and index, automatically populates this Line and
	 * returns it. If not found, returns null.
	 * 
	 * @param dataset which the variable is a part of
	 * @param var the variable which has a geometry attribute
	 * @param index of the line within the variable
	 * @return return a line
	 */
	public Line setupLine(NetcdfDataset dataset, Variable var, int index)
	{
		this.points.clear();
		Array xPts = null;
		Array yPts = null;
		Variable nodeCounts = null;
		Variable partNodeCounts = null;

		List<CoordinateAxis> axes = dataset.getCoordinateAxes();
		CoordinateAxis x = null; CoordinateAxis y = null;
		
		String[] nodeCoords = var.findAttributeIgnoreCase(CF.NODE_COORDINATES).getStringValue().split(" ");
		
		// Look for x and y
		
		for(CoordinateAxis ax : axes){
			
			if(ax.getFullName().equals(nodeCoords[0])) x = ax;
			if(ax.getFullName().equals(nodeCoords[1])) y = ax;
		}
		
		// Affirm node counts
		String node_c_str = var.findAttValueIgnoreCase(CF.NODE_COUNT, "");
		
		if(!node_c_str.equals("")) {
			nodeCounts = dataset.findVariable(node_c_str);
		}
		
		else return null;
		
		// Affirm part node counts
		String pNodeCoStr = var.findAttValueIgnoreCase(CF.PART_NODE_COUNT, "");
		
		if(!pNodeCoStr.equals("")) {
			partNodeCounts = dataset.findVariable(pNodeCoStr);
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
				
				// x and y should have the same shape, will add some handling on this
				while(itrX.hasNext()) {
					this.addPoint(itrX.getDoubleNext(), itrY.getDoubleNext());
				}
	
				this.setData(var.read(":," + index).reduce());
			}
			
			// If there are multipolygons then take the upper and lower of it and divy it up
			else {
				
				CFLine tail = this;
				Array pnc = partNodeCounts.read();
				IndexIterator pncItr = pnc.getIndexIterator();
				
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
					
					while(smaller > 0) {
						tail.addPoint(itrX.getDoubleNext(), itrY.getDoubleNext());
						smaller--;
					}
					
					// Set data of each
					tail.setData(var.read(":," + index));
					lower += tail.getPoints().size();
					pncInd++;
					tail.setNext(new CFLine());
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
	 *  Constructs an "empty" line with no members using an ArrayList to implement the point list.
	 * 
	 */
	public CFLine() {
		this.points = new ArrayList<CFPoint>();
		this.next = null;
		this.prev = null;
		this.data = null;
	}
	
	/**
	 * From a given list of points, construct a line
	 * 
	 * @param new_pt The list of points which will constitute the new line
	 */
	public CFLine(List<CFPoint> newPt) {
		this.points = newPt;
		this.next = null;
		this.data = null;
	}
	
}
