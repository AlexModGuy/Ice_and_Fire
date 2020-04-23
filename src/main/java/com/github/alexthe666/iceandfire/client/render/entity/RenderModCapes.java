package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderModCapes {
    public ResourceLocation redTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_fire.png");
    public ResourceLocation redElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_fire.png");
    public ResourceLocation blueTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_ice.png");
    public ResourceLocation blueElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_ice.png");
    public ResourceLocation betaTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_beta.png");
    public ResourceLocation betaElytraTex = new ResourceLocation("iceandfire", "textures/models/misc/elytra_beta.png");

    public UUID[] redcapes = new UUID[]{
            /* zeklo */UUID.fromString("59efccaf-902d-45da-928a-5a549b9fd5e0"),
            /* Alexthe666 */UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c")
    };
    public UUID[] bluecapes = new UUID[]{
            /* Raptorfarian */UUID.fromString("0ed918c8-d612-4360-b711-cd415671356f"),
            /*Zyranna*/        UUID.fromString("5d43896a-06a0-49fb-95c5-38485c63667f")};
    public UUID[] betatesters = new UUID[]{
    };

    @SubscribeEvent
    public void playerRender(RenderPlayerEvent.Pre event) {
        if (event.getEntityPlayer() instanceof AbstractClientPlayer) {
            NetworkPlayerInfo info = ((AbstractClientPlayer)event.getEntityPlayer()).getPlayerInfo();
            if (info != null) {
                Map<Type, ResourceLocation> textureMap = null;
                try {
                    textureMap = (Map<Type, ResourceLocation>) ReflectionHelper.findField(NetworkPlayerInfo.class, new String[]{"playerTextures", "field_187107_a"}).get(info);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (textureMap != null) {
                    if (hasBetaCape(event.getEntityPlayer().getUniqueID())) {
                        textureMap.put(Type.CAPE, betaTex);
                        textureMap.put(Type.ELYTRA, betaElytraTex);
                    }
                    if (hasRedCape(event.getEntityPlayer().getUniqueID())) {
                        textureMap.put(Type.CAPE, redTex);
                        textureMap.put(Type.ELYTRA, redElytraTex);
                    }
                    if (hasBlueCape(event.getEntityPlayer().getUniqueID())) {
                        textureMap.put(Type.CAPE, blueTex);
                        textureMap.put(Type.ELYTRA, blueElytraTex);
                    }
                }
            }
        }
        if(event.getEntityPlayer().getUniqueID().equals(ServerEvents.ALEX_UUID)){
            GL11.glPushMatrix();
            float f2 = ((float) event.getEntityPlayer().ticksExisted - 1 +  event.getPartialRenderTick());
            float f3 = MathHelper.sin(f2 / 10.0F) * 0.1F + 0.1F;
            GL11.glTranslatef((float) 0, (float) 1.3F * event.getEntityPlayer().height, (float) 0);
            float f4 = (f2 / 20.0F) * (180F / (float) Math.PI);
            GlStateManager.rotate(f4, 0.0F, 1.0F, 0.0F);
            GL11.glPushMatrix();
            GL11.glScalef(1.4F, 1.4F, 1.4F);
            Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, new ItemStack(IafItemRegistry.weezer_blue_album), ItemCameraTransforms.TransformType.GROUND);
            GL11.glPopMatrix();
            GL11.glPopMatrix();

        }
    }

    private boolean hasRedCape(UUID uniqueID) {
        for (UUID uuid1 : redcapes) {
            if (uniqueID.equals(uuid1)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasBlueCape(UUID uniqueID) {
        for (UUID uuid1 : bluecapes) {
            if (uniqueID.equals(uuid1)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasBetaCape(UUID uniqueID) {
        for (UUID uuid1 : betatesters) {
            if (uniqueID.equals(uuid1)) {
                return true;
            }
        }
        return false;
    }
}
