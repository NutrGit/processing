package jp.nyatla.nyar4psg;

import java.io.InputStream;
import java.util.ArrayList;

import jp.nyatla.nyar4psg.utils.NyARPsgBaseClass;
import jp.nyatla.nyar4psg.utils.PImageRaster;
import jp.nyatla.nyar4psg.utils.PImageSensor;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.types.NyARDoublePoint2d;
import jp.nyatla.nyartoolkit.core.types.NyARDoublePoint3d;
import jp.nyatla.nyartoolkit.core.types.NyARIntPoint2d;
import jp.nyatla.nyartoolkit.core.types.NyARIntSize;
import jp.nyatla.nyartoolkit.markersystem.NyARMarkerSystem;
import jp.nyatla.nyartoolkit.markersystem.NyARMarkerSystemConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

public class MultiMarker extends NyARPsgBaseClass {
    protected final PImageSensor _ss;
    protected final NyARMarkerSystem _ms;
    private final int _coordinate_system;
    public static final double DEFAULT_CF_THRESHOLD = 0.51D;
    public static final int DEFAULT_LOST_DELAY = 10;
    public static final int THLESHOLD_AUTO = -1;
    private ArrayList<Integer> _id_map;
    private boolean _is_in_begin_end_session;

    public void setConfidenceThreshold(double var1) {
        this._ms.setConfidenceThreshold(var1);
    }

    public void setLostDelay(int var1) {
        this._ms.setLostDelay(var1);
    }

    public void setThreshold(int var1) {
        int var2 = var1 == -1 ? 2147483647 : var1;
        this._ms.setBinThreshold(var2);
    }

    public int getCurrentThreshold() {
        return this._ms.getCurrentThreshold();
    }

    private MultiMarker(PApplet var1, SingleCameraView var2, NyAR4PsgConfig var3) {
        super(var1, var2);
        this._id_map = new ArrayList();
        this._is_in_begin_end_session = false;
        NyARIntSize var4 = var2._view.getARParam().getScreenSize();
        this._ss = new PImageSensor(new NyARIntSize(var4.w, var4.h));
        this._ms = new NyARMarkerSystem(new NyARMarkerSystemConfig(var2._view, var3.env_transmat_mode));
        this._coordinate_system = var3._coordinate_system;
    }

    public MultiMarker(PApplet var1, int var2, int var3, InputStream var4, NyAR4PsgConfig var5) {
        this(var1, new SingleCameraView(var1, NyARParam.loadFromARParamFile(var4, var2, var3), var5._ps_patch_version), var5);
    }

    public MultiMarker(PApplet var1, int var2, int var3, String var4, NyAR4PsgConfig var5) {
        this(var1, new SingleCameraView(var1, NyARParam.loadFromARParamFile(var1.createInput(var4), var2, var3), var5._ps_patch_version), var5);
    }

    public MultiMarker(PApplet var1, int var2, int var3, String var4) {
        this(var1, var2, var3, var4, NyAR4PsgConfig.CONFIG_OLD);
    }

    public MultiMarker(PApplet var1, int var2, int var3, double[] var4, double[] var5, int var6, int var7, NyAR4PsgConfig var8) {
        this(var1, new SingleCameraView(var1, NyARParam.loadFromCvCalibrateCamera2Result(var2, var3, var4, var5, var6, var7), var8._ps_patch_version), var8);
    }

    public MultiMarker(PApplet var1, int var2, int var3, double[] var4, double[] var5, int var6, int var7, int var8, int var9) {
        this(var1, var2, var3, var4, var5, var6, var7, new NyAR4PsgConfig(var8, var9));
    }

    public MultiMarker(PApplet var1, int var2, int var3, double[] var4, double[] var5, int var6, int var7) {
        this(var1, var2, var3, var4, var5, var6, var7, NyAR4PsgConfig.CONFIG_OLD);
    }

    public void beginTransform(int var1) {
        if (this._is_in_begin_end_session) {
            this._ref_papplet.die("The function beginTransform is already called.", (Exception) null);
        }

        this._is_in_begin_end_session = true;
        if (!(this._ref_papplet.g instanceof PGraphicsOpenGL)) {
            this._ref_papplet.die("NyAR4Psg require PGraphicsOpenGL instance.");
        }

        PGraphicsOpenGL var2 = (PGraphicsOpenGL) this._ref_papplet.g;
        var2.pushProjection();
        this.setARPerspective();
        this._ref_papplet.pushMatrix();
        this._ref_papplet.setMatrix(this.getMatrix(var1));
    }

    public void endTransform() {
        if (!this._is_in_begin_end_session) {
            this._ref_papplet.die("The function beginTransform is never called.", (Exception) null);
        }

        this._is_in_begin_end_session = false;
        this._ref_papplet.popMatrix();
        PGraphicsOpenGL var1 = (PGraphicsOpenGL) this._ref_papplet.g;
        var1.popProjection();
    }

    public void detect(PImage var1) {
        this.detect(var1, true);
    }

    public void detect(PImage var1, boolean var2) {
        if (var2) {
            var1.loadPixels();
            this._ss.update(var1);
            var1.updatePixels();
        } else {
            this._ss.update(var1);
        }

        this.detect(this._ss);
    }

    public void detect(PImageSensor var1) {
        this._ms.update(var1);
    }

    public int addARMarker(InputStream var1, int var2, int var3, float var4) {
        int var5 = -1;

        try {
            this._id_map.add(this._ms.addARMarker(var1, var2, var3, (double) var4));
            var5 = this._id_map.size() - 1;
        } catch (Exception var7) {
            var7.printStackTrace();
            this._ref_papplet.die("Catch an exception!");
        }

        return var5;
    }

    public int addARMarker(String var1, int var2, int var3, float var4) {
        return this.addARMarker(this._ref_papplet.createInput(var1), var2, var3, var4);
    }

    public int addARMarker(String var1, int var2, float var3) {
        return this.addARMarker((String) var1, var2, 25, var3);
    }

    public int addARMarker(PImage var1, int var2, int var3, float var4) {
        int var5 = -1;

        try {
            var1.loadPixels();
            PImageRaster var6 = new PImageRaster(var1);
            this._id_map.add(this._ms.addARMarker(var6, var2, var3, (double) var4));
            var5 = this._id_map.size() - 1;
        } catch (Exception var7) {
            var7.printStackTrace();
            this._ref_papplet.die("Catch an exception!");
        }

        return var5;
    }

    public int addARMarker(String var1, float var2) {
        return this.addARMarker((String) var1, 16, 25, var2);
    }

    public int addNyIdMarker(int var1, int var2) {
        return this.addNyIdMarker(var1, var1, var2);
    }

    public int addNyIdMarker(int var1, int var2, int var3) {
        int var4 = -1;

        try {
            this._id_map.add(this._ms.addNyIdMarker((long) var1, (long) var2, (double) var3));
            var4 = this._id_map.size() - 1;
        } catch (Exception var6) {
            var6.printStackTrace();
            this._ref_papplet.die("Catch an exception!");
        }

        return var4;
    }

    public int addPsARPlayCard(int var1, int var2) {
        int var3 = -1;

        try {
            this._id_map.add(this._ms.addPsARPlayCard(var1, (double) var2));
            var3 = this._id_map.size() - 1;
        } catch (Exception var5) {
            var5.printStackTrace();
            this._ref_papplet.die("Catch an exception!");
        }

        return var3;
    }

    public PVector[] getMarkerVertex2D(int var1) {
        int mapId = (Integer) this._id_map.get(var1);
        PVector[] pVectors = new PVector[4];

        try {
            NyARIntPoint2d[] point2ds = this._ms.getVertex2D(mapId);

            for (int j = 0; j < 4; ++j) {
                pVectors[j] = new PVector((float) point2ds[j].x, (float) point2ds[j].y);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this._ref_papplet.die("Catch an exception!");
        }

        return pVectors;
    }

    public double getConfidence(int var1) {
        int var2 = (Integer) this._id_map.get(var1);

        try {
            return this._ms.getConfidence(var2);
        } catch (Exception var4) {
            this._ref_papplet.die("Marker id " + var1 + " is not AR Marker or not exist.", (Exception) null);
            return 0.0D / 0.0;
        }
    }

    public long getNyId(int var1) {
        int var2 = (Integer) this._id_map.get(var1);

        try {
            return this._ms.getNyId(var2);
        } catch (Exception var4) {
            this._ref_papplet.die("Marker id " + var1 + " is not NyId Marker or not exist.", (Exception) null);
            return -1L;
        }
    }

    public int getLostCount(int var1) {
        if (!this.isExist(var1)) {
            this._ref_papplet.die("Marker id " + var1 + " is not on image.", (Exception) null);
        }

        int var2 = (Integer) this._id_map.get(var1);

        try {
            return (int) this._ms.getLostCount(var2);
        } catch (Exception var4) {
            this._ref_papplet.die("Catch an exception!", (Exception) null);
            return -1;
        }
    }

    public long getLife(int var1) {
        try {
            if (!this.isExist(var1)) {
                this._ref_papplet.die("Marker id " + var1 + " is not on image.", (Exception) null);
            }

            int var2 = (Integer) this._id_map.get(var1);
            return this._ms.getLife(var2);
        } catch (Exception var3) {
            this._ref_papplet.die("Catch an exception!", (Exception) null);
            return -1L;
        }
    }

    public PMatrix3D getMatrix(int var1) {
        PMatrix3D var2 = new PMatrix3D();
        if (!this.isExist(var1)) {
            this._ref_papplet.die("Marker id " + var1 + " is not exist on image.", (Exception) null);
        }

        int var3 = (Integer) this._id_map.get(var1);

        try {
            matResult2PMatrix3D(this._ms.getTransformMatrix(var3), this._coordinate_system, var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            this._ref_papplet.die("Catch an exception!");
        }

        return var2;
    }

    public boolean isExist(int var1) {
        int var2 = (Integer) this._id_map.get(var1);

        try {
            return this._ms.isExist(var2);
        } catch (Exception var4) {
            this._ref_papplet.die("Catch an exception!", (Exception) null);
            return false;
        }
    }

    public PVector object2ScreenCoordSystem(int var1, double var2, double var4, double var6) {
        try {
            int var8 = (Integer) this._id_map.get(var1);
            NyARDoublePoint2d var9 = new NyARDoublePoint2d();
            this._ms.getScreenPos(var8, var2, var4, var6, var9);
            PVector var10 = new PVector();
            var10.x = (float) var9.x;
            var10.y = (float) var9.y;
            var10.z = 0.0F;
            return var10;
        } catch (Exception var11) {
            this._ref_papplet.die("Catch an exception!", (Exception) null);
            return null;
        }
    }

    public PVector screen2ObjectCoordSystem(int var1, int var2, int var3) {
        try {
            int var4 = (Integer) this._id_map.get(var1);
            PVector var5 = new PVector();
            NyARDoublePoint3d var6 = new NyARDoublePoint3d();
            this._ms.getPlanePos(var4, var2, var3, var6);
            var5.x = (float) var6.x;
            var5.y = (float) var6.y;
            var5.z = (float) var6.z;
            if (this._coordinate_system == 1) {
                var5.x *= -1.0F;
            }

            return var5;
        } catch (Exception var7) {
            this._ref_papplet.die("Catch an exception!", (Exception) null);
            return null;
        }
    }

    public PImage pickupImage(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
        PImage var12 = new PImage(var10, var11);

        try {
            PImageRaster var13 = new PImageRaster(var12);
            int var14 = (Integer) this._id_map.get(var1);
            this._ms.getPlaneImage(var14, this._ss, (double) var2, (double) var3, (double) var4, (double) var5, (double) var6, (double) var7, (double) var8, (double) var9, var13);
            var12.updatePixels();
        } catch (Exception var15) {
            this._ref_papplet.die("pickupMarkerImage failed.", (Exception) null);
        }

        return var12;
    }

    public PImage pickupRectImage(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        return this.pickupImage(var1, var2 + var4 - 1, var3 + var5 - 1, var2, var3 + var5 - 1, var2, var3, var2 + var4 - 1, var3, var6, var7);
    }

    public NyARMarkerSystem get_ms() {
        return _ms;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean isExistMarker(int var1) {
        return this.isExist(var1);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public PMatrix3D getMarkerMatrix(int var1) {
        return this.getMatrix(var1);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public PImage pickupMarkerImage(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
        return this.pickupImage(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public PImage pickupRectMarkerImage(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        return this.pickupRectImage(var1, var2, var3, var4, var5, var6, var7);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public PVector screen2MarkerCoordSystem(int var1, int var2, int var3) {
        return this.screen2ObjectCoordSystem(var1, var2, var3);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public PVector marker2ScreenCoordSystem(int var1, double var2, double var4, double var6) {
        return this.object2ScreenCoordSystem(var1, var2, var4, var6);
    }
}
