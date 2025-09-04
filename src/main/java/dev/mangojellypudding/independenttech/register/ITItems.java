package dev.mangojellypudding.independenttech.register;

import dev.mangojellypudding.independenttech.IndependentTech;
import dev.mangojellypudding.independenttech.farmersdelight.item.FuelBlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ITItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IndependentTech.MODID);

    public static final DeferredItem<Item> BASKET = ITEMS.register("farmersdelight/basket",
            () -> new FuelBlockItem(ITBlocks.BASKET.get(), new Item.Properties(), 300));
}
