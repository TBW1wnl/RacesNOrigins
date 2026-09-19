package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.HostileToGolemsHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Iron golems have no attack-worthy targeting goal for players by default - but they DO already
 * implement {@code NeutralMob} and register {@code NearestAttackableTargetGoal<>(this, Player.class,
 * 10, true, false, this::isAngryAt)} in {@code registerGoals()} (see the decompiled sources), the
 * same "universal anger" system wolves/piglins use. Rather than adding a whole new AI goal, this
 * periodically seeds that existing system's persistent anger target with any nearby player flagged
 * {@code racesnorigins:hostile_to_golems} - vanilla's own goal then picks them up and attacks
 * exactly as if the golem had been provoked normally.
 */
@Mixin(IronGolem.class)
public abstract class MixinIronGolemHostility {

    private static final double SCAN_RADIUS = 16.0;
    private static final int SCAN_INTERVAL_TICKS = 20;

    @Unique
    private int racesnorigins$scanCooldown = 0;

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void racesnorigins$onAiStep(CallbackInfo ci) {
        IronGolem self = (IronGolem) (Object) this;
        if (self.level().isClientSide() || self.getPersistentAngerTarget() != null) {
            return;
        }
        if (this.racesnorigins$scanCooldown > 0) {
            this.racesnorigins$scanCooldown--;
            return;
        }
        this.racesnorigins$scanCooldown = SCAN_INTERVAL_TICKS;

        ServerLevel level = (ServerLevel) self.level();
        for (ServerPlayer player : level.players()) {
            if (((HostileToGolemsHolder) (LivingEntity) player).racesnorigins$isHostileToGolems()
                    && self.distanceToSqr(player) <= SCAN_RADIUS * SCAN_RADIUS) {
                self.setPersistentAngerTarget(EntityReference.of(player));
                self.startPersistentAngerTimer();
                break;
            }
        }
    }
}
