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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

/**
 * Grants any vanilla (or modded) mob effect permanently, via a hidden infinite-duration effect
 * instance reapplied on login/respawn/reload - the same mechanism {@link WaterBreathingModifierTrait}
 * uses, generalized to an arbitrary effect + amplifier. Covers passive traits like "hunger for a
 * giant race" or "hero-of-the-village-style villager discount" without bespoke code per effect.
 */
public record PermanentEffectModifierTrait(Identifier effect, int amplifier) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("permanent_effect");

    public static final MapCodec<PermanentEffectModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("effect").forGetter(PermanentEffectModifierTrait::effect),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(PermanentEffectModifierTrait::amplifier)
    ).apply(instance, PermanentEffectModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        player.addEffect(new MobEffectInstance(resolveEffect(), MobEffectInstance.INFINITE_DURATION, amplifier, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        player.removeEffect(resolveEffect());
    }

    private Holder<MobEffect> resolveEffect() {
        return BuiltInRegistries.MOB_EFFECT.get(effect)
                .orElseThrow(() -> new IllegalStateException("Unknown effect: " + effect));
    }
}
