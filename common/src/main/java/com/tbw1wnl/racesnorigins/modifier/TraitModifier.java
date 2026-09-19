package com.tbw1wnl.racesnorigins.modifier;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public interface TraitModifier {

    /**
     * The registered modifier type id this instance was created from (the "type" field in JSON).
     */
    Identifier type();

    /**
     * Applies this modifier to the player. {@code instanceId} is a deterministic id derived from
     * the owning race/class and this modifier's position in its list, stable across reloads as
     * long as the definition doesn't change shape - used by implementations that need a stable
     * key (e.g. attribute modifier ids).
     */
    void apply(ServerPlayer player, Identifier instanceId);

    /**
     * Reverts whatever {@link #apply} did, using the same {@code instanceId}. Must be safe to call
     * even if the modifier was never applied to this player (e.g. after a datapack reload changes
     * what a race grants).
     */
    void remove(ServerPlayer player, Identifier instanceId);

    /**
     * One-shot modifiers (e.g. an initial teleport) only run once, the moment a player confirms a
     * selection that grants them - never re-applied on login/respawn/reload, and {@link #remove} is
     * never called for them.
     */
    default boolean isOneShot() {
        return false;
    }
}
