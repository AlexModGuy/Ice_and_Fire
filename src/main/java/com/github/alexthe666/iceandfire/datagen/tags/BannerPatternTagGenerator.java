package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.recipe.IafBannerPatterns;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BannerPatternTagGenerator extends TagsProvider<BannerPattern> {
    public static final TagKey<BannerPattern> FIRE_BANNER_PATTERN = create("pattern_item/fire");
    public static final TagKey<BannerPattern> ICE_BANNER_PATTERN = create("pattern_item/ice");
    public static final TagKey<BannerPattern> LIGHTNING_BANNER_PATTERN = create("pattern_item/lightning");
    public static final TagKey<BannerPattern> FIRE_HEAD_BANNER_PATTERN = create("pattern_item/fire_head");
    public static final TagKey<BannerPattern> ICE_HEAD_BANNER_PATTERN = create("pattern_item/ice_head");
    public static final TagKey<BannerPattern> LIGHTNING_HEAD_BANNER_PATTERN = create("pattern_item/lightning_head");
    public static final TagKey<BannerPattern> AMPHITHERE_BANNER_PATTERN = create("pattern_item/amphithere");
    public static final TagKey<BannerPattern> BIRD_BANNER_PATTERN = create("pattern_item/bird");
    public static final TagKey<BannerPattern> EYE_BANNER_PATTERN = create("pattern_item/eye");
    public static final TagKey<BannerPattern> FAE_BANNER_PATTERN = create("pattern_item/fae");
    public static final TagKey<BannerPattern> FEATHER_BANNER_PATTERN = create("pattern_item/feather");
    public static final TagKey<BannerPattern> GORGON_BANNER_PATTERN = create("pattern_item/gorgon");
    public static final TagKey<BannerPattern> HIPPOCAMPUS_BANNER_PATTERN = create("pattern_item/hippocampus");
    public static final TagKey<BannerPattern> HIPPOGRYPH_HEAD_BANNER_PATTERN = create("pattern_item/hippogryph_head");
    public static final TagKey<BannerPattern> MERMAID_BANNER_PATTERN = create("pattern_item/mermaid");
    public static final TagKey<BannerPattern> SEA_SERPENT_BANNER_PATTERN = create("pattern_item/sea_serpent");
    public static final TagKey<BannerPattern> TROLL_BANNER_PATTERN = create("pattern_item/troll");
    public static final TagKey<BannerPattern> WEEZER_BANNER_PATTERN = create("pattern_item/weezer");
    public static final TagKey<BannerPattern> DREAD_BANNER_PATTERN = create("pattern_item/dread");

    public BannerPatternTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BANNER_PATTERN, provider, IceAndFire.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(FIRE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE.getKey());
        this.tag(ICE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE.getKey());
        this.tag(LIGHTNING_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING.getKey());
        this.tag(FIRE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE_HEAD.getKey());
        this.tag(ICE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE_HEAD.getKey());
        this.tag(LIGHTNING_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING_HEAD.getKey());
        this.tag(AMPHITHERE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_AMPHITHERE.getKey());
        this.tag(BIRD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_BIRD.getKey());
        this.tag(EYE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_EYE.getKey());
        this.tag(FAE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FAE.getKey());
        this.tag(FEATHER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FEATHER.getKey());
        this.tag(GORGON_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_GORGON.getKey());
        this.tag(HIPPOCAMPUS_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOCAMPUS.getKey());
        this.tag(HIPPOGRYPH_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOGRYPH_HEAD.getKey());
        this.tag(MERMAID_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_MERMAID.getKey());
        this.tag(SEA_SERPENT_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_SEA_SERPENT.getKey());
        this.tag(TROLL_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_TROLL.getKey());
        this.tag(WEEZER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_WEEZER.getKey());
        this.tag(DREAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_DREAD.getKey());
    }

    private static TagKey<BannerPattern> create(String name) {
        return TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(IceAndFire.MODID, name));
    }

    @Override
    public String getName() {
        return "Ice and Fire Banner Pattern Tags";
    }
}

