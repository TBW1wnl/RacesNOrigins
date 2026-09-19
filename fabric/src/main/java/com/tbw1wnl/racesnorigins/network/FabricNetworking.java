package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.client.RaceClassSelectionScreen;
import com.tbw1wnl.racesnorigins.modifier.GlideHolder;
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
        PayloadTypeRegistry.clientboundPlay().register(ClientboundSyncGlideFlagPayload.TYPE, ClientboundSyncGlideFlagPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ServerboundSelectTraitsPayload.TYPE, (payload, context) ->
                context.server().execute(() -> PayloadHandlers.handleSelect(context.player(), payload)));
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundTraitListPayload.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    if (!payload.hasChosen()) {
                        context.client().setScreenAndShow(new RaceClassSelectionScreen(payload.races(), payload.classes(),
                                ClientPlayNetworking::send));
                    }
                }));
        ClientPlayNetworking.registerGlobalReceiver(ClientboundSyncGlideFlagPayload.TYPE, (payload, context) ->
                context.client().execute(() -> ((GlideHolder) context.player()).racesnorigins$setCanGlide(payload.canGlide())));
    }

    public static void sendTraitList(ServerPlayer player) {
        sendTraitList(player, false);
    }

    public static void sendTraitList(ServerPlayer player, boolean forceReselect) {
        ServerPlayNetworking.send(player, PayloadHandlers.buildTraitList(player, forceReselect));
    }

    public static void syncCanGlide(ServerPlayer player, boolean canGlide) {
        ServerPlayNetworking.send(player, new ClientboundSyncGlideFlagPayload(canGlide));
    }
}
