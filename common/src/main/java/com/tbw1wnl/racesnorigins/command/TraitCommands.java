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
 * Admin/debug commands under {@code /racesnorigins}:
 * <ul>
 *     <li>{@code set race|class <id>} - the real, persisted selection flow: removes the previous
 *     selection's modifiers (if any), persists the new one, applies it (survives relog/respawn).</li>
 *     <li>{@code apply}/{@code clear race|class <id>} - ephemeral debug tools that apply/remove a
 *     definition's modifiers WITHOUT touching persisted state. Useful to eyeball a definition's
 *     effect without committing to it; {@code apply} does not remove a previous ephemeral
 *     application first, so use {@code clear} on the old one before switching.</li>
 * </ul>
 */
public final class TraitCommands {

    private TraitCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(Constants.MOD_ID)
                .then(slotCommand("set", TraitApplier::select))
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
