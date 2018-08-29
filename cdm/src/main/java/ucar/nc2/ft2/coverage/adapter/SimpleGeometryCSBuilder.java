/*
 * Copyright (c) 1998-2018 John Caron and University Corporation for Atmospheric Research/Unidata
 * See LICENSE for license information.
 */
package ucar.nc2.ft2.coverage.adapter;

import ucar.nc2.Dimension;
import ucar.nc2.constants.AxisType;
import ucar.nc2.constants.FeatureType;
import ucar.nc2.dataset.*;
import ucar.nc2.ft2.coverage.simpgeometry.GeometryType;
import ucar.nc2.ft2.coverage.simpgeometry.Line;
import ucar.nc2.ft2.coverage.simpgeometry.Point;
import ucar.nc2.ft2.coverage.simpgeometry.Polygon;
import ucar.nc2.ft2.coverage.simpgeometry.SimpleGeometryReader;
import ucar.unidata.geoloc.ProjectionImpl;

import java.util.*;

/**
 * Simple Geometry Coordinate System / Dataset Builder.
 * 
 * Forked from DtCoverageCSBuilder.java
 *
 * @author caron
 * @author wchen@usgs.gov
 * @since 8/22/2018
 */
public class SimpleGeometryCSBuilder {

  // classify based on largest coordinate system
  public static SimpleGeometryCSBuilder classify(NetcdfDataset ds, Formatter errlog) {
    if (errlog != null) errlog.format("CoverageFactory for '%s'%n", ds.getLocation());

    // sort by largest size first
    List<CoordinateSystem> css = new ArrayList<>(ds.getCoordinateSystems());
    Collections.sort(css, (o1, o2) -> o2.getCoordinateAxes().size() - o1.getCoordinateAxes().size());

    SimpleGeometryCSBuilder builder = null;
    for (CoordinateSystem cs : css) {
      builder = new SimpleGeometryCSBuilder(ds, cs, errlog);
      if (builder.type != null) break;
    }
    
    if (builder == null) return null;
    if (errlog != null) errlog.format("coverage = %s%n", builder.type);
    return builder;
  }

  public static String describe(NetcdfDataset ds, Formatter errlog) {
    SimpleGeometryCSBuilder fac = classify(ds, errlog);
    return (fac == null || fac.type == null) ? "" : fac.showSummary();
  }

  public static String describe(NetcdfDataset ds, CoordinateSystem cs, Formatter errlog) {
    SimpleGeometryCSBuilder fac = new SimpleGeometryCSBuilder(ds, cs, errlog);
    return fac.type == null ? "" : fac.showSummary();
  }

  
  FeatureType type;
  boolean isLatLon;
  List<CoordinateAxis> allAxes;
  List<CoordinateAxis> sgAxes;
  List<CoordinateTransform> coordTransforms;
  List<Dimension> dims;
  SimpleGeometryReader geometryReader;
  ProjectionImpl orgProj;

  SimpleGeometryCSBuilder(NetcdfDataset ds, CoordinateSystem cs, Formatter errlog) {

    // must be at least 2 dimensions
    if (cs.getRankDomain() < 2) {
      if (errlog != null) errlog.format("CoordinateSystem '%s': domain rank < 2%n", cs.getName());
      return;
    }
    
    sgAxes = new ArrayList<CoordinateAxis>();
    dims = ds.getDimensions();
    allAxes = cs.getCoordinateAxes();
    
    // Create Simple Geometry Reader if there are any Axes with type SimpleGeometryID
    // Also, populate simple geometry axis list 
    boolean sgtype = false;
    for(CoordinateAxis axis : cs.getCoordinateAxes()) {
    	if(axis.getAxisType().equals(AxisType.SimpleGeometryID) || axis.getAxisType().equals(AxisType.SimpleGeometryX)
    			|| axis.getAxisType().equals(AxisType.SimpleGeometryY) || axis.getAxisType().equals(AxisType.SimpleGeometryZ)) {
    		sgAxes.add(axis);
    		sgtype = true;
    	}
    }
    
    if(sgtype) {
    	geometryReader = new SimpleGeometryReader(ds);
    	this.type = FeatureType.SIMPLE_GEOMETRY;
    } else geometryReader = null;
    
    this.type = classify();
    this.coordTransforms = new ArrayList<>(cs.getCoordinateTransforms());
    this.orgProj = cs.getProjection();
  }

  private FeatureType classify () {

    // now to classify
    
    if(geometryReader != null) {
    	return FeatureType.SIMPLE_GEOMETRY;
    }

    // Default, station is similar to Simple Geometry
    return FeatureType.STATION;
  }

  public FeatureType getCoverageType() {
    return type;
  }
  
  /**
   * Given a variable name, returns the type of geometry which that variable is holding
   * 
   * @param name name of the variable
   * @return geometry type associated with that variable
   */
  public GeometryType getGeometryType(String name) {
	 return geometryReader.getGeometryType(name);
  }
  
  /**
   * Given a certain variable name and geometry index, returns a Simple Geometry Polygon.
   * 
   * @param name
   * @param index
   * @return polygon
   */
  public Polygon getPolygon(String name, int index)
  {
	  return geometryReader.readPolygon(name, index);
  }
  
  /**
   * Given a certain Polygon variable name and geometry begin and end indicies, returns a list of Simple Geometry Polygon
   * 
   * @param name
   * @param indexBegin
   * @param indexEnd
   * @return
   */
  public List<Polygon> getPolygons(String name, int indexBegin, int indexEnd) {
	  List<Polygon> polyList = new ArrayList<Polygon>();
	  
	  for(int i = indexBegin; i <= indexEnd; i++)
	  {
		  polyList.add(geometryReader.readPolygon(name, i));
	  }
	  
	  return polyList;
  }

  
  /**
   * Given a certain variable name and geometry index, returns a Simple Geometry Line.
   * 
   * @param name
   * @param index
   * @return line
   */
  public Line getLine(String name, int index)
  {
	  return geometryReader.readLine(name, index);
  }
  
  /**
   * Given a certain line variable name and geometry begin and end indicies, returns a list of Simple Geometry Line
   * 
   * @param name
   * @param indexBegin
   * @param indexEnd
   * @return
   */
  public List<Line> getLines(String name, int indexBegin, int indexEnd) {
	  List<Line> lineList = new ArrayList<Line>();
	  
	  for(int i = indexBegin; i <= indexEnd; i++)
	  {
		  lineList.add(geometryReader.readLine(name, i));
	  }
	  
	  return lineList;
  }

  
  /**
   * Given a certain variable name and geometry index, returns a Simple Geometry Point
   * 
   * 
   * @param name
   * @param index
   * @return
   */
  public Point getPoint(String name, int index)
  {
	  return geometryReader.readPoint(name, index);
  }
  
  /**
   * Given a certain Point variable name and geometry begin and end indicies, returns a list of Simple Geometry Points
   * 
   * @param name
   * @param indexBegin
   * @param indexEnd
   * @return
   */
  public List<Point> getPoints(String name, int indexBegin, int indexEnd) {
	  List<Point> ptList = new ArrayList<Point>();
	  
	  for(int i = indexBegin; i <= indexEnd; i++)
	  {
		  ptList.add(geometryReader.readPoint(name, i));
	  }
	  
	  return ptList;
  }

  public SimpleGeometryCS makeCoordSys() {
    if (type == null) return null;

    switch (type) {
      case SIMPLE_GEOMETRY:
    	return new SimpleGeometryCS(this);
    }
    
    return new SimpleGeometryCS(this);
  }

  @Override
  public String toString() {
    Formatter f2 = new Formatter();
    f2.format("%s", type == null ? "" : type.toString());
    if (type == null) return f2.toString();

    f2.format("}");
    f2.format("%n allAxes=(");
    for (CoordinateAxis axis : allAxes)
      f2.format("%s, ", axis.getShortName());
    f2.format(") {");
    for (Dimension dim : CoordinateSystem.makeDomain(allAxes))
      f2.format("%s, ", dim.getShortName());
    f2.format("}%n");

    return f2.toString();
  }

  public String showSummary() {
    if (type == null) return "";

    Formatter f2 = new Formatter();
    f2.format("%s", type.toString());

    f2.format("(");
    int count = 0;
    f2.format(")");

    return f2.toString();
  }

}
