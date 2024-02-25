package me.devvy.advancementcompetition;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomItems implements Listener {

    private static NamespacedKey CUSTOM_ITEM_KEY = new NamespacedKey(AdvancementCompetition.getInstance(), "custom-item-key");

    private static String MAKESHIFT_DRAGON_EGG_KEY = "makeshift-dragon-egg";
    private static String DRAGON_SCALE_KEY = "dragon-scale";
    private static NamespacedKey MAKESHIFT_DRAGON_EGG_RECIPE = new NamespacedKey(AdvancementCompetition.getInstance(), "makeshift-dragon-egg-recipe");

    public static boolean isCustomItemOfKey(ItemStack itemStack, String key) {
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(CUSTOM_ITEM_KEY, PersistentDataType.STRING, "").equalsIgnoreCase(key);
    }

    public static boolean isCustomItem(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().has(CUSTOM_ITEM_KEY);
    }

    public static ItemStack getDragonEgg() {

        ItemStack egg = new ItemStack(Material.DRAGON_EGG);
        ItemMeta meta = egg.getItemMeta();
        meta.displayName(Component.text("Unstable Dragon Egg", NamedTextColor.LIGHT_PURPLE));
        meta.getPersistentDataContainer().set(CUSTOM_ITEM_KEY, PersistentDataType.STRING, MAKESHIFT_DRAGON_EGG_KEY);

        meta.lore(Arrays.asList(
                Component.empty(),
                Component.text("Not quite like the real thing... But it seems to get the job done", AdvancementCompetition.GRAY),
                Component.text("It's probably not a good idea to place it", NamedTextColor.RED, TextDecoration.BOLD),
                Component.empty(),
                Component.text("Custom item used to achieve 'The Next Generation'", AdvancementCompetition.DARK_GRAY, TextDecoration.ITALIC)
        ));
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        egg.setItemMeta(meta);

        return egg;
    }

    public static ItemStack getDragonScale() {
        ItemStack scale = new ItemStack(Material.DISC_FRAGMENT_5);
        ItemMeta meta = scale.getItemMeta();
        meta.displayName(Component.text("Dragon Scale", NamedTextColor.LIGHT_PURPLE));
        meta.getPersistentDataContainer().set(CUSTOM_ITEM_KEY, PersistentDataType.STRING, DRAGON_SCALE_KEY);

        meta.lore(Arrays.asList(
                Component.empty(),
                Component.text("Used to craft a ", AdvancementCompetition.GRAY).append(Component.text("Unstable Dragon Egg", NamedTextColor.LIGHT_PURPLE)),
                Component.text("Check your recipe book to find out how to craft it!", NamedTextColor.GRAY),
                Component.empty(),
                Component.text("Custom item used to achieve 'The Next Generation'", AdvancementCompetition.DARK_GRAY, TextDecoration.ITALIC)
        ));
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ITEM_SPECIFICS);
        scale.setItemMeta(meta);

        return scale;
    }

    public static void registerRecipes() {

        // Dragon egg recipe
        ShapedRecipe dragonEggRecipe = new ShapedRecipe(MAKESHIFT_DRAGON_EGG_RECIPE, getDragonEgg());
        dragonEggRecipe.shape(
                "BBB",
                "SCS",
                "SSS"
        );
        dragonEggRecipe.setIngredient('B', Material.DRAGON_BREATH);
        dragonEggRecipe.setIngredient('S', getDragonScale());
        dragonEggRecipe.setIngredient('C', Material.END_CRYSTAL);
        dragonEggRecipe.setCategory(CraftingBookCategory.MISC);
        AdvancementCompetition.getInstance().getServer().addRecipe(dragonEggRecipe);

    }

    public void alertScaleDrop(Location location) {

        for (Player p : location.getNearbyEntitiesByType(Player.class, 300))
            p.sendMessage(
                    AdvancementCompetition.prefix
                            .append(Component.text("A ", AdvancementCompetition.GRAY))
                            .append(Component.text("Dragon Scale ", NamedTextColor.LIGHT_PURPLE))
                            .append(Component.text("dropped ", AdvancementCompetition.GRAY))
                            .append(Component.text((int)(location.distance(p.getLocation())) + "m ", AdvancementCompetition.GREEN))
                            .append(Component.text("away from you!", AdvancementCompetition.GRAY))
            );


    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {

        if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {

            int drops = (int) (Math.random()*3+1);

            for (int i = 0; i < drops; i++) {

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ItemStack scaleIS = getDragonScale();
                        Item drop = event.getEntity().getLocation().getWorld().dropItemNaturally(event.getEntity().getLocation(), scaleIS);
                        drop.setGlowing(true);
                        drop.customName(scaleIS.displayName());
                        drop.setCustomNameVisible(true);
                        spawnFireworksInstantly(drop.getLocation(), Color.PURPLE);
                        alertScaleDrop(drop.getLocation());
                    }
                }.runTaskLater(AdvancementCompetition.getInstance(), (int) (Math.random() * 7 * 20));

            }

        }

    }

    @EventHandler
    public void onPlaceMakeshiftDragonEgg(PlayerInteractEvent event) {

        // Only listen to right clicks that are places
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            // Forgive me for I have sinned by doing the cascade of if statements
            if (event.getItem() != null && event.getItem().hasItemMeta()) {

                if (isCustomItemOfKey(event.getItem(), MAKESHIFT_DRAGON_EGG_KEY)) {
                    event.setCancelled(true);
                    if (event.getInteractionPoint() != null) {
                        for (int i = 0; i < 3; i++)
                            spawnFireworksInstantly(event.getInteractionPoint(), Color.PURPLE);
                        event.getInteractionPoint().createExplosion(2.5f, false, false);
                        event.getItem().setAmount(event.getItem().getAmount()-1);
                        event.getPlayer().getWorld().playSound(event.getInteractionPoint(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, .5f, .3f);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickupItem(PlayerAttemptPickupItemEvent event) {

        if (isCustomItemOfKey(event.getItem().getItemStack(), DRAGON_SCALE_KEY))
            event.getPlayer().discoverRecipe(MAKESHIFT_DRAGON_EGG_RECIPE);

    }

    @EventHandler
    public void onAttemptCraftVanillaItemWithCustom(PrepareItemCraftEvent event) {

        if (event.getRecipe() == null)
            return;

        if (!event.getRecipe().getResult().hasItemMeta())
            return;

        boolean resultIsCustom = isCustomItem(event.getRecipe().getResult());
        boolean containsCustomItems = false;
        for (ItemStack item : event.getInventory().getContents())
            if (item != null && item.hasItemMeta() && isCustomItem(item))
                containsCustomItems = true;

        // Custom items cannot make vanilla recipes
        if (!resultIsCustom && containsCustomItems)
            event.getInventory().setResult(new ItemStack(Material.AIR));

    }

    public static void spawnFireworksInstantly(Location location, Color color) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, .5, 0), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Builder effectBuilder = FireworkEffect.builder();
        effectBuilder.with(FireworkEffect.Type.BALL_LARGE);
        effectBuilder.withColor(color);
        meta.addEffect(effectBuilder.build());
        firework.setFireworkMeta(meta);
        firework.detonate();
    }

}
