package com.github.alexthe666.iceandfire.entity;

import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIFlee;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIFollowOwner;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIPickupItem;
import com.github.alexthe666.iceandfire.entity.ai.PixieAISteal;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

public class EntityPixie extends TameableEntity {

    public static final float[][] PARTICLE_RGB = new float[][]{new float[]{1F, 0.752F, 0.792F}, new float[]{0.831F, 0.662F, 1F}, new float[]{0.513F, 0.843F, 1F}, new float[]{0.654F, 0.909F, 0.615F}, new float[]{0.996F, 0.788F, 0.407F}};
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityPixie.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.VARINT);

    public static final int STEAL_COOLDOWN = 3000;

    public Effect[] positivePotions = new Effect[]{Effects.STRENGTH, Effects.JUMP_BOOST, Effects.SPEED, Effects.LUCK, Effects.HASTE};
    public Effect[] negativePotions = new Effect[]{Effects.WEAKNESS, Effects.NAUSEA, Effects.SLOWNESS, Effects.UNLUCK, Effects.MINING_FATIGUE};
    public boolean slowSpeed = false;
    public int ticksUntilHouseAI;
    public int ticksHeldItemFor;
    private BlockPos housePos;
    public int stealCooldown = 0;
    private boolean isSitting;

    public EntityPixie(EntityType type, World worldIn) {
        super(type, worldIn);
        this.moveController = new EntityPixie.AIMoveControl(this);
        this.experienceValue = 3;
        this.setDropChance(EquipmentSlotType.MAINHAND, 0F);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, World world, double x, double z, Random rand) {
        BlockPos pos = new BlockPos(x, entity.getPosY(), z);
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
                    if (world.getTileEntity(entity.func_233580_cy_().add(xSearch, ySearch, zSearch)) != null && world.getTileEntity(entity.func_233580_cy_().add(xSearch, ySearch, zSearch)) instanceof TileEntityPixieHouse) {
                        TileEntityPixieHouse house = (TileEntityPixieHouse) world.getTileEntity(entity.func_233580_cy_().add(xSearch, ySearch, zSearch));
                        if (!house.hasPixie) {
                            return entity.func_233580_cy_().add(xSearch, ySearch, zSearch);
                        }
                    }
                }
            }
        }
        return entity.func_233580_cy_();
    }

    public boolean isSitting() {
        if (world.isRemote) {
            boolean isSitting = (this.dataManager.get(TAMED).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            this.func_233687_w_(isSitting);
            return isSitting;
        }
        return this.isSitting;
    }

    public void setSitting(boolean sitting) {
        if (!world.isRemote) {
            this.isSitting = sitting;
            this.func_233687_w_(sitting);
        }
        byte b0 = this.dataManager.get(TAMED).byteValue();
        if (sitting) {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    @Override
    public boolean func_233684_eK_() {
        return this.isSitting();
    }

    protected int getExperiencePoints(PlayerEntity player) {
        return 3;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.func_233666_p_()
                //HEALTH
                .func_233815_a_(Attributes.field_233818_a_, 10D)
                //SPEED
                .func_233815_a_(Attributes.field_233821_d_, 0.25D);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);

        if (!this.world.isRemote && this.getRNG().nextInt(3) == 0 && !this.getHeldItem(Hand.MAIN_HAND).isEmpty() && properties != null && !properties.isStone()) {
            this.entityDropItem(this.getHeldItem(Hand.MAIN_HAND), 0);
            this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            this.stealCooldown = STEAL_COOLDOWN;
            return true;
        }
        if (this.isOwnerClose() && (source == DamageSource.FALLING_BLOCK || source == DamageSource.IN_WALL || this.getOwner() != null && source.getTrueSource() == this.getOwner())) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        boolean invulnerable = super.isInvulnerableTo(source);
        if(!invulnerable) {
            Entity owner = this.getOwner();
            if(owner != null && source.getTrueSource() == owner) {
                return true;
            }
        }
        return invulnerable;
    }

    public void onDeath(DamageSource cause) {
        if (!this.world.isRemote && !this.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            this.entityDropItem(this.getHeldItem(Hand.MAIN_HAND), 0);
            this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.onDeath(cause);
        //if (cause.getTrueSource() instanceof PlayerEntity) {
        //	((PlayerEntity) cause.getTrueSource()).addStat(ModAchievements.killPixie);
        //}
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(COLOR, Integer.valueOf(0));
        this.dataManager.register(COMMAND, Integer.valueOf(0));
    }

    protected void collideWithEntity(Entity entityIn) {
        if (this.getOwner() != entityIn) {
            entityIn.applyEntityCollision(this);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        if (this.isOwner(player)) {

            if (player.getHeldItem(hand).getItem() == Items.SUGAR && this.getHealth() < this.getMaxHealth()) {
                this.heal(5);
                player.getHeldItem(hand).shrink(1);
                this.playSound(IafSoundRegistry.PIXIE_TAUNT, 1F, 1F);
                return ActionResultType.SUCCESS;
            } else {

                // make pixie sit via a check in livingTick() like Hippogryphs work
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() > 1) {
                    this.setCommand(0);
                }

                return ActionResultType.SUCCESS;
            }
        } else if (player.getHeldItem(hand).getItem() == Item.getItemFromBlock(IafBlockRegistry.JAR_EMPTY) && !this.isTamed()) {
            if (!player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
            }
            Block jar = IafBlockRegistry.JAR_PIXIE_0;
            switch (this.getColor()) {
                case 0:
                    jar = IafBlockRegistry.JAR_PIXIE_0;
                    break;
                case 1:
                    jar = IafBlockRegistry.JAR_PIXIE_1;
                    break;
                case 2:
                    jar = IafBlockRegistry.JAR_PIXIE_2;
                    break;
                case 3:
                    jar = IafBlockRegistry.JAR_PIXIE_3;
                    break;
                case 4:
                    jar = IafBlockRegistry.JAR_PIXIE_4;
                    break;
            }
            ItemStack stack = new ItemStack(jar, 1);
            if (!world.isRemote) {
                if (!this.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                    this.entityDropItem(this.getHeldItem(Hand.MAIN_HAND), 0.0F);
                    this.stealCooldown = STEAL_COOLDOWN;
                }

                this.entityDropItem(stack, 0.0F);
            }
            //player.addStat(ModAchievements.jarPixie);
            this.remove();
        }
        return super.func_230254_b_(player, hand);
    }

    public void flipAI(boolean flee) {
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PixieAIFollowOwner(this, 1.0D, 2.0F, 4.0F));
        this.goalSelector.addGoal(4, new PixieAIPickupItem(this, false));  // NOTE: tweakbsd priority changed!
        this.goalSelector.addGoal(2, new PixieAIFlee(this, PlayerEntity.class, 10, new Predicate<PlayerEntity>() {
            @Override
            public boolean apply(@Nullable PlayerEntity entity) {
                return true;
            }
        }));
        this.goalSelector.addGoal(2, new PixieAISteal(this, 1.0D));
        this.goalSelector.addGoal(3, new AIMoveRandom());
        this.goalSelector.addGoal(4, new AIEnterHouse());
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColor(this.rand.nextInt(5));
        this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);

        if(dataTag != null) {
            System.out.println("EntityPixie spawned with dataTag: " + dataTag.toString());
        }

        return spawnDataIn;
    }

    private boolean isBeyondHeight() {
        if (this.getPosY() > this.world.getHeight()) {
            return true;
        }
        BlockPos height = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.func_233580_cy_());
        int maxY = 20 + height.getY();
        return this.getPosY() > maxY;
    }

    public int getCommand() {
        return Integer.valueOf(this.dataManager.get(COMMAND).intValue());
    }

    public void setCommand(int command) {
        this.dataManager.set(COMMAND, Integer.valueOf(command));
        if (command == 1) {
            this.setSitting(true);
        } else {
            this.setSitting(false);
        }
    }


    public void livingTick() {
        super.livingTick();

        if (!this.world.isRemote) {

            // NOTE: This code was taken from EntityHippogryph basically same idea
            if (this.isSitting() && this.getCommand() != 1) {
                this.setSitting(false);
            }
            if (!this.isSitting() && this.getCommand() == 1) {
                this.setSitting(true);
            }
            if (this.isSitting()) {
                this.getNavigator().clearPath();
            }
        }

        if(stealCooldown > 0){
            stealCooldown--;
        }
        if(!this.getHeldItemMainhand().isEmpty() && !this.isTamed()){
            ticksHeldItemFor++;
        }else{
            ticksHeldItemFor = 0;
        }


        if (!this.func_233684_eK_() && !this.isBeyondHeight()) {
            this.setMotion(this.getMotion().add(0, 0.08, 0));
        } else {
        }
        if (world.isRemote) {
            IceAndFire.PROXY.spawnParticle("if_pixie", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(), PARTICLE_RGB[this.getColor()][0], PARTICLE_RGB[this.getColor()][1], PARTICLE_RGB[this.getColor()][2]);
        }
        if (ticksUntilHouseAI > 0) {
            ticksUntilHouseAI--;
        }
        if (!world.isRemote) {
            if (housePos != null && this.getDistanceSq(Vector3d.func_237489_a_(housePos)) < 1.5F && world.getTileEntity(housePos) != null && world.getTileEntity(housePos) instanceof TileEntityPixieHouse) {
                TileEntityPixieHouse house = (TileEntityPixieHouse) world.getTileEntity(housePos);
                if (house.hasPixie) {
                    this.housePos = null;
                } else {
                    house.hasPixie = true;
                    house.pixieType = this.getColor();
                    house.pixieItems.set(0, this.getHeldItem(Hand.MAIN_HAND));
                    house.tamedPixie = this.isTamed();
                    house.pixieOwnerUUID = this.getOwnerId();
                    IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(housePos.toLong(), true, this.getColor()));
                    this.remove();
                }
            }
        }
        if (this.getOwner() != null && this.isOwnerClose() && this.ticksExisted % 80 == 0) {
            this.getOwner().addPotionEffect(new EffectInstance(positivePotions[this.getColor()], 100, 0, false, false));
        }
        //PlayerEntity player = world.getClosestPlayerToEntity(this, 25);
        //if (player != null) {
        //	player.addStat(ModAchievements.findPixie);
        //}
    }

    public int getColor() {
        return MathHelper.clamp(this.getDataManager().get(COLOR).intValue(), 0, 4);
    }

    public void setColor(int color) {
        this.getDataManager().set(COLOR, color);
    }



    @Override
    public void readAdditional(CompoundNBT compound) {
        this.setColor(compound.getInt("Color"));

        this.stealCooldown = compound.getInt("StealCooldown");
        this.ticksHeldItemFor = compound.getInt("HoldingTicks");

        this.setSitting(compound.getBoolean("PixieSitting"));
        this.setCommand(compound.getInt("Command"));

        super.readAdditional(compound);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putInt("Color", this.getColor());
        compound.putInt("Command", this.getCommand());

        compound.putInt("StealCooldown", this.stealCooldown);
        compound.putInt("HoldingTicks", this.ticksHeldItemFor);
        compound.putBoolean("PixieSitting", this.isSitting());

        super.writeAdditional(compound);
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
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

    class AIMoveControl extends MovementController {
        public AIMoveControl(EntityPixie pixie) {
            super(pixie);
        }

        public void tick() {
            float speedMod = 1;
            if (EntityPixie.this.slowSpeed) {
                speedMod = 2F;
            }
            if (this.action == MovementController.Action.MOVE_TO) {
                if (EntityPixie.this.collidedHorizontally) {
                    EntityPixie.this.rotationYaw += 180.0F;
                    speedMod = 0.1F;
                    BlockPos target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.world, EntityPixie.this.getPosX() + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.getPosZ() + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.rand);
                    this.posX = target.getX();
                    this.posY = target.getY();
                    this.posZ = target.getZ();
                }
                double d0 = this.posX - EntityPixie.this.getPosX();
                double d1 = this.posY - EntityPixie.this.getPosY();
                double d2 = this.posZ - EntityPixie.this.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);

                if (d3 < EntityPixie.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    EntityPixie.this.setMotion(EntityPixie.this.getMotion().mul(0.5D, 0.5D, 0.5D));
                } else {
                    EntityPixie.this.setMotion(EntityPixie.this.getMotion().add(d0 / d3 * 0.05D * this.speed * speedMod, d1 / d3 * 0.05D * this.speed * speedMod, d2 / d3 * 0.05D * this.speed * speedMod));

                    if (EntityPixie.this.getAttackTarget() == null) {
                        EntityPixie.this.rotationYaw = -((float) MathHelper.atan2(EntityPixie.this.getMotion().x, EntityPixie.this.getMotion().z)) * (180F / (float) Math.PI);
                        EntityPixie.this.renderYawOffset = EntityPixie.this.rotationYaw;
                    } else {
                        double d4 = EntityPixie.this.getAttackTarget().getPosX() - EntityPixie.this.getPosX();
                        double d5 = EntityPixie.this.getAttackTarget().getPosZ() - EntityPixie.this.getPosZ();
                        EntityPixie.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityPixie.this.renderYawOffset = EntityPixie.this.rotationYaw;
                    }
                }
            }
        }
    }

    // TODO: Put into PixieAIMoveRandom
    class AIMoveRandom extends Goal {
        BlockPos target;

        public AIMoveRandom() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.world, EntityPixie.this.getPosX() + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.getPosZ() + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.rand);
            return !EntityPixie.this.isOwnerClose() && !EntityPixie.this.func_233684_eK_() && isDirectPathBetweenPoints(EntityPixie.this.func_233580_cy_(), target) && !EntityPixie.this.getMoveHelper().isUpdating() && EntityPixie.this.rand.nextInt(4) == 0 && EntityPixie.this.housePos == null;
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            return EntityPixie.this.world.rayTraceBlocks(new RayTraceContext(new Vector3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D), new Vector3d(posVec32.getX() + 0.5D, posVec32.getY() + (double) EntityPixie.this.getHeight() * 0.5D, posVec32.getZ() + 0.5D), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, EntityPixie.this)).getType() == RayTraceResult.Type.MISS;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            if (!isDirectPathBetweenPoints(EntityPixie.this.func_233580_cy_(), target)) {
                target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.world, EntityPixie.this.getPosX() + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.getPosZ() + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.rand);
            }
            if (EntityPixie.this.world.isAirBlock(target)) {
                EntityPixie.this.moveController.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntityPixie.this.getAttackTarget() == null) {
                    EntityPixie.this.getLookController().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AIEnterHouse extends Goal {
        public AIEnterHouse() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            if (EntityPixie.this.isOwnerClose() || EntityPixie.this.getMoveHelper().isUpdating() || EntityPixie.this.func_233684_eK_()/*isSiting() */ || EntityPixie.this.rand.nextInt(20) != 0 || EntityPixie.this.ticksUntilHouseAI != 0) {
                return false;
            }

            BlockPos blockpos1 = findAHouse(EntityPixie.this, EntityPixie.this.world);
            return !blockpos1.toString().equals(EntityPixie.this.func_233580_cy_().toString());
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = EntityPixie.this.getHousePos() == null ? EntityPixie.this.func_233580_cy_() : EntityPixie.this.getHousePos();

            if (blockpos == null) {
                blockpos = EntityPixie.this.func_233580_cy_();
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = findAHouse(EntityPixie.this, EntityPixie.this.world);
                EntityPixie.this.moveController.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                EntityPixie.this.housePos = blockpos1;
                if (EntityPixie.this.getAttackTarget() == null) {
                    EntityPixie.this.getLookController().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                }
            }
        }
    }
}