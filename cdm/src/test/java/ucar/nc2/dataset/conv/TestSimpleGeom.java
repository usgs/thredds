package ucar.nc2.dataset.conv;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.Array;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.constants.CF;
import ucar.nc2.constants._Coordinate;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxisTimeHelper;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.time.Calendar;
import ucar.nc2.time.CalendarDate;
import ucar.unidata.util.test.TestDir;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import static java.lang.String.format;

public class TestSimpleGeom {
	
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String cfConvention = CF1Convention.class.getName();
    
    @Test
    public void testLine() throws IOException {
    	
    	String failMessage, found, expected;
        boolean testCond;

        String tstFile = TestDir.cdmLocalTestDataDir + "dataset/SimpleGeos/hru_soil_moist_vlen_3hru_5timestep.nc";

        // open the test file
        NetcdfDataset ncd = NetcdfDataset.openDataset(tstFile);

        // make sure this dataset used the cfConvention
        expected = cfConvention;
        found = ncd.getConventionUsed();
        testCond = found.equals(expected);
        failMessage = format("This dataset used the %s convention, but should have used the %s convention.", found, expected);
        Assert.assertTrue(failMessage, testCond);
        
        //check that attributes were filled in correctly
        List<Variable> vars = ncd.getVariables();
        for (Variable v : vars) {
        	if (v.findAttribute(CF.GEOMETRY) != null) {
		        Assert.assertNotNull(v.findAttribute(CF.NODES));
		        Assert.assertNotNull(v.findAttribute(CF.NODE_COUNT));
		        Assert.assertNotNull(v.findAttribute(CF.NODE_COORDINATES));
		        Assert.assertNotNull(v.findAttribute(_Coordinate.Axes));
        	}
        }        	
    	ncd.close();
    }
    
    @Test
    public void testPolygon() throws IOException {
    	
    	String failMessage, found, expected;
        boolean testCond;

        String tstFile = TestDir.cdmLocalTestDataDir + "dataset/SimpleGeos/outflow_3seg_5timesteps_vlen.nc";

        // open the test file
        NetcdfDataset ncd = NetcdfDataset.openDataset(tstFile);

        // make sure this dataset used the cfConvention
        expected = cfConvention;
        found = ncd.getConventionUsed();
        testCond = found.equals(expected);
        failMessage = format("This dataset used the %s convention, but should have used the %s convention.", found, expected);
        Assert.assertTrue(failMessage, testCond);
        
        //check that attributes were filled in correctly
        List<Variable> vars = ncd.getVariables();
        for (Variable v : vars) {
        	if (v.findAttribute(CF.GEOMETRY) != null) {
        		Assert.assertNotNull(v.findAttribute(CF.NODES));
		        Assert.assertNotNull(v.findAttribute(CF.NODE_COUNT));
		        Assert.assertNotNull(v.findAttribute(CF.NODE_COORDINATES));
		        Assert.assertNotNull(v.findAttribute(_Coordinate.Axes));
        	}
        }       
    	ncd.close();
    }    
}