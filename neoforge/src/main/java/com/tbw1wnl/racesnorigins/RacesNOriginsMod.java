package com.tbw1wnl.racesnorigins;

import com.tbw1wnl.racesnorigins.command.TraitCommands;
import com.tbw1wnl.racesnorigins.network.NeoForgeNetworking;
import com.tbw1wnl.racesnorigins.platform.NeoForgePlayerDataStore;
import com.tbw1wnl.racesnorigins.platform.Services;
import com.tbw1wnl.racesnorigins.player.TraitApplier;
import com.tbw1wnl.racesnorigins.player.TraitTicker;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(Constants.MOD_ID)
public class RacesNOriginsMod {

    public RacesNOriginsMod(IEventBus eventBus) {

        NeoForgePlayerDataStore.register(eventBus);
        NeoForgeItems.register(eventBus);
        CommonClass.init();
        eventBus.addListener(NeoForgeNetworking::register);
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> TraitCommands.register(event.getDispatcher()));
        // AttachmentType.Builder#copyOnDeath() wasn't reliably carrying player_trait_data across
        // respawn in testing, so copy explicitly too.
        NeoForge.EVENT_BUS.addListener((PlayerEvent.Clone event) -> {
            if (event.getEntity() instanceof ServerPlayer newPlayer && event.getOriginal() instanceof ServerPlayer oldPlayer) {
                Services.PLAYER_DATA.set(newPlayer, Services.PLAYER_DATA.get(oldPlayer));
            }
        });
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            TraitApplier.reapplyAll(player);
            NeoForgeNetworking.sendTraitList(player);
        });
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerRespawnEvent event) ->
                TraitApplier.reapplyAll((ServerPlayer) event.getEntity()));
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) ->
                event.getServer().getPlayerList().getPlayers().forEach(TraitTicker::tick));
    }
}
