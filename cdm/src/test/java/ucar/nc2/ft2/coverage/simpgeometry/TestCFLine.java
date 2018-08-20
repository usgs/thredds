package ucar.nc2.ft2.coverage.simpgeometry;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for simple construction of a CF Line.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestCFLine {
	
	private static final double delt = 0.00;
	private static final int testsize = 100000;
	private Random rnd = new Random();
	
	@Test
	public void testLineSingle() {
		CFLine line = new CFLine();
		line.addPoint(0.25, 0.1);
		Assert.assertEquals(0.25, line.getPoints().get(0).getX(), delt);
		Assert.assertEquals(0.1, line.getPoints().get(0).getY(), delt);
		Assert.assertEquals(null, line.getNext());
		Assert.assertEquals(null, line.getPrev());
		Assert.assertEquals(1, line.getPoints().size());
	}
	
	@Test
	public void testLineBckwd() {
		
		CFLine line[] = new CFLine[testsize];
		CFLine ref_line[] = new CFLine[testsize];
		double ref_x[] = new double[testsize];
		double ref_y[] = new double[testsize];
		
		/* Test adding a point to
		 * the line
		 */
		
		line[0] = new CFLine();
		
		for(int i = 0; i < testsize; i++) {
			ref_x[i] = rnd.nextDouble(); 
			ref_y[i] = rnd.nextDouble();
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
		
	}
	
	@Test
	public void testLineFwd() {
		
		CFLine line[] = new CFLine[testsize];
		CFLine ref_line[] = new CFLine[testsize];
		
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
		CFLine cline = line[0];
		int k = 0;
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
	public void testEmptyLine() {
		CFLine empty = new CFLine();
		Assert.assertEquals(null, empty.getData());
		Assert.assertEquals(0, empty.getPoints().size());
	}
	
}
