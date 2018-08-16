/*
 * Copyright (c) 1998-2018 John Caron and University Corporation for Atmospheric Research/Unidata
 * See LICENSE for license information.
 */
package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.IsMissingEvaluator;
import ucar.ma2.Section;
import ucar.nc2.Attribute;
import ucar.nc2.AttributeContainerHelper;
import ucar.nc2.Dimension;
import ucar.nc2.VariableSimpleIF;
import ucar.nc2.util.Indent;
import ucar.nc2.ft2.SimpleGeoms.CFGEOMETRY;
import ucar.nc2.ft2.coverage.CoverageReader;
import ucar.nc2.ft2.coverage.SubsetParams;
import ucar.nc2.ft2.coverage.adapter.SimpleGeometryCS;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * SimpleGeomery - forked from Coverage.java
 * Immutable after setCoordSys() is called.
 *
 * @author Katie
 * @author Carron
 * @since 8/13/2018
 */
// @Immutable
public class SimpleGeometryCoverage implements VariableSimpleIF, IsMissingEvaluator { //add interface with geometry info? //subsetting data will be diffferent?? is it subset now
  private final String name;
  private final DataType dataType;
  private final AttributeContainerHelper atts;
  private final String units, description;
  private final String coordSysName;
  protected final CoverageReader reader;
  protected final Object user;
  
  private final CFGEOMETRY geometry; //use enum?

  private SimpleGeometryCS coordSys; // almost immutable use coordsys that winor made?

  public SimpleGeometryCoverage(String name, DataType dataType, List<Attribute> atts, String coordSysName, String units, String description, CoverageReader reader, Object user, CFGEOMETRY geometry) {
    this.name = name;
    this.dataType = dataType;
    this.atts = new AttributeContainerHelper(name, atts);
    this.coordSysName = coordSysName;
    this.units = units;
    this.description = description;
    this.reader = reader;
    this.user = user;
    this.geometry = geometry;
  }

  // copy constructor
  public SimpleGeometryCoverage(SimpleGeometryCoverage from, SimpleGeometryCS coordSysSubset) {
    this.name = from.getName();
    this.dataType = from.getDataType();
    this.atts = from.atts;
    this.units = from.getUnitsString();
    this.description = from.getDescription();
    this.coordSysName = (coordSysSubset != null) ? coordSysSubset.getName() : from.coordSysName;
    this.reader = from.reader;
    this.user = from.user;
    this.geometry = from.geometry;
  }

  void setCoordSys (SimpleGeometryCS coordSys) {
    if (this.coordSys != null) throw new RuntimeException("Cant change coordSys once set");
    this.coordSys = coordSys;
  }

  public String getName() {
    return name;
  }


  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public List<Attribute> getAttributes() {
    return atts.getAttributes();
  }

  @Override
  public Attribute findAttributeIgnoreCase(String name) {
    return atts.findAttributeIgnoreCase(name);
  }

  @Override
  public String getUnitsString() {
    return units;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public String getCoordSysName() {
    return coordSysName;
  }

  public Object getUserObject() {
    return user;
  }
  public CFGEOMETRY getGeometry() {
	  return geometry; 
  }
  public String getGeometryDescription() {
	  switch (geometry) {
	  
	  case CFPOINT:
		  return "Point";
	  case CFLINE:
		  return "Line";
	  case CFPOLYGON:
		  return "Polygon";
	  default:
		  return "";
	  }
  }
  @Override
  public String toString() {
    Formatter f = new Formatter();
    Indent indent = new Indent(2);
    toString(f, indent);
    return f.toString();
  }

  public void toString(Formatter f, Indent indent) {
    indent.incr();
    f.format("%n%s  %s %s(%s) desc='%s' units='%s' geometry='%s'%n", indent, dataType, name, coordSysName, description, units, this.getGeometryDescription());
    f.format("%s    attributes:%n", indent);
    for (Attribute att : atts.getAttributes())
      f.format("%s     %s%n", indent, att);
    indent.decr();
  }

  @Nonnull
  public SimpleGeometryCS getCoordSys() {
    return coordSys;
  }

  ///////////////////////////////////////////////////////////////

  public long getSizeInBytes() {
    Section section = new Section(coordSys.getShape());
    return section.computeSize() * getDataType().getSize();
  }

  // LOOK must conform to whatever grid.readData() returns
  // LOOK need to deal with runtime(time), runtime(runtime, time)
  public String getIndependentAxisNamesOrdered() {
    StringBuilder sb = new StringBuilder();
    for (CoverageCoordAxis axis : coordSys.getAxes()) {
      if (!(axis.getDependenceType() == CoverageCoordAxis.DependenceType.independent)) continue;
      sb.append(axis.getName());
      sb.append(" ");
    }
    return sb.toString();
  }

  @Override
  public boolean hasMissing() {
    return true;
  }

  @Override
  public boolean isMissing(double val) {
    return Double.isNaN(val);
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
  public GeoReferencedArray readData(SubsetParams subset) throws IOException, InvalidRangeException {
    return reader.readData(this, subset, false);
  }
  
 */
  
	/**
	 * Get the data associated the index
	 * @param  index  number associated with the geometry 
	 */
  //COME BACK TO: does is make sense for geometry to be a feild in coverage type?
  public SimpleGeometry readGeometry(int index) throws IOException, InvalidRangeException {

	  SimpleGeometry geom = null;
	  switch (geometry) {
		  
		  case CFPOINT:
			  //implement getPoint
			  break;
		  case CFLINE:
			  Line line = coordSys.getLine(name, index);
			  geom = line;
			  break;
		  case CFPOLYGON:
			  Polygon poly = coordSys.getPolygon(name, index);
			  geom = poly;
			  break;
		  }
	  return geom;
  }

	 public List<SimpleGeometry> readGeometries(SubsetParams params){
		 
		 List<SimpleGeometry> geometries = null;
		 switch (geometry) {
		  
		  case CFPOINT:
			  geometries = coordSys.getPoints(name, params);
			  break;
		  case CFLINE:
			  geometries = coordSys.getLines(name, params);
			  break;
		  case CFPOLYGON:
			  geometries = coordSys.getPolygons(name, params);
			  break;
		  }
		 
		 return geometries;
	 }
  
  ////////////////////////////////////////////////////////////////////////////////////////
  // implement VariableSimpleIF

  @Override
  public String getFullName() {
    return getName();
  }

  @Override
  public String getShortName() {
    return getName();
  }

  @Override
  public int getRank() {
    return getShape().length;
  }

  @Override
  public int[] getShape() {
    return coordSys.getShape();
  }

 // @Override
  public List<Dimension> getDimensions() {
    return null;
  }

  @Override
  public int compareTo(@Nonnull VariableSimpleIF o) {
    return getFullName().compareTo(o.getFullName());
  }
}
