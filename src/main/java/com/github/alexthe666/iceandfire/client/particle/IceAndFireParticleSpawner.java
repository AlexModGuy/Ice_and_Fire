package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class IceAndFireParticleSpawner {
    private Minecraft mc;

    public void spawnParticle(Particle particleID, boolean ignoreRange, boolean ignoreLimit, boolean minParticles, double xCoord, double yCoord, double zCoord) {
        try {
            this.spawnParticle0(particleID, ignoreRange, ignoreLimit, minParticles, xCoord, yCoord, zCoord);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("ID", particleID);
            crashreportcategory.addDetail("Position", new ICrashReportDetail<String>() {
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    private Particle spawnParticle0(Particle particleID, boolean ignoreRange, boolean ingoreLimit, boolean minParticles, double xCoord, double yCoord, double zCoord) {
        if (mc == null) {
            mc = Minecraft.getInstance();
        }

        Entity entity = this.mc.getRenderViewEntity();

        if (this.mc != null && entity != null && this.mc.effectRenderer != null) {
            int k1 = this.calculateParticleLevel(minParticles, ingoreLimit);
            double d3 = entity.getPosX() - xCoord;
            double d4 = entity.getPosY() - yCoord;
            double d5 = entity.getPosZ() - zCoord;
            if (ignoreRange) {
                return spawnEffectParticle(particleID);
            } else if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D) {
                return null;
            } else {
                return k1 > 1 ? null : spawnEffectParticle(particleID);
            }
        } else {
            return null;
        }
    }

    @Nullable
    public Particle spawnEffectParticle(Particle particle) {
        if (particle != null) {
            mc.effectRenderer.addEffect(particle);
            return particle;
        }
        return null;
    }

    private int calculateParticleLevel(boolean minimiseLevel, boolean ingoreLimit) {
        if (mc == null || mc.world == null) {
            return 2;
        }
        int k1 = this.mc.gameSettings.particleSetting;
        if (minimiseLevel && k1 == 2 && mc.world.rand.nextInt(10) == 0) {
            k1 = 1;
        }
        if (k1 == 1 && mc.world.rand.nextInt(3) == 0) {
            k1 = 2;
        }
        return k1;
    }

}
