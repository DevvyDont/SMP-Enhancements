package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledDragon extends BossInstance {

    private boolean wasSummoned = false;

    public LeveledDragon(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public @Nullable BossBar createBossBar() {
        return null;
    }

    @Override
    public int getDefaultLevel() {
        return wasSummoned ? 50 : 40;
    }


    @Override
    public double calculateBaseAttackDamage() {
        return wasSummoned ? 1250 : 500;
    }

    @Override
    public String getClassKey() {
        return VanillaEntity.VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public String getEntityName() {
        return "Ender Dragon";
    }

    public double calculateBaseHealth() {
        return 3_000_000;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ELDERFLAME_HELMET), 20, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ELDERFLAME_CHESTPLATE), 20, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ELDERFLAME_LEGGINGS), 20, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ELDERFLAME_BOOTS), 20, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.DRACONIC_CRYSTAL), 5, this),
                new QuantityLootDrop(plugin.getItemService().getCustomItem(CustomItemType.DRAGON_SCALES), 2, 7, this)
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDragonBreathDamage(EntityDamageByEntityEvent e) {

        // Dragon breath damage is treated as a normal attack
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            return;

        // Check for area of effect damage
        if (!(e.getDamager() instanceof AreaEffectCloud cloud))
            return;

        // Check if the owner of the cloud matches this dragon
        if (!entity.getUniqueId().equals(cloud.getOwnerUniqueId()))
            return;

        // Set the damage
        e.setDamage(getBaseAttackDamage());
    }

}
