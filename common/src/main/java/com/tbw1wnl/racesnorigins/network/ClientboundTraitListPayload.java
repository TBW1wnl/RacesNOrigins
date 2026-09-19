package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.Constants;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

/**
 * Sent server -> client on join and after every datapack reload: the full resolved catalog of
 * race/class summaries, plus whether this player has already made a selection (so the client knows
 * whether to prompt).
 */
public record ClientboundTraitListPayload(List<TraitSummary> races, List<TraitSummary> classes,
                                           boolean hasChosen) implements CustomPacketPayload {

    public static final Type<ClientboundTraitListPayload> TYPE = new Type<>(Constants.id("trait_list"));

    public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundTraitListPayload> STREAM_CODEC = StreamCodec.composite(
            TraitSummary.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundTraitListPayload::races,
            TraitSummary.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundTraitListPayload::classes,
            ByteBufCodecs.BOOL, ClientboundTraitListPayload::hasChosen,
            ClientboundTraitListPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
