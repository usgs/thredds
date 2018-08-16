package ucar.nc2.ft2.coverage.simpgeometry;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;
import java.util.List;

/**
 * Very simple tests for Simple Geometry container objects.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestCFSimpleGeometryCont {
	
	private static final double delt = 0.00;
	private static final int testsize = 100000;
	private Random rnd = new Random();
	private CFPoint pt[] = new CFPoint[testsize];
	private double ref_x[] = new double[testsize];
	private double ref_y[] = new double[testsize];
	private CFLine line[] = new CFLine[testsize];
	private CFLine ref_line[] = new CFLine[testsize];
	private CFPolygon poly[] = new CFPolygon[testsize];
	private CFPolygon ref_poly[] = new CFPolygon[testsize];
	
	@Test
	public void testPtBckwd() {		
		
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
	
	@Test
	public void testLine() {
		
		/* Test adding a point to
		 * the line
		 */
		
		line[0] = new CFLine();
		
		for(int i = 0; i < testsize; i++) {
			line[0].addPoint(ref_x[i], ref_y[i]);
		}
		
		List<CFPoint> pts = line[0].getPoints();
		
		for(int i = 0; i < testsize; i++) {
			Assert.assertEquals(ref_x[i], pts.get(i).getX(), delt);
			Assert.assertEquals(ref_y[i], pts.get(i).getY(), delt);
		}
		
		/* Test multiline
		 */
		
		for(int i = 0; i < testsize; i++) {
			line[i] = new CFLine();
			line[i].addPoint(rnd.nextDouble(), rnd.nextDouble());
			
			if(i != 0) {
				line[i].setPrev(line[i - 1]);
			}
			
			ref_line[i] = line[i];
		}
		
		
		/* Test
		 * forwards and backwards
		 */
		CFLine cline = line[0];
		int k = 0;
		while(cline != null) {
			Assert.assertEquals(ref_line[k], cline);
			cline = cline.getNext();
			k++;
		}
		
		cline = line[testsize - 1];
		k = testsize - 1;
		while(cline != null) {
			Assert.assertEquals(ref_line[k], cline);
			cline = cline.getPrev();
			k--;
		}
		
		/* Try with set next
		 * 
		 */
		for(int i = 0; i < testsize; i++) {
			line[i] = new CFLine();
			line[i].addPoint(rnd.nextDouble(), rnd.nextDouble());
			
			if(i != 0) {
				line[i].setNext(line[i - 1]);
			}
			
			ref_line[i] = line[i];
		}
		
		/* Test
		 * forwards and backwards
		 */
		cline = line[0];
		k = 0;
		while(cline != null) {
			Assert.assertEquals(ref_line[k], cline);
			cline = cline.getPrev();
			k++;
		}
		
		cline = line[testsize - 1];
		k = testsize - 1;
		while(cline != null) {
			Assert.assertEquals(ref_line[k], cline);
			cline = cline.getNext();
			k--;
		}
	}
	
	@Test
	public void testPoly() {
		/* Test adding a point to
		 * the polygon
		 */
		
		poly[0] = new CFPolygon();
		
		for(int i = 0; i < testsize; i++) {
			poly[0].addPoint(ref_x[i], ref_y[i]);
		}
		
		List<CFPoint> pts = poly[0].getPoints();
		
		for(int i = 0; i < testsize; i++) {
			Assert.assertEquals(ref_x[i], pts.get(i).getX(), delt);
			Assert.assertEquals(ref_y[i], pts.get(i).getY(), delt);
		}
		
		/* Test multipoly
		 */
		
		for(int i = 0; i < testsize; i++) {
			poly[i] = new CFPolygon();
			poly[i].addPoint(rnd.nextDouble(), rnd.nextDouble());
			
			if(i != 0)
			{
				poly[i].setPrev(poly[i - 1]);
			}
			
			ref_poly[i] = poly[i];
		}
		
		
		/* Test
		 * forwards and backwards
		 */
		CFPolygon cpoly = poly[0];
		int k = 0;
		while(cpoly != null) {
			Assert.assertEquals(ref_poly[k], cpoly);
			cpoly = cpoly.getNext();
			k++;
		}
		
		cpoly = poly[testsize - 1];
		k = testsize - 1;
		while(cpoly != null) {
			Assert.assertEquals(ref_poly[k], cpoly);
			cpoly = cpoly.getPrev();
			k--;
		}
		
		/* Try with set next
		 * 
		 */
		for(int i = 0; i < testsize; i++) {
			poly[i] = new CFPolygon();
			poly[i].addPoint(rnd.nextDouble(), rnd.nextDouble());
			
			if(i != 0)
			{
				poly[i].setNext(poly[i - 1]);
			}
			
			ref_poly[i] = poly[i];
		}
		
		/* Test
		 * forwards and backwards
		 */
		cpoly = poly[0];
		k = 0;
		while(cpoly != null) {
			Assert.assertEquals(ref_poly[k], cpoly);
			cpoly = cpoly.getPrev();
			k++;
		}
		
		cpoly = poly[testsize - 1];
		k = testsize - 1;
		while(cpoly != null) {
			Assert.assertEquals(ref_poly[k], cpoly);
			cpoly = cpoly.getNext();
			k--;
		}
	}

	
	@Test
	public void testPolyAddt() {
		// Test interior ring
		CFPolygon test_out = new CFPolygon();
		test_out.addPoint(5.0, 3.0);
		CFPolygon test_inner = new CFPolygon();
		test_inner.addPoint(2.0, 4.0);
		test_out.setInteriorRing(test_inner);
		Assert.assertEquals(test_inner, test_out.getInteriorRing());
	}
}