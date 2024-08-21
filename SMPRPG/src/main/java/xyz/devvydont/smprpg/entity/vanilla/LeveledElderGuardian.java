package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledElderGuardian extends BossInstance {

    public LeveledElderGuardian(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {
        return 30;
    }

    @Override
    public double calculateBaseHealth() {
        return 180_000;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 800;
    }

    @Override
    public String getClassKey() {
        return VanillaEntity.VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.ELDER_GUARDIAN;
    }

    @Override
    public String getEntityName() {
        return "Elder Guardian";
    }

    @Override
    public TextColor determineNametagColor() {
        return NamedTextColor.DARK_PURPLE;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new QuantityLootDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.SOGGY_LETTUCE), 1, 3, this),

                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.PRISMARINE_SHARD), 1, 2, this),
                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.PRISMARINE_CRYSTALS), 1, 2, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.DIAMOND), 5, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_PRISMARINE_SHARD), 7, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_PRISMARINE_CRYSTAL), 7, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_DIAMOND), 200, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_PRISMARINE_SHARD), 95, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL), 95, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_DIAMOND_BLOCK), 10_000, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_HELMET), 110, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_CHESTPLATE), 110, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_LEGGINGS), 110, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_BOOTS), 110, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_TRIDENT), 120, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_BOW), 120, this),

                // Pity drops
                // Crafts into Jupiter crystal, need 8 to get 1 crystal
                // Armor components, 24 required for full set from pity alone.
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.JUPITERS_ARTIFACT), 30, this),
                new QuantityLootDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.JUPITER_CRYSTAL), 1, 1, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PLUTO_FRAGMENT), 3, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PLUTOS_ARTIFACT), 100, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNES_CONCH), 25, this)
        );
    }
}
