package ucar.nc2.ft2.coverage;

import ucar.nc2.ft.FeatureDataset;
import ucar.nc2.ft2.coverage.adapter.SimpleGeometryCS;

public interface SimpleGeometryFeatureDataset extends FeatureDataset {

    /**
     * A set of GridDatatype objects with the same Coordinate System.
     */
    public interface Gridset {

        /** all the GridDatatype in this Gridset use this GridCoordSystem
         * @return  the common GridCoordSystem
         */
        public SimpleGeometryCS getGeoCoordSystem();
    }
}
