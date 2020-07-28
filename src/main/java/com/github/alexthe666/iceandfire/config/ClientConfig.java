package com.github.alexthe666.iceandfire.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.BooleanValue customMainMenu;
    public final ForgeConfigSpec.BooleanValue useVanillaFont;
    public final ForgeConfigSpec.BooleanValue dragonAuto3rdPerson;

    public ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        this.customMainMenu = buildBoolean(builder, "Custom main menu", "all", true, "Whether to display the dragon on the main menu or not");
        this.dragonAuto3rdPerson = buildBoolean(builder, "Auto 3rd person when riding dragon", "all", true, "True if riding dragons should make the player take a 3rd person view automatically.");
        this.useVanillaFont = buildBoolean(builder, "Use Vanilla Font", "all", false, "Whether to use the vanilla font in the bestiary or not");    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
