package com.tbw1wnl.racesnorigins.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.modifier.ModifierRegistry;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * A single race or class definition, as loaded from a JSON file under
 * {@code data/<namespace>/races/*.json} or {@code data/<namespace>/classes/*.json}. Both use this
 * exact same schema - races and classes are only distinguished by which folder they're loaded from.
 */
public record TraitDefinition(String name, String description, Identifier icon, List<TraitModifier> modifiers) {

    public static final Codec<TraitDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(TraitDefinition::name),
            Codec.STRING.fieldOf("description").forGetter(TraitDefinition::description),
            Identifier.CODEC.fieldOf("icon").forGetter(TraitDefinition::icon),
            ModifierRegistry.codec().listOf().optionalFieldOf("modifiers", List.of()).forGetter(TraitDefinition::modifiers)
    ).apply(instance, TraitDefinition::new));
}
