package com.bokmcdok.wheat.screen;

import com.bokmcdok.wheat.container.FlourMillContainer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlourMillScreen extends ContainerScreen<FlourMillContainer> {

    /**
     * Construction
     * @param container The container this screen is rendering
     * @param playerInventory The player's inventory
     * @param name The localised string to display
     */
    public FlourMillScreen(FlourMillContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    /**
     * Just tick
     */
    public void tick() {
        super.tick();
    }

    /**
     * render the GUI
     * @param mouseX The mouse X position on the screen
     * @param mouseY The mouse Y position on the screen
     * @param partialTicks The number of ticks since the last update
     */
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     * @param mouseX The mouse X position on the screen
     * @param mouseY The mouse Y position on the screen
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        font.drawString(title.getFormattedText(), 28.0F, 6.0F, 4210752);
        font.drawString(playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(ySize - 96 + 2), 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     * @param mouseX The mouse X position on the screen
     * @param mouseY The mouse Y position on the screen
     * @param partialTicks The number of ticks since the last update
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        int i = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        blit(i, j, 0, 0, xSize, ySize);
    }

    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("docwheat", "textures/gui/container/flour_mill.png");
}
