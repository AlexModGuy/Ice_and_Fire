package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

import java.util.Optional;

import static com.github.alexthe666.iceandfire.client.IafClientSetup.*;

public class AtlasGenerator extends SpriteSourceProvider {

    public AtlasGenerator(PackOutput output, ExistingFileHelper helper) {
        super(output, helper, IceAndFire.MODID);
    }
    @Override
    protected void addSources() {
        this.atlas(CHESTS_ATLAS).addSource(new SingleFile(GHOST_CHEST_LOCATION, Optional.empty()));
        this.atlas(CHESTS_ATLAS).addSource(new SingleFile(GHOST_CHEST_LEFT_LOCATION, Optional.empty()));
        this.atlas(CHESTS_ATLAS).addSource(new SingleFile(GHOST_CHEST_RIGHT_LOCATION, Optional.empty()));

        //this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new SingleFile(TwilightForestMod.prefix("model/knightmetal_shield"), Optional.empty()));


    }
}
