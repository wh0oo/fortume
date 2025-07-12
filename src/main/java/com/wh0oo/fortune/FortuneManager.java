package com.wh0oo.fortume;

import java.util.List;
import java.util.Random;

public class FortuneManager {
    private static final List<String> DEFAULT_FORTUNES = List.of(
        "Beware the sheep that watches you sleep.",
        "You will find diamonds—but not before lava finds you.",
        "Don't trust the villager with green eyes.",
        "The Nether whispers your name.",
        "Your dog knows something you don't.",
        "A minecart will take you where you need to go.",
        "The End is not the end."
    );

    private static final Random RANDOM = new Random();

    public static String getRandomFortune() {
        return DEFAULT_FORTUNES.get(RANDOM.nextInt(DEFAULT_FORTUNES.size()));
    }
}
