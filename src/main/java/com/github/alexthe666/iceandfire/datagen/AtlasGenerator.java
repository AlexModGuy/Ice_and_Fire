package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraftforge.client.event.RegisterTextureAtlasSpriteLoadersEvent;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Optional;

import static com.github.alexthe666.iceandfire.client.IafClientSetup.*;

// TODO :: 1.19.2 -> Use TextureStitchEvent.Pre instead?
//public class AtlasGenerator extends SpriteSourceProvider {
//    public AtlasGenerator(PackOutput output, ExistingFileHelper helper) {
//        super(output, helper, IceAndFire.MODID);
//    }
//    @Override
//    protected void addSources() {
//        this.atlas(CHESTS_ATLAS).addSource(new SingleFile(GHOST_CHEST_LOCATION, Optional.empty()));
//        this.atlas(CHESTS_ATLAS).addSource(new SingleFile(GHOST_CHEST_LEFT_LOCATION, Optional.empty()));
//        this.atlas(CHESTS_ATLAS).addSource(new SingleFile(GHOST_CHEST_RIGHT_LOCATION, Optional.empty()));
//
//        //this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new SingleFile(TwilightForestMod.prefix("model/knightmetal_shield"), Optional.empty()));
//
//
//    }
//}
