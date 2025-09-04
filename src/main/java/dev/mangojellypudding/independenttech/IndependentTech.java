package dev.mangojellypudding.independenttech;

import com.mojang.logging.LogUtils;
import dev.mangojellypudding.independenttech.register.ITBlockEntityTypes;
import dev.mangojellypudding.independenttech.register.ITBlocks;
import dev.mangojellypudding.independenttech.register.ITCreativeModeTabs;
import dev.mangojellypudding.independenttech.register.ITItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(IndependentTech.MODID)
public class IndependentTech {
    public static final String MODID = "independent_tech";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IndependentTech(IEventBus modEventBus, ModContainer modContainer) {
        ITBlocks.BLOCKS.register(modEventBus);
        ITItems.ITEMS.register(modEventBus);
        ITCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ITBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
