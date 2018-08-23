package ucar.nc2.ft2.coverage.simpgeometry;

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
 * Point construction tests at the dataset level.
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
		
		}
		
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new SimpleGeometryReader(dataset);
	} // Will be expanded on
	
	@Test
	public void testReadPoint()
	{
		SimpleGeometryReader rdr = newReader();
		Assert.assertNotNull(rdr);
		Point point0 = rdr.readPoint("gage_avg_temp", 0);
		Point point1 = rdr.readPoint("gage_avg_temp", 1);
		Point point2 = rdr.readPoint("gage_avg_temp", 2);
		
		// Test point data
		Assert.assertEquals(-91.277, point0.getX(), 0.0001); Assert.assertEquals(40.75365, point0.getY(), 0.000001);
		Assert.assertEquals(-91.674, point1.getX(), 0.0003); Assert.assertEquals(40.9253, point1.getY(), 0.00001);
		Assert.assertEquals(-91.5515, point2.getX(), 0.00006); Assert.assertEquals(41, point2.getY(), 0.1);
		
		//Test data
		Assert.assertEquals(0.623, point0.getData().getDouble(0), 0.001);
		Assert.assertEquals(9.02, point1.getData().getDouble(3), 0.001);
		Assert.assertEquals(3.14, point2.getData().getDouble(1), 0.001);
	}
}
