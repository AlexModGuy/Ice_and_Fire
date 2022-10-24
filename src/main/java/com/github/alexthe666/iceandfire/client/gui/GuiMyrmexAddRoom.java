package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GuiMyrmexAddRoom extends Screen {
    private static final ResourceLocation JUNGLE_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_jungle.png");
    private static final ResourceLocation DESERT_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_desert.png");
    private final boolean jungle;
    private final BlockPos interactPos;
    private final Direction facing;

    public GuiMyrmexAddRoom(ItemStack staff, BlockPos interactPos, Direction facing) {
        super(new TranslatableComponent("myrmex_add_room"));
        this.jungle = staff.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get();
        this.interactPos = interactPos;
        this.facing = facing;
        init();
    }

    public static void onGuiClosed() {
        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageGetMyrmexHive(ClientProxy.getReferedClientHive().toNBT()));
    }

    @Override
    protected void init() {
        super.init();
        this.renderables.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        if (ClientProxy.getReferedClientHive() != null) {
            Player player = Minecraft.getInstance().player;
            this.addWidget(new Button(i + 50, j + 35, 150, 20, new TranslatableComponent("myrmex.message.establishroom_food"), (p_214132_1_) -> {
                ClientProxy.getReferedClientHive().addRoomWithMessage(player, interactPos, WorldGenMyrmexHive.RoomType.FOOD);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);
            }));
            this.addWidget(new Button(i + 50, j + 60, 150, 20, new TranslatableComponent("myrmex.message.establishroom_nursery"), (p_214132_1_) -> {
                ClientProxy.getReferedClientHive().addRoomWithMessage(player, interactPos, WorldGenMyrmexHive.RoomType.NURSERY);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);
            }));
            this.addWidget(new Button(i + 50, j + 85, 150, 20, new TranslatableComponent("myrmex.message.establishroom_enterance_surface"), (p_214132_1_) -> {
                ClientProxy.getReferedClientHive().addEnteranceWithMessage(player, false, interactPos, facing);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);

            }));
            this.addWidget(new Button(i + 50, j + 110, 150, 20, new TranslatableComponent("myrmex.message.establishroom_enterance_bottom"), (p_214132_1_) -> {
                ClientProxy.getReferedClientHive().addEnteranceWithMessage(player, true, interactPos, facing);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);

            }));
            this.addWidget(new Button(i + 50, j + 135, 150, 20, new TranslatableComponent("myrmex.message.establishroom_misc"), (p_214132_1_) -> {
                ClientProxy.getReferedClientHive().addRoomWithMessage(player, interactPos, WorldGenMyrmexHive.RoomType.EMPTY);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);

            }));
        }

    }

    @Override
    public void renderBackground(PoseStack ms) {
        super.renderBackground(ms);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindForSetup(jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.blit(ms, i, j, 0, 0, 248, 166);
    }

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        init();
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        super.render(ms, mouseX, mouseY, partialTicks);
        int color = this.jungle ? 0X35EA15 : 0XFFBF00;
        if (ClientProxy.getReferedClientHive() != null) {
            if (!ClientProxy.getReferedClientHive().colonyName.isEmpty()) {
                String title = I18n.get("myrmex.message.colony_named", ClientProxy.getReferedClientHive().colonyName);
                this.getMinecraft().font.draw(ms, title, i + 40 - title.length() / 2, j - 3, color);
            } else {
                this.getMinecraft().font.draw(ms, I18n.get("myrmex.message.colony"), i + 80, j - 3, color);
            }
            this.getMinecraft().font.draw(ms, I18n.get("myrmex.message.create_new_room", interactPos.getX(), interactPos.getY(), interactPos.getZ()), i + 30, j + 6, color);

        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
