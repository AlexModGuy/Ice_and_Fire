package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModAchievements;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.BlockChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EventLiving {
    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityWitherSkeleton) {
            event.getEntityLiving().dropItem(ModItems.witherbone, event.getEntityLiving().getRNG().nextInt(2));
        }

        if(event.getEntityLiving() instanceof EntityLiving){
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
            if(properties != null && properties.isStone){
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if(event.getTarget() instanceof EntityLiving) {
            boolean stonePlayer = event.getTarget() instanceof EntityStoneStatue;
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((EntityLiving)event.getTarget(), StoneEntityProperties.class);
            if(properties != null && properties.isStone || stonePlayer) {
                if(event.getEntityPlayer() != null) {
                    ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
                    if (stack.getItem() != null && stack.getItem() instanceof ItemPickaxe){
                        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
                        boolean ready = false;
                        if(properties != null && !stonePlayer){
                            properties.breakLvl++;
                            ready = properties.breakLvl > 9;
                        }
                        if(stonePlayer){
                            EntityStoneStatue statue = (EntityStoneStatue)event.getTarget();
                            statue.setCrackAmount(statue.getCrackAmount() + 1);
                            ready = statue.getCrackAmount() > 9;
                        }
                        if(ready){
                            event.getTarget().setDead();
                            if(silkTouch){
                                ItemStack statuette = new ItemStack(ModItems.stone_statue);
                                statuette.setTagCompound(new NBTTagCompound());
                                statuette.getTagCompound().setBoolean("IAFStoneStatueEntityPlayer", stonePlayer);
                                statuette.getTagCompound().setInteger("IAFStoneStatueEntityID", stonePlayer ? 90 : EntityList.getID(event.getTarget().getClass()));
                                ((EntityLiving)event.getTarget()).writeEntityToNBT(statuette.getTagCompound());
                                if(!event.getTarget().world.isRemote){
                                    event.getTarget().entityDropItem(statuette, 1);
                                }
                            }else{
                                if(!((EntityLiving) event.getTarget()).world.isRemote){
                                    event.getTarget().dropItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 2 + event.getEntityLiving().getRNG().nextInt(4));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof EntityLiving) {
            boolean stonePlayer = event.getEntityLiving() instanceof EntityStoneStatue;
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
            if (properties != null && properties.isStone || stonePlayer) {
                EntityLiving living = ((EntityLiving) event.getEntityLiving());
                living.motionX *= 0D;
                living.motionZ *= 0D;
                living.motionY -= 0.1D;
                living.swingProgress = 0;
                living.limbSwing = 0;
                living.setInvisible(!stonePlayer);
                living.livingSoundTime = 0;
                living.hurtTime = 0;
                living.hurtResistantTime = living.maxHurtResistantTime - 1;
                living.extinguish();
                if(!living.isAIDisabled()){
                    living.setNoAI(true);
                }
                if(living.getAttackTarget() != null){
                    living.setAttackTarget(null);
                }
                if(living instanceof EntityHorse){
                    EntityHorse horse = (EntityHorse)living;
                    horse.tailCounter = 0;
                    horse.setEatingHaystack(false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if(event.getEntityLiving() instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntityPlayer() != null && (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockChest)) {
            float dist = IceAndFire.CONFIG.dragonGoldSearchLength;
            List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getEntityPlayer(), event.getEntityPlayer().getEntityBoundingBox().expand(dist, dist, dist));
            Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(event.getEntityPlayer()));
            if (!list.isEmpty()) {
                Iterator<Entity> itr = list.iterator();
                while (itr.hasNext()){
                    Entity entity = itr.next();
                    if(entity instanceof EntityDragonBase ){
                        EntityDragonBase dragon = (EntityDragonBase)entity;
                        if(!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getEntityPlayer()) && !event.getEntityPlayer().capabilities.isCreativeMode) {
                            dragon.setSleeping(false);
                            dragon.setSitting(false);
                            dragon.setAttackTarget(event.getEntityPlayer());
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && (event.getState().getBlock() == ModBlocks.goldPile || event.getState().getBlock() == ModBlocks.silverPile)) {
            float dist = IceAndFire.CONFIG.dragonGoldSearchLength;
            List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getPlayer(), event.getPlayer().getEntityBoundingBox().expand(dist, dist, dist));
            Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(event.getPlayer()));
            if (!list.isEmpty()) {
                Iterator<Entity> itr = list.iterator();
                while (itr.hasNext()){
                    Entity entity = itr.next();
                    if(entity instanceof EntityDragonBase){
                       EntityDragonBase dragon = (EntityDragonBase)entity;
                        if(!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getPlayer()) && !event.getPlayer().capabilities.isCreativeMode) {
                            dragon.setSleeping(false);
                            dragon.setSitting(false);
                            dragon.setAttackTarget(event.getPlayer());
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onChestGenerated(LootTableLoadEvent event) {
        if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
            final LootPool pool1 = event.getTable().getPool("pool1");
            if (pool1 != null) {
                pool1.addEntry(new LootEntryItem(ModItems.manuscript, 10, 5, new LootFunction[0], new LootCondition[0], "iceandfire:manuscript"));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player != null && !event.player.getPassengers().isEmpty()) {
            Iterator<Entity> itr = event.player.getPassengers().iterator();
            while (itr.hasNext()) {
                (itr.next()).dismountRidingEntity();
            }
        }
    }

    @SubscribeEvent
    public void onItemEvent(PlayerEvent.ItemPickupEvent event) {
        if (event.pickedUp.getItem().getItem() == ModItems.manuscript) {
            event.player.addStat(ModAchievements.manuscript, 1);
        }
        if (event.pickedUp.getItem().getItem() == Item.getItemFromBlock((ModBlocks.silverOre))) {
            event.player.addStat(ModAchievements.silver, 1);
        }
        if (event.pickedUp.getItem().getItem() == Item.getItemFromBlock((ModBlocks.sapphireOre))) {
            event.player.addStat(ModAchievements.sapphire, 1);
        }
    }

    @SubscribeEvent
    public void onCraftEvent(PlayerEvent.ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        if (event.crafting.getItem() == ModItems.bestiary) {
            player.addStat(ModAchievements.bestiary, 1);
        }
        if (event.crafting.getItem() == ModItems.silver_sword || event.crafting.getItem() == ModItems.silver_pickaxe || event.crafting.getItem() == ModItems.silver_axe || event.crafting.getItem() == ModItems.silver_shovel || event.crafting.getItem() == ModItems.silver_hoe) {
            player.addStat(ModAchievements.silverTool, 1);
        }
        if (event.crafting.getItem() == ModItems.dragon_horn || event.crafting.getItem() == ModItems.dragonbone_bow || event.crafting.getItem() == ModItems.dragonbone_sword || event.crafting.getItem() == ModItems.dragonbone_pickaxe || event.crafting.getItem() == ModItems.dragonbone_axe || event.crafting.getItem() == ModItems.dragonbone_shovel || event.crafting.getItem() == ModItems.dragonbone_hoe) {
            player.addStat(ModAchievements.boneTool, 1);
        }
        if (event.crafting.getItem() instanceof ItemDragonArmor) {
            player.addStat(ModAchievements.dragonArmor, 1);
        }
        if (event.crafting.getItem() == ModItems.dragonbone_sword_fire) {
            player.addStat(ModAchievements.fireSword, 1);
        }
        if (event.crafting.getItem() == ModItems.dragonbone_sword_ice) {
            player.addStat(ModAchievements.iceSword, 1);
        }
    }
}
