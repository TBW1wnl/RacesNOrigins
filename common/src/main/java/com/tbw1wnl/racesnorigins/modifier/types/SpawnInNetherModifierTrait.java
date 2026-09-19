package com.tbw1wnl.racesnorigins.modifier.types;

import com.mojang.serialization.MapCodec;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.modifier.TraitModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Set;

/**
 * One-shot: teleports the player to a safe spot in the Nether the moment this modifier is first
 * applied (i.e. right after the player confirms a race/class selection that grants it). Never
 * re-applied on subsequent logins/respawns/reloads, and never reverted.
 * <p>
 * There is no reusable vanilla "find a safe spot" helper for arbitrary coordinates (portal-finding
 * code assumes a portal), so this does a simple expanding-ring block scan around the world origin,
 * falling back to carving out a small obsidian platform (like the vanilla End platform) if nothing
 * safe is found nearby.
 */
public record SpawnInNetherModifierTrait() implements TraitModifier {

    public static final Identifier TYPE = Constants.id("spawn_in_nether");

    public static final MapCodec<SpawnInNetherModifierTrait> CODEC =
            MapCodec.unit(SpawnInNetherModifierTrait::new);

    private static final int SCAN_RADIUS = 48;
    private static final int SCAN_STEP = 4;
    private static final int SCAN_MIN_Y = 32;
    private static final int SCAN_MAX_Y = 100;
    private static final int FALLBACK_Y = 100;

    @Override
    public Identifier type() {
        return TYPE;
    }

    @Override
    public boolean isOneShot() {
        return true;
    }

    @Override
    public void apply(ServerPlayer player, Identifier instanceId) {
        ServerLevel nether = player.level().getServer().getLevel(Level.NETHER);
        if (nether == null) {
            Constants.LOG.warn("Nether dimension unavailable, skipping spawn_in_nether for {}", player.getScoreboardName());
            return;
        }
        BlockPos target = findSafeSpot(nether).orElseGet(() -> buildFallbackPlatform(nether));
        player.teleportTo(nether, target.getX() + 0.5, target.getY(), target.getZ() + 0.5,
                Set.<Relative>of(), player.getYRot(), player.getXRot(), false);
    }

    @Override
    public void remove(ServerPlayer player, Identifier instanceId) {
        // One-shot: nothing to revert.
    }

    private Optional<BlockPos> findSafeSpot(ServerLevel level) {
        for (int radius = 0; radius <= SCAN_RADIUS; radius += SCAN_STEP) {
            for (int x = -radius; x <= radius; x += SCAN_STEP) {
                for (int z = -radius; z <= radius; z += SCAN_STEP) {
                    if (Math.max(Math.abs(x), Math.abs(z)) != radius) {
                        continue;
                    }
                    for (int y = SCAN_MAX_Y; y >= SCAN_MIN_Y; y--) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (isSafe(level, pos)) {
                            return Optional.of(pos);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    private boolean isSafe(ServerLevel level, BlockPos pos) {
        BlockState feet = level.getBlockState(pos);
        BlockState head = level.getBlockState(pos.above());
        BlockState ground = level.getBlockState(pos.below());
        return feet.isAir() && head.isAir() && !ground.isAir() && !ground.liquid();
    }

    private BlockPos buildFallbackPlatform(ServerLevel level) {
        BlockPos center = new BlockPos(0, FALLBACK_Y, 0);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                level.setBlockAndUpdate(center.offset(x, -1, z), Blocks.OBSIDIAN.defaultBlockState());
                level.setBlockAndUpdate(center.offset(x, 0, z), Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(center.offset(x, 1, z), Blocks.AIR.defaultBlockState());
            }
        }
        return center;
    }
}
