package xyz.devvydont.smprpg.entity.bosses;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomBossInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class BlazeBoss extends CustomBossInstance {

    public BlazeBoss(SMPRPG plugin, Entity entity, CustomEntityType type) {
        super(plugin, entity, type);
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        updateBaseAttribute(Attribute.GENERIC_SCALE, 9);
        updateBaseAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(

                // Common drops
                new QuantityLootDrop(ItemService.getItem(CustomItemType.CHILI_PEPPER), 1, 3, this),
                new QuantityLootDrop(ItemService.getItem(Material.BLAZE_ROD), 2, 6, this),
                new QuantityLootDrop(ItemService.getItem(Material.IRON_INGOT), 2, 6, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.PREMIUM_BLAZE_ROD), 2, this),

                // Pity system drops
                new ChancedItemDrop(ItemService.getItem(CustomItemType.ENCHANTED_IRON), 20, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.ENCHANTED_IRON_BLOCK), 90, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.IRON_SINGULARITY), 2000, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.ENCHANTED_BLAZE_ROD), 25, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.SCORCHING_STRING), 90, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.BOILING_INGOT), 20, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.INFERNO_REMNANT), 40, this),
                new QuantityLootDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.INFERNO_RESIDUE), 1, 2, this),

                // Gear drops
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_HELMET), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_CHESTPLATE), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_LEGGINGS), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_BOOTS), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_SABER), 175, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_SHORTBOW), 175, this),

                // Chance to summon again
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_ARROW), 50, this)
        );
    }
}
