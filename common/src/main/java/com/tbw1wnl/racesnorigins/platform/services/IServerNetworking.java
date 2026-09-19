package com.tbw1wnl.racesnorigins.platform.services;

import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public interface IServerNetworking {

    void sendTraitList(ServerPlayer player, boolean forceReselect);

    void syncCanGlide(ServerPlayer player, boolean canGlide);

    void syncDiet(ServerPlayer player, Optional<String> restriction);
}
