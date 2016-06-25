package com.github.alexthe666.iceandfire;

import java.util.Random;

import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.github.alexthe666.iceandfire.client.GuiHandler;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModEntities;
import com.github.alexthe666.iceandfire.core.ModFoods;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.core.ModRecipes;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.github.alexthe666.iceandfire.event.StructureGenerator;
import com.github.alexthe666.iceandfire.message.MessageDaytime;
import com.github.alexthe666.iceandfire.misc.CreativeTab;

@Mod(modid = IceAndFire.MODID, version = IceAndFire.VERSION)
public class IceAndFire {

	public static final String MODID = "iceandfire";
	public static final String VERSION = "0.1.4";
	@Instance(value = MODID)
	public static IceAndFire instance;
	@NetworkWrapper({ MessageDaytime.class })
	public static SimpleNetworkWrapper NETWORK_WRAPPER;
	@SidedProxy(clientSide = "com.github.alexthe666.iceandfire.ClientProxy", serverSide = "com.github.alexthe666.iceandfire.CommonProxy")
	public static CommonProxy proxy;
	public static CreativeTabs tab;
	public static DamageSource dragon;
	public static DamageSource dragonFire;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventLiving());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		tab = new CreativeTab(MODID);
		dragon = new DamageSource("dragon") {
			@Override
			public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon";
				String s1 = s + ".player_" + new Random().nextInt(2);
				return I18n.canTranslate(s1) ? new TextComponentString(entityLivingBaseIn.getDisplayName() + I18n.translateToLocal(s1)) : new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName() });
			}
		};
		dragonFire = new DamageSource("dragon_fire") {
			@Override
			public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon_fire";
				String s1 = s + ".player_" + new Random().nextInt(2);
				return I18n.canTranslate(s1) ? new TextComponentString(entityLivingBaseIn.getDisplayName() + I18n.translateToLocal(s1)) : new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName() });
			}
		};
		ModBlocks.init();
		ModItems.init();
		ModRecipes.init();
		ModEntities.init();
		ModKeys.init();
		ModFoods.init();
		ModSounds.init();
		proxy.render();
		GameRegistry.registerWorldGenerator(new StructureGenerator(), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

	}
}
