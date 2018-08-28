package ucar.nc2.ft2.coverage.simpgeometry;

import com.google.errorprone.annotations.DoNotMock;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.ft2.coverage.CoverageReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import ucar.nc2.ft2.coverage.adapter.SimpleGeometryCS;

import static ucar.nc2.ft2.coverage.simpgeometry.CFGEOMETRY.CFLINE;
import static ucar.nc2.ft2.coverage.simpgeometry.CFGEOMETRY.CFPOINT;
import static ucar.nc2.ft2.coverage.simpgeometry.CFGEOMETRY.CFPOLYGON;

public class TestSimpleGeometryCoverage {

    String name = "name";
    DataType dt;
    List<Attribute> att = new ArrayList<Attribute>();
    String coordSysName = "coordsysname";
    String units = "units";
    String description = "desc";
    CoverageReader reader;
    Object user;
    CFGEOMETRY geometry;



    @Test (expected = RuntimeException.class)
    public void testSetCoordSysNull() {
        SimpleGeometryCoverage cov = new SimpleGeometryCoverage(name, dt, att, coordSysName, units, description, reader, user, geometry);
        SimpleGeometryCS cs = mock(SimpleGeometryCS.class);
        cov.setCoordSys(cs);
        cov.setCoordSys(cov.getCoordSys());
    }

    @Test
    public void testReadGeometry() throws IOException, InvalidRangeException {

        int index = 1;

        SimpleGeometryCS cs = mock(SimpleGeometryCS.class);

        Point point = mock(CFPoint.class);
        given(cs.getPoint(name, index)).willReturn(point);
        SimpleGeometryCoverage cov = new SimpleGeometryCoverage(name, dt, att, coordSysName, units, description, reader, user, CFPOINT);
        cov.setCoordSys(cs);
        Assert.assertEquals(cov.readGeometry(index), cs.getPoint(name, index));

        Line line = mock(CFLine.class);
        given(cs.getLine(name, index)).willReturn(line);
        cov = new SimpleGeometryCoverage(name, dt, att, coordSysName, units, description, reader, user, CFLINE);
        cov.setCoordSys(cs);
        Assert.assertEquals(cov.readGeometry(index), cs.getLine(name, index));

        Polygon polygon = mock(CFPolygon.class);
        given(cs.getPolygon(name, index)).willReturn(polygon);
        cov = new SimpleGeometryCoverage(name, dt, att, coordSysName, units, description, reader, user, CFPOLYGON);
        cov.setCoordSys(cs);
        Assert.assertEquals(cov.readGeometry(index), cs.getPolygon(name, index));

    }



}
