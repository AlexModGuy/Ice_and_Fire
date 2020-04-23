package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.gui.bestiary.ChangePageButton;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class GuiMyrmexStaff extends GuiScreen {
    private static final ResourceLocation JUNGLE_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_jungle.png");
    private static final ResourceLocation DESERT_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_desert.png");
    private static final WorldGenMyrmexHive.RoomType[] ROOMS = {WorldGenMyrmexHive.RoomType.FOOD, WorldGenMyrmexHive.RoomType.NURSERY, WorldGenMyrmexHive.RoomType.EMPTY};
    private static final int ROOMS_PER_PAGE = 5;
    private final List<Room> allRoomPos = Lists.newArrayList();
    private final List<MyrmexDeleteButton> allRoomButtonPos = Lists.newArrayList();
    public ChangePageButton previousPage;
    public ChangePageButton nextPage;
    int ticksSinceDeleted = 0;
    int currentPage = 0;
    private ItemStack staff;
    private boolean jungle;
    private int hiveCount;

    public GuiMyrmexStaff(ItemStack staff) {
        this.staff = staff;
        this.jungle = staff.getItem() == IafItemRegistry.myrmex_jungle_staff;
        initGui();
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.allRoomButtonPos.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        int x_translate = 193;
        int y_translate = 37;
        if (ClientProxy.getReferedClientHive() == null) {
            return;
        }
        populateRoomMap();
        this.buttonList.add(new GuiButton(0, i + 124, j + 15, 120, 20, ClientProxy.getReferedClientHive().reproduces ? I18n.format("myrmex.message.disablebreeding") : I18n.format("myrmex.message.enablebreeding")));
        this.buttonList.add(this.previousPage = new ChangePageButton(1, i + 5, j + 150, false, 0, this.jungle ? 2 : 1));
        this.buttonList.add(this.nextPage = new ChangePageButton(2, i + 225, j + 150, true, 0, this.jungle ? 2 : 1));
        int totalRooms = allRoomPos.size();
        for (int rooms = 0; rooms < allRoomPos.size(); rooms++) {
            int yIndex = rooms % ROOMS_PER_PAGE;
            //IndexPageButton button = new IndexPageButton(2 + i, centerX + 15 + (xIndex * 200), centerY + 10 + (yIndex * 20) - (xIndex == 1 ? 20 : 0), StatCollector.translateToLocal("bestiary." + EnumBestiaryPages.values()[allPageTypes.get(i).ordinal()].toString().toLowerCase()));
            MyrmexDeleteButton button = new MyrmexDeleteButton(2 + rooms, i + x_translate, j + y_translate + (yIndex) * 22, allRoomPos.get(rooms).pos, I18n.format("myrmex.message.delete"));
            button.visible = rooms < ROOMS_PER_PAGE * (this.currentPage + 1) && rooms >= ROOMS_PER_PAGE * this.currentPage;
            this.buttonList.add(button);
            this.allRoomButtonPos.add(button);
        }
        if (totalRooms <= ROOMS_PER_PAGE * (this.currentPage) && this.currentPage > 0) {
            this.currentPage--;
        }
    }

    private void populateRoomMap() {
        allRoomPos.clear();

        for (WorldGenMyrmexHive.RoomType type : ROOMS) {
            List<BlockPos> roomPos = ClientProxy.getReferedClientHive().getRooms(type);
            for (BlockPos pos : roomPos) {
                String name = type == WorldGenMyrmexHive.RoomType.FOOD ? "food" : type == WorldGenMyrmexHive.RoomType.NURSERY ? "nursery" : "misc";
                allRoomPos.add(new Room(pos, name));
                //this.buttonList.add(new MyrmexDeleteButton(buttons, i + x_translate, j + y_translate + (-1 + buttons) * 22, pos, I18n.format("myrmex.message.delete")));
            }
        }
        for (BlockPos pos : ClientProxy.getReferedClientHive().getEntrances().keySet()) {
            allRoomPos.add(new Room(pos, "enterance_surface"));
            //this.buttonList.add(new MyrmexDeleteButton(buttons, i + x_translate, j + y_translate + (-1 + buttons) * 22, pos, I18n.format("myrmex.message.delete")));
        }
        for (BlockPos pos : ClientProxy.getReferedClientHive().getEntranceBottoms().keySet()) {
            allRoomPos.add(new Room(pos, "enterance_bottom"));
            //this.buttonList.add(new MyrmexDeleteButton(buttons, i + x_translate, j + y_translate + (-1 + buttons) * 22, pos, I18n.format("myrmex.message.delete")));
        }
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            ClientProxy.getReferedClientHive().reproduces = !ClientProxy.getReferedClientHive().reproduces;
        } else if (button.id == 1 && this.currentPage > 0) {
            this.currentPage--;
        }
        if (button.id == 2 && this.currentPage < this.allRoomButtonPos.size() / ROOMS_PER_PAGE) {
            this.currentPage++;
        } else if (button instanceof MyrmexDeleteButton && ticksSinceDeleted <= 0) {
            ClientProxy.getReferedClientHive().removeRoom(((MyrmexDeleteButton) button).pos);
            ticksSinceDeleted = 5;
        }
        initGui();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawDefaultBackground() {
        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (ticksSinceDeleted > 0) {
            ticksSinceDeleted--;
        }
        initGui();
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        super.drawScreen(mouseX, mouseY, partialTicks);
        int color = this.jungle ? 0X35EA15 : 0XFFBF00;
        hiveCount = 0;
        for (int rooms = 0; rooms < this.allRoomButtonPos.size(); rooms++) {
            if (rooms < ROOMS_PER_PAGE * (this.currentPage + 1) && rooms >= ROOMS_PER_PAGE * this.currentPage) {
                this.drawRoomInfo(this.allRoomPos.get(rooms).string, this.allRoomPos.get(rooms).pos, i, j, color);
            }
        }
        if (ClientProxy.getReferedClientHive() != null) {
            if (!ClientProxy.getReferedClientHive().colonyName.isEmpty()) {
                String title = I18n.format("myrmex.message.colony_named", ClientProxy.getReferedClientHive().colonyName);
                this.fontRenderer.drawString(title, i + 40 - title.length() / 2, j - 3, color, true);
            } else {
                this.fontRenderer.drawString(I18n.format("myrmex.message.colony"), i + 80, j - 3, color, true);
            }
            int opinion = ClientProxy.getReferedClientHive().getPlayerReputation(Minecraft.getMinecraft().player.getUniqueID());
            this.fontRenderer.drawString(I18n.format("myrmex.message.hive_opinion", opinion), i, j + 12, color, true);
            this.fontRenderer.drawString(I18n.format("myrmex.message.rooms"), i, j + 25, color, true);
            /*int hiveCount = 0;
            for (WorldGenMyrmexHive.RoomType type : ROOMS) {
                List<BlockPos> roomPos = ClientProxy.getReferedClientHive().getRooms(type);
                String name = type == WorldGenMyrmexHive.RoomType.FOOD ? "myrmex.message.room.food" : type == WorldGenMyrmexHive.RoomType.NURSERY ? "myrmex.message.room.nursery" : "myrmex.message.room.misc";
                for (BlockPos pos : roomPos) {
                    hiveCount++;
                    this.fontRenderer.drawString(I18n.format(name, pos.getX(), pos.getY(), pos.getZ()), i, j + 16 + hiveCount * 22, color, true);
                }
            }
            for (BlockPos pos : ClientProxy.getReferedClientHive().getEntrances().keySet()) {
                hiveCount++;
                this.fontRenderer.drawString(I18n.format("myrmex.message.room.enterance_surface", pos.getX(), pos.getY(), pos.getZ()), i, j + 16 + hiveCount * 22, color, true);
            }
            for (BlockPos pos : ClientProxy.getReferedClientHive().getEntranceBottoms().keySet()) {
                hiveCount++;
                this.fontRenderer.drawString(I18n.format("myrmex.message.room.enterance_bottom", pos.getX(), pos.getY(), pos.getZ()), i, j + 16 + hiveCount * 22, color, true);
            }*/

        }
    }

    public void onGuiClosed() {
        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageGetMyrmexHive(ClientProxy.getReferedClientHive()));
    }


    private void drawRoomInfo(String type, BlockPos pos, int i, int j, int color) {
        String translate = "myrmex.message.room." + type;
        this.fontRenderer.drawString(I18n.format(translate, pos.getX(), pos.getY(), pos.getZ()), i, j + 36 + hiveCount * 22, color, true);
        hiveCount++;
    }

    private class Room {
        public BlockPos pos;
        public String string;

        public Room(BlockPos pos, String string) {
            this.pos = pos;
            this.string = string;
        }
    }
}
