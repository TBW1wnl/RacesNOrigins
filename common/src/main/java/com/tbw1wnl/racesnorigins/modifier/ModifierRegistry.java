package com.tbw1wnl.racesnorigins.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.types.AttributeModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.EffectImmunityModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.FireImmunityModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.FlightModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.PermanentEffectModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.ScaleModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.SpawnInNetherModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.SunSensitivityModifierTrait;
import com.tbw1wnl.racesnorigins.modifier.types.WaterBreathingModifierTrait;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Java-side registry of modifier ("perk") types. This is NOT datapack-driven - it's the fixed set
 * of behaviors a "type" field in a race/class JSON can reference. Race/class definitions themselves
 * are free to mix any combination of these via datapacks; new behaviors are added here in code
 * (by this mod or an addon) without touching the data format.
 */
public final class ModifierRegistry {

    private static final Map<Identifier, MapCodec<? extends TraitModifier>> TYPES = new HashMap<>();

    private ModifierRegistry() {
    }

    public static void bootstrap() {
        register(AttributeModifierTrait.TYPE, AttributeModifierTrait.CODEC);
        register(ScaleModifierTrait.TYPE, ScaleModifierTrait.CODEC);
        register(WaterBreathingModifierTrait.TYPE, WaterBreathingModifierTrait.CODEC);
        register(FireImmunityModifierTrait.TYPE, FireImmunityModifierTrait.CODEC);
        register(SpawnInNetherModifierTrait.TYPE, SpawnInNetherModifierTrait.CODEC);
        register(PermanentEffectModifierTrait.TYPE, PermanentEffectModifierTrait.CODEC);
        register(EffectImmunityModifierTrait.TYPE, EffectImmunityModifierTrait.CODEC);
        register(SunSensitivityModifierTrait.TYPE, SunSensitivityModifierTrait.CODEC);
        register(FlightModifierTrait.TYPE, FlightModifierTrait.CODEC);
    }

    public static void register(Identifier id, MapCodec<? extends TraitModifier> codec) {
        if (TYPES.putIfAbsent(id, codec) != null) {
            throw new IllegalStateException("Duplicate trait modifier type registered: " + id);
        }
    }

    /**
     * Full dispatch codec used to decode the "modifiers" list of a race/class definition. Encoding
     * is never actually exercised by the datapack loading pathway (which only reads JSON), but is
     * implemented fully via {@link TraitModifier#type()} for correctness.
     */
    public static Codec<TraitModifier> codec() {
        return Identifier.CODEC.dispatch("type", TraitModifier::type, id -> {
            MapCodec<? extends TraitModifier> codec = TYPES.get(id);
            if (codec == null) {
                throw new IllegalArgumentException("Unknown " + Constants.MOD_ID + " modifier type: " + id);
            }
            return codec;
        });
    }
}
