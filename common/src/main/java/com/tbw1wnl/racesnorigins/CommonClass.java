package com.tbw1wnl.racesnorigins;

import com.tbw1wnl.racesnorigins.modifier.ModifierRegistry;
import com.tbw1wnl.racesnorigins.platform.Services;
import com.tbw1wnl.racesnorigins.registry.TraitRegistry;

public class CommonClass {

    public static void init() {

        Constants.LOG.info("Initializing RacesNOrigins on {}", Services.PLATFORM.getPlatformName());

        ModifierRegistry.bootstrap();
        Services.RELOAD_LISTENERS.registerDataReloadListener(TraitRegistry.RACES);
        Services.RELOAD_LISTENERS.registerDataReloadListener(TraitRegistry.CLASSES);
    }
}
