package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.network.ClientboundTraitListPayload;
import com.tbw1wnl.racesnorigins.network.PayloadHandlers;
import com.tbw1wnl.racesnorigins.network.ServerboundSelectTraitsPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class FabricNetworking {

    private FabricNetworking() {
    }

    public static void registerCommon() {
        PayloadTypeRegistry.clientboundPlay().register(ClientboundTraitListPayload.TYPE, ClientboundTraitListPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ServerboundSelectTraitsPayload.TYPE, ServerboundSelectTraitsPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ServerboundSelectTraitsPayload.TYPE, (payload, context) ->
                context.server().execute(() -> PayloadHandlers.handleSelect(context.player(), payload)));
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundTraitListPayload.TYPE, (payload, context) ->
                Constants.LOG.info("Received trait list: {} races, {} classes, hasChosen={}",
                        payload.races().size(), payload.classes().size(), payload.hasChosen()));
    }

    public static void sendTraitList(ServerPlayer player) {
        ServerPlayNetworking.send(player, PayloadHandlers.buildTraitList(player));
    }
}
