//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jp.nyatla.nyar4psg;

import processing.core.PApplet;

public class NyAR4PsgConfig {
    public static final int PV_221 = 513;
    public static final int PV_302 = 769;
    public static final int PV_DEFAULT = 769;
    public static final int TM_ARTK = 1;
    public static final int TM_NYARTK = 2;
    public static final int TM_ARTKICP = 3;
    public static final int CS_RIGHT_HAND = 0;
    public static final int CS_LEFT_HAND = 1;
    public final int env_transmat_mode;
    public final int _coordinate_system;
    public final int _ps_patch_version;
    public static final NyAR4PsgConfig CONFIG_PSG = new NyAR4PsgConfig(1, 3);
    public static final NyAR4PsgConfig CONFIG_PSG_PV221 = new NyAR4PsgConfig(1, 3, 513);
    public static final NyAR4PsgConfig CONFIG_OLD = new NyAR4PsgConfig(0, 3);

    public NyAR4PsgConfig(int var1, int var2, int var3) {
        switch(var1) {
            default:
                PApplet.println("Invalid CS param. select CS_LEFT_HAND or CS_RIGHT_HAND.");
            case 0:
            case 1:
                switch(var2) {
                    default:
                        PApplet.println("Invalid TM param. select TM_NYARTK or TM_ARTK　or TM_ARTKICP.");
                    case 1:
                    case 2:
                    case 3:
                        this._coordinate_system = var1;
                        this.env_transmat_mode = var2;
                        this._ps_patch_version = var3;
                }
        }
    }

    public NyAR4PsgConfig(int var1, int var2) {
        this(var1, var2, 769);
    }
}
