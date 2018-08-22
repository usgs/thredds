package ucar.nc2.ft2.coverage.simpgeometry;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for simple construction of a CF Polygon.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestCFPolygon {

	private static final double delt = 0.00;
	private static final int testsize = 8;
	private Random rnd = new Random();
	
	@Test
	public void testPolygonSingle() {
		CFPolygon polygon = new CFPolygon();
		polygon.addPoint(0.2, 0.05);
		Assert.assertEquals(0.2, polygon.getPoints().get(0).getX(), delt);
		Assert.assertEquals(0.05, polygon.getPoints().get(0).getY(), delt);
		Assert.assertEquals(null, polygon.getNext());
		Assert.assertEquals(null, polygon.getPrev());
		Assert.assertEquals(false, polygon.getInteriorRing());
		Assert.assertEquals(1, polygon.getPoints().size());
	}
	
	@Test
	public void testPolygonBckwd() {
		
		CFPolygon poly[] = new CFPolygon[testsize];
		CFPolygon ref_poly[] = new CFPolygon[testsize];
		double ref_x[] = new double[testsize];
		double ref_y[] = new double[testsize];
		
		/* Test adding a point to
		 * the polygon
		 */
		
		poly[0] = new CFPolygon();
		
		for(int i = 0; i < testsize; i++) {
			ref_x[i] = rnd.nextDouble();
			ref_y[i] = rnd.nextDouble();
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
	}
		
	@Test
	public void testPolygonFwd() {
		
		CFPolygon poly[] = new CFPolygon[testsize];
		CFPolygon ref_poly[] = new CFPolygon[testsize];
		double ref_x[] = new double[testsize];
		double ref_y[] = new double[testsize];
		
		/* Try with set next
		 * 
		 */
		for(int i = 0; i < testsize; i++) {
			poly[i] = new CFPolygon();
			poly[i].addPoint(rnd.nextDouble(), rnd.nextDouble());
			
			if(i != 0) {
				poly[i].setNext(poly[i - 1]);
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
	public void testPolygonInteriorRing() {
		// Test interior ring
		CFPolygon test_out = new CFPolygon();
		test_out.setInteriorRing(true);
		Assert.assertEquals(test_out.getInteriorRing(), true);
	}
	
	@Test
	public void testEmptyPolygon() {
		CFPolygon empty = new CFPolygon();
		Assert.assertEquals(null, empty.getData());
		Assert.assertEquals(0, empty.getPoints().size());
	}
}
