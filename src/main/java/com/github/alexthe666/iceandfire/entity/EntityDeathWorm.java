package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.message.MessageDeathWormHitbox;
import com.github.alexthe666.iceandfire.message.MessageHippogryphArmor;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityDeathWorm extends EntityTameable implements IMultipartEntity, IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private PartEntity[] segments = new PartEntity[6];
    @SideOnly(Side.CLIENT)
    public ChainBuffer tail_buffer;
    private boolean isSandNavigator;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityDeathWorm.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE = EntityDataManager.<Float>createKey(EntityDeathWorm.class, DataSerializers.FLOAT);
    public static Animation ANIMATION_BITE = Animation.create(10);

    public EntityDeathWorm(World worldIn) {
        super(worldIn);
        this.ignoreFrustumCheck = true;
        this.setSize(0.8F, 0.8F);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            tail_buffer = new ChainBuffer();
        }
        this.switchNavigator(false);
        this.tasks.addTask(0, new EntityAIAttackMelee(this, 1.5D, true));
        this.tasks.addTask(1, new DeathWormAIFindSandTarget(this, 10));
        this.tasks.addTask(2, new DeathWormAIGetInSand(this, 1.0D));
        this.tasks.addTask(3, new DeathWormAIWander(this, 1));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(5, new DeathWormAITarget(this, EntityLivingBase.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity input) {
                return input instanceof EntityLivingBase && !(input instanceof EntityHorse);
            }
        }));
    }

    public void onUpdateParts() {
        for (Entity entity : segments){
            if(entity != null)
                entity.onUpdate();
            }
    }

    public void onDeath(DamageSource cause) {
        for (Entity entity : segments){
            if(entity != null)
                world.removeEntityDangerously(entity);
        }
       super.onDeath(cause);
    }

    public void initSegments(float scale){
        segments = new PartEntity[6];
        for(int i = 0; i < segments.length; i++){
            segments[i] = new PartEntity(this, (-0.8F - (i * 0.8F)) * scale, 0, 0, 0.7F * scale, 0.7F * scale, 1);
        }
    }

    @Override
    public void setScaleForAge(boolean par1) {
        this.setScale(Math.min(this.getScale(), 7F));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        this.setAnimation(ANIMATION_BITE);
        if(this.getRNG().nextInt(3) == 0){
            SandExplosion explosion = new SandExplosion(world, this, entityIn.posX, entityIn.posY, entityIn.posZ, 2);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
        return false;
    }

    protected void onDeathUpdate() {
        if(this.deathTime == 20){
            int chitinCount = Math.round((1 + this.getRNG().nextInt(3)) * this.getScale());
            int rottenFleshCount = Math.round((this.getRNG().nextInt(2)) * this.getScale());
            for(int i = 0; i < chitinCount; i++){
                dropItemAt(new ItemStack(ModItems.deathworm_chitin, 1, this.getVariant()), this.posX, this.getSurface((int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ)) + 0.5F, this.posZ);
            }
            for(int i = 0; i < rottenFleshCount; i++){
                dropItemAt(new ItemStack(Items.ROTTEN_FLESH, 1), this.posX, this.getSurface((int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ)) + 0.5F, this.posZ);
            }
        }
        super.onDeathUpdate();
    }

    @Nullable
    private EntityItem dropItemAt(ItemStack stack, double x, double y, double z){
        EntityItem entityitem = new EntityItem(this.world, x, y, z, stack);
        entityitem.setDefaultPickupDelay();
        if (captureDrops)
            this.capturedDrops.add(entityitem);
        else
            this.world.spawnEntity(entityitem);
        return entityitem;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(SCALE, 1F);

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setFloat("Scale", this.getScale());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setDeathWormScale(compound.getFloat("Scale"));
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public float getScale() {
        return this.dataManager.get(SCALE);
    }

    public void setDeathWormScale(float scale) {
        this.dataManager.set(SCALE, scale);
        this.updateAttributes();
        if(!this.world.isRemote){
            initSegments(scale);
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDeathWormHitbox(this.getEntityId(), scale));
        }
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(3));
        float size = 0.75F + (float)(Math.random() * 0.5F);
        this.setDeathWormScale(size);
        if(isSandBelow() && this.getScale() != 1){
            this.motionY = -0.5F;
        }
        return livingdata;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30F);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IceAndFire.CONFIG.deathWormTargetSearchLength));
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if(itemstack.getItem() == Items.DIAMOND){
            if(this.getScale() < 4){
                this.setDeathWormScale(4);
                return true;
            }else{
                this.setDeathWormScale(1);
                return true;
            }
        }
        return super.processInteract(player, hand);
    }

    private void switchNavigator(boolean inSand){
        if(inSand){
            this.moveHelper = new EntityDeathWorm.SandMoveHelper();
            this.navigator = new PathNavigateDeathWormSand(this, world);
            this.isSandNavigator = true;
        }else{
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateDeathWormLand(this, world);
            this.isSandNavigator = false;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source == DamageSource.IN_WALL){
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean checkBlockCollision(AxisAlignedBB bb) {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    IBlockState iblockstate1 = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    if (iblockstate1.getMaterial() != Material.AIR && iblockstate1.getMaterial() != Material.SAND) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return false;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        else {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int i = 0; i < 8; ++i) {
                int j = MathHelper.floor(this.posY + (double)(((float)((i >> 0) % 2) - 0.5F) * 0.1F) + (double)this.getEyeHeight());
                int k = MathHelper.floor(this.posX + (double)(((float)((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
                int l = MathHelper.floor(this.posZ + (double)(((float)((i >> 2) % 2) - 0.5F) * this.width * 0.8F));

                if (blockpos$pooledmutableblockpos.getX() != k || blockpos$pooledmutableblockpos.getY() != j || blockpos$pooledmutableblockpos.getZ() != l) {
                    blockpos$pooledmutableblockpos.setPos(k, j, l);

                    if (this.world.getBlockState(blockpos$pooledmutableblockpos).causesSuffocation() && this.world.getBlockState(blockpos$pooledmutableblockpos).getMaterial() != Material.SAND) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();
            return false;
        }
    }


    protected boolean pushOutOfBlocks(double x, double y, double z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = x - (double)blockpos.getX();
        double d1 = y - (double)blockpos.getY();
        double d2 = z - (double)blockpos.getZ();

        if (!this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
            return false;
        }
        else {
            EnumFacing enumfacing = EnumFacing.UP;
            double d3 = Double.MAX_VALUE;

            if (!isBlockFullCube(blockpos.west()) && d0 < d3) {
                d3 = d0;
                enumfacing = EnumFacing.WEST;
            }

            if (!isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
                d3 = 1.0D - d0;
                enumfacing = EnumFacing.EAST;
            }

            if (!isBlockFullCube(blockpos.north()) && d2 < d3) {
                d3 = d2;
                enumfacing = EnumFacing.NORTH;
            }

            if (!isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
                d3 = 1.0D - d2;
                enumfacing = EnumFacing.SOUTH;
            }

            if (!isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
                d3 = 1.0D - d1;
                enumfacing = EnumFacing.UP;
            }

            float f = this.rand.nextFloat() * 0.2F + 0.1F;
            float f1 = (float)enumfacing.getAxisDirection().getOffset();

            if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                this.motionX = (double)(f1 * f);
                this.motionY *= 0.75D;
                this.motionZ *= 0.75D;
            }
            else if (enumfacing.getAxis() == EnumFacing.Axis.Y){
                this.motionX *= 0.75D;
                this.motionY = (double)(f1 * f);
                this.motionZ *= 0.75D;
            }
            else if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
                this.motionX *= 0.75D;
                this.motionY *= 0.75D;
                this.motionZ = (double)(f1 * f);
            }
            return true;
        }
    }

    private boolean isBlockFullCube(BlockPos pos) {
        AxisAlignedBB axisalignedbb = world.getBlockState(pos).getCollisionBoundingBox(world, pos);
        return world.getBlockState(pos).getMaterial() != Material.SAND && axisalignedbb != Block.NULL_AABB && axisalignedbb.getAverageEdgeLength() >= 1.0D;
    }

    private void updateAttributes() {
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D * this.getScale());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D * this.getScale());
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30F * this.getScale());
        this.heal(30F * this.getScale());
    }

    public void onLivingUpdate(){
        super.onLivingUpdate();
        if(this.ticksExisted == 1){
            initSegments(this.getScale());
        }
        if(this.getAttackTarget() == null){
            this.rotationPitch = 0;
        }else{

            this.faceEntity(this.getAttackTarget(), 10.0F, 10.0F);
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist >= 4.0D * getScale() && dist <= 16.0D * getScale() && (this.isInSand() || this.onGround)) {
                double d0 = this.getAttackTarget().posX - this.posX;
                double d1 = this.getAttackTarget().posZ - this.posZ;
                float leap = MathHelper.sqrt(d0 * d0 + d1 * d1);
                if ((double)leap >= 1.0E-4D) {
                    this.motionX += d0 / (double)leap * 0.800000011920929D + this.motionX * 0.20000000298023224D * getScale();
                    this.motionZ += d1 / (double)leap * 0.800000011920929D + this.motionZ * 0.20000000298023224D * getScale();
                }
                this.motionY = 0.5F;

            }
            if(dist < 5D * getScale() && this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 6){
                float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), f);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));

        if (this.world.isBlockLoaded(blockpos$mutableblockpos)) {
            blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + (double)this.getEyeHeight()));
            if(world.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.SAND || this.isEntityInsideOpaqueBlock()){
                blockpos$mutableblockpos.setY(world.getHeight(MathHelper.floor(this.posX), MathHelper.floor(this.posZ)));
            }
            return this.world.getCombinedLight(blockpos$mutableblockpos, 0);
        }
        else {
            return 0;
        }
    }

    public int getSurface(int x, int y, int z){
        BlockPos pos = new BlockPos(x, y, z);
        while(!world.isAirBlock(pos)){
            pos = pos.up();
        }
        return pos.getY();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        onUpdateParts();
        if(!this.isInSand()){
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
        }else{
            this.noClip = true;
            if (world.isRemote) {
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getSurface((int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ)) + 0.5F, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, new int[]{Block.getIdFromBlock(Blocks.SAND)});
                for(int i = 0; i < segments.length; i++){
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, segments[i].posX + (double) (this.rand.nextFloat() * segments[i].width * 2.0F) - (double) segments[i].width, this.getSurface((int)Math.floor(segments[i].posX), (int)Math.floor(segments[i].posY), (int)Math.floor(segments[i].posZ)) + 0.5F, segments[i].posZ + (double) (this.rand.nextFloat() * segments[i].width * 2.0F) - (double) segments[i].width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, new int[]{Block.getIdFromBlock(Blocks.SAND)});
                }
            }
        }

        if(isInSand() && !this.isSandNavigator){
            switchNavigator(true);
        }
        if(!isInSand() && this.isSandNavigator){
            switchNavigator(false);
        }
        if (world.isRemote) {
            tail_buffer.calculateChainSwingBuffer(90, 20, 5F, this);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private boolean isSandBelow(){
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY - 1);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        return iblockstate.getMaterial() == Material.SAND;
    }

    public boolean isInSand(){
        return this.world.isMaterialInBB(this.getEntityBoundingBox().grow(0.10000000149011612D, 0.4000000059604645D, 0.10000000149011612D), Material.SAND);
    }
    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_BITE};
    }

    public Entity[] getWormParts(){
        return segments;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        float f4;
        if (this.isBeingRidden() && this.canBeSteered()) {
            EntityLivingBase controller = (EntityLivingBase) this.getControllingPassenger();
            if (controller != null) {
                strafe = controller.moveStrafing * 0.5F;
                forward = controller.moveForward;
                if (forward <= 0.0F) {
                    forward *= 0.25F;
                }
                this.fallDistance = 0;
                if (this.isInWater()) {
                    this.moveRelative(strafe, vertical, forward,1F);
                    f4 = 0.8F;
                    float d0 = 3;
                    if (!this.onGround) {
                        d0 *= 0.5F;
                    }
                    if (d0 > 0.0F) {
                        f4 += (0.54600006F - f4) * d0 / 3.0F;
                    }
                    //this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= (double) f4;
                    this.motionX *= 0.900000011920929D;
                    this.motionY *= 0.900000011920929D;
                    this.motionY *= (double) f4;
                    this.motionZ *= 0.900000011920929D;
                    this.motionZ *= (double) f4;
                    motionY += 0.01185D;
                }else{
                    forward = controller.moveForward * 0.25F;
                    strafe = controller.moveStrafing * 0.125F;

                    this.setAIMoveSpeed(onGround ? (float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() : 2);
                    super.travel(strafe, vertical, forward);
                    return;
                }
                this.setAIMoveSpeed(onGround ? (float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() : 2);
                super.travel(strafe, vertical = 0, forward);
                this.prevLimbSwingAmount = this.limbSwingAmount;
                double deltaX = this.posX - this.prevPosX;
                double deltaZ = this.posZ - this.prevPosZ;
                double deltaY = this.posY - this.prevPosY;
                float delta = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 4.0F;
                if (delta > 1.0F) {
                    delta = 1.0F;
                }
                this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
                return;
            }
        }
        if (this.isServerWorld()) {
            float f5;
            if (this.isInSand()) {
                this.moveRelative(strafe, vertical, forward,0.1F);
                f4 = 0.8F;
                float d0 = (float) EnchantmentHelper.getDepthStriderModifier(this);
                if (d0 > 3.0F) {
                    d0 = 3.0F;
                }
                if (!this.onGround) {
                    d0 *= 0.5F;
                }
                if (d0 > 0.0F) {
                    f4 += (0.54600006F - f4) * d0 / 3.0F;
                }
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double) f4;
                this.motionX *= 0.900000011920929D;
                this.motionY *= 0.900000011920929D;
                this.motionY *= (double) f4;
                this.motionZ *= 0.900000011920929D;
                this.motionZ *= (double) f4;
            } else {
                super.travel(strafe, vertical, forward);
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        double deltaY = this.posY - this.prevPosY;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;

    }

    public class SandMoveHelper extends EntityMoveHelper {
        private EntityDeathWorm worm = EntityDeathWorm.this;

        public SandMoveHelper() {
            super(EntityDeathWorm.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.worm.getNavigator().noPath() && !this.worm.isBeingRidden()) {
                double distanceX = this.posX - this.worm.posX;
                double distanceY = this.posY - this.worm.posY;
                double distanceZ = this.posZ - this.worm.posZ;
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = (double)MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.worm.rotationYaw = this.limitAngle(this.worm.rotationYaw, angle, 30.0F);
                this.worm.setAIMoveSpeed((float) 2F);
                this.worm.motionY += (double)this.worm.getAIMoveSpeed() * distanceY * 0.1D;
                if (distance < (double)Math.max(1.0F, this.entity.width)) {
                    float f = this.worm.rotationYaw * 0.017453292F;
                    this.worm.motionX -= (double)(MathHelper.sin(f) * 0.35F);
                    this.worm.motionZ += (double)(MathHelper.cos(f) * 0.35F);
                }
            }else if (this.action == EntityMoveHelper.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            } else {
                this.worm.setAIMoveSpeed(0.0F);
            }
        }
    }
}
