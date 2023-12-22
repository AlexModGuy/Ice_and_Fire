package com.github.alexthe666.iceandfire.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.github.alexthe666.iceandfire.IceAndFire.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafDamageRegistry { // TODO 1.19.2
    public static final String GORGON_DMG_TYPE = "gorgon";
    public static final String DRAGON_FIRE_TYPE = "dragon_fire";
    public static final String DRAGON_ICE_TYPE = "dragon_ice";
    public static final String DRAGON_LIGHTNING_TYPE = "dragon_lightning";

    static class CustomEntityDamageSource extends EntityDamageSource {
        public CustomEntityDamageSource(String damageTypeIn, @NotNull Entity damageSourceEntityIn) {
            super(damageTypeIn, damageSourceEntityIn);
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
            String s = "death.attack." + this.getMsgId();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Component.translatable(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : Component.translatable(s1, entityLivingBaseIn.getDisplayName());
        }
    }

    static class CustomIndirectEntityDamageSource extends IndirectEntityDamageSource {

        public CustomIndirectEntityDamageSource(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
            super(damageTypeIn, source, indirectEntityIn);
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
            String s = "death.attack." + this.getMsgId();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Component.translatable(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : Component.translatable(s1, entityLivingBaseIn.getDisplayName());
        }
    }

    public static CustomEntityDamageSource causeGorgonDamage(@Nullable Entity entity) {
        return (CustomEntityDamageSource) new CustomEntityDamageSource(GORGON_DMG_TYPE, entity).bypassArmor().bypassMagic();
    }

    public static CustomEntityDamageSource causeDragonFireDamage(@Nullable Entity entity) {
        return new CustomEntityDamageSource(DRAGON_FIRE_TYPE, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonFireDamage(Entity source, @Nullable Entity indirectEntityIn) {
        return new CustomIndirectEntityDamageSource(DRAGON_FIRE_TYPE, source, indirectEntityIn);
    }

    public static CustomEntityDamageSource causeDragonIceDamage(@Nullable Entity entity) {
        return new CustomEntityDamageSource(DRAGON_ICE_TYPE, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonIceDamage(Entity source, @Nullable Entity indirectEntityIn) {
        return new CustomIndirectEntityDamageSource(DRAGON_ICE_TYPE, source, indirectEntityIn);
    }

    public static CustomEntityDamageSource causeDragonLightningDamage(@Nullable Entity entity) {
        return new CustomEntityDamageSource(DRAGON_LIGHTNING_TYPE, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonLightningDamage(Entity source, @Nullable Entity indirectEntityIn) {
        return new CustomIndirectEntityDamageSource(DRAGON_LIGHTNING_TYPE, source, indirectEntityIn);
    }
}
