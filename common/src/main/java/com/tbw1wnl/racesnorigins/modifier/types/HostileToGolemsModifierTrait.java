package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.MapCodec;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.HostileToGolemsHolder;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * No fields - see {@link com.tbw1wnl.racesnorigins.mixin.MixinIronGolemHostility} for the
 * enforcement (nearby iron golems periodically turn their own existing anger system against a
 * flagged player).
 */
public record HostileToGolemsModifierTrait() implements TraitModifier {

    public static final Identifier TYPE = Constants.id("hostile_to_golems");

    public static final MapCodec<HostileToGolemsModifierTrait> CODEC = MapCodec.unit(HostileToGolemsModifierTrait::new);

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        ((HostileToGolemsHolder) player).racesnorigins$setHostileToGolems(true);
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        ((HostileToGolemsHolder) player).racesnorigins$setHostileToGolems(false);
    }
}
