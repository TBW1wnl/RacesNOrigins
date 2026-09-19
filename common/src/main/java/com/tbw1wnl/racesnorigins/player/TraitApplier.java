package com.tbw1wnl.racesnorigins.player;

import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * Applies (or removes) every modifier of a race/class definition to a player. {@code slot} is
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
            Identifier instanceId = instanceId(slot, definitionId, index);
            if (!modifier.isOneShot()) {
                modifier.apply(player, instanceId);
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

    private static Identifier instanceId(String slot, Identifier definitionId, int index) {
        return Constants.id(slot + "/" + definitionId.getNamespace() + "/" + definitionId.getPath() + "/mod_" + index);
    }
}
