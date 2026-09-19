package com.tbw1wnl.racesnorigins;

import com.tbw1wnl.racesnorigins.network.FabricNetworking;
import net.fabricmc.api.ClientModInitializer;

public class RacesNOriginsClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricNetworking.registerClient();
    }
}
