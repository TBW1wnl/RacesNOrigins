package com.tbw1wnl.racesnorigins.platform.services;

import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface IReloadListenerRegistrar {

    /**
     * Registers a server data-pack reload listener (Fabric: {@code ResourceManagerHelper}, NeoForge:
     * {@code AddReloadListenerEvent}). Called once during mod init for each of
     * {@link com.tbw1wnl.racesnorigins.registry.TraitRegistry#RACES} / {@code #CLASSES}.
     */
    void registerDataReloadListener(PreparableReloadListener listener);
}
