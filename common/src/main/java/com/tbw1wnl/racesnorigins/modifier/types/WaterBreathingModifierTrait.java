package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.MapCodec;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * Grants permanent water breathing (no drowning, no bubble UI) via a hidden infinite-duration
 * vanilla effect, reapplied on login/respawn/reload rather than persisted itself.
 */
public record WaterBreathingModifierTrait() implements TraitModifier {

    public static final Identifier TYPE = Constants.id("water_breathing");

    public static final MapCodec<WaterBreathingModifierTrait> CODEC =
            MapCodec.unit(WaterBreathingModifierTrait::new);

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, MobEffectInstance.INFINITE_DURATION, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        player.removeEffect(MobEffects.WATER_BREATHING);
    }
}
