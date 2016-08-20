package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModVillagers;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;

public class EntitySnowVillager extends EntityVillager {

    private String professionName;

    public EntitySnowVillager(World worldIn) {
        super(worldIn);
    }

    public EntitySnowVillager(World worldIn, int profession) {
        super(worldIn, profession);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        ModVillagers.INSTANCE.setRandomProfession(this, this.worldObj.rand);
        return livingdata;
    }

    public void setProfession(int professionId) {
        if(professionId > 2){
            professionId = 2;
        }
        super.setProfession(professionId);
    }

    public void onDeath(DamageSource cause) {
        if(cause.getEntity() != null && cause.getEntity() instanceof EntityZombie && (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD)){
            return;
        }else{
            super.onDeath(cause);
        }
    }

    public void setProfession(net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof) {
        if(ModVillagers.INSTANCE.professions.containsValue(prof)){
            try {
                this.dataManager.set((DataParameter<String>) ReflectionHelper.findField(EntityVillager.class, new String[]{"PROFESSION_STR", "PROFESSION_STR"}).get(this), prof.getRegistryName().toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            this.prof = prof;
        }else{
            ModVillagers.INSTANCE.setRandomProfession(this, this.worldObj.rand);
        }
    }

    private net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof;

    public net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession getProfessionForge()
    {
        if (this.prof == null) {
            String p = this.getEntityData().getString("ProfessionName");
            this.prof = ModVillagers.INSTANCE.professions.get(intFromProfesion(p));
        }
        return this.prof;
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setProfession(compound.getInteger("Profession"));
        if (compound.hasKey("ProfessionName"))
        {
            net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession p =
                    ModVillagers.INSTANCE.professions.get(intFromProfesion(compound.getString("ProfessionName")));
            if (p == null)
                p = ModVillagers.INSTANCE.professions.get(0);
            this.setProfession(p);
        }

    }

    private int intFromProfesion(String prof){
        if(prof.contains("fisherman")){
            return 0;
        }
        if(prof.contains("craftsman")){
            return 1;
        }
        if(prof.contains("shaman")){
            return 2;
        }
        return 0;
    }

}
