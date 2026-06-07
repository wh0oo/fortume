package com.wh0oo.fortune;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve("fortune");
        Path file = configDir.resolve(CONFIG_FILE);

        if (!Files.exists(file)) {
            loadedFortunes = new ArrayList<>(DEFAULT_FORTUNES);

            try {
                Files.createDirectories(configDir);

                try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                    GSON.toJson(DEFAULT_FORTUNES, writer);
                }

                System.out.println("[Fortune] Created default fortunes.json");
            } catch (IOException e) {
                System.err.println("[Fortune] Failed to create default config: " + e.getMessage());
            }

            return;
        }

        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> fortunes = GSON.fromJson(reader, listType);

            if (fortunes != null) {
                List<String> cleanedFortunes = fortunes.stream()
                    .filter(fortune -> fortune != null && !fortune.isBlank())
                    .toList();

                if (!cleanedFortunes.isEmpty()) {
                    loadedFortunes = new ArrayList<>(cleanedFortunes);
                    System.out.println("[Fortune] Loaded " + loadedFortunes.size() + " custom fortunes.");
                    return;
                }
            }

            loadedFortunes = new ArrayList<>(DEFAULT_FORTUNES);
            System.out.println("[Fortune] fortunes.json was empty or invalid. Using defaults.");
        } catch (IOException | JsonSyntaxException e) {
            loadedFortunes = new ArrayList<>(DEFAULT_FORTUNES);
            System.err.println("[Fortune] Failed to read fortunes.json: " + e.getMessage());
            System.err.println("[Fortune] Using default fortunes.");
        }
    }

    public static String getRandomFortune() {
        if (loadedFortunes.isEmpty()) {
            return "[No fortunes found]";
        }

        return loadedFortunes.get(RANDOM.nextInt(loadedFortunes.size()));
    }
}