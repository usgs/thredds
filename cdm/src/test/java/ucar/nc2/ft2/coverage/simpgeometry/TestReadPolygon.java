package ucar.nc2.ft2.coverage.simpgeometry;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ucar.ma2.Array;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ft2.coverage.simpgeometry.*;
import ucar.unidata.util.test.TestDir;

/**
 * Polygon construction tests at the dataset level.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestReadPolygon {

	/**
	 * Tests ability to read and construct Polygons and Multipolygons.
	 * 
	 */
	@Test
	public void testMultiPolygonsPolygons() throws IOException {

		double err = 0.01;
		
		NetcdfDataset data = null ;
		data = NetcdfDataset.openDataset(TestDir.cdmLocalTestDataDir + "dataset/SimpleGeos/hru_soil_moist_3hru_5timestep.nc");
			
		SimpleGeometryReader rdr = new SimpleGeometryReader(data);
		Polygon poly = rdr.readPolygon("hru_soil_moist", 0);
		Polygon poly2 = rdr.readPolygon("hru_soil_moist", 1);
		Polygon poly3 = rdr.readPolygon("hru_soil_moist", 2);
		
		Assert.assertEquals(6233, poly.getPoints().size());
		Assert.assertEquals(5, poly2.getPoints().size());
		Assert.assertEquals(6033, poly2.getNext().getPoints().size());
		Assert.assertEquals(5135, poly3.getPoints().size());
		
		// Test data retrieval
		Assert.assertEquals(1.28, poly.getData().getDouble(2), err);
		Assert.assertEquals(1.52, poly2.getData().getDouble(3), err);
		Assert.assertEquals(1.36, poly3.getData().getDouble(0), err);
	}
}
