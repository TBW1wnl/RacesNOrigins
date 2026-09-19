package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.Constants;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Sent server -> client whenever {@code racesnorigins:flight} (elytra mode) is granted or removed.
 * Needed because the client's own {@code LocalPlayer#canGlide()} override decides locally whether
 * to even attempt gliding (and send the request to the server) - a server-only flag on the
 * corresponding {@code ServerPlayer} is never consulted for that local check, so it must be
 * mirrored onto the client's player object too.
 */
public record ClientboundSyncGlideFlagPayload(boolean canGlide) implements CustomPacketPayload {

    public static final Type<ClientboundSyncGlideFlagPayload> TYPE = new Type<>(Constants.id("sync_glide_flag"));

    public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundSyncGlideFlagPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ClientboundSyncGlideFlagPayload::canGlide,
            ClientboundSyncGlideFlagPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
