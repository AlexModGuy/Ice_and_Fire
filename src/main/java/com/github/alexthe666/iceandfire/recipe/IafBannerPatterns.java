package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class IafBannerPatterns {
    public static final DeferredRegister<BannerPattern> BANNERS = DeferredRegister.create(Registries.BANNER_PATTERN, IceAndFire.MODID);
    public static final RegistryObject<BannerPattern> PATTERN_FIRE = BANNERS.register("fire", () -> new BannerPattern("iaf_fire"));
    public static final RegistryObject<BannerPattern> PATTERN_ICE = BANNERS.register("ice", () -> new BannerPattern("iaf_ice"));
    public static final RegistryObject<BannerPattern> PATTERN_LIGHTNING = BANNERS.register("lightning", () -> new BannerPattern("iaf_lightning"));
    public static final RegistryObject<BannerPattern> PATTERN_FIRE_HEAD = BANNERS.register("fire_head", () -> new BannerPattern("iaf_fire_head"));
    public static final RegistryObject<BannerPattern> PATTERN_ICE_HEAD = BANNERS.register("ice_head", () -> new BannerPattern("iaf_ice_head"));
    public static final RegistryObject<BannerPattern> PATTERN_LIGHTNING_HEAD = BANNERS.register("lightning_head", () -> new BannerPattern("iaf_lightning_head"));
    public static final RegistryObject<BannerPattern> PATTERN_AMPHITHERE = BANNERS.register("amphithere", () -> new BannerPattern("iaf_amphithere"));
    public static final RegistryObject<BannerPattern> PATTERN_BIRD = BANNERS.register("bird", () -> new BannerPattern("iaf_bird"));
    public static final RegistryObject<BannerPattern> PATTERN_EYE = BANNERS.register("eye", () -> new BannerPattern("iaf_eye"));
    public static final RegistryObject<BannerPattern> PATTERN_FAE = BANNERS.register("fae", () -> new BannerPattern("iaf_fae"));
    public static final RegistryObject<BannerPattern> PATTERN_FEATHER = BANNERS.register("feather", () -> new BannerPattern("iaf_feather"));
    public static final RegistryObject<BannerPattern> PATTERN_GORGON = BANNERS.register("gorgon", () -> new BannerPattern("iaf_gorgon"));
    public static final RegistryObject<BannerPattern> PATTERN_HIPPOCAMPUS = BANNERS.register("hippocampus", () -> new BannerPattern("iaf_hippocampus"));
    public static final RegistryObject<BannerPattern> PATTERN_HIPPOGRYPH_HEAD = BANNERS.register("hippogryph_head", () -> new BannerPattern("iaf_hippogryph_head"));
    public static final RegistryObject<BannerPattern> PATTERN_MERMAID = BANNERS.register("mermaid", () -> new BannerPattern("iaf_mermaid"));
    public static final RegistryObject<BannerPattern> PATTERN_SEA_SERPENT = BANNERS.register("sea_serpent", () -> new BannerPattern("iaf_sea_serpent"));
    public static final RegistryObject<BannerPattern> PATTERN_TROLL = BANNERS.register("troll", () -> new BannerPattern("iaf_troll"));
    public static final RegistryObject<BannerPattern> PATTERN_WEEZER = BANNERS.register("weezer", () -> new BannerPattern("iaf_weezer"));
    public static final RegistryObject<BannerPattern> PATTERN_DREAD = BANNERS.register("dread", () -> new BannerPattern("iaf_dread"));

}
