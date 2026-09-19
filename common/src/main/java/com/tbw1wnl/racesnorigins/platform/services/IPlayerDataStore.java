package com.tbw1wnl.racesnorigins.platform.services;

import com.tbw1wnl.racesnorigins.player.PlayerTraitData;
import net.minecraft.server.level.ServerPlayer;

public interface IPlayerDataStore {

    PlayerTraitData get(ServerPlayer player);

    void set(ServerPlayer player, PlayerTraitData data);
}
