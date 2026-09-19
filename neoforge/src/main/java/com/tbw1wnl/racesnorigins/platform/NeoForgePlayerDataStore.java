package com.tbw1wnl.racesnorigins.platform;

import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.platform.services.IPlayerDataStore;
import com.tbw1wnl.racesnorigins.player.PlayerTraitData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NeoForgePlayerDataStore implements IPlayerDataStore {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerTraitData>> TRAIT_DATA =
            ATTACHMENT_TYPES.register("player_trait_data", () -> AttachmentType.builder(() -> PlayerTraitData.EMPTY)
                    .serialize(PlayerTraitData.MAP_CODEC)
                    .copyOnDeath()
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

    @Override
    public PlayerTraitData get(ServerPlayer player) {
        return player.getData(TRAIT_DATA);
    }

    @Override
    public void set(ServerPlayer player, PlayerTraitData data) {
        player.setData(TRAIT_DATA, data);
    }
}
