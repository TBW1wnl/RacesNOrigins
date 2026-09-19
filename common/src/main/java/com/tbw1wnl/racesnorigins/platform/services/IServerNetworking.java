package com.tbw1wnl.racesnorigins.platform.services;

import net.minecraft.server.level.ServerPlayer;

public interface IServerNetworking {

    void sendTraitList(ServerPlayer player, boolean forceReselect);
}
