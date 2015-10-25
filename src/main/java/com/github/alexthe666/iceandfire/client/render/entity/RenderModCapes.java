package com.github.alexthe666.iceandfire.client.render.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderModCapes {
	public static final String[] playerInfo = new String[]{"playerInfo", "field_175157_a"};
	public static final String[] cape = new String[]{"locationCape", "field_178862_f"};


	public ResourceLocation redTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_fire.png");
	public ResourceLocation blueTex = new ResourceLocation("iceandfire", "textures/models/misc/cape_ice.png");

	public UUID[] redcapes = new UUID[]{
			/*Alexthe666*/UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c"),
			/*zeklo*/UUID.fromString("59efccaf-902d-45da-928a-5a549b9fd5e0"),
	};
	public UUID[] bluecapes = new UUID[]{
			/*Raptorfarian*/UUID.fromString("0ed918c8-d612-4360-b711-cd415671356f"),
	};

	//field_178862_f
	@SubscribeEvent
	public void playerRender(RenderPlayerEvent.Pre event){
		if(event.entityPlayer instanceof AbstractClientPlayer){
			if (hasRedCape(event.entityPlayer.getUniqueID())){
				AbstractClientPlayer player = (AbstractClientPlayer)event.entityPlayer;
				Field field = ReflectionHelper.findField(AbstractClientPlayer.class, ObfuscationReflectionHelper.remapFieldNames(AbstractClientPlayer.class.getName(), playerInfo));
				try
				{
					Field modifier = Field.class.getDeclaredField("modifiers");
					modifier.setAccessible(true);
					modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
					NetworkPlayerInfo info = (NetworkPlayerInfo)field.get(player);
					Field field_capes = ReflectionHelper.findField(NetworkPlayerInfo.class, ObfuscationReflectionHelper.remapFieldNames(NetworkPlayerInfo.class.getName(), cape));
					try
					{
						Field modifier_i = Field.class.getDeclaredField("modifiers");
						modifier_i.setAccessible(true);
						modifier_i.setInt(field_capes, field_capes.getModifiers() & ~Modifier.FINAL);
						field_capes.set(info, redTex);
					}
					catch (Exception exception)
					{
						exception.printStackTrace();
					}
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}

			}
			else if (hasBlueCape(event.entityPlayer.getUniqueID())){
				AbstractClientPlayer player = (AbstractClientPlayer)event.entityPlayer;
				Field field = ReflectionHelper.findField(AbstractClientPlayer.class, ObfuscationReflectionHelper.remapFieldNames(AbstractClientPlayer.class.getName(), playerInfo));
				try
				{
					Field modifier = Field.class.getDeclaredField("modifiers");
					modifier.setAccessible(true);
					modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
					NetworkPlayerInfo info = (NetworkPlayerInfo)field.get(player);
					Field field_capes = ReflectionHelper.findField(NetworkPlayerInfo.class, ObfuscationReflectionHelper.remapFieldNames(NetworkPlayerInfo.class.getName(), cape));
					try
					{
						Field modifier_i = Field.class.getDeclaredField("modifiers");
						modifier_i.setAccessible(true);
						modifier_i.setInt(field_capes, field_capes.getModifiers() & ~Modifier.FINAL);
						field_capes.set(info, blueTex);
					}
					catch (Exception exception)
					{
						exception.printStackTrace();
					}
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}

			}
		}
	}


	private boolean hasRedCape(UUID uniqueID) {
		for (UUID uuid1 : redcapes)
		{
			if (uniqueID.equals(uuid1))
			{
				return true;
			}
		}
		return false;
	}
	private boolean hasBlueCape(UUID uniqueID) {
		for (UUID uuid1 : bluecapes)
		{
			if (uniqueID.equals(uuid1))
			{
				return true;
			}
		}
		return false;
	}   
}
