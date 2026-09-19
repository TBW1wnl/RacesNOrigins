package com.tbw1wnl.racesnorigins;

import com.tbw1wnl.racesnorigins.item.ModItems;
import com.tbw1wnl.racesnorigins.item.RaceClassSelectorItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final DeferredItem<RaceClassSelectorItem> RACE_CLASS_SELECTOR =
            ITEMS.registerItem("race_class_selector", ModItems::createRaceClassSelector, ModItems::baseProperties);

    private NeoForgeItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
