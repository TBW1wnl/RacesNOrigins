package com.tbw1wnl.racesnorigins;

import com.tbw1wnl.racesnorigins.command.TraitCommands;
import com.tbw1wnl.racesnorigins.item.ModItems;
import com.tbw1wnl.racesnorigins.network.FabricNetworking;
import com.tbw1wnl.racesnorigins.platform.Services;
import com.tbw1wnl.racesnorigins.player.TraitApplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class RacesNOriginsMod implements ModInitializer {

    @Override
    public void onInitialize() {

        CommonClass.init();
        FabricNetworking.registerCommon();
        Registry.register(BuiltInRegistries.ITEM, ModItems.RACE_CLASS_SELECTOR_KEY,
                ModItems.createRaceClassSelector(ModItems.baseProperties().setId(ModItems.RACE_CLASS_SELECTOR_KEY)));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TraitCommands.register(dispatcher));
        // AttachmentType#copyOnDeath() alone wasn't enough for player_trait_data in testing, so copy explicitly too.
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
                Services.PLAYER_DATA.set(newPlayer, Services.PLAYER_DATA.get(oldPlayer)));
        ServerPlayerEvents.JOIN.register(player -> {
            TraitApplier.reapplyAll(player);
            FabricNetworking.sendTraitList(player);
        });
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> TraitApplier.reapplyAll(newPlayer));
    }
}
