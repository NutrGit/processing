package jp.nyatla.nyar4psg.utils;

import processing.core.PApplet;

public final class PatchCollection_Psg302 implements PatchCollection {
    public PatchCollection_Psg302() {
    }

    public void setBackgroundOrtho(PApplet var1, float var2, float var3, float var4, float var5) {
        var1.ortho(-var2 / 2.0F, var2 / 2.0F, -var3 / 2.0F, var3 / 2.0F, var4, var5 + 1.0F);
    }
}
