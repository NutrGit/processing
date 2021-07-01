package jp.nyatla.nyar4psg.utils;

import jp.nyatla.nyartoolkit.core.types.NyARIntSize;
import jp.nyatla.nyartoolkit.markersystem.NyARSensor;
import processing.core.PImage;

public class PImageSensor extends NyARSensor {
    private PImageRaster _src;

    public PImageSensor(NyARIntSize var1) {
        super(var1);
    }

    public void update(PImage var1) {
        this._src = new PImageRaster(var1);
        super.update(this._src);
    }
}
