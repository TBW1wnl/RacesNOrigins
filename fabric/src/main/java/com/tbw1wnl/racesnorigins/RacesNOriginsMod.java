package com.tbw1wnl.racesnorigins;

import com.tbw1wnl.racesnorigins.command.TraitCommands;
import com.tbw1wnl.racesnorigins.player.TraitApplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class RacesNOriginsMod implements ModInitializer {

    @Override
    public void onInitialize() {

        CommonClass.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TraitCommands.register(dispatcher));
        ServerPlayerEvents.JOIN.register(TraitApplier::reapplyAll);
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> TraitApplier.reapplyAll(newPlayer));
    }
}
