package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.HostileMobNeutralityHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/**
 * {@code canAttack} is the single chokepoint {@code TargetingConditions#test} funnels through for
 * essentially all vanilla hostile-mob targeting goals (see the decompiled sources) - cancelling it
 * here, keyed on the potential target's own held set of "types that leave me alone", covers any
 * mob type without a per-mob mixin.
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityHostileNeutrality implements HostileMobNeutralityHolder {

    @Unique
    private Set<Identifier> racesnorigins$neutralMobTypes = Set.of();

    @Override
    public Set<Identifier> racesnorigins$neutralMobTypes() {
        return this.racesnorigins$neutralMobTypes;
    }

    @Override
    public void racesnorigins$setNeutralMobTypes(Set<Identifier> mobTypes) {
        this.racesnorigins$neutralMobTypes = mobTypes;
    }

    @Inject(method = "canAttack", at = @At("HEAD"), cancellable = true)
    private void racesnorigins$onCanAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof HostileMobNeutralityHolder holder && !holder.racesnorigins$neutralMobTypes().isEmpty()) {
            LivingEntity self = (LivingEntity) (Object) this;
            Identifier attackerType = EntityType.getKey(self.getType());
            if (holder.racesnorigins$neutralMobTypes().contains(attackerType)) {
                cir.setReturnValue(false);
            }
        }
    }
}
