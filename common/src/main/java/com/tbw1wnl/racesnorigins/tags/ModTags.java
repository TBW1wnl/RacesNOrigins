package com.tbw1wnl.racesnorigins.tags;

import com.tbw1wnl.racesnorigins.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Datapack-editable item tags used by trait modifiers (e.g. {@code racesnorigins:diet}) - defined
 * here only as the Java-side handle; the actual tag membership lives in
 * {@code data/racesnorigins/tags/item/meat.json} and is freely extensible by players/datapacks.
 */
public final class ModTags {

    public static final TagKey<Item> MEAT = TagKey.create(Registries.ITEM, Constants.id("meat"));

    private ModTags() {
    }
}
