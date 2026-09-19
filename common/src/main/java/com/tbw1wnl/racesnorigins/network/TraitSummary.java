package com.tbw1wnl.racesnorigins.network;

import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

/**
 * Client-facing summary of a {@link TraitDefinition}: just enough to render a picker entry (id,
 * name, description, icon). Deliberately excludes modifier internals - the client only needs to
 * let the player choose an id, not know what it does mechanically.
 */
public record TraitSummary(Identifier id, String name, String description, Identifier icon) {

    public static final StreamCodec<io.netty.buffer.ByteBuf, TraitSummary> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, TraitSummary::id,
            ByteBufCodecs.STRING_UTF8, TraitSummary::name,
            ByteBufCodecs.STRING_UTF8, TraitSummary::description,
            Identifier.STREAM_CODEC, TraitSummary::icon,
            TraitSummary::new
    );

    public static TraitSummary of(Identifier id, TraitDefinition definition) {
        return new TraitSummary(id, definition.name(), definition.description(), definition.icon());
    }
}
