package com.github.alexthe666.iceandfire.config;

public class DefaultBiomes {
    public static final String[] ALL_BIOMES = new String[] { "*" };
    public static final String[] OVERWORLD_BIOMES = new String[] { "|#overworld" };
    public static final String[] FIREDRAGON_ROOST = new String[] { "&#overworld+|#dry+|#hot+|#plains+!#wet+!#cold+!#forest+!@taiga+!#ocean" };
    public static final String[] ICEDRAGON_ROOST = new String[] { "&#overworld+|@icy+|#snowy+|#cold+|@taiga+!#hot+!#wet+!#ocean" };
    public static final String[] LIGHTNING_ROOSTS = new String[] { "&#overworld+&#jungle+&#wet", "&#overworld+&#hot+&#wet+!#ocean", "&#overworld+|#wet+!#cold+!#ocean+!@taiga", "&#overworld+|#savanna"};
    public static final String[] VERY_HOT = new String[] { "&#overworld+|#dry+|#hot+!#wet+!#cold+!#forest+!@taiga+!#ocean" };
    public static final String[] VERY_SNOWY = new String[] { "&#overworld+&#snowy+|@icy+|#cold+|@taiga+!#hot+!#wet+!#ocean" };
    public static final String[] SNOWY = new String[] { "&#overworld+|#snowy+|@taiga+&#cold" };
    public static final String[] VERY_HILLY = new String[] { "&#overworld+|#mountains+|@extreme_hills" };
    public static final String[] WOODLAND = new String[] { "&#overworld+|@forest+|#forest" };
    public static final String[] SAVANNAS = new String[] { "&#overworld+|@savanna+|#savanna" };
    public static final String[] BEACHES = new String[] { "&#overworld+|@beach+|#beach" };
    public static final String[] SWAMPS = new String[] { "&#overworld+|@swamp+|#swamp+|#hot+&#wet+!#cold+!#dry+!#ocean" };
    public static final String[] DESERT = new String[] { "&#overworld+|@desert" };
    public static final String[] OCEANS = new String[] { "&#overworld+|@ocean+|#ocean" };
    public static final String[] PIXIES = new String[] { "&#overworld+|#rare+|#magical+|#dense+&#forest+!@taiga+!#hills+!#mountain+!#wet+!#hot+!#cold+!#dry" };
    public static final String[] JUNGLE = new String[] { "&#overworld+|#jungle" };
    public static final String[] HILLS = new String[] { "&#overworld+|#mesa+|#mountains+|#hills+|@extreme_hills" };
    public static final String[] PLAINS = new String[] { "&#overworld+|@plains+|#plains" };
    public static final String[] GRAVEYARD_BIOMES = new String[] { "&#overworld+!@ocean+!#ocean+!@beach+!#beach" };

}
