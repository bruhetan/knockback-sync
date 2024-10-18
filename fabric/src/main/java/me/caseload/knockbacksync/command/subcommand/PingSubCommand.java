package me.caseload.knockbacksync.command.subcommand;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.caseload.knockbacksync.KnockbackSyncBase;
import me.caseload.knockbacksync.command.PlatformSender;
import me.caseload.knockbacksync.manager.PlayerDataManager;
import me.caseload.knockbacksync.player.PlayerData;
import me.caseload.knockbacksync.util.ChatUtil;
import me.caseload.knockbacksync.util.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static me.caseload.knockbacksync.util.ChatUtil.getPingMessage;

public class PingSubCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("ping")
                .requires(source -> true)
                .executes(context -> { // Added .executes() here to handle no target
                    if (context.getSource().getEntity() instanceof ServerPlayer sender) {
                        CommandUtil.sendSuccessMessage(context, () -> Component.literal(getPingMessage(sender.getUUID(), null)));
                    } else {
                        CommandUtil.sendFailureMessage(context, "You must specify a player to use this command from the console.");
                    }
                    return 1;
                })
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(context -> {
                            EntitySelector selector = context.getArgument("target", EntitySelector.class);
                            ServerPlayer target = selector.findSinglePlayer(context.getSource());
                            if (context.getSource().getEntity() instanceof ServerPlayer sender) {
                                CommandUtil.sendSuccessMessage(context, getPingMessage(sender.getUUID(), target.getUUID()));
                            } else {
                                CommandUtil.sendSuccessMessage(context, getPingMessage(PlatformSender.CONSOLE_UUID, target.getUUID()));
                            }
                            return 1;
                        })
                );
    }
}
