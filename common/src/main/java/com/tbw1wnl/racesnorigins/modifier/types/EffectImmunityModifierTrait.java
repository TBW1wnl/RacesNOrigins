package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.EffectImmunityHolder;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * Immunity to a single named mob effect (e.g. poison). Backed by a runtime-only set on the entity
 * (see {@link EffectImmunityHolder}, added via mixin) rather than persisted state, since it is
 * recomputed every time modifiers are (re)applied. Stack several of these to grant immunity to
 * multiple effects.
 */
public record EffectImmunityModifierTrait(Identifier effect) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("effect_immunity");

    public static final MapCodec<EffectImmunityModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("effect").forGetter(EffectImmunityModifierTrait::effect)
    ).apply(instance, EffectImmunityModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        ((EffectImmunityHolder) player).racesnorigins$setImmune(effect, true);
        player.removeEffect(BuiltInRegistries.MOB_EFFECT.get(effect)
                .orElseThrow(() -> new IllegalStateException("Unknown effect: " + effect)));
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        ((EffectImmunityHolder) player).racesnorigins$setImmune(effect, false);
    }
}
