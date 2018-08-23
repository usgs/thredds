package ucar.nc2.ft2.coverage.simpgeometry;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import ucar.ma2.Array;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ft2.coverage.simpgeometry.*;
import ucar.unidata.util.test.TestDir;

/**
 * Line construction tests at the dataset level.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestReadLine {

	private SimpleGeometryReader newReader()
	{
		String filepath = TestDir.cdmLocalTestDataDir + "dataset/SimpleGeos/outflow_3seg_5timesteps.nc";
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
	public void testReadLine()
	{
		SimpleGeometryReader rdr = newReader();
		Assert.assertNotNull(rdr);
		
		Line line_0 = rdr.readLine("seg_outflow", 0);
		Line line_1 = rdr.readLine("seg_outflow", 1);
		Line line_2 = rdr.readLine("seg_outflow", 2);
		
		// Test data
		Assert.assertEquals(0.462, line_0.getData().getDouble(0), 0.001);
		Assert.assertEquals(3.61, line_1.getData().getDouble(3), 0.01);
		Assert.assertEquals(9.49, line_2.getData().getDouble(1), 0.01);
		
		// Test point amounts
		Assert.assertEquals(1125, line_0.getPoints().size());
		Assert.assertEquals(10, line_1.getPoints().size());
		Assert.assertEquals(2280, line_2.getPoints().size());
	}
}
