package fossilsarcheology.api;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

import java.util.Map;

public class FoodMappings {
	private static final FoodMappings INSTANCE = new FoodMappings();
	private Map<Integer, Integer> carnivoreItemDiet;
	private Map<Integer, Integer> herbivoreItemDiet;
	private Map<Integer, Integer> omnivoreItemDiet;
	private Map<Integer, Integer> piscivoreItemDiet;
	private Map<Integer, Integer> carnivore_eggItemDiet;
	private Map<Integer, Integer> insectivoreItemDiet;
	private Map<Integer, Integer> pisccarnivoreItemDiet;
	private Map<Integer, Integer> carnivoreBlockDiet;
	private Map<Integer, Integer> herbivoreBlockDiet;
	private Map<Integer, Integer> omnivoreBlockDiet;
	private Map<Integer, Integer> piscivoreBlockDiet;
	private Map<Integer, Integer> carnivore_eggBlockDiet;
	private Map<Integer, Integer> insectivoreBlockDiet;
	private Map<Integer, Integer> pisccarnivoreBlockDiet;
	private Map<Class<? extends Entity>, Integer> carnivoreEntityDiet;
	private Map<Class<? extends Entity>, Integer> herbivoreEntityDiet;
	private Map<Class<? extends Entity>, Integer> omnivoreEntityDiet;
	private Map<Class<? extends Entity>, Integer> piscivoreEntityDiet;
	private Map<Class<? extends Entity>, Integer> carnivore_eggEntityDiet;
	private Map<Class<? extends Entity>, Integer> insectivoreEntityDiet;
	private Map<Class<? extends Entity>, Integer> pisccarnivoreEntityDiet;

	/**
	 * Register all your food items to this.
	 */
	public static FoodMappings instance() {
		return INSTANCE;
	}

	/**
	 * Add an item to a specific diet.
	 * @param item The item to be added as Food.
	 * @param food The amount of food points for the item.
	 * @param diet The specific diet to add the item to.
	 */
	public void addToItemMappings(Item item, int food, EnumDiet diet) {
		if (item != null) {
			switch (diet) {
			case CARNIVORE:
				if (carnivoreItemDiet == null) {
					carnivoreItemDiet = Maps.newHashMap();
				}
				if(!carnivoreItemDiet.containsKey(Item.getIdFromItem(item))){
					carnivoreItemDiet.put(Item.getIdFromItem(item), food);
				}
				break;
			case HERBIVORE:
				if (herbivoreItemDiet == null) {
					herbivoreItemDiet = Maps.newHashMap();
				}
				if(!herbivoreItemDiet.containsKey(Item.getIdFromItem(item))){
					herbivoreItemDiet.put(Item.getIdFromItem(item), food);
				}
				break;
			case OMNIVORE:
				if (omnivoreItemDiet == null) {
					omnivoreItemDiet = Maps.newHashMap();
				}
				if(!omnivoreItemDiet.containsKey(Item.getIdFromItem(item))){
					omnivoreItemDiet.put(Item.getIdFromItem(item), food);
				}
				break;
			case PISCIVORE:
				if (piscivoreItemDiet == null) {
					piscivoreItemDiet = Maps.newHashMap();
				}
				if(!piscivoreItemDiet.containsKey(Item.getIdFromItem(item))){
					piscivoreItemDiet.put(Item.getIdFromItem(item), food);
				}
				break;
			case CARNIVORE_EGG:
				if (carnivore_eggItemDiet == null) {
					carnivore_eggItemDiet = Maps.newHashMap();
				}
				if(!carnivore_eggItemDiet.containsKey(Item.getIdFromItem(item))){
					carnivore_eggItemDiet.put(Item.getIdFromItem(item), food);
				}
				break;
			case INSECTIVORE:
				if (insectivoreItemDiet == null) {
					insectivoreItemDiet = Maps.newHashMap();
				}
				if(!insectivoreItemDiet.containsKey(Item.getIdFromItem(item))){
					insectivoreItemDiet.put(Item.getIdFromItem(item), food);
				}
				break;
			case PISCCARNIVORE:
				if (pisccarnivoreItemDiet == null) {
					pisccarnivoreItemDiet = Maps.newHashMap();
				}
				if(!pisccarnivoreItemDiet.containsKey(Item.getIdFromItem(item))){
					pisccarnivoreItemDiet.put(Item.getIdFromItem(item), food);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Get the amount of food points from an item in the mapping.
	 * @param item The item to find.
	 * @param diet The specific diet to find the item from.
	 */
	public int getItemFoodAmount(Item item, EnumDiet diet) {
		switch (diet) {
		case CARNIVORE:
			if (carnivoreItemDiet != null && carnivoreItemDiet.containsKey(Item.getIdFromItem(item))) {
				return carnivoreItemDiet.get(Item.getIdFromItem(item));
			}
			break;
		case HERBIVORE:
			if (herbivoreItemDiet != null && herbivoreItemDiet.containsKey(Item.getIdFromItem(item))) {
				return herbivoreItemDiet.get(Item.getIdFromItem(item));
			}
			break;
		case OMNIVORE:
			if (omnivoreItemDiet != null && omnivoreItemDiet.containsKey(Item.getIdFromItem(item))) {
				return omnivoreItemDiet.get(Item.getIdFromItem(item));
			}
			break;
		case PISCIVORE:
			if (piscivoreItemDiet != null && piscivoreItemDiet.containsKey(Item.getIdFromItem(item))) {
				return piscivoreItemDiet.get(Item.getIdFromItem(item));
			}
			break;
		case CARNIVORE_EGG:
			if (carnivore_eggItemDiet != null && carnivore_eggItemDiet.containsKey(Item.getIdFromItem(item))) {
				return carnivore_eggItemDiet.get(Item.getIdFromItem(item));
			}
			break;
		case INSECTIVORE:
			if (insectivoreItemDiet != null && insectivoreItemDiet.containsKey(Item.getIdFromItem(item))) {
				return insectivoreItemDiet.get(Item.getIdFromItem(item));
			}
			break;
		case PISCCARNIVORE:
			if (pisccarnivoreItemDiet != null && pisccarnivoreItemDiet.containsKey(Item.getIdFromItem(item))) {
				return pisccarnivoreItemDiet.get(Item.getIdFromItem(item));
			}
			break;
		default:
			return 0;
		}
		return 0;
	}

	/**
	 * Add an block to a specific diet. Usually only for herbivores and omnivores.
	 * @param block The block to be added as Food.
	 * @param food The amount of food points for the block.
	 * @param diet The specific diet to add the block to.
	 * @param registerItem Register the block's item as food or not. Usually true, but false for technical blocks like wheat.
	 */
	public void addToBlockMappings(Block block, int food, EnumDiet diet, boolean registerItem) {
		switch (diet) {
		case CARNIVORE:
			if (carnivoreBlockDiet == null) {
				carnivoreBlockDiet = Maps.newHashMap();
			}
			if(!carnivoreBlockDiet.containsKey(Block.getIdFromBlock(block))){
				carnivoreBlockDiet.put(Block.getIdFromBlock(block), food);
			}
			break;
		case HERBIVORE:
			if (herbivoreBlockDiet == null) {
				herbivoreBlockDiet = Maps.newHashMap();
			}
			if(!herbivoreBlockDiet.containsKey(Block.getIdFromBlock(block))){
				herbivoreBlockDiet.put(Block.getIdFromBlock(block), food);
			}
			break;
		case OMNIVORE:
			if (omnivoreBlockDiet == null) {
				omnivoreBlockDiet = Maps.newHashMap();
			}
			if(!omnivoreBlockDiet.containsKey(Block.getIdFromBlock(block))){
				omnivoreBlockDiet.put(Block.getIdFromBlock(block), food);
			}
			break;
		case PISCIVORE:
			if (piscivoreBlockDiet == null) {
				piscivoreBlockDiet = Maps.newHashMap();
			}
			if(!piscivoreBlockDiet.containsKey(Block.getIdFromBlock(block))){
				piscivoreBlockDiet.put(Block.getIdFromBlock(block), food);
			}
			break;
		case CARNIVORE_EGG:
			if (carnivore_eggBlockDiet == null) {
				carnivore_eggBlockDiet = Maps.newHashMap();
			}
			if(!carnivore_eggBlockDiet.containsKey(Block.getIdFromBlock(block))){
				carnivore_eggBlockDiet.put(Block.getIdFromBlock(block), food);
			}
			break;
		case INSECTIVORE:
			if (insectivoreBlockDiet == null) {
				insectivoreBlockDiet = Maps.newHashMap();
			}
			if(!insectivoreBlockDiet.containsKey(Block.getIdFromBlock(block))){
				insectivoreBlockDiet.put(Block.getIdFromBlock(block), food);
			}
			break;
		case PISCCARNIVORE:
			if (pisccarnivoreBlockDiet == null) {
				pisccarnivoreBlockDiet = Maps.newHashMap();
			}
			if(!pisccarnivoreBlockDiet.containsKey(Block.getIdFromBlock(block))){
				pisccarnivoreBlockDiet.put(Block.getIdFromBlock(block), food);
			}	
			break;
		default:
			break;
		}
		if (registerItem) {
			addToItemMappings(Item.getItemFromBlock(block), food, diet);
		}
	}

	/**
	 * Get the amount of food points from a block in the mapping.
	 * @param block The block to find.
	 * @param diet The specific diet to find the block from.
	 */
	public int getBlockFoodAmount(Block block, EnumDiet diet) {
		switch (diet) {
		case CARNIVORE:
			if (carnivoreBlockDiet != null && carnivoreBlockDiet.containsKey(Block.getIdFromBlock(block))) {
				return carnivoreBlockDiet.get(Block.getIdFromBlock(block));
			}
			break;
		case HERBIVORE:
			if (herbivoreBlockDiet != null && herbivoreBlockDiet.containsKey(Block.getIdFromBlock(block))) {
				return herbivoreBlockDiet.get(Block.getIdFromBlock(block));
			}
			break;
		case OMNIVORE:
			if (omnivoreBlockDiet != null && omnivoreBlockDiet.containsKey(Block.getIdFromBlock(block))) {
				return omnivoreBlockDiet.get(Block.getIdFromBlock(block));
			}
			break;
		case PISCIVORE:
			if (piscivoreBlockDiet != null && piscivoreBlockDiet.containsKey(Block.getIdFromBlock(block))) {
				return piscivoreBlockDiet.get(Block.getIdFromBlock(block));
			}
			break;
		case CARNIVORE_EGG:
			if (carnivore_eggBlockDiet != null && carnivore_eggBlockDiet.containsKey(Block.getIdFromBlock(block))) {
				return carnivore_eggBlockDiet.get(Block.getIdFromBlock(block));
			}
			break;
		case INSECTIVORE:
			if (insectivoreBlockDiet != null && insectivoreBlockDiet.containsKey(Block.getIdFromBlock(block))) {
				return insectivoreBlockDiet.get(Block.getIdFromBlock(block));
			}
			break;
		case PISCCARNIVORE:
			if (pisccarnivoreBlockDiet != null && pisccarnivoreBlockDiet.containsKey(Block.getIdFromBlock(block))) {
				return pisccarnivoreBlockDiet.get(Block.getIdFromBlock(block));
			}
			break;
		default:
			return 0;
		}
		return 0;
	}


	public void addToEntityMappings(Class<? extends Entity> entity, int food, EnumDiet diet) {
		switch (diet) {
		case CARNIVORE:
			if (carnivoreEntityDiet == null) {
				carnivoreEntityDiet = Maps.newHashMap();
			}
			if(!carnivoreEntityDiet.containsKey(entity)){
				carnivoreEntityDiet.put(entity, food);
			}	
			break;
		case HERBIVORE:
			if (herbivoreEntityDiet == null) {
				herbivoreEntityDiet = Maps.newHashMap();
			}
			if(!herbivoreEntityDiet.containsKey(entity)){
				herbivoreEntityDiet.put(entity, food);
			}	
			break;
		case OMNIVORE:
			if (omnivoreEntityDiet == null) {
				omnivoreEntityDiet = Maps.newHashMap();
			}
			if(!omnivoreEntityDiet.containsKey(entity)){
				omnivoreEntityDiet.put(entity, food);
			}	
			break;
		case PISCIVORE:
			if (piscivoreEntityDiet == null) {
				piscivoreEntityDiet = Maps.newHashMap();
			}
			if(!piscivoreEntityDiet.containsKey(entity)){
				piscivoreEntityDiet.put(entity, food);
			}	
			break;
		case CARNIVORE_EGG:
			if (carnivore_eggEntityDiet == null) {
				carnivore_eggEntityDiet = Maps.newHashMap();
			}
			if(!carnivore_eggEntityDiet.containsKey(entity)){
				carnivore_eggEntityDiet.put(entity, food);
			}
			break;
		case INSECTIVORE:
			if (insectivoreEntityDiet == null) {
				insectivoreEntityDiet = Maps.newHashMap();
			}
			if(!insectivoreEntityDiet.containsKey(entity)){
				insectivoreEntityDiet.put(entity, food);
			}
			break;
		case PISCCARNIVORE:
			if (pisccarnivoreEntityDiet == null) {
				pisccarnivoreEntityDiet = Maps.newHashMap();
			}
			if(!pisccarnivoreEntityDiet.containsKey(entity)){
				pisccarnivoreEntityDiet.put(entity, food);
			}	
			break;
		default:
			break;
		}
	}

	/**
	 * Add an entity to a specific diet. Usually only for carnivores and omnivores.
	 * @param entity The entity class to be added as Food.
	 * @param food The amount of food points for the entity.
	 * @param diet The specific diet to add the entity to.
	 */
	public int getEntityFoodAmount(Class<? extends Entity> entity, EnumDiet diet) {
		switch (diet) {
		case CARNIVORE:
			if (carnivoreEntityDiet != null && carnivoreEntityDiet.containsKey(entity)) {
				return carnivoreEntityDiet.get(entity);
			}
			break;
		case HERBIVORE:
			if (herbivoreEntityDiet != null && herbivoreEntityDiet.containsKey(entity)) {
				return herbivoreEntityDiet.get(entity);
			}
			break;
		case OMNIVORE:
			if (omnivoreEntityDiet != null && omnivoreEntityDiet.containsKey(entity)) {
				return omnivoreEntityDiet.get(entity);
			}
			break;
		case PISCIVORE:
			if (piscivoreEntityDiet != null && piscivoreEntityDiet.containsKey(entity)) {
				return piscivoreEntityDiet.get(entity);
			}
			break;
		case CARNIVORE_EGG:
			if (carnivore_eggEntityDiet != null && carnivore_eggEntityDiet.containsKey(entity)) {
				return carnivore_eggEntityDiet.get(entity);
			}
			break;
		case INSECTIVORE:
			if (insectivoreEntityDiet != null && insectivoreEntityDiet.containsKey(entity)) {
				return insectivoreEntityDiet.get(entity);
			}
			break;
		case PISCCARNIVORE:
			if (pisccarnivoreEntityDiet != null && pisccarnivoreEntityDiet.containsKey(entity)) {
				return pisccarnivoreEntityDiet.get(entity);
			}
			break;
		default:
			return 0;
		}
		return 0;
	}

	/**
	 * Gives out a list of all of the food items for a diet. Used in dinopedia.
	 * @param diet The specific diet to show.
	 */
	public Map<Integer, Integer> getFoodRenderList(EnumDiet diet) {
		switch (diet) {
		case CARNIVORE:
			if (carnivoreItemDiet == null) {
				carnivoreItemDiet = Maps.newHashMap();
			}
			return carnivoreItemDiet;
		case HERBIVORE:
			if (herbivoreItemDiet == null) {
				herbivoreItemDiet = Maps.newHashMap();
			}
			return herbivoreItemDiet;
		case OMNIVORE:
			if (omnivoreItemDiet == null) {
				omnivoreItemDiet = Maps.newHashMap();
			}
			return omnivoreItemDiet;
		case PISCIVORE:
			if (piscivoreItemDiet == null) {
				piscivoreItemDiet = Maps.newHashMap();
			}
			return piscivoreItemDiet;
		case CARNIVORE_EGG:
			if (carnivore_eggItemDiet == null) {
				carnivore_eggItemDiet = Maps.newHashMap();
			}
			return carnivore_eggItemDiet;
		case INSECTIVORE:
			if (insectivoreItemDiet == null) {
				insectivoreItemDiet = Maps.newHashMap();
			}
			return insectivoreItemDiet;
		case PISCCARNIVORE:
			if (pisccarnivoreItemDiet == null) {
				pisccarnivoreItemDiet = Maps.newHashMap();
			}
			return pisccarnivoreItemDiet;
		default:
			return carnivoreItemDiet;
		}
	}

	/**
	 * Removes an item from the mapping.
	 * @param diet The specific diet to show.
	 */
	public void removeItemMapping(Item item, EnumDiet diet) {
		this.getFoodRenderList(diet).remove(Item.getIdFromItem(item));
	}

	/**
	 *  Adds a item, block, or entity class to all the carnivore mappings.
	 * @param object The item, block, or entity class being registered.
	 * @param food The amount of food points for the object.
	 */
	public void addMeat(Object object, int food){
		if(object instanceof Block){
			this.addToBlockMappings((Block)object, food, EnumDiet.CARNIVORE, true);
			this.addToBlockMappings((Block)object, food, EnumDiet.CARNIVORE_EGG, true);
			this.addToBlockMappings((Block)object, food, EnumDiet.OMNIVORE, true);
			this.addToBlockMappings((Block)object, food, EnumDiet.PISCCARNIVORE, true);
		}
		else if(object instanceof Item){
			this.addToItemMappings((Item)object, food, EnumDiet.CARNIVORE);
			this.addToItemMappings((Item)object, food, EnumDiet.CARNIVORE_EGG);
			this.addToItemMappings((Item)object, food, EnumDiet.OMNIVORE);
			this.addToItemMappings((Item)object, food, EnumDiet.PISCCARNIVORE);
		}
		else if(object instanceof Class){
			this.addToEntityMappings((Class)object, food, EnumDiet.CARNIVORE);
			this.addToEntityMappings((Class)object, food, EnumDiet.CARNIVORE_EGG);
			this.addToEntityMappings((Class)object, food, EnumDiet.OMNIVORE);
			this.addToEntityMappings((Class)object, food, EnumDiet.PISCCARNIVORE);
		}
	}

	/**
	 *  Adds a item, block, or entity class to all the herbivore mappings.
	 * @param object The item, block, or entity class being registered.
	 * @param food The amount of food points for the object.
	 */
	public void addPlant(Object object, int food){
		if(object instanceof Block){
			this.addToBlockMappings((Block)object, food, EnumDiet.HERBIVORE, true);
			this.addToBlockMappings((Block)object, food, EnumDiet.OMNIVORE, true);
		}
		else if(object instanceof Item){
			this.addToItemMappings((Item)object, food, EnumDiet.HERBIVORE);
			this.addToItemMappings((Item)object, food, EnumDiet.OMNIVORE);
		}
		else if(object instanceof Class){
			this.addToEntityMappings((Class)object, food, EnumDiet.HERBIVORE);
			this.addToEntityMappings((Class)object, food, EnumDiet.OMNIVORE);
		}
	}

	/**
	 *  Adds a item, block, or entity class to all the piscivore mappings.
	 * @param object The item, block, or entity class being registered.
	 * @param food The amount of food points for the object.
	 */
	public void addFish(Object object, int food){
		if(object instanceof Block){
			this.addToBlockMappings((Block)object, food, EnumDiet.PISCCARNIVORE, true);
			this.addToBlockMappings((Block)object, food, EnumDiet.PISCIVORE, true);
		}
		else if(object instanceof Item){
			this.addToItemMappings((Item)object, food, EnumDiet.PISCCARNIVORE);
			this.addToItemMappings((Item)object, food, EnumDiet.PISCIVORE);
		}
		else if(object instanceof Class){
			this.addToEntityMappings((Class)object, food, EnumDiet.PISCCARNIVORE);
			this.addToEntityMappings((Class)object, food, EnumDiet.PISCIVORE);
		}
	}

	/**
	 *  Adds a item, block, or entity class to all the egg eating mappings.
	 * @param object The item, block, or entity class being registered.
	 * @param food The amount of food points for the object.
	 */
	public void addEgg(Object object, int food){
		if(object instanceof Block){
			this.addToBlockMappings((Block)object, food, EnumDiet.CARNIVORE_EGG, true);
			this.addToBlockMappings((Block)object, food, EnumDiet.OMNIVORE, true);
		}
		else if(object instanceof Item){
			this.addToItemMappings((Item)object, food, EnumDiet.CARNIVORE_EGG);
			this.addToItemMappings((Item)object, food, EnumDiet.OMNIVORE);
		}
		else if(object instanceof Class){
			this.addToEntityMappings((Class)object, food, EnumDiet.CARNIVORE_EGG);
			this.addToEntityMappings((Class)object, food, EnumDiet.OMNIVORE);
		}
	}
}
