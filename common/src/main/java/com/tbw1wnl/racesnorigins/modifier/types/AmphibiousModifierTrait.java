package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.AmphibiousHolder;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * Inflicts a "reverse drowning" while out of water, mirroring vanilla's own air-supply/drowning
 * logic (see {@code LivingEntity#baseTick}/{@code shouldTakeDrowningDamage} in the decompiled
 * sources) but inverted: the countdown drains on land instead of underwater, and refills instantly
 * on contact with water rather than gradually. Doesn't touch water breathing itself - combine with
 * {@link WaterBreathingModifierTrait} in the same race for a fully amphibious creature; this type
 * alone only adds the land-based penalty.
 */
public record AmphibiousModifierTrait(int landGraceTicks) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("amphibious");

    private static final int DROWNING_DAMAGE_THRESHOLD = -20;
    private static final float DROWNING_DAMAGE = 2.0F;

    public static final MapCodec<AmphibiousModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("land_grace_ticks", 200).forGetter(AmphibiousModifierTrait::landGraceTicks)
    ).apply(instance, AmphibiousModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        ((AmphibiousHolder) player).racesnorigins$setLandTicksRemaining(landGraceTicks);
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
    }

    @Override
    public void tick(ServerPlayer player, Identifier instanceId) {
        if (player.getAbilities().invulnerable) {
            return;
        }
        AmphibiousHolder holder = (AmphibiousHolder) player;
        if (player.isInWater()) {
            holder.racesnorigins$setLandTicksRemaining(landGraceTicks);
            return;
        }
        int remaining = holder.racesnorigins$landTicksRemaining() - 1;
        if (remaining <= DROWNING_DAMAGE_THRESHOLD) {
            player.hurtServer(player.level(), player.damageSources().drown(), DROWNING_DAMAGE);
            remaining = 0;
        }
        holder.racesnorigins$setLandTicksRemaining(remaining);
    }
}
