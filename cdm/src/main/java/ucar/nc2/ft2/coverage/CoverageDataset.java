/* Copyright */
package ucar.nc2.ft2.coverage;

import net.jcip.annotations.Immutable;
import ucar.nc2.Attribute;
import ucar.nc2.AttributeContainerHelper;
import ucar.nc2.constants.AxisType;
import ucar.nc2.time.CalendarDateRange;
import ucar.nc2.util.Indent;
import ucar.unidata.geoloc.LatLonRect;
import ucar.unidata.geoloc.ProjectionRect;

import java.io.IOException;
import java.util.*;

/**
 * A Coverage Dataset
 *
 * @author caron
 * @since 7/11/2015
 */
@Immutable
public class CoverageDataset implements AutoCloseable, CoordSysContainer {

  private final List<CoordSysSet> coverageSets;

  private final String name;
  private final AttributeContainerHelper atts;
  private final LatLonRect latLonBoundingBox;
  private final ProjectionRect projBoundingBox;
  private final CalendarDateRange calendarDateRange;

  private final List<CoverageCoordSys> coordSys;
  private final List<CoverageTransform> coordTransforms;
  private final List<CoverageCoordAxis> coordAxes;
  private final Map<String, Coverage> coverageMap = new HashMap<>();

  private final CoverageCoordSys.Type coverageType;
  protected final CoverageReader reader;

  public CoverageDataset(String name, CoverageCoordSys.Type coverageType, AttributeContainerHelper atts,
                         LatLonRect latLonBoundingBox, ProjectionRect projBoundingBox, CalendarDateRange calendarDateRange,
                         List<CoverageCoordSys> coordSys, List<CoverageTransform> coordTransforms, List<CoverageCoordAxis> coordAxes, List<Coverage> coverages,
                         CoverageReader reader) {
    this.name = name;
    this.atts = atts;
    this.latLonBoundingBox = latLonBoundingBox;
    this.projBoundingBox = projBoundingBox;
    this.calendarDateRange = calendarDateRange;
    this.coverageType = coverageType;

    this.coordSys = coordSys;
    this.coordTransforms = coordTransforms;
    this.coordAxes = coordAxes;

    this.coverageSets = wireObjectsTogether(coverages);
    this.reader = reader;
  }

  private List<CoordSysSet> wireObjectsTogether(List<Coverage> coverages) {
    Map<String, CoordSysSet> map = new HashMap<>();

    for (Coverage coverage : coverages) {
      coverageMap.put(coverage.getName(), coverage);
      CoordSysSet gset = map.get(coverage.getCoordSysName());
      if (gset == null) {
        gset = new CoordSysSet(findCoordSys(coverage.getCoordSysName())); // must use findByName because objects arent wired up yet
        map.put(coverage.getCoordSysName(), gset);
        gset.getCoordSys().setDataset(this);  // wire dataset into coordSys
      }
      gset.addCoverage(coverage);
      coverage.setCoordSys(gset.getCoordSys()); // wire coordSys into coverage
    }

        // sort the coordsys
    List<CoordSysSet> csets = new ArrayList<>(map.values());
    Collections.sort(csets, (o1, o2) -> o1.getCoordSys().getName().compareTo(o2.getCoordSys().getName()));

    // construct the HorizCoordSys
    Map<String, HorizCoordSys> hcsMap = new HashMap<>();
    for (CoordSysSet cset : csets) {
      CoverageCoordSys coordsys = cset.getCoordSys();

      CoverageCoordAxis xaxis = coordsys.getAxis(AxisType.GeoX);
      CoverageCoordAxis yaxis = coordsys.getAxis(AxisType.GeoY);
      CoverageCoordAxis lataxis = coordsys.getAxis(AxisType.Lat);
      CoverageCoordAxis lonaxis = coordsys.getAxis(AxisType.Lon);

      CoverageTransform hct = coordsys.getHorizTransform();
      HorizCoordSys hcs = new HorizCoordSys(xaxis, yaxis, lataxis, lonaxis, hct);
      HorizCoordSys old = hcsMap.get(hcs.getName());
      if (old == null)
        hcsMap.put(hcs.getName(), hcs);
      else
        hcs = old;
      coordsys.setHorizCoordSys(hcs);
    }

    // wire in the "CoordSysContainer"
    for (CoverageCoordAxis axis : coordAxes) {
      axis.setDataset( this);
    }

    return csets;
  }

  @Override
  public void close() throws IOException {
    try {
      reader.close();
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  public String findAttValueIgnoreCase(String attName, String defaultValue) {
    return atts.findAttValueIgnoreCase(attName, defaultValue);
  }

  public Attribute findAttribute(String attName) {
    return atts.findAttribute(attName);
  }

  public Attribute findAttributeIgnoreCase(String attName) {
    return atts.findAttributeIgnoreCase(attName);
  }

  public String getName() {
    return name;
  }

  public List<Attribute> getGlobalAttributes() {
    return atts.getAttributes();
  }

  public LatLonRect getLatLonBoundingBox() {
    return latLonBoundingBox;
  }

  public ProjectionRect getProjBoundingBox() {
    return projBoundingBox;
  }

  public CalendarDateRange getCalendarDateRange() {
    return calendarDateRange;
  }

  public ucar.nc2.time.Calendar getCalendar() {
    if (calendarDateRange != null)
      return calendarDateRange.getStart().getCalendar();
    return ucar.nc2.time.Calendar.getDefault();
  }

  public Iterable<Coverage> getCoverages() {
    return coverageMap.values();
  }

  public int getCoverageCount() {
    return coverageMap.values().size();
  }

  public CoverageCoordSys.Type getCoverageType() {
    return coverageType;
  }

  public List<CoordSysSet> getCoverageSets() {
    return coverageSets;
  }

  public List<CoverageCoordSys> getCoordSys() {
    return coordSys;
  }

  public List<CoverageTransform> getCoordTransforms() {
    return (coordTransforms != null) ? coordTransforms : new ArrayList<>();
  }

  public List<CoverageCoordAxis> getCoordAxes() {
    return coordAxes;
  }

  public HorizCoordSys getHorizCoordSys() {
    return coordSys.get(0).getHorizCoordSys(); // LOOK kludge
  }

  @Override
  public String toString() {
    Formatter f = new Formatter();
    toString(f);
    return f.toString();
  }

  public void toString(Formatter f) {
    Indent indent = new Indent(2);
    f.format("%sGridDatasetCoverage %s%n", indent, name);
    f.format("%s Global attributes:%n", indent);
    for (Attribute att : atts.getAttributes())
      f.format("%s  %s%n", indent, att);
    f.format("%s Date Range:%s%n", indent, calendarDateRange);
    f.format("%s LatLon BoundingBox:%s%n", indent, latLonBoundingBox);
    if (projBoundingBox != null)
      f.format("%s Projection BoundingBox:%s%n", indent, projBoundingBox);

    f.format("%n%s Coordinate Systems:%n", indent);
    for (CoverageCoordSys cs : coordSys)
      cs.toString(f, indent);
    f.format("%s Coordinate Transforms:%n", indent);
    for (CoverageTransform t : coordTransforms)
      t.toString(f, indent);
    f.format("%s Coordinate Axes:%n", indent);
    for (CoverageCoordAxis a : coordAxes)
      a.toString(f, indent);

    f.format("%n%s Grids:%n", indent);
    for (Coverage grid : getCoverages())
      grid.toString(f, indent);
  }

  ////////////////////////////////////////////////////////////

  public Coverage findCoverage(String name) {
    return coverageMap.get(name);
  }

  public CoverageCoordSys findCoordSys(String name) {
    for (CoverageCoordSys gcs : coordSys)
      if (gcs.getName().equalsIgnoreCase(name)) return gcs;
    return null;
  }

  public CoverageCoordAxis findCoordAxis(String name) {
    for (CoverageCoordAxis axis : coordAxes) {
      if (axis.getName().equalsIgnoreCase(name)) return axis;
    }
    return null;
  }

  public CoverageTransform findCoordTransform(String name) {
    for (CoverageTransform ct : coordTransforms)
      if (ct.getName().equalsIgnoreCase(name)) return ct;
    return null;
  }

  ////////////////////////////////////////////////

  CoverageDataset(CoverageDataset from, List<CoverageCoordSys> coordSys, List<CoverageCoordAxis> coordAxes) {
    this.name = from.name;
    this.atts = from.atts;
    this.coverageType = from.coverageType;

    this.coordSys = coordSys;
    this.coordTransforms = from.coordTransforms;
    this.coordAxes = coordAxes;
    this.reader = from.reader;

    // ??
    this.latLonBoundingBox = null;
    this.projBoundingBox = null;
    this.calendarDateRange = null;
    this.coverageSets = null;
  }

}
