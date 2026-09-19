package com.tbw1wnl.racesnorigins.platform;

import com.tbw1wnl.racesnorigins.platform.services.IReloadListenerRegistrar;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricReloadListenerRegistrar implements IReloadListenerRegistrar {

    @Override
    public void registerDataReloadListener(PreparableReloadListener listener) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {

            @Override
            public Identifier getFabricId() {
                return Identifier.parse(listener.getName());
            }

            @Override
            public CompletableFuture<Void> reload(PreparableReloadListener.SharedState sharedState, Executor prepareExecutor,
                                                    PreparableReloadListener.PreparationBarrier barrier, Executor applyExecutor) {
                return listener.reload(sharedState, prepareExecutor, barrier, applyExecutor);
            }
        });
    }
}
