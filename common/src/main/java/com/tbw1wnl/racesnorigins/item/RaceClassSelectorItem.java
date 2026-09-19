package com.tbw1wnl.racesnorigins.item;

import com.tbw1wnl.racesnorigins.platform.Services;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;

/**
 * Reopens the race/class picker on use, without touching the player's persisted choice - only
 * {@link com.tbw1wnl.racesnorigins.network.PayloadHandlers#handleSelect} (i.e. actually confirming
 * a new selection in the reopened GUI) changes it.
 */
public class RaceClassSelectorItem extends Item {

    public RaceClassSelectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            Services.SERVER_NETWORK.sendTraitList(serverPlayer, true);
        }
        return InteractionResult.SUCCESS;
    }
}
