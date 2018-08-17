package ucar.nc2.ft.simpgeometry;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ucar.ma2.Array;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ft2.coverage.simpgeometry.*;
import ucar.unidata.util.test.TestDir;

public class TestPoly {

	@Test
	public void testMultiPolygons() {

		NetcdfDataset data = null ;
		try {
			data = NetcdfDataset.openDataset(TestDir.cdmLocalTestDataDir + "dataset/SimpleGeos/hru_soil_moist_3hru_5timestep.nc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		SimpleGeometryReader rdr = new SimpleGeometryReader(data);
		Polygon poly = rdr.readPolygon("hru_soil_moist", 0);
		Polygon poly_2 = rdr.readPolygon("hru_soil_moist", 1);
		Polygon poly_3 = rdr.readPolygon("hru_soil_moist", 2);
				
		while(poly != null)
		{
			
				List<CFPoint> pts = poly.getPoints();
				Iterator<CFPoint> itr = pts.iterator();
				int i = 0;
				//while(itr.hasNext())
				//{
				//	i++;
				//	CFPoint pt = itr.next();
				//	println("( " + pt.getX() + " , " + pt.getY() + " )");
				//}

				poly = poly.getNext();
		}
		
		Assert.assertEquals(6233, poly.getPoints().size());
		Assert.assertEquals(5, poly_2.getPoints().size());
		Assert.assertEquals(6033, poly_2.getNext().getPoints().size());
		Assert.assertEquals(5135, poly_3.getPoints().size());
	}
	
}
