package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.IDragonFlute;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemDragonFlute extends Item {

    public ItemDragonFlute() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 60);

        float chunksize = 16 * IafConfig.dragonFluteDistance;
        List<Entity> list = worldIn.getEntities(player, (new AABB(player.getX(), player.getY(), player.getZ(), player.getX() + 1.0D, player.getY() + 1.0D, player.getZ() + 1.0D)).inflate(chunksize, 256, chunksize));
        Collections.sort(list, new Sorter(player));
        List<IDragonFlute> dragons = new ArrayList<IDragonFlute>();
        Iterator<Entity> itr_entities = list.iterator();
        while (itr_entities.hasNext()) {
            Entity entity = itr_entities.next();
            if (entity instanceof IDragonFlute) {
                dragons.add((IDragonFlute) entity);
            }
        }

        Iterator<IDragonFlute> itr_dragons = dragons.iterator();
        while (itr_dragons.hasNext()) {
            IDragonFlute dragon = itr_dragons.next();
            dragon.onHearFlute(player);
			/*
			if(dragon.isTamed() && dragon.isOwner(player)) {
                if (dragon.isFlying() || dragon.isHovering()) {
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                }
            }*/
        }
        worldIn.playSound(player, player.blockPosition(), IafSoundRegistry.DRAGONFLUTE, SoundSource.NEUTRAL, 1, 1.75F);

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, itemStackIn);
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}