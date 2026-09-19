package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.GlideHolder;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import com.tbw1wnl.racesnorigins.platform.Services;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * {@code "creative"} mode grants free-fly like creative mode (ticked: vanilla recalculates
 * abilities on its own whenever the game mode changes, so this must be continuously reasserted
 * rather than set once). {@code "elytra"} mode lets the player glide like they have an elytra
 * equipped, without one (physics only - see {@link GlideHolder}, no custom wing rendering).
 */
public record FlightModifierTrait(String mode) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("flight");

    public static final MapCodec<FlightModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("mode").forGetter(FlightModifierTrait::mode)
    ).apply(instance, FlightModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        if ("elytra".equals(mode)) {
            ((GlideHolder) player).racesnorigins$setCanGlide(true);
            Services.SERVER_NETWORK.syncCanGlide(player, true);
        }
        // "creative" mode is granted continuously via tick() rather than here, since it must be
        // reasserted every tick to survive vanilla's own ability recalculation.
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        if ("elytra".equals(mode)) {
            ((GlideHolder) player).racesnorigins$setCanGlide(false);
            Services.SERVER_NETWORK.syncCanGlide(player, false);
        } else if ("creative".equals(mode)) {
            // Only take flight away if the player isn't independently entitled to it (real
            // creative/spectator mode) - don't fight the vanilla gamemode system.
            player.getAbilities().mayfly = player.isCreative() || player.isSpectator();
            if (!player.getAbilities().mayfly) {
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();
        }
    }

    @Override
    public void tick(ServerPlayer player, Identifier instanceId) {
        if ("creative".equals(mode) && !player.getAbilities().mayfly) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }
}
