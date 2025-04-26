package xyz.devvydont.smprpg.entity.player;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public enum ProfileDifficulty {
    NOT_CHOSEN("Unknown", NamedTextColor.GRAY),
    EASY("Wanderer", NamedTextColor.GREEN),
    STANDARD("Adventurer", NamedTextColor.BLUE),
    HARD("Ascendant", NamedTextColor.RED)
    ;

    public static class ProfileDifficultyPersistentAdapter implements PersistentDataType<String, ProfileDifficulty> {
        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<ProfileDifficulty> getComplexType() {
            return ProfileDifficulty.class;
        }

        @Override
        public @NotNull String toPrimitive(@NotNull ProfileDifficulty complex, @NotNull PersistentDataAdapterContext context) {
            return complex.name();
        }

        @Override
        public @NotNull ProfileDifficulty fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
            try {
                return valueOf(primitive.toUpperCase());
            } catch (IllegalArgumentException e) {
                return NOT_CHOSEN;
            }
        }
    }

    public static final ProfileDifficultyPersistentAdapter ADAPTER = new ProfileDifficultyPersistentAdapter();

    public final String Display;
    public final TextColor Color;

    ProfileDifficulty(String display, TextColor color) {
        Display = display;
        Color = color;
    }


}
