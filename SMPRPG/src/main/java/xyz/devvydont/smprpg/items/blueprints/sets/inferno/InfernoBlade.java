package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;
import xyz.devvydont.smprpg.gui.spawner.InterfaceSpawnerMainMenu;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InfernoBlade extends CustomAttributeItem implements Listener {

    public static final int COOLDOWN = 3;
    public static final int COST = 150;
    public static final int DAMAGE = 15_000;

    public InfernoBlade(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 400),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.6),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .25)
        );
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> lines = new ArrayList<>(super.getDescriptionComponent(meta));
        lines.add(Component.empty());
        lines.add(AbilityUtil.getAbilityComponent("Hot Shot (Right Click)").append(AbilityUtil.getCooldownComponent(" " + COOLDOWN + "s")));
        lines.add(ComponentUtil.getDefaultText("Shoot a ").append(ComponentUtil.getColoredComponent("fireball", NamedTextColor.GOLD)).append(ComponentUtil.getDefaultText(" in the direction")));
        lines.add(ComponentUtil.getDefaultText("you are looking that deals ").append(ComponentUtil.getColoredComponent(DAMAGE + "", NamedTextColor.RED)).append(ComponentUtil.getDefaultText(" damage")));
        lines.add(AbilityUtil.getHealthCostComponent(COST));
        return lines;
    }

    @Override
    public int getPowerRating() {
        return InfernoArmorSet.POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.WEAPON;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    public boolean canUse(Player player) {
        return player.getHealth() > COST;
    }

    public void summonFireball(Player player) {
        Projectile projectile = player.launchProjectile(Fireball.class, player.getLocation().getDirection().normalize().multiply(2));
        SMPRPG.getInstance().getEntityDamageCalculatorService().setBaseProjectileDamage(projectile, DAMAGE);
        player.damage(COST, DamageSource.builder(DamageType.MAGIC).withCausingEntity(player).withDirectEntity(player).build());
        player.setCooldown(getCustomItemType().material, COOLDOWN*20);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractWhileHoldingBlade(PlayerInteractEvent event) {

        // Is this the blade?
        ItemStack item = event.getItem();
        if (!isItemOfType(item))
            return;

        // Are we right clicking?
        if (!event.getAction().isRightClick())
            return;

        // Are we right clicking a block?
        if (event.getClickedBlock() != null)
            return;

        // Are we on cooldown?
        if (event.getPlayer().getCooldown(getCustomItemType().material) > 0)
            return;

        event.setCancelled(true);

        // Do we have the health to use this?
        if (!canUse(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatUtil.getErrorMessage("You do not have enough life in you to use this item!"));
            return;
        }

        summonFireball(event.getPlayer());
    }
}
