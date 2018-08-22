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
	public void testLinePoint()
	{
		SimpleGeometryReader rdr = newReader();
		Assert.assertNotNull(rdr);
	}
}
