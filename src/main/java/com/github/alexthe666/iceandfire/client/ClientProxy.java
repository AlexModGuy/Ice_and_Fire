package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.CommonProxy;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexAddRoom;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexStaff;
import com.github.alexthe666.iceandfire.client.gui.bestiary.GuiBestiary;
import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.particle.*;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.render.tile.IceAndFireTEISR;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.event.ClientEvents;
import com.github.alexthe666.iceandfire.event.PlayerRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.Callable;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    private static final ModelFireDragonArmor FIRE_DRAGON_SCALE_ARMOR_MODEL = new ModelFireDragonArmor(0.5F, false);
    private static final ModelFireDragonArmor FIRE_DRAGON_SCALE_ARMOR_MODEL_LEGS = new ModelFireDragonArmor(0.2F, true);
    private static final ModelIceDragonArmor ICE_DRAGON_SCALE_ARMOR_MODEL = new ModelIceDragonArmor(0.5F, false);
    private static final ModelIceDragonArmor ICE_DRAGON_SCALE_ARMOR_MODEL_LEGS = new ModelIceDragonArmor(0.2F, true);
    private static final ModelLightningDragonArmor LIGHTNING_DRAGON_SCALE_ARMOR_MODEL = new ModelLightningDragonArmor(0.5F, false);
    private static final ModelLightningDragonArmor LIGHTNING_DRAGON_SCALE_ARMOR_MODEL_LEGS = new ModelLightningDragonArmor(0.2F, true);
    private static final ModelDeathWormArmor DEATHWORM_ARMOR_MODEL = new ModelDeathWormArmor(0.5F);
    private static final ModelDeathWormArmor DEATHWORM_ARMOR_MODEL_LEGS = new ModelDeathWormArmor(0.2F);
    private static final ModelTrollArmor TROLL_ARMOR_MODEL = new ModelTrollArmor(0.75F);
    private static final ModelTrollArmor TROLL_ARMOR_MODEL_LEGS = new ModelTrollArmor(0.35F);
    private static final ModelSeaSerpentArmor TIDE_ARMOR_MODEL = new ModelSeaSerpentArmor(0.4F);
    private static final ModelSeaSerpentArmor TIDE_ARMOR_MODEL_LEGS = new ModelSeaSerpentArmor(0.2F);
    private static final ModelDragonsteelFireArmor DRAGONSTEEL_FIRE_ARMOR_MODEL = new ModelDragonsteelFireArmor(0.4F);
    private static final ModelDragonsteelFireArmor DRAGONSTEEL_FIRE_ARMOR_MODEL_LEGS = new ModelDragonsteelFireArmor(0.2F);
    private static final ModelDragonsteelIceArmor DRAGONSTEEL_ICE_ARMOR_MODEL = new ModelDragonsteelIceArmor(0.4F);
    private static final ModelDragonsteelIceArmor DRAGONSTEEL_ICE_ARMOR_MODEL_LEGS = new ModelDragonsteelIceArmor(0.2F);
    private static final ModelDragonsteelLightningArmor DRAGONSTEEL_LIGHTNING_ARMOR_MODEL = new ModelDragonsteelLightningArmor(0.4F);
    private static final ModelDragonsteelLightningArmor DRAGONSTEEL_LIGHTNING_ARMOR_MODEL_LEGS = new ModelDragonsteelLightningArmor(0.2F);
    private static final ModelSilverArmor SILVER_ARMOR_MODEL = new ModelSilverArmor(0.5F);
    private static final ModelSilverArmor SILVER_ARMOR_MODEL_LEGS = new ModelSilverArmor(0.2F);
    private static final ModelCopperArmor COPPER_ARMOR_MODEL = new ModelCopperArmor(0.5F);
    private static final ModelCopperArmor COPPER_ARMOR_MODEL_LEGS = new ModelCopperArmor(0.2F);


    private static MyrmexHive referedClientHive = null;
    private int previousViewType = 0;
    private int thirdPersonViewDragon = 0;
    private Entity referencedMob = null;
    private TileEntity referencedTE = null;

    public static MyrmexHive getReferedClientHive() {
        return referedClientHive;
    }

    private static Callable<ItemStackTileEntityRenderer> getTEISR() {
        return IceAndFireTEISR::new;
    }

    @Override
    public void setReferencedHive(MyrmexHive hive) {
        referedClientHive = hive;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void init() {
        IafKeybindRegistry.init();
        MinecraftForge.EVENT_BUS.register(new PlayerRenderEvents());
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        renderEntities();
    }

    @Override
    public void postInit() {

    }

    @SuppressWarnings("deprecation")
    private void renderEntities() {


    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void spawnDragonParticle(final EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase) {
        ClientWorld world = Minecraft.getInstance().world;
        if (world == null) {
            return;
        }
        net.minecraft.client.particle.Particle particle = null;
        if (name == EnumParticles.DragonFire) {
            particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
        } else if (name == EnumParticles.DragonIce) {
            particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
        }
        if (particle != null) {
            Minecraft.getInstance().particles.addEffect(particle);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void spawnParticle(final EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, float size) {
        ClientWorld world = Minecraft.getInstance().world;
        if (world == null) {
            return;
        }
        net.minecraft.client.particle.Particle particle = null;
        switch (name) {
            case DragonFire:
                particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, size);
                break;
            case DragonIce:
                particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, size);
                break;
            case Dread_Torch:
                particle = new ParticleDreadTorch(world, x, y, z, motX, motY, motZ, size);
                break;
            case Dread_Portal:
                particle = new ParticleDreadPortal(world, x, y, z, motX, motY, motZ, size);
                break;
            case Blood:
                particle = new ParticleBlood(world, x, y, z);
                break;
            case If_Pixie:
                particle = new ParticlePixieDust(world, x, y, z, (float) motX, (float) motY, (float) motZ);
                break;
            case Siren_Appearance:
                particle = new ParticleSirenAppearance(world, x, y, z, (int) motX);
                break;
            case Ghost_Appearance:
                particle = new ParticleGhostAppearance(world, x, y, z, (int) motX);
                break;
            case Siren_Music:
                particle = new ParticleSirenMusic(world, x, y, z, motX, motY, motZ, 1);
                break;
            case Serpent_Bubble:
                particle = new ParticleSerpentBubble(world, x, y, z, motX, motY, motZ, 1);
                break;
            case Hydra:
                particle = new ParticleHydraBreath(world, x, y, z, (float) motX, (float) motY, (float) motZ);
                break;
            default:
                break;
        }
        if (particle != null) {
            Minecraft.getInstance().particles.addEffect(particle);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openBestiaryGui(ItemStack book) {
        Minecraft.getInstance().displayGuiScreen(new GuiBestiary(book));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openMyrmexStaffGui(ItemStack staff) {
        Minecraft.getInstance().displayGuiScreen(new GuiMyrmexStaff(staff));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, Direction facing) {
        Minecraft.getInstance().displayGuiScreen(new GuiMyrmexAddRoom(staff, pos, facing));
    }

    @Override
    public Object getArmorModel(int armorId) {
        switch (armorId) {
            case 0:
                return FIRE_DRAGON_SCALE_ARMOR_MODEL;
            case 1:
                return FIRE_DRAGON_SCALE_ARMOR_MODEL_LEGS;
            case 2:
                return ICE_DRAGON_SCALE_ARMOR_MODEL;
            case 3:
                return ICE_DRAGON_SCALE_ARMOR_MODEL_LEGS;
            case 4:
                return DEATHWORM_ARMOR_MODEL;
            case 5:
                return DEATHWORM_ARMOR_MODEL_LEGS;
            case 6:
                return TROLL_ARMOR_MODEL;
            case 7:
                return TROLL_ARMOR_MODEL_LEGS;
            case 8:
                return TIDE_ARMOR_MODEL;
            case 9:
                return TIDE_ARMOR_MODEL_LEGS;
            case 10:
                return DRAGONSTEEL_FIRE_ARMOR_MODEL;
            case 11:
                return DRAGONSTEEL_FIRE_ARMOR_MODEL_LEGS;
            case 12:
                return DRAGONSTEEL_ICE_ARMOR_MODEL;
            case 13:
                return DRAGONSTEEL_ICE_ARMOR_MODEL_LEGS;
            case 14:
                return SILVER_ARMOR_MODEL;
            case 15:
                return SILVER_ARMOR_MODEL_LEGS;
            case 16:
                return COPPER_ARMOR_MODEL;
            case 17:
                return COPPER_ARMOR_MODEL_LEGS;
            case 18:
                return LIGHTNING_DRAGON_SCALE_ARMOR_MODEL;
            case 19:
                return LIGHTNING_DRAGON_SCALE_ARMOR_MODEL_LEGS;
            case 20:
                return DRAGONSTEEL_LIGHTNING_ARMOR_MODEL;
            case 21:
                return DRAGONSTEEL_LIGHTNING_ARMOR_MODEL_LEGS;
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object getFontRenderer() {
        return Minecraft.getInstance().fontRenderer;
    }

    @Override
    public int getDragon3rdPersonView() {
        return thirdPersonViewDragon;
    }

    @Override
    public void setDragon3rdPersonView(int view) {
        thirdPersonViewDragon = view;
    }

    @Override
    public Object getDreadlandsRender(int i) {
        return null;
    }

    @Override
    public int getPreviousViewType() {
        return previousViewType;
    }

    @Override
    public void setPreviousViewType(int view) {
        previousViewType = view;
    }

    @Override
    public void updateDragonArmorRender(String clear) {
        LayerDragonArmor.clearCache(clear);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldSeeBestiaryContents() {
        return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 340) || InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 344);
    }

    @Override
    public Entity getReferencedMob() {
        return referencedMob;
    }

    @Override
    public void setReferencedMob(Entity dragonBase) {
        referencedMob = dragonBase;
    }

    @Override
    public TileEntity getRefrencedTE() {
        return referencedTE;
    }

    @Override
    public void setRefrencedTE(TileEntity tileEntity) {
        referencedTE = tileEntity;
    }

    @Override
    public Item.Properties setupISTER(Item.Properties group) {
        return group.setISTER(ClientProxy::getTEISR);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PlayerEntity getClientSidePlayer() {
        return Minecraft.getInstance().player;
    }
}
