package xyz.devvydont.treasureitems.blueprints.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.util.Vector;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.ComponentUtils;
import xyz.devvydont.treasureitems.util.RandomUtil;

import java.util.List;

public class GrapplingHookBlueprint extends CustomItemBlueprint {

    public static final int COOLDOWN = 2;

    public static final float VELOCITY_DAMPENING_FACTOR = .3f;

    public static final float MAX_HORIZONTAL_VELOCITY = 4f;
    public static final float MIN_VERTICAL_VELOCITY = 1f;
    public static final float MAX_VERTICAL_VELOCITY = 2f;

    public static final int MIN_FALL_DAMAGE_REDUCTION = 25;
    public static final int MAX_FALL_DAMAGE_REDUCTION = 95;

    public static final int MIN_SAFE_FALL = 3;
    public static final int MAX_SAFE_FALL = 20;

    public enum GrapplingState {
        CAST,
        REEL
    }

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        Repairable itemMeta = (Repairable) item.getItemMeta();
        Damageable damageable = (Damageable) itemMeta;

        damageable.setMaxDamage(16000);

        itemMeta.displayName(Component.text("Grappling Hook", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        double fallDamageMultiplier = RandomUtil.randomIntBound(MIN_FALL_DAMAGE_REDUCTION, MAX_FALL_DAMAGE_REDUCTION) / 100.0 * -1;
        double safeFall = RandomUtil.randomIntBound(MIN_SAFE_FALL, MAX_SAFE_FALL);

        itemMeta.addAttributeModifier(Attribute.FALL_DAMAGE_MULTIPLIER, generateAttributeModifier(Attribute.FALL_DAMAGE_MULTIPLIER, EquipmentSlotGroup.HAND, AttributeModifier.Operation.MULTIPLY_SCALAR_1, fallDamageMultiplier));
        itemMeta.addAttributeModifier(Attribute.SAFE_FALL_DISTANCE, generateAttributeModifier(Attribute.SAFE_FALL_DISTANCE, EquipmentSlotGroup.HAND, AttributeModifier.Operation.ADD_NUMBER, safeFall));

        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public int calculateScore(ItemStack itemStack) {
        // Return a number from 0-100 depending on the attributes on the item
        int safeFallFactor = (int) Math.floor((getSafeFall(itemStack) - MIN_SAFE_FALL) / (MAX_SAFE_FALL - MIN_SAFE_FALL) * 100);
        int fallDamageFactor = (int) Math.floor(((getFallDamageMultiplier(itemStack)*-100) - MIN_FALL_DAMAGE_REDUCTION) / (MAX_FALL_DAMAGE_REDUCTION - MIN_FALL_DAMAGE_REDUCTION) * 100);
        return (safeFallFactor + fallDamageFactor) / 2;
    }

    @Override
    public String key() {
        return "grappling_hook";
    }

    public float getSafeFall(ItemStack itemStack) {

        try {
            return (float) itemStack.getItemMeta().getAttributeModifiers(Attribute.SAFE_FALL_DISTANCE).stream().findFirst().get().getAmount();
        } catch (Exception e) {
            return 0;
        }

    }

    public float getFallDamageMultiplier(ItemStack itemStack) {
        try {
            return (float) itemStack.getItemMeta().getAttributeModifiers(Attribute.FALL_DAMAGE_MULTIPLIER).stream().findFirst().get().getAmount();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected List<Component> getExtraLore() {
        return List.of(
                ComponentUtils.getAbilityComponent("Grapple"),
                Component.text("Use to propel yourself", NamedTextColor.GRAY),
                Component.text("when reeling in!", NamedTextColor.GRAY),
                ComponentUtils.getCooldownComponent(COOLDOWN + "s")
        );
    }

    @Override
    public void onFish(PlayerFishEvent event) {
        if (event.getHand() == null)
            return;

        // If the player is casting the hook like normal, allow the event to happen
        if (event.getState().equals(PlayerFishEvent.State.FISHING)) {
            use(event, GrapplingState.CAST);
            return;
        }

        // If the player is reeling in, allow the event to happen
        if (event.getState().equals(PlayerFishEvent.State.REEL_IN) || event.getState().equals(PlayerFishEvent.State.IN_GROUND) || event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
            use(event, GrapplingState.REEL);
            return;
        }

        // Every other state is not allowed
        event.setCancelled(true);
    }

    private void use(PlayerFishEvent event, GrapplingState state) {

        // Handle the case we are sending the bobber out
        if (state.equals(GrapplingState.CAST))
            return;

        // Handle the case where we are reeling
        Vector dir = event.getHook().getLocation().toVector().subtract(event.getPlayer().getLocation().toVector());

        // Dampen the velocity
        dir.multiply(VELOCITY_DAMPENING_FACTOR);

        // Make sure we didn't hit a limit
        dir.setX(Math.min(Math.max(-MAX_HORIZONTAL_VELOCITY, dir.getX()), MAX_HORIZONTAL_VELOCITY));
        dir.setY(Math.min(Math.max(MIN_VERTICAL_VELOCITY, dir.getY()), MAX_VERTICAL_VELOCITY));
        dir.setZ(Math.min(Math.max(-MAX_HORIZONTAL_VELOCITY, dir.getZ()), MAX_HORIZONTAL_VELOCITY));

        event.getPlayer().setVelocity(dir);
        event.getPlayer().setFallDistance(-30);
        event.getPlayer().setCooldown(Material.FISHING_ROD, COOLDOWN*20);

        if (event.getHand() == null)
            return;
        ItemStack rod = event.getPlayer().getInventory().getItem(event.getHand());
        rod.damage(1, event.getPlayer());
    }
}
