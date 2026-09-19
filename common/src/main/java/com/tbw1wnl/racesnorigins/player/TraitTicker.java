package com.tbw1wnl.racesnorigins.player;

import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import com.tbw1wnl.racesnorigins.platform.Services;
import com.tbw1wnl.racesnorigins.registry.TraitRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * Drives {@link TraitModifier#tick} for a player's current race and class every server tick -
 * called from each loader's server tick hook (see {@code FabricNetworking}-style glue in
 * {@code RacesNOriginsMod}), iterating online players rather than relying on a per-player tick
 * event so the hook stays identical in shape across both loaders.
 */
public final class TraitTicker {

    private TraitTicker() {
    }

    public static void tick(ServerPlayer player) {
        PlayerTraitData data = Services.PLAYER_DATA.get(player);
        data.raceId().flatMap(TraitRegistry::getRace)
                .ifPresent(def -> tickDefinition(player, "race", data.raceId().get(), def));
        data.classId().flatMap(TraitRegistry::getClassDefinition)
                .ifPresent(def -> tickDefinition(player, "class", data.classId().get(), def));
    }

    private static void tickDefinition(ServerPlayer player, String slot, Identifier definitionId, TraitDefinition definition) {
        int index = 0;
        for (TraitModifier modifier : definition.modifiers()) {
            modifier.tick(player, TraitApplier.instanceId(slot, definitionId, index));
            index++;
        }
    }
}
