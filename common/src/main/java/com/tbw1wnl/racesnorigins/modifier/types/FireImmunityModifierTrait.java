package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.MapCodec;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.FireImmunityHolder;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * Immunity to fire and lava damage. Backed by a runtime-only flag on the entity (see
 * {@link FireImmunityHolder}, added via mixin) rather than persisted state, since it is
 * recomputed every time modifiers are (re)applied.
 */
public record FireImmunityModifierTrait() implements TraitModifier {

    public static final Identifier TYPE = Constants.id("fire_immunity");

    public static final MapCodec<FireImmunityModifierTrait> CODEC =
            MapCodec.unit(FireImmunityModifierTrait::new);

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        ((FireImmunityHolder) player).racesnorigins$setFireImmune(true);
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        ((FireImmunityHolder) player).racesnorigins$setFireImmune(false);
    }
}
