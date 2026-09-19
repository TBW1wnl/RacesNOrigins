package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * Controls the player's model size via the vanilla {@code minecraft:scale} attribute. {@code value}
 * is the resulting scale when this is the only scale modifier applied (base value 1.0 + amount);
 * combines additively with other scale modifiers (e.g. a race and a class both affecting scale).
 */
public record ScaleModifierTrait(double value) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("scale");

    public static final MapCodec<ScaleModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("value").forGetter(ScaleModifierTrait::value)
    ).apply(instance, ScaleModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        AttributeInstance instance = player.getAttribute(Attributes.SCALE);
        if (instance == null) {
            return;
        }
        instance.removeModifier(instanceId);
        instance.addPermanentModifier(new AttributeModifier(instanceId, value - 1.0, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        AttributeInstance instance = player.getAttribute(Attributes.SCALE);
        if (instance != null) {
            instance.removeModifier(instanceId);
        }
    }
}
