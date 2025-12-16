// minecraft mappings 1.21.11
package com.wh0oo.fortune;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import static net.minecraft.commands.Commands.literal;

public class FortuneMod implements ModInitializer {
    public static final String MOD_ID = "fortune";

    @Override
    public void onInitialize() {
        // Load fortunes at startup
        FortuneManager.loadFortunes();

        // Player join message
        ServerPlayConnectionEvents.JOIN.register(
            (ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) -> {
                ServerPlayer player = handler.getPlayer();
                String fortune = FortuneManager.getRandomFortune();
                player.displayClientMessage(
                    Component.literal("§6Fortune: §r" + fortune),
                    false
                );
            }
        );

        // /fortune reload command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                literal("fortune")
                    .then(
                        literal("reload")
                            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                            .executes(context -> {
                                FortuneManager.loadFortunes();
                                context.getSource().sendSuccess(
                                    () -> Component.literal("§aFortunes reloaded."),
                                    true
                                );
                                return 1;
                            })
                    )
            );
        });
    }
}
