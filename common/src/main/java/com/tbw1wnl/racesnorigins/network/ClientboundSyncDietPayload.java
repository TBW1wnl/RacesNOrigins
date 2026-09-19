package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.Constants;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Optional;

/**
 * Sent server -> client whenever a {@code racesnorigins:diet} restriction is granted or removed.
 * Needed because {@code FoodProperties#onConsume} runs identically on both sides (see
 * {@code MixinFoodPropertiesOnConsume}) - without mirroring the restriction onto the client's own
 * player object, the client's local hunger prediction would desync from the server's authoritative
 * value, same reasoning as {@link ClientboundSyncGlideFlagPayload}.
 */
public record ClientboundSyncDietPayload(Optional<String> restriction) implements CustomPacketPayload {

    public static final Type<ClientboundSyncDietPayload> TYPE = new Type<>(Constants.id("sync_diet"));

    public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundSyncDietPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), ClientboundSyncDietPayload::restriction,
            ClientboundSyncDietPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
