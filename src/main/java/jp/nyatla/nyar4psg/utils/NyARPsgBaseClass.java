package jp.nyatla.nyar4psg.utils;

import jp.nyatla.nyar4psg.SingleCameraView;
import jp.nyatla.nyartoolkit.core.types.NyARIntSize;
import jp.nyatla.nyartoolkit.core.types.matrix.NyARDoubleMatrix44;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PMatrix3D;

public abstract class NyARPsgBaseClass {
    public final float FRUSTUM_DEFAULT_NEAR_CLIP = 100.0F;
    public final float FRUSTUM_DEFAULT_FAR_CLIP = 100000.0F;
    public static final String VERSION = "NyAR4psg/3.0.7;NyARToolkit/5.0.9";
    protected final PApplet _ref_papplet;
    public final SingleCameraView cameraview;
    private static final PMatrix3D _lh_mat = new PMatrix3D(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);

    protected NyARPsgBaseClass(PApplet var1, SingleCameraView var2) {
        this._ref_papplet = var1;
        this.cameraview = var2;
        NyARIntSize var3 = var2._view.getARParam().getScreenSize();
        var2.setARClipping(var3.w, var3.h, 100.0F, 100000.0F);
    }

    public void drawBackground(PImage var1) {
        this.cameraview.drawBackground(var1);
    }

    public void setBackgroundOrtho(int var1, int var2) {
        this.cameraview.setBackgroundOrtho(var1, var2);
    }

    public void setARPerspective() {
        this.setPerspective(this.getProjectionMatrix());
    }

    public final void setPerspective(PMatrix3D var1) {
        float var2 = var1.m23 / (var1.m22 + 1.0F);
        float var3 = var1.m23 / (var1.m22 - 1.0F);
        this._ref_papplet.frustum((var1.m02 - 1.0F) * var3 / var1.m00, (var1.m02 + 1.0F) * var3 / var1.m00, (var1.m12 - 1.0F) * var3 / var1.m11, (var1.m12 + 1.0F) * var3 / var1.m11, var3, var2);
    }

    public final PMatrix3D getProjectionMatrix() {
        return this.cameraview.getProjectionMatrix();
    }

    public final void setARClipping(float var1, float var2) {
        NyARIntSize var3 = this.cameraview._view.getARParam().getScreenSize();
        this.cameraview.setARClipping(var3.w, var3.h, var1, var2);
    }

    protected static void matResult2PMatrix3D(NyARDoubleMatrix44 var0, int var1, PMatrix3D var2) {
        var2.m00 = (float)var0.m00;
        var2.m01 = (float)var0.m01;
        var2.m02 = (float)var0.m02;
        var2.m03 = (float)var0.m03;
        var2.m10 = (float)var0.m10;
        var2.m11 = (float)var0.m11;
        var2.m12 = (float)var0.m12;
        var2.m13 = (float)var0.m13;
        var2.m20 = (float)(-var0.m20);
        var2.m21 = (float)(-var0.m21);
        var2.m22 = (float)(-var0.m22);
        var2.m23 = (float)(-var0.m23);
        var2.m30 = 0.0F;
        var2.m31 = 0.0F;
        var2.m32 = 0.0F;
        var2.m33 = 1.0F;
        if (var1 == 1) {
            var2.apply(_lh_mat);
        }

    }

    protected static void matResult2GLArray(NyARDoubleMatrix44 var0, double[] var1) {
        var1[0] = var0.m00;
        var1[4] = var0.m01;
        var1[8] = var0.m02;
        var1[12] = var0.m03;
        var1[1] = -var0.m10;
        var1[5] = -var0.m11;
        var1[9] = -var0.m12;
        var1[13] = -var0.m13;
        var1[2] = -var0.m20;
        var1[6] = -var0.m21;
        var1[10] = -var0.m22;
        var1[14] = -var0.m23;
        var1[3] = 0.0D;
        var1[7] = 0.0D;
        var1[11] = 0.0D;
        var1[15] = 1.0D;
    }

    public PMatrix3D get_lh_mat() {
        return this._lh_mat;
    }
}
