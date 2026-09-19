package com.tbw1wnl.racesnorigins.platform;

import com.tbw1wnl.racesnorigins.network.NeoForgeNetworking;
import com.tbw1wnl.racesnorigins.platform.services.IServerNetworking;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class NeoForgeServerNetworking implements IServerNetworking {

    @Override
    public void sendTraitList(ServerPlayer player, boolean forceReselect) {
        NeoForgeNetworking.sendTraitList(player, forceReselect);
    }

    @Override
    public void syncCanGlide(ServerPlayer player, boolean canGlide) {
        NeoForgeNetworking.syncCanGlide(player, canGlide);
    }

    @Override
    public void syncDiet(ServerPlayer player, Optional<String> restriction) {
        NeoForgeNetworking.syncDiet(player, restriction);
    }
}
