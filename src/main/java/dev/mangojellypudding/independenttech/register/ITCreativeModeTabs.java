package dev.mangojellypudding.independenttech.register;

import dev.mangojellypudding.independenttech.IndependentTech;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ITCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndependentTech.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.independent_tech.tab"))
//            .withTabsImage(ResourceLocation.fromNamespaceAndPath(IndependentTech.MODID, "textures/gui/container/creative_inventory/tabs.png"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(ITItems.BASKET::toStack)
            .displayItems((parameters, output) -> {
                output.acceptAll(
                    BuiltInRegistries.ITEM
                            .stream()
                            .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(IndependentTech.MODID))
                            .map(Item::getDefaultInstance)
                            .toList()
                );
            }).build());
}
