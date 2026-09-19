package com.tbw1wnl.racesnorigins.platform;

import com.tbw1wnl.racesnorigins.platform.services.IReloadListenerRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeReloadListenerRegistrar implements IReloadListenerRegistrar {

    private static final List<PreparableReloadListener> PENDING = new ArrayList<>();

    public NeoForgeReloadListenerRegistrar() {
        NeoForge.EVENT_BUS.addListener(NeoForgeReloadListenerRegistrar::onAddReloadListeners);
    }

    @Override
    public void registerDataReloadListener(PreparableReloadListener listener) {
        PENDING.add(listener);
    }

    private static void onAddReloadListeners(AddServerReloadListenersEvent event) {
        for (PreparableReloadListener listener : PENDING) {
            event.addListener(Identifier.parse(listener.getName()), listener);
        }
    }
}
