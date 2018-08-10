package ucar.nc2.ft2.coverage.simpgeo;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

/**
 * Very simple tests for Simple Geometry Polygon objects.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestSimpleGeo {
	
	Random rnd = new Random();
	CFPoint pt[] = new CFPoint[8];
	double ref_x[] = new double[8];
	double ref_y[] = new double[8];
	CFLine line[] = new CFLine[8];
	CFLine ref_line[] = new CFLine[8];
	CFPolygon poly[] = new CFPolygon[8];
	CFPolygon ref_poly[] = new CFPolygon[8];
	
	@Test
	public void testPt() {		
		
		// Make the points
		for(int i = 0; i < 8; i++) {
			double x = rnd.nextDouble();
			double y = rnd.nextDouble();
			
			if(i == 0)
			{
				pt[i] = new CFPoint(x, y, null, null);
			}
			
			else
			{
				pt[i] = new CFPoint(x, y, pt[i - 1], null);
			}
			
			ref_x[i] = x;
			ref_y[i] = y;
		}
		
		// Test them out
		
		// Forward
		CFPoint test_pt = pt[0];
		int k = 0;
				
		while(test_pt != null) {
			Assert.assertEquals(ref_x[k], test_pt.getX());
			Assert.assertEquals(ref_y[k], test_pt.getY());
			test_pt = test_pt.getNext();
			k++;
		}
		
		// Backward
		test_pt = pt[7];
		k = 7;
			
		while(test_pt != null){
				Assert.assertEquals(ref_x[k], test_pt.getX());
				Assert.assertEquals(ref_y[k], test_pt.getY());
				test_pt = test_pt.getPrev();	
				k--;
		}
	
		// Make some more points test backwards setting
		CFPoint pt[] = new CFPoint[8];
		double ref_x[] = new double[8];
		double ref_y[] = new double[8];
		
		
		for(int i = 0; i < 8; i++){
			
			double x = rnd.nextDouble();
			double y = rnd.nextDouble();
				
			if(i == 0)
			{
				pt[i] = new CFPoint(x, y, null, null);
			}
				
			else
			{
				pt[i] = new CFPoint(x, y, null, pt[i - 1]);
			}
				
			ref_x[i] = x;
			ref_y[i] = y;
		}
		
		/* Test them out, very similar test
		 * Except this time, the points are backwards order
		 */
		
		// Backward
		test_pt = pt[0];
		k = 0;
						
		while(test_pt != null) {
			Assert.assertEquals(ref_x[k], test_pt.getX());
			Assert.assertEquals(ref_y[k], test_pt.getY());
			test_pt = test_pt.getPrev();
			k++;
		}
				
		// Forward
		test_pt = pt[7];
		k = 7;
		
		while(test_pt != null) {
			
			Assert.assertEquals(ref_x[k], test_pt.getX());
			Assert.assertEquals(ref_y[k], test_pt.getY());	
			test_pt = test_pt.getNext();	
			k--;
		}
		
	}
	
	@Test
	public void testLine() {
		// Test add
	}
	
	@Test
	public void testPoly() {
		
	}

}
