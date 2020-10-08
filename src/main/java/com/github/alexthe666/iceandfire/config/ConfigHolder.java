package com.github.alexthe666.iceandfire.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHolder {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec BIOME_SPEC;
    public static final ClientConfig CLIENT;
    public static final ServerConfig SERVER;
    public static final BiomeConfig BIOME;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
        {
            final Pair<BiomeConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BiomeConfig::new);
            BIOME = specPair.getLeft();
            BIOME_SPEC = specPair.getRight();
        }
    }
}