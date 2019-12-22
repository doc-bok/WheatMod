package com.bokmcdok.wheat.terraingen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

public class ModWindmillPiecesHelper {
    public static void assemble(ChunkGenerator<?> generator, TemplateManager templateManager, BlockPos position, List<StructurePiece> pieces, SharedSeedRandom random) {
        JigsawManager.func_214889_a(new ResourceLocation("docwheat:windmill/windmills"), 7, ModWindmillPiecesHelper.ModWindmill::new, generator, templateManager, position, pieces, random);
    }

    static {
        JigsawManager.field_214891_a.register(new JigsawPattern(new ResourceLocation("docwheat:windmill/windmills"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece("docwheat:windmill/windmill"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
    }

    public static class ModWindmill extends AbstractVillagePiece {
        public ModWindmill(TemplateManager templateManager, JigsawPiece piece, BlockPos position, int x, Rotation rotation, MutableBoundingBox bounds) {
            super(ModStructurePieceType.WM, templateManager, piece, position, x, rotation, bounds);
        }

        public ModWindmill(TemplateManager templateManager, CompoundNBT nbt) {
            super(templateManager, nbt, ModStructurePieceType.WM);
        }
    }
}
