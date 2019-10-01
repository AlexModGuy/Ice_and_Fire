package com.github.alexthe666.iceandfire.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

public class IceAndFireCoremod extends DummyModContainer {

	public IceAndFireCoremod() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "iceandfirecore";
		meta.name = "Ice And Fire Core";
		meta.version = "1.0.0";
		meta.credits = "";
		meta.authorList = Arrays.asList("Alexthe666");
		meta.description = "Coremod used to fix server side issues with dragons moving too fast, despawning, etc.";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
	
	@Subscribe
	public void modConstruction(FMLConstructionEvent event){
		event.getSide();
	}
	
	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	@Subscribe
	public void init(FMLInitializationEvent event) {
	
	}
	
	
	@Subscribe
	public void postInit(FMLPostInitializationEvent event) {
	
	}
	
}
