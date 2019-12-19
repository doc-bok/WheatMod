package com.bokmcdok.wheat.material;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class ModMaterialBuilder extends Material.Builder {
    public ModMaterialBuilder(MaterialColor color) {
        super(color);
    }

    public Material.Builder requiresTool() {
        return super.requiresTool();
    }

    public Material.Builder flammable() {
        return super.flammable();
    }

    public Material.Builder replaceable() {
        return  super.replaceable();
    }

    public Material.Builder pushDestroys() {
        return super.pushDestroys();
    }

    public Material.Builder pushBlocks() {
        return super.pushBlocks();
    }
}