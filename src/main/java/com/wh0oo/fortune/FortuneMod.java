package com.wh0oo.fortune;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FortuneMod implements ModInitializer {
    public static final String MOD_ID = "fortune";

    @Override
    public void onInitialize() {
        // Send a random fortune on player join
        ServerPlayConnectionEvents.JOIN.register((ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            String fortune = FortuneManager.getRandomFortune();
            player.sendMessage(Text.literal("§6Fortune: §r" + fortune), false);
        });
    }
}
