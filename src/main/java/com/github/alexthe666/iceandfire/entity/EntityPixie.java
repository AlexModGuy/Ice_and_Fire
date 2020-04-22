package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIFlee;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIFollowOwner;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIPickupItem;
import com.github.alexthe666.iceandfire.entity.ai.PixieAISteal;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityPixie extends EntityTameable {

    public static final float[][] PARTICLE_RGB = new float[][]{new float[]{1F, 0.752F, 0.792F}, new float[]{0.831F, 0.662F, 1F}, new float[]{0.513F, 0.843F, 1F}, new float[]{0.654F, 0.909F, 0.615F}, new float[]{0.996F, 0.788F, 0.407F}};
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "if_pixie"));
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityPixie.class, DataSerializers.VARINT);
    public Potion[] positivePotions = new Potion[]{MobEffects.STRENGTH, MobEffects.JUMP_BOOST, MobEffects.SPEED, MobEffects.LUCK, MobEffects.HASTE};
    public Potion[] negativePotions = new Potion[]{MobEffects.WEAKNESS, MobEffects.NAUSEA, MobEffects.SLOWNESS, MobEffects.UNLUCK, MobEffects.MINING_FATIGUE};
    public boolean slowSpeed = false;
    public int ticksUntilHouseAI;
    private BlockPos housePos;
    private PixieAIFlee aiFlee;
    private PixieAISteal aiTempt;

    public EntityPixie(World worldIn) {
        super(worldIn);
        this.moveHelper = new EntityPixie.AIMoveControl(this);
        this.setSize(0.4F, 0.8F);
        this.experienceValue = 3;
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0F);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, World world, double x, double z, Random rand) {
        BlockPos pos = new BlockPos(x, entity.posY, z);
        for (int yDown = 0; yDown < 3; yDown++) {
            if (!world.isAirBlock(pos.down(yDown))) {
                return pos.up(yDown);
            }
        }
        return pos;
    }

    public static BlockPos findAHouse(Entity entity, World world) {
        for (int xSearch = -10; xSearch < 10; xSearch++) {
            for (int ySearch = -10; ySearch < 10; ySearch++) {
                for (int zSearch = -10; zSearch < 10; zSearch++) {
                    if (world.getTileEntity(entity.getPosition().add(xSearch, ySearch, zSearch)) != null && world.getTileEntity(entity.getPosition().add(xSearch, ySearch, zSearch)) instanceof TileEntityPixieHouse) {
                        TileEntityPixieHouse house = (TileEntityPixieHouse) world.getTileEntity(entity.getPosition().add(xSearch, ySearch, zSearch));
                        if (!house.hasPixie) {
                            return entity.getPosition().add(xSearch, ySearch, zSearch);
                        }
                    }
                }
            }
        }
        return entity.getPosition();
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 3;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);

        if (!this.world.isRemote && this.getRNG().nextInt(3) == 0 && this.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY && !properties.isStone) {
            this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND), 0);
            this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            return true;
        }
        if (this.isOwnerClose() && (source == DamageSource.FALLING_BLOCK || source == DamageSource.IN_WALL || this.getOwner() != null && source.getTrueSource() == this.getOwner())) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    public void onDeath(DamageSource cause) {
        if (!this.world.isRemote && !this.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND), 0);
            this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.onDeath(cause);
        //if (cause.getTrueSource() instanceof EntityPlayer) {
        //	((EntityPlayer) cause.getTrueSource()).addStat(ModAchievements.killPixie);
        //}
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(COLOR, Integer.valueOf(0));
    }

    protected void collideWithEntity(Entity entityIn) {
        if (this.getOwner() != entityIn) {
            entityIn.applyEntityCollision(this);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }

        if (onGroundIn) {
            if (this.fallDistance > 0.0F) {
                state.getBlock().onFallenUpon(this.world, pos, this, this.fallDistance);
            }

            this.fallDistance = 0.0F;
        } else if (y < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - y);
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
            return true;
        }
        if (this.isOwner(player)) {
            if (player.getHeldItem(hand).getItem() == Items.SUGAR && this.getHealth() < this.getMaxHealth()) {
                this.heal(5);
                player.getHeldItem(hand).shrink(1);
                this.playSound(IafSoundRegistry.PIXIE_TAUNT, 1F, 1F);
                return true;
            } else {
                this.setSitting(!this.isSitting());
                return true;
            }
        } else if (player.getHeldItem(hand).getItem() == Item.getItemFromBlock(IafBlockRegistry.jar_empty) && player.getHeldItem(hand).getMetadata() == 0 && !this.isTamed()) {
            if (!player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
            }
            ItemStack stack = new ItemStack(IafBlockRegistry.jar_pixie, 1, this.getColor());
            if (!world.isRemote) {
                if (!this.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
                    this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND), 0.0F);
                }
                this.entityDropItem(stack, 0.0F);
            }
            //player.addStat(ModAchievements.jarPixie);
            this.setDead();
        }
        return super.processInteract(player, hand);
    }

    public void flipAI(boolean flee) {
        if (flee) {
            this.tasks.removeTask(aiTempt);
            this.tasks.addTask(3, aiFlee);
        } else {
            this.tasks.removeTask(aiFlee);
            this.tasks.addTask(3, aiTempt);
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new PixieAIFollowOwner(this, 1.0D, 2.0F, 4.0F));
        this.tasks.addTask(1, new PixieAIPickupItem(this, false));

        this.tasks.addTask(2, aiTempt = new PixieAISteal(this, 1.0D));

        this.tasks.addTask(2, aiFlee = new PixieAIFlee(this, EntityPlayer.class, 10, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer entity) {
                return true;
            }
        }));
        this.tasks.addTask(3, new AIMoveRandom());
        this.tasks.addTask(4, new AIEnterHouse());
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.removeTask(aiFlee);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setColor(this.rand.nextInt(5));
        this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        return livingdata;
    }

    private boolean isBeyondHeight() {
        if (this.posY > this.world.getHeight()) {
            return true;
        }
        BlockPos height = this.world.getHeight(new BlockPos(this));
        int maxY = 20 + height.getY();
        return this.posY > maxY;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.isSitting() && !this.isBeyondHeight()) {
            this.motionY += 0.08D;
        } else {
            this.moveHelper.action = EntityMoveHelper.Action.WAIT;
        }
        if (world.isRemote) {
            IceAndFire.PROXY.spawnParticle("if_pixie", this.posX + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width, PARTICLE_RGB[this.getColor()][0], PARTICLE_RGB[this.getColor()][1], PARTICLE_RGB[this.getColor()][2]);
        }
        if (ticksUntilHouseAI > 0) {
            ticksUntilHouseAI--;
        }
        if (housePos != null && this.getDistanceSqToCenter(housePos) < 1.5F && world.getTileEntity(housePos) != null && world.getTileEntity(housePos) instanceof TileEntityPixieHouse) {

            if (((TileEntityPixieHouse) world.getTileEntity(housePos)).hasPixie) {
                this.housePos = null;
                this.moveHelper.action = EntityMoveHelper.Action.WAIT;
            } else {
                ((TileEntityPixieHouse) world.getTileEntity(housePos)).hasPixie = true;
                ((TileEntityPixieHouse) world.getTileEntity(housePos)).pixieType = this.getColor();
                ((TileEntityPixieHouse) world.getTileEntity(housePos)).pixieItems.set(0, this.getHeldItem(EnumHand.MAIN_HAND));
                ((TileEntityPixieHouse) world.getTileEntity(housePos)).tamedPixie = this.isTamed();
                ((TileEntityPixieHouse) world.getTileEntity(housePos)).pixieOwnerUUID = this.getOwnerId();
                IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieHouse(housePos.toLong(), true, this.getColor()));
                this.setDead();
            }
        }
        if (this.getOwner() != null && this.isOwnerClose() && this.ticksExisted % 80 == 0) {
            this.getOwner().addPotionEffect(new PotionEffect(positivePotions[this.getColor()], 100, 0, false, false));
        }
        //EntityPlayer player = world.getClosestPlayerToEntity(this, 25);
        //if (player != null) {
        //	player.addStat(ModAchievements.findPixie);
        //}
    }

    public int getColor() {
        return this.getDataManager().get(COLOR).intValue();
    }

    public void setColor(int color) {
        this.getDataManager().set(COLOR, color);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.setColor(compound.getInteger("Color"));
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Color", this.getColor());
        super.writeEntityToNBT(compound);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    public BlockPos getHousePos() {
        return housePos;
    }

    public boolean isOwnerClose() {
        return this.isTamed() && this.getOwner() != null && this.getDistanceSq(this.getOwner()) < 100;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.PIXIE_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return IafSoundRegistry.PIXIE_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.PIXIE_DIE;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    class AIMoveControl extends EntityMoveHelper {
        public AIMoveControl(EntityPixie pixie) {
            super(pixie);
            this.speed = 0.75F;
        }

        public void onUpdateMoveHelper() {
            if (EntityPixie.this.slowSpeed) {
                this.speed = 2F;
            }
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                if (EntityPixie.this.collidedHorizontally) {
                    EntityPixie.this.rotationYaw += 180.0F;
                    this.speed = 0.1F;
                    BlockPos target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.world, EntityPixie.this.posX + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.posZ + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.rand);
                    this.posX = target.getX();
                    this.posY = target.getY();
                    this.posZ = target.getZ();
                }
                double d0 = this.posX - EntityPixie.this.posX;
                double d1 = this.posY - EntityPixie.this.posY;
                double d2 = this.posZ - EntityPixie.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);

                if (d3 < EntityPixie.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityPixie.this.motionX *= 0.5D;
                    EntityPixie.this.motionY *= 0.5D;
                    EntityPixie.this.motionZ *= 0.5D;
                } else {
                    EntityPixie.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityPixie.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityPixie.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityPixie.this.getAttackTarget() == null) {
                        EntityPixie.this.rotationYaw = -((float) MathHelper.atan2(EntityPixie.this.motionX, EntityPixie.this.motionZ)) * (180F / (float) Math.PI);
                        EntityPixie.this.renderYawOffset = EntityPixie.this.rotationYaw;
                    } else {
                        double d4 = EntityPixie.this.getAttackTarget().posX - EntityPixie.this.posX;
                        double d5 = EntityPixie.this.getAttackTarget().posZ - EntityPixie.this.posZ;
                        EntityPixie.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityPixie.this.renderYawOffset = EntityPixie.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase {
        BlockPos target;

        public AIMoveRandom() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.world, EntityPixie.this.posX + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.posZ + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.rand);
            return !EntityPixie.this.isOwnerClose() && !EntityPixie.this.isSitting() && isDirectPathBetweenPoints(EntityPixie.this.getPosition(), target) && !EntityPixie.this.getMoveHelper().isUpdating() && EntityPixie.this.rand.nextInt(4) == 0 && EntityPixie.this.housePos == null;
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            RayTraceResult raytraceresult = EntityPixie.this.world.rayTraceBlocks(new Vec3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D), new Vec3d(posVec32.getX() + 0.5D, posVec32.getY() + (double) EntityPixie.this.height * 0.5D, posVec32.getZ() + 0.5D), false, true, false);
            return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            if (!isDirectPathBetweenPoints(EntityPixie.this.getPosition(), target)) {
                target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.world, EntityPixie.this.posX + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.posZ + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.rand);
            }
            if (EntityPixie.this.world.isAirBlock(target)) {
                EntityPixie.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntityPixie.this.getAttackTarget() == null) {
                    EntityPixie.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AIEnterHouse extends EntityAIBase {
        public AIEnterHouse() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            if (EntityPixie.this.isOwnerClose() || EntityPixie.this.getMoveHelper().isUpdating() || EntityPixie.this.isSitting() || EntityPixie.this.rand.nextInt(20) != 0 || EntityPixie.this.ticksUntilHouseAI != 0) {
                return false;
            }

            BlockPos blockpos1 = findAHouse(EntityPixie.this, EntityPixie.this.world);
            return !blockpos1.toString().equals(EntityPixie.this.getPosition().toString());
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = EntityPixie.this.getHousePos() == null ? EntityPixie.this.getPosition() : EntityPixie.this.getHousePos();

            if (blockpos == null) {
                blockpos = new BlockPos(EntityPixie.this);
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = findAHouse(EntityPixie.this, EntityPixie.this.world);
                EntityPixie.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                EntityPixie.this.housePos = blockpos1;
                if (EntityPixie.this.getAttackTarget() == null) {
                    EntityPixie.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                }
            }
        }
    }
}