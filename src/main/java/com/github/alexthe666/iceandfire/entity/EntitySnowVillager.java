package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModVillagers;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

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

    public void setProfession(net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof) {
        if(ModVillagers.INSTANCE.professions.containsValue(prof)){
            super.setProfession(prof);
        }else{
            ModVillagers.INSTANCE.setRandomProfession(this, this.worldObj.rand);
        }
    }

    private net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof;

    public net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession getProfessionForge()
    {
        if (this.prof == null) {
            String p = this.getEntityData().getString("ProfessionName");
            net.minecraft.util.ResourceLocation res = new net.minecraft.util.ResourceLocation(p == null ? "iceandfire:fisherman" : p);
            this.prof = ModVillagers.INSTANCE.professions.getValue(res);
            if (this.prof == null)
                return ModVillagers.INSTANCE.professions.getValue(new net.minecraft.util.ResourceLocation("iceandfire:fisherman"));
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
                    ModVillagers.INSTANCE.professions.getValue(new net.minecraft.util.ResourceLocation(compound.getString("ProfessionName")));
            if (p == null)
                p = ModVillagers.INSTANCE.professions.getValue(new net.minecraft.util.ResourceLocation("minecraft:farmer"));
            this.setProfession(p);
        }

    }

}
