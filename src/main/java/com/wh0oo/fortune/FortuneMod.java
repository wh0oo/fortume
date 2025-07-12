package com.wh0oo.fortune;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class FortuneMod implements ModInitializer {
    public static final String MOD_ID = "fortune";

    @Override
    public void onInitialize() {
        // Load fortunes from config file at startup
        FortuneManager.loadFortunes();

        // Register player join event
        ServerPlayConnectionEvents.JOIN.register((ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            String fortune = FortuneManager.getRandomFortune();
            player.sendMessage(Text.literal("§6Fortune: §r" + fortune), false);
        });

        // Register /fortune reload command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("fortune")
                .then(literal("reload")
                    .requires(source -> source.hasPermissionLevel(2)) // level 2 = operator
                    .executes(context -> {
                        FortuneManager.loadFortunes();
                        ServerCommandSource source = context.getSource();
                        source.sendFeedback(() -> Text.literal("§aFortunes reloaded."), true);
                        return 1;
                    })
                )
            );
        });
    }
}
