package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.client.RaceClassSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoForgeNetworking {

    private NeoForgeNetworking() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ClientboundTraitListPayload.TYPE, ClientboundTraitListPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (!payload.hasChosen()) {
                        Minecraft.getInstance().setScreenAndShow(new RaceClassSelectionScreen(payload.races(), payload.classes(),
                                selection -> ClientPacketDistributor.sendToServer(selection)));
                    }
                }));
        registrar.playToServer(ServerboundSelectTraitsPayload.TYPE, ServerboundSelectTraitsPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> PayloadHandlers.handleSelect((ServerPlayer) context.player(), payload)));
    }

    public static void sendTraitList(ServerPlayer player) {
        sendTraitList(player, false);
    }

    public static void sendTraitList(ServerPlayer player, boolean forceReselect) {
        PacketDistributor.sendToPlayer(player, PayloadHandlers.buildTraitList(player, forceReselect));
    }
}
