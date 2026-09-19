package com.tbw1wnl.racesnorigins.item;

import com.tbw1wnl.racesnorigins.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {

    public static final ResourceKey<Item> RACE_CLASS_SELECTOR_KEY =
            ResourceKey.create(Registries.ITEM, Constants.id("race_class_selector"));

    private ModItems() {
    }

    public static Item.Properties baseProperties() {
        return new Item.Properties().stacksTo(1);
    }

    public static RaceClassSelectorItem createRaceClassSelector(Item.Properties properties) {
        return new RaceClassSelectorItem(properties);
    }
}
