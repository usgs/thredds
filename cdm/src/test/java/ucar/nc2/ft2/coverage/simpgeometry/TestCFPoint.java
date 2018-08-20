package ucar.nc2.ft2.coverage.simpgeometry;

import java.util.Random;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for simple construction of a CF Point.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestCFPoint {

	private static final double delt = 0.00;
	private static final int testsize = 100000;
	private Random rnd = new Random();
	
	@Test
	public void testPointSingle() {
		CFPoint pt = new CFPoint(0.1, 0.3, null, null);
		Assert.assertEquals(0.1, pt.getX(), delt);
		Assert.assertEquals(0.3, pt.getY(), delt);
		Assert.assertEquals(null, pt.getNext());
		Assert.assertEquals(null, pt.getPrev());
	}
	
	@Test
	public void testPtBckwd() {		
		
		CFPoint pt[] = new CFPoint[testsize];
		double ref_x[] = new double[testsize];
		double ref_y[] = new double[testsize];
		
		// Make the points
		for(int i = 0; i < testsize; i++) {
			double x = rnd.nextDouble();
			double y = rnd.nextDouble();
			
			if(i == 0) {
				pt[i] = new CFPoint(x, y, null, null);
			}
			
			else {
				pt[i] = new CFPoint(x, y, pt[i-1], null);
			}
			
			ref_x[i] = x;
			ref_y[i] = y;
		}
		
		// Forward
		CFPoint test_pt = pt[0];
		int k = 0;
				
		while(test_pt != null) {
			Assert.assertEquals(ref_x[k], test_pt.getX(), delt);
			Assert.assertEquals(ref_y[k], test_pt.getY(), delt);
			test_pt = test_pt.getNext();
			k++;
		}
		
		// Backward
		test_pt = pt[testsize - 1];
		k = testsize - 1;
			
		while(test_pt != null){
				if(ref_x[k] != test_pt.getX()) Assert.assertEquals(ref_x[k], test_pt.getX(), delt);
				if(ref_y[k] != test_pt.getY()) Assert.assertEquals(ref_y[k], test_pt.getY(), delt);
				test_pt = test_pt.getPrev();	
				k--;
		}
	
		
	}
	
	@Test
	public void testPtFwd() {
	
		// Make some more points test backwards setting
		CFPoint pt[] = new CFPoint[testsize];
		double ref_x[] = new double[testsize];
		double ref_y[] = new double[testsize];
		
		for(int i = 0; i < testsize; i++){
			
			double x = rnd.nextDouble();
			double y = rnd.nextDouble();
				
			if(i == 0) {
				pt[i] = new CFPoint(x, y, null, null);
			}
				
			else {
				pt[i] = new CFPoint(x, y, pt[i - 1], null);
			}
				
			ref_x[i] = x;
			ref_y[i] = y;
		}
		
		/* Test them out, very similar test
		 * Except this time, the points are backwards order
		 */
		CFPoint test_pt = pt[0];
		int k = 0;
		
		// Backward
		test_pt = pt[0];
		k = 0;
						
		while(test_pt != null) {
			Assert.assertEquals(ref_x[k], test_pt.getX(), delt);
			Assert.assertEquals(ref_y[k], test_pt.getY(), delt);
			test_pt = test_pt.getPrev();
			k++;
		}
				
		// Forward
		test_pt = pt[testsize - 1];
		k = testsize - 1;
		
		while(test_pt != null) {
			
			Assert.assertEquals(ref_x[k], test_pt.getX(), delt);
			Assert.assertEquals(ref_y[k], test_pt.getY(), delt);	
			test_pt = test_pt.getNext();	
			k--;
		}
		
	}
}
