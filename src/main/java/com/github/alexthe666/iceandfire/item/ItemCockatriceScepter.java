package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.MiscEntityProperties;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCockatriceScepter extends Item {

    public ItemCockatriceScepter() {
        super();
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.cockatrice_scepter");
        this.setRegistryName(IceAndFire.MODID, "cockatrice_scepter");
        this.maxStackSize = 1;
        this.setMaxDamage(700);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(I18n.format("item.iceandfire.cockatrice_scepter.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.cockatrice_scepter.desc_1"));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
        MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, MiscEntityProperties.class);
        if(properties != null && properties.specialWeaponDmg > 0){
            stack.damageItem(properties.specialWeaponDmg, entity);
            properties.specialWeaponDmg = 0;
            for(Entity e : properties.entitiesWeAreGlaringAt){
                MiscEntityProperties theirProp = EntityPropertiesHandler.INSTANCE.getProperties(e, MiscEntityProperties.class);
                if(theirProp.glarers.contains(entity)){
                    theirProp.glarers.remove(entity);
                }
            }
            properties.entitiesWeAreGlaringAt.clear();
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, MiscEntityProperties.class);
        if (properties != null) {
            if (player instanceof EntityPlayer) {
                double dist = 32;
                Vec3d vec3d = player.getPositionEyes(1.0F);
                Vec3d vec3d1 = player.getLook(1.0F);
                Vec3d vec3d2 = vec3d.add(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
                double d1 = dist;
                Entity pointedEntity = null;
                List<Entity> list = player.world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().expand(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                    public boolean apply(@Nullable Entity entity) {
                        boolean blindness = entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS) || (entity instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) entity).canBeTurnedToStone());
                        return entity != null && entity.canBeCollidedWith() && !blindness && (entity instanceof EntityPlayer || (entity instanceof EntityLiving && EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class) != null && !EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class).isStone));
                    }
                }));
                double d2 = d1;
                for (int j = 0; j < list.size(); ++j) {
                    Entity entity1 = (Entity) list.get(j);
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
                    RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

                    if (axisalignedbb.contains(vec3d)) {
                        if (d2 >= 0.0D) {
                            pointedEntity = entity1;
                            d2 = 0.0D;
                        }
                    } else if (raytraceresult != null) {
                        double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                        if (d3 < d2 || d2 == 0.0D) {
                            if (entity1.getLowestRidingEntity() == player.getLowestRidingEntity() && !player.canRiderInteract()) {
                                if (d2 == 0.0D) {
                                    pointedEntity = entity1;
                                }
                            } else {
                                pointedEntity = entity1;
                                d2 = d3;
                            }
                        }
                    }
                }
                if (pointedEntity != null) {
                    if (pointedEntity instanceof EntityLivingBase) {
                        MiscEntityProperties theirProperties = EntityPropertiesHandler.INSTANCE.getProperties(pointedEntity, MiscEntityProperties.class);
                        theirProperties.isBeingGlaredAt = true;
                        if(!theirProperties.glarers.contains(player) && !properties.entitiesWeAreGlaringAt.contains(pointedEntity)){
                            theirProperties.glarers.add(player);
                            properties.entitiesWeAreGlaringAt.add(pointedEntity);
                        }
                    }
                }
            }
        }
    }

}
