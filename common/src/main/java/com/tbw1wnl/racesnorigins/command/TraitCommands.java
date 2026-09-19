package com.tbw1wnl.racesnorigins.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import com.tbw1wnl.racesnorigins.player.TraitApplier;
import com.tbw1wnl.racesnorigins.registry.TraitRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.function.Function;

/**
 * Debug/admin commands: {@code /racesnorigins apply|clear race|class <id>} directly
 * applies/removes a definition's modifiers on the executing player, bypassing
 * selection/persistence entirely. Useful to try out a race/class in-game before the full selection
 * flow (GUI, networking, persistence) exists.
 * <p>
 * {@code apply} does NOT remove a previously applied definition first (that diffing is the job of
 * the not-yet-built persistence layer) - use {@code clear} on the old one first when switching.
 */
public final class TraitCommands {

    private TraitCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(Constants.MOD_ID)
                .then(slotCommand("apply", TraitApplier::apply))
                .then(slotCommand("clear", TraitApplier::remove)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> slotCommand(String literal, TraitAction action) {
        return Commands.literal(literal)
                .requires(source -> Commands.LEVEL_GAMEMASTERS.check(source.permissions()))
                .then(Commands.literal("race")
                        .then(Commands.argument("id", IdentifierArgument.id())
                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(TraitRegistry.races().keySet(), builder))
                                .executes(ctx -> run(ctx, literal, "race", TraitRegistry::getRace, action))))
                .then(Commands.literal("class")
                        .then(Commands.argument("id", IdentifierArgument.id())
                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(TraitRegistry.classes().keySet(), builder))
                                .executes(ctx -> run(ctx, literal, "class", TraitRegistry::getClassDefinition, action))));
    }

    private static int run(CommandContext<CommandSourceStack> ctx, String literal, String slot,
                            Function<Identifier, Optional<TraitDefinition>> lookup, TraitAction action) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Identifier id = IdentifierArgument.getId(ctx, "id");
        Optional<TraitDefinition> definition = lookup.apply(id);
        if (definition.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Unknown " + slot + ": " + id));
            return 0;
        }
        action.run(player, slot, id, definition.get());
        ctx.getSource().sendSuccess(() -> Component.literal(literal + " " + slot + " " + id + " on " + player.getScoreboardName()), true);
        return 1;
    }

    @FunctionalInterface
    private interface TraitAction {
        void run(ServerPlayer player, String slot, Identifier id, TraitDefinition definition);
    }
}
