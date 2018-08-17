package ucar.nc2.ft.simpgeometry;

import org.junit.Assert;
import org.junit.Test;

import ucar.ma2.Array;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ft2.coverage.simpgeometry.*;
import ucar.unidata.util.test.TestDir;

import java.util.Random;
import java.io.IOException;
import java.util.List;

/**
 * Tests the simple geometry reader's capability to read a point and a multipoint.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestReadPoint {

	private SimpleGeometryReader newReader()
	{
		String filepath = TestDir.cdmLocalTestDataDir + "dataset/SimpleGeos/avg_temp_3gage_5timesteps.nc";
		NetcdfDataset dataset = null;
		
		try {
		
			dataset = NetcdfDataset.openDataset(filepath);
			dataset.enhance();
		
		}
		
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new SimpleGeometryReader(dataset);
	}
	
	@Test
	public void testReadPoint() {
		SimpleGeometryReader rdr = newReader();
		Array data = null;
		Point test_point = null;
		double err = 0.001;
		
		// Read the first point
		test_point = rdr.readPoint("Average Temperature", 0);
		
		// Check the coordinates
		Assert.assertEquals(-91.277, test_point.getX(), err);
		Assert.assertEquals(40.753, test_point.getY(), err);
		
		data = test_point.getData();
		
		// Check the data
		Assert.assertEquals(0.688, data.getDouble(2), err);
	}
	
	@Test
	public void testReadMultiPoint() {
		
	}
}
