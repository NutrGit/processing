//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jp.nyatla.nyar4psg;

import jp.nyatla.nyar4psg.utils.PatchCollection;
import jp.nyatla.nyar4psg.utils.PatchCollection_Psg2x;
import jp.nyatla.nyar4psg.utils.PatchCollection_Psg302;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.types.matrix.NyARDoubleMatrix44;
import jp.nyatla.nyartoolkit.markersystem.NyARSingleCameraView;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PMatrix3D;
import processing.opengl.PGraphicsOpenGL;

public class SingleCameraView {
    final PatchCollection _patch_collection;
    public final NyARSingleCameraView _view;
    PApplet _ref_applet;
    private float _clip_far;
    private float _clip_near;
    protected final PMatrix3D _ps_background_mv = new PMatrix3D();
    private final PMatrix3D _ps_projection = new PMatrix3D();

    SingleCameraView(PApplet var1, NyARParam var2, int var3) {
        this._view = new NyARSingleCameraView(var2);
        this._ref_applet = var1;
        this._patch_collection = createPatchCollection(var3);
    }

    public void drawBackground(PImage var1) {
        PApplet var2 = this._ref_applet;
        PGraphicsOpenGL var3 = (PGraphicsOpenGL)var2.g;
        var3.pushProjection();
        this.setBackgroundOrtho(var1.width, var1.height);
        var2.pushMatrix();
        var2.setMatrix(this._ps_background_mv);
        var2.image(var1, (float)(-var1.width / 2), (float)(-var1.height / 2));
        var2.popMatrix();
        var3.popProjection();
    }

    public void setBackgroundOrtho(int var1, int var2) {
        this._patch_collection.setBackgroundOrtho(this._ref_applet, (float)var1, (float)var2, this._clip_near, this._clip_far);
    }

    public PMatrix3D getProjectionMatrix() {
        return this._ps_projection;
    }

    public void setARClipping(int var1, int var2, float var3, float var4) {
        this._clip_far = var4;
        this._clip_near = var3;
        this._ps_background_mv.reset();
        this._ps_background_mv.translate(0.0F, 0.0F, -var4);
        NyARDoubleMatrix44 var5 = new NyARDoubleMatrix44();
        this._view.getARParam().getPerspectiveProjectionMatrix().makeCameraFrustumRH((double)var1, (double)var2, (double)var3, (double)var4, var5);
        NyPsUtils.nyarMat2PsMat(var5, this._ps_projection);
    }

    private static PatchCollection createPatchCollection(int var0) {
        switch(var0) {
            case 513:
                return new PatchCollection_Psg2x();
            case 769:
            default:
                return new PatchCollection_Psg302();
        }
    }
}
