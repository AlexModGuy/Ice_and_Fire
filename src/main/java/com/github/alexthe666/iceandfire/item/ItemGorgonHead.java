package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGorgonHead extends Item {

    public ItemGorgonHead() {
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName("iceandfire.gorgon_head");
        this.maxStackSize = 1;
        this.setRegistryName(IceAndFire.MODID, "gorgon_head");
        GameRegistry.register(this);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
        double dist = 32;
        Vec3d vec3d = entity.getPositionEyes(1.0F);
        Vec3d vec3d1 = entity.getLook(1.0F);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
        double d1 = dist;
        Entity pointedEntity = null;
        List<Entity> list = worldIn.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                return entity != null && entity.canBeCollidedWith();
            }
        }));
        double d2 = d1;
        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity)list.get(j);
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

            if (axisalignedbb.contains(vec3d)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    d2 = 0.0D;
                }
            }
            else if (raytraceresult != null) {
                double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                        }
                    }
                    else {
                        pointedEntity = entity1;
                        d2 = d3;
                    }
                }
            }
        }
        if(pointedEntity != null){
            if(pointedEntity instanceof EntityLiving){
                if(pointedEntity instanceof EntityZombie){
                    pointedEntity.setDead();
                    EntityStoneStatue statue = new EntityStoneStatue(worldIn);
                    statue.setPositionAndRotation(pointedEntity.posX, pointedEntity.posY, pointedEntity.posZ, pointedEntity.rotationYaw, pointedEntity.rotationPitch);
                    statue.smallArms = true;
                    if (!worldIn.isRemote) {
                        worldIn.spawnEntity(statue);
                    }
                    for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
                        statue.setItemStackToSlot(slot, ((EntityZombie) pointedEntity).getItemStackFromSlot(slot));
                    }
                }else{
                    StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(pointedEntity, StoneEntityProperties.class);
                    if(properties != null){
                        properties.isStone = true;
                    }
                }

            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        int i = this.getMaxItemUseDuration(stack) - count;
        if(i > 20 && stack.getMetadata() == 0) {
            stack.setItemDamage(1);
        }
    }
}
