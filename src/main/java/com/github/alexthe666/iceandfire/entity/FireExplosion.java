package com.github.alexthe666.iceandfire.entity;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class FireExplosion extends Explosion
{
	/** whether or not this explosion spawns smoke particles */
	private final boolean isSmoking;
	private final Random explosionRNG;
	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3> playerKnockbackMap;
	private final Vec3 position;

	public FireExplosion(World world, Entity entity, double x, double y, double z, float size, boolean smoke)
	{
		super(world, entity, x, y, z, size, true, smoke);
		this.explosionRNG = new Random();
		this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
		this.playerKnockbackMap = Maps.<EntityPlayer, Vec3>newHashMap();
		this.worldObj = world;
		this.exploder = entity;
		this.explosionSize = size;
		this.explosionX = x;
		this.explosionY = y;
		this.explosionZ = z;
		this.isSmoking = smoke;
		this.position = new Vec3(explosionX, explosionY, explosionZ);
	}

	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	 public void doExplosionA()
	 {
		 Set<BlockPos> set = Sets.<BlockPos>newHashSet();
		 int i = 16;

		 for (int j = 0; j < 16; ++j)
		 {
			 for (int k = 0; k < 16; ++k)
			 {
				 for (int l = 0; l < 16; ++l)
				 {
					 if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
					 {
						 double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
						 double d1 = (double)((float)k / 3.0F * 2.0F - 1.0F);
						 double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
						 double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						 d0 = d0 / d3;
						 d1 = d1 / d3;
						 d2 = d2 / d3;
						 float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						 double d4 = this.explosionX;
						 double d6 = this.explosionY;
						 double d8 = this.explosionZ;

						 for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F)
						 {
							 BlockPos blockpos = new BlockPos(d4, d6, d8);
							 IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

							 if (iblockstate.getBlock().getMaterial() != Material.air)
							 {
								 float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(worldObj, blockpos, (Entity)null, this);
								 f -= (f2 + 0.3F) * 0.3F;
							 }

							 if (f > 0.0F && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f)))
							 {
								 set.add(blockpos);
							 }

							 d4 += d0 * 0.30000001192092896D;
							 d6 += d1 * 0.30000001192092896D;
							 d8 += d2 * 0.30000001192092896D;
						 }
					 }
				 }
			 }
		 }

		 this.affectedBlockPositions.addAll(set);
		 float f3 = this.explosionSize * 2.0F;
		 int k1 = MathHelper.floor_double(this.explosionX - (double)f3 - 1.0D);
		 int l1 = MathHelper.floor_double(this.explosionX + (double)f3 + 1.0D);
		 int i2 = MathHelper.floor_double(this.explosionY - (double)f3 - 1.0D);
		 int i1 = MathHelper.floor_double(this.explosionY + (double)f3 + 1.0D);
		 int j2 = MathHelper.floor_double(this.explosionZ - (double)f3 - 1.0D);
		 int j1 = MathHelper.floor_double(this.explosionZ + (double)f3 + 1.0D);
		 List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
		 net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, this, list, f3);
		 Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);

		 for (int k2 = 0; k2 < list.size(); ++k2)
		 {
			 Entity entity = (Entity)list.get(k2);

			 if (!entity.isImmuneToExplosions())
			 {
				 double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)f3;

				 if (d12 <= 1.0D)
				 {
					 double d5 = entity.posX - this.explosionX;
					 double d7 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
					 double d9 = entity.posZ - this.explosionZ;
					 double d13 = (double)MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);

					 if (d13 != 0.0D)
					 {
						 d5 = d5 / d13;
						 d7 = d7 / d13;
						 d9 = d9 / d13;
						 double d14 = (double)this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
						 double d10 = (1.0D - d12) * d14;
						 entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float)((int)((d10 * d10 + d10) / 2.0D * 8.0D * (double)f3 + 1.0D)));
						 double d11 = EnchantmentProtection.func_92092_a(entity, d10);
						 entity.motionX += d5 * d11;
						 entity.motionY += d7 * d11;
						 entity.motionZ += d9 * d11;

						 if (entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.disableDamage)
						 {
							 this.playerKnockbackMap.put((EntityPlayer)entity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
						 }
					 }
				 }
			 }
		 }
	 }

	 /**
	  * Does the second part of the explosion (sound, particles, drop spawn)
	  */
	 public void doExplosionB(boolean spawnParticles)
	 {
		 if (this.isSmoking)
		 {
			 for (BlockPos blockpos : this.affectedBlockPositions)
			 {
				 Block block = this.worldObj.getBlockState(blockpos).getBlock();

				 if (spawnParticles)
				 {
					 double d0 = (double)((float)blockpos.getX() + this.worldObj.rand.nextFloat());
					 double d1 = (double)((float)blockpos.getY() + this.worldObj.rand.nextFloat());
					 double d2 = (double)((float)blockpos.getZ() + this.worldObj.rand.nextFloat());
					 double d3 = d0 - this.explosionX;
					 double d4 = d1 - this.explosionY;
					 double d5 = d2 - this.explosionZ;
					 double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
					 d3 = d3 / d6;
					 d4 = d4 / d6;
					 d5 = d5 / d6;
					 double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
					 d7 = d7 * (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
					 d3 = d3 * d7;
					 d4 = d4 * d7;
					 d5 = d5 * d7;
					 this.worldObj.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, d3, d4, d5, new int[0]);
					 this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
					 this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
				 }

				 if (block.getMaterial() != Material.air)
				 {
					 if(block instanceof BlockGrass){
						 worldObj.setBlockState(blockpos, ModBlocks.charedGrass.getDefaultState());
					 }

					 if(block instanceof BlockDirt){
						 worldObj.setBlockState(blockpos, ModBlocks.charedDirt.getDefaultState());
					 }
					 
					 if(block instanceof BlockLeaves || block.getMaterial() == Material.water){
						 worldObj.setBlockState(blockpos, Blocks.air.getDefaultState());
					 }
					 
					 if(block instanceof BlockGravel){
						 worldObj.setBlockState(blockpos, ModBlocks.charedGravel.getDefaultState());
					 }
					 
					 if(block.getMaterial() == Material.rock && (block != ModBlocks.charedCobblestone && block != Blocks.cobblestone && block != Blocks.mossy_cobblestone && block != Blocks.cobblestone_wall)){
						 worldObj.setBlockState(blockpos, ModBlocks.charedStone.getDefaultState());
					 }else if(block.getMaterial() == Material.rock){
						 worldObj.setBlockState(blockpos, ModBlocks.charedCobblestone.getDefaultState());
					 }
				 }
			 }
		 }

		 for (BlockPos blockpos1 : this.affectedBlockPositions)
		 {
			 if (this.worldObj.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && this.worldObj.getBlockState(blockpos1.down()).getBlock().isFullBlock() && this.explosionRNG.nextInt(3) == 0)
			 {
				 this.worldObj.setBlockState(blockpos1, Blocks.fire.getDefaultState());
			 }
		 }
	 }

	 public Map<EntityPlayer, Vec3> getPlayerKnockbackMap()
	 {
		 return this.playerKnockbackMap;
	 }

	 /**
	  * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
	  */
	 public EntityLivingBase getExplosivePlacedBy()
	 {
		 return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
	 }

	 public void func_180342_d()
	 {
		 this.affectedBlockPositions.clear();
	 }

	 public List<BlockPos> getAffectedBlockPositions()
	 {
		 return this.affectedBlockPositions;
	 }

	 public Vec3 getPosition(){ return this.position; }
}