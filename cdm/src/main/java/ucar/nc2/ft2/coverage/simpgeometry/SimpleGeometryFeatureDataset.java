package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.VariableSimpleIF;
import ucar.nc2.constants.CDM;
import ucar.nc2.constants.FeatureType;
import ucar.nc2.dataset.*;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GeoGrid;
import ucar.nc2.ft.FeatureDataset;
import ucar.nc2.ft2.coverage.adapter.SimpleGeometryCS;
import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateRange;
import ucar.nc2.util.cache.FileCacheIF;
import ucar.unidata.geoloc.LatLonRect;

import java.io.IOException;
import java.util.*;

public class SimpleGeometryFeatureDataset implements FeatureDataset {

    private NetcdfDataset ncd;

    /**
     * Open a netcdf dataset, using NetcdfDataset.defaultEnhanceMode plus CoordSystems
     * and turn into a SimpleGeometryFeatureDataset.
     *
     * @param location netcdf dataset to open, using NetcdfDataset.acquireDataset().
     * @return SimpleGeometryFeatureDataset
     * @throws java.io.IOException on read error
     * @see ucar.nc2.dataset.NetcdfDataset#acquireDataset
     */
    static public SimpleGeometryFeatureDataset open(String location) throws IOException {
        return open(location, NetcdfDataset.getDefaultEnhanceMode());
    }

    /**
     * Open a netcdf dataset, using NetcdfDataset.defaultEnhanceMode plus CoordSystems
     * and turn into a SimpleGeometryFeatureDataset.
     *
     * @param location netcdf dataset to open, using NetcdfDataset.acquireDataset().
     * @param enhanceMode open netcdf dataset with this enhanceMode
     * @return SimpleGeometryFeatureDataset
     * @throws java.io.IOException on read error
     * @see ucar.nc2.dataset.NetcdfDataset#acquireDataset
     */
    static public SimpleGeometryFeatureDataset open(String location, Set<NetcdfDataset.Enhance> enhanceMode) throws IOException {
        NetcdfDataset ds = ucar.nc2.dataset.NetcdfDataset.acquireDataset(null, DatasetUrl.findDatasetUrl(location), enhanceMode, -1, null, null);
        return new SimpleGeometryFeatureDataset(ds, null);
    }

    /**
     * Create a SimpleGeometryFeatureDataset from a NetcdfDataset.
     *
     * @param ncd underlying NetcdfDataset, will do Enhance.CoordSystems if not already done.
     * @throws java.io.IOException on read error
     */
    public SimpleGeometryFeatureDataset(NetcdfDataset ncd) throws IOException {
        this(ncd, null);
    }

    /**
     * Create a SimpleGeometryFeatureDataset from a NetcdfDataset.
     *
     * @param ncd underlying NetcdfDataset, will do Enhance.CoordSystems if not already done.
     * @param parseInfo put parse info here, may be null
     * @throws java.io.IOException on read error
     */
    public SimpleGeometryFeatureDataset(NetcdfDataset ncd, Formatter parseInfo) throws IOException {
        this.ncd = ncd;
        // ds.enhance(EnumSet.of(NetcdfDataset.Enhance.CoordSystems));
        Set<NetcdfDataset.Enhance> enhance = ncd.getEnhanceMode();
        if(enhance == null || enhance.isEmpty()) enhance = NetcdfDataset.getDefaultEnhanceMode();
        ncd.enhance(enhance);
    }

    // stuff to satisfy ucar.nc2.dt.TypedDataset
    public String getTitle() {
        String title = ncd.getTitle();
        if (title == null)
            title = ncd.findAttValueIgnoreCase(null, CDM.TITLE, null);
        if (title == null)
            title = getName();
        return title;
    }

    public String getDescription() {
        String desc = ncd.findAttValueIgnoreCase(null, "description", null);
        if (desc == null)
            desc = ncd.findAttValueIgnoreCase(null, CDM.HISTORY, null);
        return (desc == null) ? getName() : desc;
    }

    public String getLocation() {
        return (ncd != null) ?  ncd.getLocation() : "";
    }

    @Override
    public CalendarDateRange getCalendarDateRange() {
        return null;
    }

    @Override
    public CalendarDate getCalendarDateStart() {
        return null;
    }

    @Override
    public CalendarDate getCalendarDateEnd() {
        return null;
    }

    @Override
    public LatLonRect getBoundingBox() {
        return null;
    }

    public List<Attribute> getGlobalAttributes() {
        return ncd.getGlobalAttributes();
    }

    public Attribute findGlobalAttributeIgnoreCase(String name) {
        return ncd.findGlobalAttributeIgnoreCase(name);
    }

    @Override
    public List<VariableSimpleIF> getDataVariables() {
        return null;
    }


    public VariableSimpleIF getDataVariable(String shortName) {
        return ncd.getRootGroup().findVariable(shortName);
    }

    public NetcdfFile getNetcdfFile() {
        return ncd;
    }

    @Override
    public void close() throws IOException {
        ncd.close();
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public void setFileCache(FileCacheIF fileCache) {
        ncd.setFileCache(fileCache);
    }

    @Override
    public void getDetailInfo(Formatter sf) {
        ncd.getDetailInfo(sf);
    }

    /**
     * the name of the dataset is the last part of the location
     * @return the name of the dataset
     */
    public String getName() {
        String loc = ncd.getLocation();
        int pos = loc.lastIndexOf('/');
        if (pos < 0)
            pos = loc.lastIndexOf('\\');
        return (pos < 0) ? loc : loc.substring(pos+1);
    }

    /**
     * @return the underlying NetcdfDataset
     */
    public NetcdfDataset getNetcdfDataset() {
        return ncd;
    }

    /**
     * This is a set of GeoGrids with the same GeoCoordSys.
     */
    public static class Gridset  {

        private SimpleGeometryCS gcc;
        private List<GridDatatype> grids = new ArrayList<>();

        private Gridset(SimpleGeometryCS gcc) {
            this.gcc = gcc;
        }

        private void add(GeoGrid grid) {
            grids.add(grid);
        }

        /**
         * Get list of GeoGrid objects
         */
        public List<GridDatatype> getGrids() {
            return grids;
        }

        /**
         * all GridDatatype point to this GridCoordSystem
         */
        public SimpleGeometryCS getSGCoordSystem() {
            return gcc;
        }

        /**
         * all GeoGrids point to this GeoCoordSysImpl.
         *
         * @deprecated use getGeoCoordSystem() if possible.
         */
        public SimpleGeometryCS getSGCoordSys() {
            return gcc;
        }

    }

    ////////////////////////////
    // for ucar.nc2.ft.FeatureDataset

    public FeatureType getFeatureType() {
        return FeatureType.SIMPLE_GEOMETRY;
    }

    public String getImplementationName() {
        return ncd.getConventionUsed();
    }

    // release any resources like file handles
    public void release() throws IOException {
        if (ncd != null) ncd.release();
    }

    // reacquire any resources like file handles
    public void reacquire() throws IOException {
        if (ncd != null) ncd.reacquire();
    }

    /////////////////////////////
    // deprecated


    /**
     * Open a netcdf dataset, parse Conventions, find all the geoGrids, return a GridDataset.
     *
     * @deprecated : use GridDataset.open().
     */
    static public SimpleGeometryFeatureDataset factory(String netcdfFileURI) throws IOException {
        return open(netcdfFileURI);
    }
}
