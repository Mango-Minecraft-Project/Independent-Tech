package dev.mangojellypudding.independenttech.register;

import dev.mangojellypudding.independenttech.IndependentTech;
import dev.mangojellypudding.independenttech.cookingforblockheads.block.entity.SinkBlockEntity;
import dev.mangojellypudding.independenttech.farmersdelight.blocks.entity.BasketBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ITBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, IndependentTech.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasketBlockEntity>> BASKET = BLOCK_ENTITY_TYPES.register("farmersdelight/basket",
            () -> BlockEntityType.Builder.of(BasketBlockEntity::new, ITBlocks.BASKET.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SinkBlockEntity>> SINK = BLOCK_ENTITY_TYPES.register("cookingforblockheads/sink",
            () -> BlockEntityType.Builder.of(SinkBlockEntity::new, ITBlocks.SINK.get()).build(null));
}
