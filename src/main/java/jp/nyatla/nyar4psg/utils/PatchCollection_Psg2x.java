package jp.nyatla.nyar4psg.utils;

import processing.core.PApplet;

public final class PatchCollection_Psg2x implements PatchCollection {
    public PatchCollection_Psg2x() {
    }

    public void setBackgroundOrtho(PApplet var1, float var2, float var3, float var4, float var5) {
        var1.ortho(0.0F, var2, 0.0F, var3, var4, var5 + 1.0F);
    }
}
