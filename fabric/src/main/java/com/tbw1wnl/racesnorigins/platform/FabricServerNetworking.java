package com.tbw1wnl.racesnorigins.platform;

import com.tbw1wnl.racesnorigins.network.FabricNetworking;
import com.tbw1wnl.racesnorigins.platform.services.IServerNetworking;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class FabricServerNetworking implements IServerNetworking {

    @Override
    public void sendTraitList(ServerPlayer player, boolean forceReselect) {
        FabricNetworking.sendTraitList(player, forceReselect);
    }

    @Override
    public void syncCanGlide(ServerPlayer player, boolean canGlide) {
        FabricNetworking.syncCanGlide(player, canGlide);
    }

    @Override
    public void syncDiet(ServerPlayer player, Optional<String> restriction) {
        FabricNetworking.syncDiet(player, restriction);
    }
}
