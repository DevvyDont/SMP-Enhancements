package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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

import java.util.Collection;
import java.util.List;

public class LeveledElderGuardian extends BossInstance implements Listener {

    public LeveledElderGuardian(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {
        return 30;
    }

    @Override
    public double calculateBaseHealth() {
        return 150_000;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 575;
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
    public String getDefaultName() {
        return "Elder Guardian";
    }

    @Override
    public TextColor getEntityNametagColor() {
        return NamedTextColor.DARK_PURPLE;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_HELMET), 100, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_CHESTPLATE), 100, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_LEGGINGS), 100, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_BOOTS), 100, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_TRIDENT), 100, this)
        );
    }
}
