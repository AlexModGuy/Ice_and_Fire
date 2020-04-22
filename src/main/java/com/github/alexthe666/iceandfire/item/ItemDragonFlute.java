package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.IDragonFlute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.*;

public class ItemDragonFlute extends Item {

    public ItemDragonFlute() {
        this.maxStackSize = 1;
        this.setTranslationKey("iceandfire.dragon_flute");
        this.setRegistryName(IceAndFire.MODID, "dragon_flute");
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        player.getCooldownTracker().setCooldown(this, 60);

        float chunksize = 16 * IceAndFire.CONFIG.dragonFluteDistance;
        List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(player, (new AxisAlignedBB(player.posX, player.posY, player.posZ, player.posX + 1.0D, player.posY + 1.0D, player.posZ + 1.0D)).grow(chunksize, 256, chunksize));
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
        worldIn.playSound(player, player.getPosition(), IafSoundRegistry.DRAGONFLUTE, SoundCategory.NEUTRAL, 1, 1.75F);

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}