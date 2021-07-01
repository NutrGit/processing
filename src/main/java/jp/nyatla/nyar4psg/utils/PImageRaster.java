package jp.nyatla.nyar4psg.utils;

import jp.nyatla.nyartoolkit.core.raster.rgb.format.NyARRgbRaster_INT1D_X8R8G8B8_32;
import processing.core.PImage;

public class PImageRaster extends NyARRgbRaster_INT1D_X8R8G8B8_32 {
    public static final int BUFFER_TYPE = 262402;
    protected int[] _buf;

    public PImageRaster(PImage var1) {
        super(var1.width, var1.height, var1.pixels);
    }
}
