package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;

/**
 * Ticking: sets the player on fire (or applies an effect instead) while standing in direct
 * sunlight, using the same probabilistic condition vanilla uses for undead mobs tagged
 * {@code #minecraft:burn_in_daylight} (see {@code Mob#isSunBurnTick} in the decompiled sources) -
 * a randomized per-tick chance scaling with light level, not an instant/guaranteed ignition, and
 * blocked by water/rain/powder snow same as vanilla. Deliberately does NOT replicate vanilla's
 * "a helmet blocks it" behavior - unlike a mob, this is meant to be a real downside of the race,
 * not something any spare helmet cancels.
 */
public record SunSensitivityModifierTrait(String action, Optional<Identifier> effect, int amplifier) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("sun_sensitivity");

    public static final MapCodec<SunSensitivityModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(SunSensitivityModifierTrait::action),
            Identifier.CODEC.optionalFieldOf("effect").forGetter(SunSensitivityModifierTrait::effect),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(SunSensitivityModifierTrait::amplifier)
    ).apply(instance, SunSensitivityModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
    }

    @Override
    public void tick(ServerPlayer player, Identifier instanceId) {
        if (!isSunBurnTick(player)) {
            return;
        }
        if ("effect".equals(action)) {
            effect.ifPresent(id -> player.addEffect(new MobEffectInstance(resolveEffect(id), 60, amplifier, true, true, true)));
        } else {
            player.igniteForSeconds(8.0F);
        }
    }

    private boolean isSunBurnTick(ServerPlayer player) {
        float brightness = player.getLightLevelDependentMagicValue();
        BlockPos eyePos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
        boolean shielded = player.isInWaterOrRain() || player.isInPowderSnow || player.wasInPowderSnow;
        return brightness > 0.5F
                && player.getRandom().nextFloat() * 30.0F < (brightness - 0.4F) * 2.0F
                && !shielded
                && player.level().canSeeSky(eyePos);
    }

    private Holder<MobEffect> resolveEffect(Identifier id) {
        return BuiltInRegistries.MOB_EFFECT.get(id)
                .orElseThrow(() -> new IllegalStateException("Unknown effect: " + id));
    }
}
