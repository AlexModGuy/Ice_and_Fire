package com.github.alexthe666.iceandfire.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;

public class ItemDragonBow extends Item {


	public ItemDragonBow(){
		this.maxStackSize = 1;
		this.setMaxDamage(584);
		this.setCreativeTab(IceAndFire.tab);
		this.setUnlocalizedName("iceandfire.dragonbone_bow");
		GameRegistry.registerItem(this, "dragonbone_bow");
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return itemstack != null && itemstack.getItem() == ModItems.dragonbone_bow ? (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
	}

	private ItemStack func_185060_a(EntityPlayer player)
    {
        if (this.func_185058_h_(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.func_185058_h_(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.func_185058_h_(itemstack))
                {
                    return itemstack;
                }
            }

            return null;
        }
    }
	
	  protected boolean func_185058_h_(ItemStack stack)
	    {
	        return stack != null && stack.getItem() == ModItems.dragonbone_arrow;
	    }

	    /**
	     * Called when the player stops using an Item (stops holding the right mouse button).
	     */
	    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	    {
	        if (entityLiving instanceof EntityPlayer)
	        {
	            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
	            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.infinity, stack) > 0;
	            ItemStack itemstack = this.func_185060_a(entityplayer);

	            int i = this.getMaxItemUseDuration(stack) - timeLeft;
	            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer)entityLiving, i, itemstack != null || flag);
	            if (i < 0) return;

	            if (itemstack != null || flag)
	            {
	                if (itemstack == null)
	                {
	                    itemstack = new ItemStack(ModItems.dragonbone_arrow);
	                }

	                float f = func_185059_b(i);

	                if ((double)f >= 0.1D)
	                {
	                    boolean flag1 = flag && itemstack.getItem() == ModItems.dragonbone_arrow; //Forge: Fix consuming custom arrows.

	                    if (!worldIn.isRemote)
	                    {
	                        EntityArrow entityarrow = makeTippedArrow(worldIn, itemstack, entityplayer);
	                        entityarrow.func_184547_a(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

	                        if (f == 1.0F)
	                        {
	                            entityarrow.setIsCritical(true);
	                        }

	                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.power, stack);

	                        if (j > 0)
	                        {
	                            entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
	                        }

	                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.punch, stack);

	                        if (k > 0)
	                        {
	                            entityarrow.setKnockbackStrength(k);
	                        }

	                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.flame, stack) > 0)
	                        {
	                            entityarrow.setFire(100);
	                        }

	                        stack.damageItem(1, entityplayer);

	                        if (flag1)
	                        {
	                            entityarrow.canBePickedUp = EntityArrow.PickupStatus.CREATIVE_ONLY;
	                        }

	                       // worldIn.spawnEntityInWorld(entityarrow);
	                    }

	                    worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

	                    if (!flag1)
	                    {
	                        --itemstack.stackSize;

	                        if (itemstack.stackSize == 0)
	                        {
	                            entityplayer.inventory.deleteStack(itemstack);
	                        }
	                    }

	                    entityplayer.addStat(StatList.func_188057_b(this));
	                }
	            }
	        }
	    }

	    public static float func_185059_b(int p_185059_0_)
	    {
	        float f = (float)p_185059_0_ / 20.0F;
	        f = (f * f + f * 2.0F) / 3.0F;

	        if (f > 1.0F)
	        {
	            f = 1.0F;
	        }

	        return f;
	    }

	    public int getMaxItemUseDuration(ItemStack stack)
	    {
	        return 72000;
	    }

	    public EnumAction getItemUseAction(ItemStack stack)
	    {
	        return EnumAction.BOW;
	    }

	    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	    {
	        boolean flag = this.func_185060_a(playerIn) != null;

	        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
	        if (ret != null) return ret;

	        if (!playerIn.capabilities.isCreativeMode && !flag)
	        {
	            return !flag ? new ActionResult(EnumActionResult.FAIL, itemStackIn) : new ActionResult(EnumActionResult.PASS, itemStackIn);
	        }
	        else
	        {
	            playerIn.setActiveHand(hand);
	            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	        }
	    }

	    public EntityArrow makeTippedArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
	    {
	        EntityDragonArrow entitytippedarrow = new EntityDragonArrow(worldIn, shooter);
	        return entitytippedarrow;
	    }

	    public int getItemEnchantability()
	    {
	        return 1;
	    }
}
