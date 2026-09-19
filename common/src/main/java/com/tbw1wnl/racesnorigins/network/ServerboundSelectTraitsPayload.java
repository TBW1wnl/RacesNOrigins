package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.Constants;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Sent client -> server to confirm a race + class selection.
 */
public record ServerboundSelectTraitsPayload(Identifier raceId, Identifier classId) implements CustomPacketPayload {

    public static final Type<ServerboundSelectTraitsPayload> TYPE = new Type<>(Constants.id("select_traits"));

    public static final StreamCodec<io.netty.buffer.ByteBuf, ServerboundSelectTraitsPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ServerboundSelectTraitsPayload::raceId,
            Identifier.STREAM_CODEC, ServerboundSelectTraitsPayload::classId,
            ServerboundSelectTraitsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
