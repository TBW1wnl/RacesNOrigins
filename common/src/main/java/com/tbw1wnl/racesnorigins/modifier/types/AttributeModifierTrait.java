package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * Generic wrapper around a vanilla attribute + modifier. Covers most stat tweaks (max health,
 * movement speed, attack damage, knockback resistance, ...) without bespoke code per stat.
 */
public record AttributeModifierTrait(Identifier attribute, double amount,
                                      AttributeModifier.Operation operation) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("attribute_modifier");

    public static final MapCodec<AttributeModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("attribute").forGetter(AttributeModifierTrait::attribute),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifierTrait::amount),
            AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeModifierTrait::operation)
    ).apply(instance, AttributeModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        AttributeInstance instance = player.getAttribute(resolveAttribute());
        if (instance == null) {
            Constants.LOG.warn("Player attribute {} not present, skipping modifier {}", attribute, instanceId);
            return;
        }
        instance.removeModifier(instanceId);
        instance.addPermanentModifier(new AttributeModifier(instanceId, amount, operation));
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        AttributeInstance instance = player.getAttribute(resolveAttribute());
        if (instance != null) {
            instance.removeModifier(instanceId);
        }
    }

    private Holder<Attribute> resolveAttribute() {
        return BuiltInRegistries.ATTRIBUTE.get(attribute)
                .orElseThrow(() -> new IllegalStateException("Unknown attribute: " + attribute));
    }
}
