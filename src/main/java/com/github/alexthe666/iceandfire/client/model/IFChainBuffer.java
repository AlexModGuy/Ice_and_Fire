package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.util.IFlapable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author rafa_mv
 * @since 1.0.0
 */
public class IFChainBuffer {
    private int yawTimer;
    private float yawVariation;
    private int pitchTimer;
    private float pitchVariation;
    private float prevYawVariation;
    private float prevPitchVariation;

    /**
     * Resets this ChainBuffer's rotations.
     */
    public void resetRotations() {
        this.yawVariation = 0.0F;
        this.pitchVariation = 0.0F;
        this.prevYawVariation = 0.0F;
        this.prevPitchVariation = 0.0F;
    }

    private boolean compareDouble(double a, double b) {
        double c = a - b;
        return Math.abs(c - 1.0) <= 0.01D;
    }

    /**
     * Calculates the swing amounts for the given entity (Y axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can swing
     * @param bufferTime     the time it takes to swing this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param divisor        the amount to divide the swing amount by
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity) {
        this.prevYawVariation = this.yawVariation;
        if (!compareDouble(entity.yBodyRot, entity.yBodyRotO) && Mth.abs(this.yawVariation) < maxAngle) {
            this.yawVariation += Mth.clamp((entity.yBodyRotO - entity.yBodyRot) / divisor, -maxAngle, maxAngle);
        }
        if (this.yawVariation > angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = angleDecrement;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -1F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = angleDecrement;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
    }

    public void calculateChainPitchBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity) {
        this.prevPitchVariation = entity.xRotO;
        this.pitchVariation = entity.getXRot();
    }

    /**
     * Calculates the wave amounts for the given entity (X axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can wave
     * @param bufferTime     the time it takes to wave this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param divisor        the amount to divide the wave amount by
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity) {
        this.prevPitchVariation = this.pitchVariation;
        if (Math.abs(entity.getXRot()) > maxAngle) {
            return;
        }
        if (!compareDouble(entity.getXRot(), entity.xRotO) && Mth.abs(this.pitchVariation) < maxAngle) {
            this.pitchVariation += Mth.clamp((entity.xRotO - entity.getXRot()) / divisor, -maxAngle, maxAngle);
        }
        if (this.pitchVariation > angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation -= angleDecrement;
                if (Mth.abs(this.pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        } else if (this.pitchVariation < -1F * angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation += angleDecrement;
                if (Mth.abs(this.pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        }
    }


    /**
     * Calculates the flap amounts for the given entity (Z axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can wave
     * @param bufferTime     the time it takes to wave this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param divisor        the amount to divide the wave amount by
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainFlapBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity) {
        this.prevYawVariation = this.yawVariation;

        if (!compareDouble(entity.yBodyRot, entity.yBodyRotO) && Mth.abs(this.yawVariation) < maxAngle) {
            this.yawVariation += Mth.clamp((entity.yBodyRotO - entity.yBodyRot) / divisor, -maxAngle, maxAngle);
            if (entity instanceof IFlapable && Math.abs(entity.yBodyRotO - entity.yBodyRot) > 15D) {
                ((IFlapable) entity).flapWings();
            }
        }
        if (this.yawVariation > angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -1F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
    }

    /**
     * Calculates the flap amounts for the given entity (Z axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can wave
     * @param bufferTime     the time it takes to wave this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param divisor        the amount to divide the wave amount by
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainFlapBufferHead(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity) {
        this.prevYawVariation = this.yawVariation;

        if (!compareDouble(entity.yHeadRotO, entity.yHeadRot) && Mth.abs(this.yawVariation) < maxAngle) {
            this.yawVariation += Mth.clamp((entity.yHeadRot - entity.yHeadRotO) / divisor, -maxAngle, maxAngle);
            if (entity instanceof IFlapable && Math.abs(entity.yHeadRot - entity.yHeadRotO) > 15D) {
                ((IFlapable) entity).flapWings();
            }
        }
        if (this.yawVariation > angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -1F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (Mth.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
    }


    /**
     * Calculates the swing amounts for the given entity (Y axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can swing
     * @param bufferTime     the time it takes to swing this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, LivingEntity entity) {
        this.calculateChainSwingBuffer(maxAngle, bufferTime, angleDecrement, 1.0F, entity);
    }

    /**
     * Calculates the wave amounts for the given entity (X axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can wave
     * @param bufferTime     the time it takes to wave this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, LivingEntity entity) {
        this.calculateChainWaveBuffer(maxAngle, bufferTime, angleDecrement, 1.0F, entity);
    }

    /**
     * Calculates the flap amounts for the given entity (Z axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can wave
     * @param bufferTime     the time it takes to wave this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainFlapBuffer(float maxAngle, int bufferTime, float angleDecrement, LivingEntity entity) {
        this.calculateChainFlapBuffer(maxAngle, bufferTime, angleDecrement, 1.0F, entity);
    }

    /**
     * Applies this buffer on the Y axis to the given array of model boxes.
     *
     * @param boxes the box array
     */
    public void applyChainSwingBuffer(BasicModelPart... boxes) {
        float rotateAmount = 0.01745329251F * Mth.lerp(getPartialTicks(), this.prevYawVariation, this.yawVariation) / boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleY += rotateAmount;
        }
    }

    /**
     * Applies this buffer on the X axis to the given array of model boxes.
     *
     * @param boxes the box array
     */
    public void applyChainWaveBuffer(BasicModelPart... boxes) {
        float rotateAmount = 0.01745329251F * Mth.lerp(getPartialTicks(), this.prevYawVariation, this.yawVariation) / boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleX += rotateAmount;
        }
    }

    /**
     * Applies this buffer on the Z axis to the given array of model boxes.
     *
     * @param boxes the box array
     */
    public void applyChainFlapBuffer(BasicModelPart... boxes) {
        float rotateAmount = 0.01745329251F * Mth.lerp(getPartialTicks(), this.prevYawVariation, this.yawVariation) / boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleZ += rotateAmount;
        }
    }

    /**
     * Applies this buffer on the Z axis to the given array of model boxes. Reverses the calculation.
     *
     * @param boxes the box array
     */
    public void applyChainFlapBufferReverse(BasicModelPart... boxes) {
        float rotateAmount = 0.01745329251F * Mth.lerp(getPartialTicks(), this.prevYawVariation, this.yawVariation) / boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleZ -= rotateAmount * 0.5F;
        }
    }

    public void applyChainSwingBufferReverse(BasicModelPart... boxes) {
        float rotateAmount = 0.01745329251F * Mth.lerp(getPartialTicks(), this.prevYawVariation, this.yawVariation) / boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleY -= rotateAmount;
        }
    }

    public void applyChainWaveBufferReverse(BasicModelPart... boxes) {
        float rotateAmount = 0.01745329251F * Mth.lerp(getPartialTicks(), this.prevYawVariation, this.yawVariation) / boxes.length;
        for (BasicModelPart box : boxes) {
            box.rotateAngleX -= rotateAmount;
        }
    }


    private float getPartialTicks() {
        return Minecraft.getInstance().getFrameTime();
    }

}