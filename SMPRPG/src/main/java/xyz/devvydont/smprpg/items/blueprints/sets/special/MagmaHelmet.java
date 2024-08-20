package xyz.devvydont.smprpg.items.blueprints.sets.special;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomTexturedAttributeHelmetBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MagmaHelmet extends CustomTexturedAttributeHelmetBlueprint implements Craftable, ToolBreakable, Listener {

    public MagmaHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 50)
        );
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> components = new ArrayList<>(super.getDescriptionComponent(meta));
        components.add(Component.empty());
        components.add(AbilityUtil.getAbilityComponent("Heat Proof (Passive)"));
        components.add(ComponentUtil.getDefaultText("Grants immunity to ").append(ComponentUtil.getColoredComponent(Symbols.FIRE + "fire based", NamedTextColor.RED)).append(ComponentUtil.getDefaultText(" damage")));
        return components;
    }

    @Override
    public int getPowerRating() {
        return 10;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, itemService.getCustomItem(CustomItemType.PREMIUM_MAGMA_CREAM), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.MAGMA_CREAM)
        );
    }

    @Override
    public int getMaxDurability() {
        return 2500;
    }

    private boolean isFireCause(EntityDamageEvent.DamageCause cause) {
        return switch (cause) {
            case FIRE, LAVA, FIRE_TICK, CAMPFIRE, MELTING, LIGHTNING, HOT_FLOOR -> true;
            default -> false;
        };
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHeatDamageWithHelmet(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        if (!isFireCause(event.getCause()))
            return;

        ItemStack helmet = living.getEquipment().getHelmet();
        if (!isItemOfType(helmet))
            return;

        event.setCancelled(true);
    }
}
