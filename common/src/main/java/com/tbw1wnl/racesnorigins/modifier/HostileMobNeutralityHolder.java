package com.tbw1wnl.racesnorigins.modifier;

import net.minecraft.resources.Identifier;

import java.util.Set;

/**
 * Implemented on {@code LivingEntity} via mixin. Backs {@link com.tbw1wnl.racesnorigins.modifier.types.HostileMobNeutralityModifierTrait}:
 * a runtime-only set of entity type ids that should never target this entity, recomputed every time
 * trait modifiers are (re)applied. Server-only (mob AI targeting never runs on the client), unlike
 * {@link DietHolder}/{@link GlideHolder} which needed client-side mirroring.
 */
public interface HostileMobNeutralityHolder {

    Set<Identifier> racesnorigins$neutralMobTypes();

    void racesnorigins$setNeutralMobTypes(Set<Identifier> mobTypes);
}
