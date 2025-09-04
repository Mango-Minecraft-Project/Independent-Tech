package dev.mangojellypudding.independenttech.register;

import dev.mangojellypudding.independenttech.IndependentTech;
import dev.mangojellypudding.independenttech.farmersdelight.blocks.BasketBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ITBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IndependentTech.MODID);

    public static final DeferredBlock<Block> BASKET = BLOCKS.register("farmersdelight/basket",
            () -> new BasketBlock(Block.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD)));
}
