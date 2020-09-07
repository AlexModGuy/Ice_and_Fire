package com.github.alexthe666.iceandfire.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class IafDamageRegistry {
    public static final DamageSource GORGON_DMG = new DamageCustomDeathMessage("gorgon");
    public static final DamageSource DRAGON_FIRE = new DamageCustomDeathMessage("dragon_fire");
    public static final DamageSource DRAGON_ICE = new DamageCustomDeathMessage("dragon_ice");
    public static final DamageSource DRAGON_LIGHTNING = new DamageCustomDeathMessage("dragon_lightning");

    static class DamageCustomDeathMessage extends DamageSource{

        public DamageCustomDeathMessage(String damageTypeIn) {
            super(damageTypeIn);
        }

        public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getAttackingEntity();
            String s = "death.attack." + this.damageType;
            int index = entityLivingBaseIn.getRNG().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? new TranslationTextComponent(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName());
        }

    }
}
