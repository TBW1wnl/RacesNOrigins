package com.tbw1wnl.racesnorigins.player;

import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import com.tbw1wnl.racesnorigins.platform.Services;
import com.tbw1wnl.racesnorigins.registry.TraitRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Applies (or removes) every modifier of a race/class definition to a player, and orchestrates the
 * persisted {@link PlayerTraitData} (via {@link Services#PLAYER_DATA}) around it. {@code slot} is
 * "race" or "class" and, together with the definition id, seeds the deterministic per-modifier
 * instance id ({@code racesnorigins:<slot>/<namespace>/<path>/mod_<index>}) that modifier
 * implementations use as their stable key (e.g. attribute modifier ids) - the same id is produced
 * again on a later reload/reapply, so re-applying is idempotent and removal never needs a
 * separately persisted list of "what was applied last".
 */
public final class TraitApplier {

    private TraitApplier() {
    }

    public static void apply(ServerPlayer player, String slot, Identifier definitionId, TraitDefinition definition) {
        int index = 0;
        for (TraitModifier modifier : definition.modifiers()) {
            if (!modifier.isOneShot()) {
                modifier.apply(player, instanceId(slot, definitionId, index));
            }
            index++;
        }
        // One-shot modifiers (e.g. spawn_in_nether) run last and only here, never on reapply.
        index = 0;
        for (TraitModifier modifier : definition.modifiers()) {
            if (modifier.isOneShot()) {
                modifier.apply(player, instanceId(slot, definitionId, index));
            }
            index++;
        }
    }

    public static void remove(ServerPlayer player, String slot, Identifier definitionId, TraitDefinition definition) {
        int index = 0;
        for (TraitModifier modifier : definition.modifiers()) {
            if (!modifier.isOneShot()) {
                modifier.remove(player, instanceId(slot, definitionId, index));
            }
            index++;
        }
    }

    /**
     * Re-applies the player's currently persisted race/class (non-one-shot modifiers only) - call
     * on login and respawn, since transient state (attribute modifiers, the fire-immunity flag,
     * ...) doesn't survive the underlying entity object being recreated even though the *choice*
     * of race/class does (it's persisted separately).
     */
    public static void reapplyAll(ServerPlayer player) {
        PlayerTraitData data = Services.PLAYER_DATA.get(player);
        data.raceId().flatMap(TraitRegistry::getRace)
                .ifPresent(def -> apply(player, "race", data.raceId().get(), def));
        data.classId().flatMap(TraitRegistry::getClassDefinition)
                .ifPresent(def -> apply(player, "class", data.classId().get(), def));
    }

    /**
     * Sets the player's race or class: removes the previous selection's (non-one-shot) modifiers if
     * any, persists the new choice, and applies the new definition's modifiers (including one-shot
     * ones - this is the only path that should ever trigger a one-shot effect like
     * {@code spawn_in_nether}, since {@link #reapplyAll} deliberately skips them).
     */
    public static void select(ServerPlayer player, String slot, Identifier newId, TraitDefinition newDefinition) {
        PlayerTraitData data = Services.PLAYER_DATA.get(player);
        Optional<Identifier> previousId = "race".equals(slot) ? data.raceId() : data.classId();
        previousId.flatMap(id -> lookup(slot, id))
                .ifPresent(previousDef -> remove(player, slot, previousId.get(), previousDef));

        PlayerTraitData updated = "race".equals(slot) ? data.withRace(newId) : data.withClassId(newId);
        Services.PLAYER_DATA.set(player, updated);

        apply(player, slot, newId, newDefinition);
    }

    private static Optional<TraitDefinition> lookup(String slot, Identifier id) {
        return "race".equals(slot) ? TraitRegistry.getRace(id) : TraitRegistry.getClassDefinition(id);
    }

    private static Identifier instanceId(String slot, Identifier definitionId, int index) {
        return Constants.id(slot + "/" + definitionId.getNamespace() + "/" + definitionId.getPath() + "/mod_" + index);
    }
}
