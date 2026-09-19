package com.tbw1wnl.racesnorigins.platform;

import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.platform.services.IPlayerDataStore;
import com.tbw1wnl.racesnorigins.player.PlayerTraitData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.server.level.ServerPlayer;

public class FabricPlayerDataStore implements IPlayerDataStore {

    private static final AttachmentType<PlayerTraitData> TRAIT_DATA = AttachmentRegistry.<PlayerTraitData>builder()
            .persistent(PlayerTraitData.CODEC)
            .copyOnDeath()
            .initializer(() -> PlayerTraitData.EMPTY)
            .buildAndRegister(Constants.id("player_trait_data"));

    @Override
    public PlayerTraitData get(ServerPlayer player) {
        return player.getAttachedOrCreate(TRAIT_DATA);
    }

    @Override
    public void set(ServerPlayer player, PlayerTraitData data) {
        player.setAttached(TRAIT_DATA, data);
    }
}
