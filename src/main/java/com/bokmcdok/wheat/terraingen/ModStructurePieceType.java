package com.bokmcdok.wheat.terraingen;

import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public interface ModStructurePieceType {
    IStructurePieceType WM = IStructurePieceType.register(ModWindmillPieces.ModWindmill::new, "WM");
}
