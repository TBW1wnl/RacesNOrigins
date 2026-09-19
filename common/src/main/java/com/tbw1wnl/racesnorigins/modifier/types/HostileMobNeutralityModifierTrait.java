package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.HostileMobNeutralityHolder;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Set;

/**
 * An explicit, extensible list of mob type ids that will never target this player - see
 * {@link com.tbw1wnl.racesnorigins.mixin.MixinLivingEntityHostileNeutrality} for the enforcement
 * (a single mixin on {@code LivingEntity#canAttack}, not one per mob type).
 */
public record HostileMobNeutralityModifierTrait(List<Identifier> mobs) implements TraitModifier {

    public static final Identifier TYPE = Constants.id("hostile_mob_neutrality");

    public static final MapCodec<HostileMobNeutralityModifierTrait> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.listOf().fieldOf("mobs").forGetter(HostileMobNeutralityModifierTrait::mobs)
    ).apply(instance, HostileMobNeutralityModifierTrait::new));

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        ((HostileMobNeutralityHolder) player).racesnorigins$setNeutralMobTypes(Set.copyOf(mobs));
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        ((HostileMobNeutralityHolder) player).racesnorigins$setNeutralMobTypes(Set.of());
    }
}
