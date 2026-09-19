package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.Constants;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoForgeNetworking {

    private NeoForgeNetworking() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ClientboundTraitListPayload.TYPE, ClientboundTraitListPayload.STREAM_CODEC,
                (payload, context) -> Constants.LOG.info("Received trait list: {} races, {} classes, hasChosen={}",
                        payload.races().size(), payload.classes().size(), payload.hasChosen()));
        registrar.playToServer(ServerboundSelectTraitsPayload.TYPE, ServerboundSelectTraitsPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> PayloadHandlers.handleSelect((ServerPlayer) context.player(), payload)));
    }

    public static void sendTraitList(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, PayloadHandlers.buildTraitList(player));
    }
}
