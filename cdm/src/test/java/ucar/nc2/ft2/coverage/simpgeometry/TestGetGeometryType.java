package ucar.nc2.ft2.coverage.simpgeometry;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import ucar.nc2.dataset.NetcdfDataset;
import ucar.unidata.util.test.TestDir;

/**
 * Tests ability to get Simple Geometry Type using Simple Geometry Reader.
 * 
 * @author wchen@usgs.gov
 *
 */
public class TestGetGeometryType {
	
	private SimpleGeometryReader openReaderOverFile(String filename) throws IOException {
		String filepath = TestDir.cdmLocalTestDataDir + "dataset/SimpleGeos/" + filename;
		NetcdfDataset dataset = null;
		dataset = NetcdfDataset.openDataset(filepath);
		return new SimpleGeometryReader(dataset);
	}
	
	
	@Test
	public void testGetPointGeometryType() throws IOException {
		SimpleGeometryReader rdr = openReaderOverFile("avg_temp_3gage_5timesteps.nc");
		Assert.assertEquals(GeometryType.POINT, rdr.getGeometryType("gage_avg_temp"));
	}
	
	@Test
	public void testGetLineGeometryType() throws IOException {
		SimpleGeometryReader rdr = openReaderOverFile("outflow_3seg_5timesteps.nc");
		Assert.assertEquals(GeometryType.LINE, rdr.getGeometryType("seg_outflow"));
	}
	
	@Test
	public void testGetPolygonGeometryType() throws IOException {
		SimpleGeometryReader rdr = openReaderOverFile("hru_soil_moist_3hru_5timestep.nc");
		Assert.assertEquals(GeometryType.POLYGON, rdr.getGeometryType("hru_soil_moist"));
	}
}
