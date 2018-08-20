package ucar.nc2.ft2.coverage.simpgeometry;

import java.io.IOException;

import ucar.ma2.Array;
import ucar.nc2.Variable;
import ucar.nc2.constants.CF;
import ucar.nc2.ft2.coverage.simpgeometry.*;

/**
 * A Simple Geometry Index Finder can go find the beginning and end indicies of
 * a simple geometry X and Y within a variable. But first the Index Finder needs a few variables to find it in.
 * If the indexer is tasked to find a Simple Geometry close to one before, it will find it faster.
 * 
 * @author wchen@usgs.gov
 *
 */
public class SimpleGeometryIndexFinder {
	
	private Array node_count = null;
	private int past_index;
	private int previous_end;
	private int previous_begin;
	
	private int getNodeCount(int index) {
		return node_count.getInt(index);
	}
	
	public int getBeginning(int index) {
		
		//Test if the last end is the new beginning
		if(index == (past_index + 1 ))
		{
			return previous_end + 1;
		}
		
		// Otherwise, find it!
		int new_beginning = 0;
		for(int i = 0; i < index; i++) {
			new_beginning += getNodeCount(i);
		}
		
		past_index = index;
		previous_begin = new_beginning;
		return new_beginning;
	}
	
	public int getEnd(int index) {

		// Test if the last beginning is the new end
		if(index == (past_index - 1))
		{
			return previous_begin - 1;
		}
		
		// Otherwise find it!
		int new_end = 0;
		for(int i = 0; i < index + 1; i++) {
			new_end += getNodeCount(i);
		}
		
		past_index = index;
		previous_end = new_end;
		return new_end - 1;
	}
	
	/**
	 * Create a new indexer, the indexer must be given a variable to look through.
	 * 
	 * @param node_count Amount of nodes per geometry
	 */
	public SimpleGeometryIndexFinder(Variable node_count) {
		
		try {
			this.node_count = node_count.read();
		} catch (IOException e) {

			this.node_count = null;
			e.printStackTrace();
		}
		
		past_index = -10;
		previous_end = -10;
		previous_begin = -10;
	}
}
