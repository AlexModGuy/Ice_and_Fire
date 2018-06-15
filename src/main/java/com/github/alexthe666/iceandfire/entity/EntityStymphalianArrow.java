package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityStymphalianArrow extends EntityArrow {

    public EntityStymphalianArrow(World worldIn) {
        super(worldIn);
        this.setDamage(3.5F);
    }

    public EntityStymphalianArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setDamage(3.5F);
    }

    public void onUpdate() {
        super.onUpdate();
        float sqrt = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (sqrt < 0.1F) {
            this.motionY -= 0.01F;
        }
    }

    public EntityStymphalianArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(3.5F);
    }

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.stymphalian_arrow);
    }
}
