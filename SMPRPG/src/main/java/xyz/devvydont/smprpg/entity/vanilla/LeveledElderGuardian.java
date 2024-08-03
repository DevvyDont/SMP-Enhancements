package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledElderGuardian extends VanillaEntity implements Listener {

    public LeveledElderGuardian(SMPRPG plugin, LivingEntity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {
        return 30;
    }

    @Override
    public double calculateBaseHealthMultiplier() {
        return 1.0;
    }

    @Override
    public double calculateBaseHealth() {
        return 75_000;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 575;
    }

    @Override
    public double calculateBaseDamageMultiplier() {
        return 1.0;
    }

    @Override
    public TextColor getEntityNametagColor() {
        return NamedTextColor.DARK_PURPLE;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_HELMET), 120, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_CHESTPLATE), 120, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_LEGGINGS), 120, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_BOOTS), 120, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_TRIDENT), 120, this)
        );
    }
}
