package com.tbw1wnl.racesnorigins.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * A player's persisted race/class selection. Stored via the platform-specific attachment (Fabric
 * Attachment API v1 / NeoForge Data Attachments) - see {@code IPlayerDataStore}. Does NOT store the
 * actual applied effects (attribute modifiers, fire-immunity flag, ...) - those are transient,
 * recomputed from the current {@link com.tbw1wnl.racesnorigins.data.TraitDefinition}s every time
 * {@link TraitApplier#reapplyAll} runs (login, respawn).
 */
public record PlayerTraitData(Optional<Identifier> raceId, Optional<Identifier> classId,
                               boolean hasChosen, boolean netherSpawnConsumed) {

    public static final PlayerTraitData EMPTY = new PlayerTraitData(Optional.empty(), Optional.empty(), false, false);

    public static final MapCodec<PlayerTraitData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("race").forGetter(PlayerTraitData::raceId),
            Identifier.CODEC.optionalFieldOf("class").forGetter(PlayerTraitData::classId),
            Codec.BOOL.optionalFieldOf("has_chosen", false).forGetter(PlayerTraitData::hasChosen),
            Codec.BOOL.optionalFieldOf("nether_spawn_consumed", false).forGetter(PlayerTraitData::netherSpawnConsumed)
    ).apply(instance, PlayerTraitData::new));

    public static final Codec<PlayerTraitData> CODEC = MAP_CODEC.codec();

    public PlayerTraitData withRace(Identifier id) {
        return new PlayerTraitData(Optional.of(id), classId, hasChosen, netherSpawnConsumed);
    }

    public PlayerTraitData withClassId(Identifier id) {
        return new PlayerTraitData(raceId, Optional.of(id), hasChosen, netherSpawnConsumed);
    }

    public PlayerTraitData withHasChosen(boolean value) {
        return new PlayerTraitData(raceId, classId, value, netherSpawnConsumed);
    }

    public PlayerTraitData withNetherSpawnConsumed(boolean value) {
        return new PlayerTraitData(raceId, classId, hasChosen, value);
    }
}
