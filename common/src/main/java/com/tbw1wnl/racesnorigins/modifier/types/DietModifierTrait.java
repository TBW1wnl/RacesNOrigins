package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.DietHolder;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import com.tbw1wnl.racesnorigins.platform.Services;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * {@code "carnivore"} restricts nourishment to {@code #racesnorigins:meat}-tagged food, {@code "herbivore"}
 * to everything else - see {@link com.tbw1wnl.racesnorigins.mixin.MixinFoodPropertiesOnConsume} for
 * the actual enforcement (disallowed food is still consumable, it just grants no hunger/saturation).
 */
public record DietModifierTrait(String restriction) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("diet");

    public static final MapCodec<DietModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("restriction").forGetter(DietModifierTrait::restriction)
    ).apply(instance, DietModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        ((DietHolder) player).racesnorigins$setDietRestriction(Optional.of(restriction));
        Services.SERVER_NETWORK.syncDiet(player, Optional.of(restriction));
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        ((DietHolder) player).racesnorigins$setDietRestriction(Optional.empty());
        Services.SERVER_NETWORK.syncDiet(player, Optional.empty());
    }
}
