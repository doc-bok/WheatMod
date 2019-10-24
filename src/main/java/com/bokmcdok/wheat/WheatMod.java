package com.bokmcdok.wheat;

import com.bokmcdok.wheat.Entity.ModCampfireTileEntity;
import com.bokmcdok.wheat.Entity.StoneEntity;
import com.bokmcdok.wheat.Render.StoneRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.CampfireTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The Mod Class
 *
 * Just holds constants used in other places in the mod.
 */
@Mod(WheatMod.MOD_ID)
public class WheatMod
{
    public static final String MOD_ID = "docwheat";

    public WheatMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(StoneEntity.class, new StoneRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(ModCampfireTileEntity.class, new ModCampfireTileEntityRenderer());
    }

    public class ModCampfireTileEntityRenderer extends TileEntityRenderer<ModCampfireTileEntity> {
        public void render(CampfireTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
            Direction direction = tileEntityIn.getBlockState().get(CampfireBlock.FACING);
            NonNullList<ItemStack> nonnulllist = tileEntityIn.getInventory();

            for(int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = nonnulllist.get(i);
                if (itemstack != ItemStack.EMPTY) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef((float)x + 0.5F, (float)y + 0.44921875F, (float)z + 0.5F);
                    Direction direction1 = Direction.byHorizontalIndex((i + direction.getHorizontalIndex()) % 4);
                    GlStateManager.rotatef(-direction1.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.translatef(-0.3125F, -0.3125F, 0.0F);
                    GlStateManager.scalef(0.375F, 0.375F, 0.375F);
                    Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
                    GlStateManager.popMatrix();
                }
            }

        }
    }
}
