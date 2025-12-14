package com.wh0oo.fortune;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import static net.minecraft.commands.Commands.literal;

public class FortuneMod implements ModInitializer {
    public static final String MOD_ID = "fortune";

    @Override
    public void onInitialize() {
        // Load fortunes from config file at startup
        FortuneManager.loadFortunes();

        // Register player join event
        ServerPlayConnectionEvents.JOIN.register((ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) -> {
            ServerPlayer player = handler.getPlayer();
            String fortune = FortuneManager.getRandomFortune();
            player.displayClientMessage(Component.literal("§6Fortune: §r" + fortune), false);
        });

        // Register /fortune reload command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("fortune")
                .then(literal("reload")
                    .requires(source -> source.hasPermission(2)) // level 2 = operator
                    .executes(context -> {
                        FortuneManager.loadFortunes();
                        CommandSourceStack source = context.getSource();
                        source.sendSuccess(() -> Component.literal("§aFortunes reloaded."), true);
                        return 1;
                    })
                )
            );
        });
    }
}
