package com.wh0oo.fortune;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.fabricmc.loader.api.FabricLoader;

public class FortuneManager {
    private static final Random RANDOM = new Random();
    private static final Gson GSON = new Gson();
    private static final String CONFIG_FILE = "fortunes.json";

    private static final List<String> DEFAULT_FORTUNES = List.of(
        "Beware the sheep that watches you sleep.",
        "You will find diamonds—but not before lava finds you.",
        "Don't trust the villager with green eyes.",
        "The Nether whispers your name.",
        "Your dog knows something you don't.",
        "A minecart will take you where you need to go.",
        "The End is not the end."
    );

    private static List<String> loadedFortunes = new ArrayList<>(DEFAULT_FORTUNES);

    public static void loadFortunes() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        File file = configDir.resolve("fortune").resolve(CONFIG_FILE).toFile();

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(file)) {
                    GSON.toJson(DEFAULT_FORTUNES, writer);
                }
                System.out.println("[Fortune] Created default fortunes.json");
            } catch (IOException e) {
                System.err.println("[Fortune] Failed to create default config: " + e.getMessage());
            }
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> fortunes = GSON.fromJson(reader, listType);
            if (fortunes != null && !fortunes.isEmpty()) {
                loadedFortunes = fortunes;
                System.out.println("[Fortune] Loaded " + loadedFortunes.size() + " custom fortunes.");
            } else {
                System.out.println("[Fortune] fortunes.json was empty or invalid. Using defaults.");
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("[Fortune] Failed to read fortunes.json: " + e.getMessage());
        }
    }

    public static String getRandomFortune() {
        if (loadedFortunes.isEmpty()) {
            return "[No fortunes found]";
        }
        return loadedFortunes.get(RANDOM.nextInt(loadedFortunes.size()));
    }
}
