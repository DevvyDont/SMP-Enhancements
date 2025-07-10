package xyz.devvydont.smprpg.fishing.loot.requirements;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.block.Biome;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Map;

public record BiomeRequirement(Biome biome) implements FishingLootRequirement {

    private static final Map<Biome, TextColor> COLORS = Map.<Biome, TextColor>ofEntries(
            Map.entry(Biome.BADLANDS, TextColor.color(0xD2691E)),
            Map.entry(Biome.BAMBOO_JUNGLE, TextColor.color(0x2E8B57)),
            Map.entry(Biome.BASALT_DELTAS, TextColor.color(0xFF4500)),
            Map.entry(Biome.BEACH, TextColor.color(0xF4A460)),
            Map.entry(Biome.BIRCH_FOREST, TextColor.color(0x228B22)),
            Map.entry(Biome.CHERRY_GROVE, TextColor.color(0xFF69B4)),
            Map.entry(Biome.COLD_OCEAN, TextColor.color(0x4682B4)),
            Map.entry(Biome.CRIMSON_FOREST, TextColor.color(0xFF4500)),
            Map.entry(Biome.DARK_FOREST, TextColor.color(0x006400)),
            Map.entry(Biome.DEEP_COLD_OCEAN, TextColor.color(0x4682B4)),
            Map.entry(Biome.DEEP_DARK, TextColor.color(0x8B4513)),
            Map.entry(Biome.DEEP_FROZEN_OCEAN, TextColor.color(0x87CEFA)),
            Map.entry(Biome.DEEP_LUKEWARM_OCEAN, TextColor.color(0x00CED1)),
            Map.entry(Biome.DEEP_OCEAN, TextColor.color(0x191970)),
            Map.entry(Biome.DESERT, TextColor.color(0xEDC9AF)),
            Map.entry(Biome.DRIPSTONE_CAVES, TextColor.color(0x8B4513)),
            Map.entry(Biome.END_BARRENS, TextColor.color(0xBA55D3)),
            Map.entry(Biome.END_HIGHLANDS, TextColor.color(0xBA55D3)),
            Map.entry(Biome.END_MIDLANDS, TextColor.color(0xBA55D3)),
            Map.entry(Biome.ERODED_BADLANDS, TextColor.color(0xD2691E)),
            Map.entry(Biome.FLOWER_FOREST, TextColor.color(0xFFB6C1)),
            Map.entry(Biome.FOREST, TextColor.color(0x228B22)),
            Map.entry(Biome.FROZEN_OCEAN, TextColor.color(0x87CEFA)),
            Map.entry(Biome.FROZEN_PEAKS, TextColor.color(0xADD8E6)),
            Map.entry(Biome.FROZEN_RIVER, TextColor.color(0xADD8E6)),
            Map.entry(Biome.GROVE, TextColor.color(0xADD8E6)),
            Map.entry(Biome.ICE_SPIKES, TextColor.color(0xADD8E6)),
            Map.entry(Biome.JAGGED_PEAKS, TextColor.color(0xA9A9A9)),
            Map.entry(Biome.JUNGLE, TextColor.color(0x2E8B57)),
            Map.entry(Biome.LUKEWARM_OCEAN, TextColor.color(0x00CED1)),
            Map.entry(Biome.LUSH_CAVES, TextColor.color(0x8B4513)),
            Map.entry(Biome.MANGROVE_SWAMP, TextColor.color(0x556B2F)),
            Map.entry(Biome.MEADOW, TextColor.color(0x7CFC00)),
            Map.entry(Biome.MUSHROOM_FIELDS, TextColor.color(0x9370DB)),
            Map.entry(Biome.NETHER_WASTES, TextColor.color(0xFF4500)),
            Map.entry(Biome.OCEAN, TextColor.color(0x1E90FF)),
            Map.entry(Biome.OLD_GROWTH_BIRCH_FOREST, TextColor.color(0x228B22)),
            Map.entry(Biome.OLD_GROWTH_PINE_TAIGA, TextColor.color(0x228B22)),
            Map.entry(Biome.OLD_GROWTH_SPRUCE_TAIGA, TextColor.color(0x228B22)),
            Map.entry(Biome.PALE_GARDEN, TextColor.color(0xE6E6FA)),
            Map.entry(Biome.PLAINS, TextColor.color(0x7CFC00)),
            Map.entry(Biome.RIVER, TextColor.color(0x1E90FF)),
            Map.entry(Biome.SAVANNA, TextColor.color(0xDAA520)),
            Map.entry(Biome.SAVANNA_PLATEAU, TextColor.color(0xDAA520)),
            Map.entry(Biome.SMALL_END_ISLANDS, TextColor.color(0xBA55D3)),
            Map.entry(Biome.SNOWY_BEACH, TextColor.color(0xF4A460)),
            Map.entry(Biome.SNOWY_PLAINS, TextColor.color(0xADD8E6)),
            Map.entry(Biome.SNOWY_SLOPES, TextColor.color(0xADD8E6)),
            Map.entry(Biome.SNOWY_TAIGA, TextColor.color(0xADD8E6)),
            Map.entry(Biome.SOUL_SAND_VALLEY, TextColor.color(0xFF4500)),
            Map.entry(Biome.SPARSE_JUNGLE, TextColor.color(0x2E8B57)),
            Map.entry(Biome.STONY_PEAKS, TextColor.color(0xA9A9A9)),
            Map.entry(Biome.STONY_SHORE, TextColor.color(0xA9A9A9)),
            Map.entry(Biome.SUNFLOWER_PLAINS, TextColor.color(0x7CFC00)),
            Map.entry(Biome.SWAMP, TextColor.color(0x556B2F)),
            Map.entry(Biome.TAIGA, TextColor.color(0x228B22)),
            Map.entry(Biome.THE_END, TextColor.color(0xBA55D3)),
            Map.entry(Biome.THE_VOID, NamedTextColor.DARK_GRAY),
            Map.entry(Biome.WARM_OCEAN, TextColor.color(0x00FFFF)),
            Map.entry(Biome.WARPED_FOREST, TextColor.color(0xFF4500)),
            Map.entry(Biome.WINDSWEPT_FOREST, TextColor.color(0xA9A9A9)),
            Map.entry(Biome.WINDSWEPT_GRAVELLY_HILLS, TextColor.color(0xA9A9A9)),
            Map.entry(Biome.WINDSWEPT_HILLS, TextColor.color(0xA9A9A9)),
            Map.entry(Biome.WINDSWEPT_SAVANNA, TextColor.color(0xDAA520)),
            Map.entry(Biome.WOODED_BADLANDS, TextColor.color(0xD2691E))
    );

    private TextColor getBiomeColor() {
        return COLORS.getOrDefault(biome, NamedTextColor.YELLOW);
    }

    @Override
    public boolean passes(FishingContext context) {
        return context.getLocation().getBlock().getBiome().equals(biome);
    }

    @Override
    public Component display() {
        return ComponentUtils.merge(ComponentUtils.create(biome.getKey().getKey(), getBiomeColor()), ComponentUtils.create(" biome"));
    }


}
