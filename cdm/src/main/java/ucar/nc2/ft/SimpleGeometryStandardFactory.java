/*
 * Copyright (c) 1998-2018 John Caron and University Corporation for Atmospheric Research/Unidata
 * See LICENSE for license information.
 */
package ucar.nc2.ft;

import ucar.nc2.constants.FeatureType;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ft2.coverage.adapter.SimpleGeometryCSBuilder;
import ucar.nc2.util.CancelTask;
import ucar.nc2.ft2.coverage.simpgeometry.SimpleGeometryFeatureDataset;

import java.io.IOException;
import java.util.Formatter;

/**
 * Standard factory for Simple Geometry datatypes. Forked from GridDatasetStandardFactory.java
 * 
 * @author caron
 * @author wchen@usgs.gov
 * @since 8/22/2018
 */
public class SimpleGeometryStandardFactory implements FeatureDatasetFactory {

  public Object isMine(FeatureType wantFeatureType, NetcdfDataset ncd, Formatter errlog) throws IOException {
    SimpleGeometryCSBuilder sgCoverage = SimpleGeometryCSBuilder.classify(ncd, errlog);
    if (sgCoverage == null || sgCoverage.getCoverageType() == null) return null;
    if (!match(wantFeatureType, sgCoverage.getCoverageType())) return null;
    return sgCoverage;
  }

  private boolean match(FeatureType wantFeatureType, FeatureType covType) {
    if (wantFeatureType == null || wantFeatureType == FeatureType.ANY) return true;
    // LOOK ever have to return false?
    return true;
  }

  public FeatureDataset open(FeatureType ftype, NetcdfDataset ncd, Object analysis, CancelTask task, Formatter errlog) throws IOException {

	  return new SimpleGeometryFeatureDataset( ncd);
  }

  public FeatureType[] getFeatureTypes() {
    return new FeatureType[] {FeatureType.GRID, FeatureType.FMRC, FeatureType.SWATH};
  }
}
