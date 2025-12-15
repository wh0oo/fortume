// minecraft mappings 1.21.11
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

    @Override
    public void onInitialize() {
        FortuneManager.loadFortunes();

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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                literal("fortune")
                    .then(
                        literal("reload")
                            .requires(source ->
                                source.getPlayer() == null
                                || source.getServer()
                                         .getProfilePermissions(
                                             source.getPlayer().getGameProfile()
                                         ) >= 2
                            )
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
