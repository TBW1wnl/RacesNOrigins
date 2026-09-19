package com.tbw1wnl.racesnorigins.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import com.tbw1wnl.racesnorigins.player.TraitApplier;
import com.tbw1wnl.racesnorigins.registry.TraitRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.function.Function;

/**
 * Debug/admin commands: {@code /racesnorigins apply race|class <id>} directly applies a
 * definition's modifiers to the executing player, bypassing selection/persistence entirely. Useful
 * to try out a race/class in-game before the full selection flow (GUI, networking, persistence)
 * exists.
 */
public final class TraitCommands {

    private TraitCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(Constants.MOD_ID)
                .then(Commands.literal("apply")
                        .requires(source -> Commands.LEVEL_GAMEMASTERS.check(source.permissions()))
                        .then(Commands.literal("race")
                                .then(Commands.argument("id", IdentifierArgument.id())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider
                                                .suggestResource(TraitRegistry.races().keySet(), builder))
                                        .executes(ctx -> apply(ctx, "race", TraitRegistry::getRace))))
                        .then(Commands.literal("class")
                                .then(Commands.argument("id", IdentifierArgument.id())
                                        .suggests((ctx, builder) -> net.minecraft.commands.SharedSuggestionProvider
                                                .suggestResource(TraitRegistry.classes().keySet(), builder))
                                        .executes(ctx -> apply(ctx, "class", TraitRegistry::getClassDefinition))))));
    }

    private static int apply(CommandContext<CommandSourceStack> ctx, String slot,
                              Function<Identifier, Optional<TraitDefinition>> lookup) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Identifier id = IdentifierArgument.getId(ctx, "id");
        Optional<TraitDefinition> definition = lookup.apply(id);
        if (definition.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Unknown " + slot + ": " + id));
            return 0;
        }
        TraitApplier.apply(player, slot, id, definition.get());
        ctx.getSource().sendSuccess(() -> Component.literal("Applied " + slot + " " + id + " to " + player.getScoreboardName()), true);
        return 1;
    }
}
