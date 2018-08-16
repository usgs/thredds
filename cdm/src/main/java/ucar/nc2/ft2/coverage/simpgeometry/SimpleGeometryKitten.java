package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.nc2.Variable;
import ucar.nc2.constants.CF;
import ucar.nc2.ft2.coverage.simpgeometry.*;

/**
 * A cat (the animal) trained in finding Simple Geometry.
 * 
 * A Simple Geometry Kitten can go find the beginning and end indicies of
 * a simple geometry X and Y within a variable. But first the Kitten needs a few variables to find it in.
 * The kitten remembers previous adventures so if the kitten is tasked to find a Simple Geometry close to one before it
 * will find it faster.
 * 
 * @author wchen
 *
 */
public class SimpleGeometryKitten {
	
	private Variable cat_toy = null;
	private Variable node_count = null;
	private Variable part_node_count = null;
	private String geometry_type;
	private int past_index;
	private int previous_end, new_end;
	private int previous_begin, new_begin;
	
	private int getNodeCount(int index) {
		
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
		
		new_end -= 1;
		
		return new_end;
	}
	
	/**
	 * Call up a new Kitten, the Kitten must be given a few variables to look through though.
	 * 
	 * @param variable Variable with all information concerning polygon point data
	 * @param node_count Amount of nodes per geometry
	 * @param variable part_node_count
	 */
	public SimpleGeometryKitten(Variable variable, Variable node_count, Variable part_node_count) {
		
		past_index = -1;
		previous_end = -1; new_end = -1;
		previous_begin = -1; new_begin = -1;
	}
}
