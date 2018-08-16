/*
 * Copyright (c) 1998-2018 John Caron and University Corporation for Atmospheric Research/Unidata
 * See LICENSE for license information.
 */

package ucar.nc2.ft2.coverage.adapter;

import java.util.List;

import ucar.nc2.Variable;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateAxis1DTime;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.time.CalendarDateRange;
import ucar.nc2.ft2.coverage.simpgeometry.*;

/**
 * Simple Geometry Coordinate System Implementation
 * Forked from ucar.nc2.ft2.coverage.adapter.GridCS
 *
 * @author John
 * @author wchen@usgs.gov
 * @since 8/9/2018
 */
public class SimpleGeometryCS extends DtCoverageCS {

  SimpleGeometryCS(DtCoverageCSBuilder builder) {
    super(builder);
  }

  @Override
  public boolean isRegularSpatial() {
    return getXHorizAxis().isRegular() && getYHorizAxis().isRegular();
  }

  @Override
  public CoordinateAxis1D getXHorizAxis() {
    return (CoordinateAxis1D) super.getXHorizAxis();
  }

  @Override
  public CoordinateAxis1D getYHorizAxis() {
    return (CoordinateAxis1D) super.getYHorizAxis();
  }
  
  /**
   * Given a Variable name and a geometry index
   * returns a Polygon 
   * 
   * @param name of the data variable
   * @param index within the variable
   * 
   * @return polygon with all associated data, null if not found
   */
  public Polygon getPolygon(String name, int index)
  {
	 return builder.getPolygon(name, index);
  }
  
  /**
   * Given a Variable name and a geometry index
   * returns a Line 
   * 
   * @param name of the data variable
   * @param index within the variable
   * 
   * @return line with all associated data, null if not found
   */
  public Line getLine(String name, int index)
  {
	 return builder.getLine(name, index);
  }
  
  /**
   * Given a Variable name and a geometry index
   * returns a Point
   * 
   * @param name of the data variable
   * @param index within the variable
   * @return point with all associated data, null if not found
   */
  public Point getPoint(String name, int index)
  {
	 return builder.getPoint(name,index);
  }
  
  /**
   * Given a Variable name and a beginning index and end index
   * returns a list of points (inclusive on both sides)
   * 
   * @param name of the data variable
   * @param index_begin within the variable
   * @param index_end within the varible
   * @return a list of points with associated data
   */
  public List<Point> getPoints(String name, int index_begin, int index_end)
  {
	  return builder.getPoints(name, index_begin, index_end);
  }
  
  // LOOK another possibility is a scalar runtime and a 1D time offset
  
  @Override
  public CoordinateAxis1DTime getTimeAxis() {
    return (CoordinateAxis1DTime) super.getTimeAxis();
  }

  @Override
  public CalendarDateRange getCalendarDateRange() {
    if (getTimeAxis() != null)
      return getTimeAxis().getCalendarDateRange();

    else if (getRunTimeAxis() != null)
      return getRunTimeAxis().getCalendarDateRange();

    else
      return null;
  }


}
