package com.github.alexthe666.iceandfire;

import java.util.ArrayList;
import java.util.List;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.animation.Animation;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.ilexiconn.llibrary.common.animation.MessageLLibraryAnimation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.github.alexthe666.iceandfire.client.GuiHandler;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModEntities;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.event.EventKeys;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.github.alexthe666.iceandfire.event.StructureGenerator;
import com.github.alexthe666.iceandfire.message.MessageCorrectAnimation;
import com.github.alexthe666.iceandfire.message.MessageDragonMotion;
import com.github.alexthe666.iceandfire.message.MessageModKeys;
import com.github.alexthe666.iceandfire.misc.CreativeTab;

@Mod(modid = IceAndFire.MODID, version = IceAndFire.VERSION)
public class IceAndFire
{

	public static final String MODID = "iceandfire";
	public static final String VERSION = "0.1.4";

	public static final List treasure_dragondungeon = new ArrayList<WeightedRandomChestContent>();
	@Instance(value = MODID)
	public static IceAndFire instance;
	public static SimpleNetworkWrapper channel;
	@SidedProxy(clientSide = "com.github.alexthe666.iceandfire.ClientProxy", serverSide = "com.github.alexthe666.iceandfire.CommonProxy")
	public static CommonProxy proxy;
	public static CreativeTabs tab;

    public static DamageSource dragon = (new DamageSource("dragon")).setFireDamage();
    public static DamageSource dragonFire = (new DamageSource("dragonFire")).setFireDamage();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		channel.registerMessage(MessageModKeys.class, MessageModKeys.class, 0, Side.SERVER);
		channel.registerMessage(MessageDragonMotion.class, MessageDragonMotion.class, 1, Side.CLIENT);
		channel.registerMessage(MessageCorrectAnimation.class, MessageCorrectAnimation.class, 2, Side.CLIENT);
		 MinecraftForge.EVENT_BUS.register(new EventLiving());
	}
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		tab = new CreativeTab(MODID);
		ModBlocks.init();
		ModItems.init();
		ModEntities.init();
		ModKeys.init();
		FMLCommonHandler.instance().bus().register(new EventKeys());
		initilizeTreasure();
		proxy.render();
		GameRegistry.registerWorldGenerator(new StructureGenerator(), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

	}
	public void initilizeTreasure(){
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 1, 3, 75));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 2, 15));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.emerald), 1, 2, 15));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.quartz), 1, 4, 35));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.clock), 1, 1, 15));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.experience_bottle), 1, 2, 15));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 10));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.minecart), 1, 1, 5));
		treasure_dragondungeon.add(new WeightedRandomChestContent(new ItemStack(Items.glowstone_dust), 1, 5, 25));

	}
}
