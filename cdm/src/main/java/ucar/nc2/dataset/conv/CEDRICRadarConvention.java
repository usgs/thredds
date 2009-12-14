/*
 * Copyright (c) 1998 - 2009. University Corporation for Atmospheric Research/Unidata
 * Portions of this software were developed by the Unidata Program at the
 * University Corporation for Atmospheric Research.
 *
 * Access and use of this software shall impose the following obligations
 * and understandings on the user. The user is granted the right, without
 * any fee or cost, to use, copy, modify, alter, enhance and distribute
 * this software, and any derivative works thereof, and its supporting
 * documentation for any purpose whatsoever, provided that this entire
 * notice appears in all copies of the software, derivative works and
 * supporting documentation.  Further, UCAR requests that the user credit
 * UCAR/Unidata in any publications that result from the use of this
 * software or in any product that includes this software. The names UCAR
 * and/or Unidata, however, may not be used in any advertising or publicity
 * to endorse or promote any products or commercial entity unless specific
 * written permission is obtained from UCAR/Unidata. The user also
 * understands that UCAR/Unidata is not obligated to provide the user with
 * any support, consulting, training or assistance of any kind with regard
 * to the use, operation and performance of this software nor to provide
 * the user with any updates, revisions, new versions or "bug fixes."
 *
 * THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 * INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 * WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ucar.nc2.dataset.conv;

import ucar.nc2.dataset.CoordSysBuilder;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.Attribute;
import ucar.nc2.constants._Coordinate;
import ucar.nc2.ncml.NcMLReader;
import ucar.nc2.util.CancelTask;
import ucar.unidata.geoloc.ProjectionImpl;
import ucar.unidata.geoloc.projection.FlatEarth;
import ucar.unidata.util.Parameter;
import ucar.ma2.DataType;
import ucar.ma2.Array;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yuanho
 * Date: Dec 8, 2009
 * Time: 10:40:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class CEDRICRadarConvention extends CF1Convention {

  /**
   * @param ncfile test this NetcdfFile
   * @return true if we think this is a ATDRadarConvention file.
   */
  public static boolean isMine(NetcdfFile ncfile) {
    // not really sure until we can examine more files
    Dimension s = ncfile.findDimension("cedric_general_scaling_factor");
    Variable v = ncfile.findVariable("cedric_run_date");
    if(v != null && s != null)
        return true;
    else
        return false;
  }

  public CEDRICRadarConvention() {
    this.conventionName = "CEDRICRadar";
  }
      
  public void augmentDataset(NetcdfDataset ncDataset, CancelTask cancelTask) throws IOException {
 /*   float lat = 40.45f;
    float lon = -104.64f;
    ProjectionImpl projection = new FlatEarth(lat, lon);

    Variable ct = new Variable( ncDataset, null, null, projection.getClassName());
    ct.setDataType( DataType.CHAR);
    ct.setDimensions( "");
    List params = projection.getProjectionParameters();
    for (int i = 0; i < params.size(); i++) {
      Parameter p = (Parameter) params.get(i);
      ct.addAttribute( new Attribute(p));
    }
    ct.addAttribute( new Attribute(_Coordinate.TransformType, "Projection"));
    ct.addAttribute( new Attribute(_Coordinate.Axes, "GeoX GeoY"));
    ncDataset.getReferencedFile().addVariable(null, ct);
   */
    NcMLReader.wrapNcMLresource(ncDataset, CoordSysBuilder.resourcesDir + "CEDRICRadar.ncml", cancelTask);

    super.augmentDataset(ncDataset, cancelTask);

  //  ncDataset.finish();
  }
}


