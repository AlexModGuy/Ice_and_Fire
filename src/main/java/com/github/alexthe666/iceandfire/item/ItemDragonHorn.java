package com.github.alexthe666.iceandfire.item;

					  

								 

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemDragonHorn extends Item {

  public ItemDragonHorn() {
    super((new Item.Properties()).func_200916_a(IceAndFire.TAB_ITEMS).func_200917_a(1));
    setRegistryName("iceandfire", "dragon_horn");
  }
  
  public static int getDragonType(ItemStack stack) {
    if (stack.func_77978_p() != null) {
      String id = stack.func_77978_p().func_74779_i("DragonHornEntityID");
      if (EntityType.func_220327_a(id).isPresent()) {
        EntityType entityType = EntityType.func_220327_a(id).get();
        if (entityType == IafEntityRegistry.FIRE_DRAGON)
          return 1; 
				 
        if (entityType == IafEntityRegistry.ICE_DRAGON)
          return 2; 
				 
        if (entityType == IafEntityRegistry.LIGHTNING_DRAGON)
          return 3; 
      } 
    } 
		 
    return 0;
  }
  
			 
  public void func_77622_d(ItemStack itemStack, World world, PlayerEntity player) {
    itemStack.func_77982_d(new CompoundNBT());
  }
  
			 
  public ActionResultType func_111207_a(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
    trueStack = playerIn.func_184586_b(hand);
    if (!playerIn.field_70170_p.field_72995_K && hand == Hand.MAIN_HAND && 
      target instanceof EntityDragonBase && ((EntityDragonBase)target).func_152114_e((LivingEntity)playerIn) && (trueStack.func_77978_p() == null || (trueStack.func_77978_p() != null && trueStack.func_77978_p().func_74775_l("EntityTag").isEmpty()))) {
      Object entityTag = new CompoundNBT();
      target.func_213281_b((CompoundNBT)entityTag);
      Object newTag = new CompoundNBT();
      newTag.func_74778_a("DragonHornEntityID", Registry.field_212629_r.func_177774_c(target.func_200600_R()).toString());
      newTag.func_218657_a("EntityTag", (INBT)entityTag);
      newTag.func_186854_a("EntityUUID", target.func_110124_au());
      trueStack.func_77982_d((CompoundNBT)newTag);
      playerIn.func_184609_a(hand);
      playerIn.field_70170_p.func_184133_a(playerIn, playerIn.func_233580_cy_(), SoundEvents.field_187941_ho, SoundCategory.NEUTRAL, 3.0F, 0.75F);
      target.func_70106_y();
      return ActionResultType.SUCCESS;
    } 
		 
    return ActionResultType.FAIL;
  }
  
			 
  public ActionResultType func_195939_a(ItemUseContext context) {
    if (context.func_196000_l() != Direction.UP)
      return ActionResultType.FAIL; 
				
    ItemStack stack = context.func_195999_j().func_184586_b(context.func_221531_n());
    if (stack.func_77978_p() != null && !stack.func_77978_p().func_74779_i("DragonHornEntityID").isEmpty()) {
      World world = context.func_195991_k();
      String id = stack.func_77978_p().func_74779_i("DragonHornEntityID");
      EntityType type = EntityType.func_220327_a(id).orElse(null);
      if (type != null) {
        Entity entity = type.func_200721_a(world);
        if (entity instanceof EntityDragonBase) {
          EntityDragonBase dragon = (EntityDragonBase)entity;
          dragon.func_70037_a(stack.func_77978_p().func_74775_l("EntityTag"));
        } 
        if (stack.func_77978_p().func_186855_b("EntityUUID"))
          entity.func_184221_a(stack.func_77978_p().func_186857_a("EntityUUID")); 
					 
        entity.func_70012_b(context.func_195995_a().func_177958_n() + 0.5D, (context.func_195995_a().func_177956_o() + 1), context.func_195995_a().func_177952_p() + 0.5D, (context.func_195999_j()).field_70177_z, 0.0F);
        if (world.func_217376_c(entity))
          stack.func_77982_d(new CompoundNBT()); 
      } 
    } 
			 
		 
    return ActionResultType.SUCCESS;
  }
  
			 
  public void func_77624_a(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.func_77978_p() != null) {
      CompoundNBT entityTag = stack.func_77978_p().func_74775_l("EntityTag");
      if (!entityTag.isEmpty()) {
        String id = stack.func_77978_p().func_74779_i("DragonHornEntityID");
        if (EntityType.func_220327_a(id).isPresent()) {
          EntityType type = EntityType.func_220327_a(id).get();
          tooltip.add((new TranslationTextComponent(type.func_210760_d())).func_240699_a_(getTextColorForEntityType(type)));
          String name = (new TranslationTextComponent("dragon.unnamed")).getString();
          if (!entityTag.func_74779_i("CustomName").isEmpty()) {
            IFormattableTextComponent component = ITextComponent.Serializer.func_240643_a_(entityTag.func_74779_i("CustomName"));
            if (component != null)
              name = component.getString(); 
          } 
					 
          tooltip.add((new StringTextComponent(name)).func_240699_a_(TextFormatting.GRAY));
          String gender = (new TranslationTextComponent("dragon.gender")).getString() + " " + (new TranslationTextComponent(entityTag.func_74767_n("Gender") ? "dragon.gender.male" : "dragon.gender.female")).getString();
          tooltip.add((new StringTextComponent(gender)).func_240699_a_(TextFormatting.GRAY));
          int stagenumber = entityTag.func_74762_e("AgeTicks") / 24000;
          int stage1 = 0;
          if (stagenumber >= 100) {
            stage1 = 5;
          } else if (stagenumber >= 75) {
            stage1 = 4;
          } else if (stagenumber >= 50) {
            stage1 = 3;
          } else if (stagenumber >= 25) {
            stage1 = 2;
          } else {
            stage1 = 1;
          } 
          String stage = (new TranslationTextComponent("dragon.stage")).getString() + " " + stage1 + " " + (new TranslationTextComponent("dragon.days.front")).getString() + stagenumber + " " + (new TranslationTextComponent("dragon.days.back")).getString();
          tooltip.add((new StringTextComponent(stage)).func_240699_a_(TextFormatting.GRAY));
        } 
      } 

    } 
  }
  
  private TextFormatting getTextColorForEntityType(EntityType type) {
    if (type == IafEntityRegistry.FIRE_DRAGON)
      return TextFormatting.DARK_RED; 
		 
    if (type == IafEntityRegistry.ICE_DRAGON)
      return TextFormatting.BLUE; 
		 
    if (type == IafEntityRegistry.LIGHTNING_DRAGON)
      return TextFormatting.DARK_PURPLE; 
		 
    return TextFormatting.GRAY;
  }
}
