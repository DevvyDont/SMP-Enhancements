package xyz.devvydont.smprpg.entity.creatures;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class ProtocolSentinel extends CustomEntityInstance<IronGolem> implements Listener {

    public ProtocolSentinel(Entity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        this.updateBaseAttribute(AttributeWrapper.SCALE, 1.25);
    }

    @Override
    public TextColor getNameColor() {
        return NamedTextColor.RED;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.PROTOCOL_HELMET), 200, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PROTOCOL_CHESTPLATE), 200, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PROTOCOL_LEGGINGS), 200, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PROTOCOL_BOOTS), 200, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.DISPLACEMENT_MATRIX), 200, this),
                new QuantityLootDrop(ItemService.generate(Material.IRON_INGOT), 1, 2, this)
        );
    }

    /**
     * These enemies will only target players.
     */
    @EventHandler
    public void onTargetNonPlayer(EntityTargetLivingEntityEvent event) {

        if (!event.getEntity().equals(_entity))
            return;

        if (!(event.getTarget() instanceof Player))
            event.setCancelled(true);
    }
}
