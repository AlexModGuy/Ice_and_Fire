package com.github.alexthe666.iceandfire.compat.theoneprobe;
import mcjty.theoneprobe.TheOneProbe;
public class IceAndFireOneProbeCompat {

  public static void register() {
    TheOneProbe.theOneProbeImp.registerEntityProvider(new DragonInfoProvider());
  }
}