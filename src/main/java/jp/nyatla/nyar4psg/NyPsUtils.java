//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jp.nyatla.nyar4psg;

import jp.nyatla.nyartoolkit.core.types.matrix.NyARDoubleMatrix44;
import processing.core.PApplet;
import processing.core.PMatrix3D;

public class NyPsUtils {
    public NyPsUtils() {
    }

    public static void dumpObject(PMatrix3D var0) {
        PApplet.println("PMatrix3D");
        PApplet.println(String.format("%f %f %f %f", var0.m00, var0.m01, var0.m02, var0.m03));
        PApplet.println(String.format("%f %f %f %f", var0.m10, var0.m11, var0.m12, var0.m13));
        PApplet.println(String.format("%f %f %f %f", var0.m20, var0.m21, var0.m22, var0.m23));
        PApplet.println(String.format("%f %f %f %f", var0.m30, var0.m31, var0.m32, var0.m33));
    }

    public static void dumpObject(NyARDoubleMatrix44 var0) {
        PApplet.println("NyARDoubleMatrix44");
        PApplet.println(String.format("%f %f %f %f", var0.m00, var0.m01, var0.m02, var0.m03));
        PApplet.println(String.format("%f %f %f %f", var0.m10, var0.m11, var0.m12, var0.m13));
        PApplet.println(String.format("%f %f %f %f", var0.m20, var0.m21, var0.m22, var0.m23));
        PApplet.println(String.format("%f %f %f %f", var0.m30, var0.m31, var0.m32, var0.m33));
    }

    public static void dumpObject(double[] var0) {
        PApplet.println("double[]");

        for(int var1 = 0; var1 < var0.length; ++var1) {
            PApplet.println(var0[var1]);
        }

    }

    public static PMatrix3D nyarMat2PsMat(NyARDoubleMatrix44 var0, PMatrix3D var1) {
        var1.m00 = (float)var0.m00;
        var1.m01 = (float)var0.m01;
        var1.m02 = (float)var0.m02;
        var1.m03 = (float)var0.m03;
        var1.m10 = (float)var0.m10;
        var1.m11 = (float)var0.m11;
        var1.m12 = (float)var0.m12;
        var1.m13 = (float)var0.m13;
        var1.m20 = (float)var0.m20;
        var1.m21 = (float)var0.m21;
        var1.m22 = (float)var0.m22;
        var1.m23 = (float)var0.m23;
        var1.m30 = (float)var0.m30;
        var1.m31 = (float)var0.m31;
        var1.m32 = (float)var0.m32;
        var1.m33 = (float)var0.m33;
        return var1;
    }

    protected static float[] pMatrix2GLMatrix(PMatrix3D var0, float[] var1) {
        var1[0] = var0.m00;
        var1[1] = var0.m10;
        var1[2] = var0.m20;
        var1[3] = var0.m30;
        var1[4] = var0.m01;
        var1[5] = var0.m11;
        var1[6] = var0.m21;
        var1[7] = var0.m31;
        var1[8] = var0.m02;
        var1[9] = var0.m12;
        var1[10] = var0.m22;
        var1[11] = var0.m32;
        var1[12] = var0.m03;
        var1[13] = var0.m13;
        var1[14] = var0.m23;
        var1[15] = var0.m33;
        return var1;
    }

    protected static double[] pMatrix2GLMatrix(PMatrix3D var0, double[] var1) {
        var1[0] = (double)var0.m00;
        var1[1] = (double)var0.m10;
        var1[2] = (double)var0.m20;
        var1[3] = (double)var0.m30;
        var1[4] = (double)var0.m01;
        var1[5] = (double)var0.m11;
        var1[6] = (double)var0.m21;
        var1[7] = (double)var0.m31;
        var1[8] = (double)var0.m02;
        var1[9] = (double)var0.m12;
        var1[10] = (double)var0.m22;
        var1[11] = (double)var0.m32;
        var1[12] = (double)var0.m03;
        var1[13] = (double)var0.m13;
        var1[14] = (double)var0.m23;
        var1[15] = (double)var0.m33;
        return var1;
    }
}
