package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import com.tbw1wnl.racesnorigins.platform.Services;
import com.tbw1wnl.racesnorigins.player.PlayerTraitData;
import com.tbw1wnl.racesnorigins.player.TraitApplier;
import com.tbw1wnl.racesnorigins.registry.TraitRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handler bodies shared by both loaders' networking glue - loader-agnostic once you have a
 * {@link ServerPlayer}, so the actual payload registration (which differs enough between Fabric and
 * NeoForge to not be worth abstracting) just calls into these.
 */
public final class PayloadHandlers {

    private PayloadHandlers() {
    }

    public static ClientboundTraitListPayload buildTraitList(ServerPlayer player) {
        List<TraitSummary> races = summaries(TraitRegistry.races());
        List<TraitSummary> classes = summaries(TraitRegistry.classes());
        boolean hasChosen = Services.PLAYER_DATA.get(player).hasChosen();
        return new ClientboundTraitListPayload(races, classes, hasChosen);
    }

    private static List<TraitSummary> summaries(Map<Identifier, TraitDefinition> definitions) {
        return definitions.entrySet().stream()
                .map(entry -> TraitSummary.of(entry.getKey(), entry.getValue()))
                .toList();
    }

    public static void handleSelect(ServerPlayer player, ServerboundSelectTraitsPayload payload) {
        Optional<TraitDefinition> race = TraitRegistry.getRace(payload.raceId());
        Optional<TraitDefinition> classDefinition = TraitRegistry.getClassDefinition(payload.classId());
        if (race.isEmpty() || classDefinition.isEmpty()) {
            Constants.LOG.warn("Player {} tried to select an unknown race/class: {} / {}",
                    player.getScoreboardName(), payload.raceId(), payload.classId());
            return;
        }

        TraitApplier.select(player, "race", payload.raceId(), race.get());
        TraitApplier.select(player, "class", payload.classId(), classDefinition.get());

        PlayerTraitData data = Services.PLAYER_DATA.get(player);
        Services.PLAYER_DATA.set(player, data.withHasChosen(true));

        Constants.LOG.info("Player {} selected race={} class={}", player.getScoreboardName(), payload.raceId(), payload.classId());
    }
}
