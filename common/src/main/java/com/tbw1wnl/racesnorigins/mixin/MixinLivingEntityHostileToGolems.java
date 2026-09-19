package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.HostileToGolemsHolder;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityHostileToGolems implements HostileToGolemsHolder {

    @Unique
    private boolean racesnorigins$hostileToGolems = false;

    @Override
    public boolean racesnorigins$isHostileToGolems() {
        return this.racesnorigins$hostileToGolems;
    }

    @Override
    public void racesnorigins$setHostileToGolems(boolean hostile) {
        this.racesnorigins$hostileToGolems = hostile;
    }
}
