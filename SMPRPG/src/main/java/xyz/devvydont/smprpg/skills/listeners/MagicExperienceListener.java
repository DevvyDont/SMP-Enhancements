package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;

public class MagicExperienceListener implements Listener {

    final SMPRPG plugin;

    public static final int BREWING_OUTPUT_LEFT = 0;
    public static final int BREWING_OUTPUT_MIDDLE = 1;
    public static final int BREWING_OUTPUT_RIGHT = 2;

    private NamespacedKey experienceStowKey = null;

    public MagicExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private NamespacedKey getExperienceStowKey() {

        if (experienceStowKey == null)
            experienceStowKey = new NamespacedKey(plugin, "stowed_exp");

        return experienceStowKey;
    }

    private boolean isBrewingOutputSlot(int slot) {
        return slot <= BREWING_OUTPUT_RIGHT && slot >= BREWING_OUTPUT_LEFT;
    }

    private int getExperienceForPotionExtract(ItemStack item) {

        if (!(item.getItemMeta() instanceof PotionMeta meta))
            return 0;

        int exp = 0;


        // Find a potion type addition
        switch (item.getType()) {
            case LINGERING_POTION -> exp += 25;
            case SPLASH_POTION -> exp += 10;
        }

        // Find a potion effect addition
        if (meta.getBasePotionType() != null) {
            switch (meta.getBasePotionType()) {
                case WATER:
                    exp += 1;
                    break;

                case MUNDANE, AWKWARD, THICK:
                    exp += 2;
                    break;
            }
        }

        // Consider the base potion effect
        if (meta.getBasePotionType() != null)
            for (PotionEffect effect : meta.getBasePotionType().getPotionEffects())
                exp += ((effect.getAmplifier()+1) * effect.getDuration() / 150);

        // Consider the extra potion effects
        for (PotionEffect effect : meta.getCustomEffects())
            exp += ((effect.getAmplifier()+1) * effect.getDuration() / 250);

        return exp;
    }

    /**
     * Given any item stack, stows experience on it so that when someone picks it up from the brewing stand it will
     * award experience. Also supports stacking experience from previous brews
     *
     * @param item
     */
    private void stowExperience(ItemStack item) {
        item.editMeta(meta -> {
            int oldXp = meta.getPersistentDataContainer().getOrDefault(getExperienceStowKey(), PersistentDataType.INTEGER, 0);
            meta.getPersistentDataContainer().set(getExperienceStowKey(), PersistentDataType.INTEGER, oldXp + getExperienceForPotionExtract(item));
        });
    }

    /**
     * Awards experience to a player from stowed experience on an itemstack and resets the stowed xp to 0
     *
     * @param player
     * @param item
     */
    private void awardExperience(LeveledPlayer player, ItemStack item) {

        // Extract the experience from the item
        int exp = item.getPersistentDataContainer().getOrDefault(getExperienceStowKey(), PersistentDataType.INTEGER, 0);

        // Remove the stored experience on the item
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().remove(getExperienceStowKey());
        });

        // Award!
        if (exp > 0)
            player.getMagicSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.BREW);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent event) {

        if (event.isCancelled())
            return;

        int exp = event.getExpLevelCost() + 10;
        for (CustomEnchantment enchantment : plugin.getEnchantmentService().getCustomEnchantments(event.getEnchantsToAdd()))
            exp += enchantment.getMagicExperience();

        // Multiply it by a fifth of the exp cost
        exp *= (int) (event.getExpLevelCost() / 5.0 + 1.0);

        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getEnchanter());
        player.getMagicSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.ENCHANT);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBrewEvent(BrewEvent event) {

        if (event.isCancelled())
            return;

        for (ItemStack result : event.getResults())
            stowExperience(result);
    }

    @EventHandler
    public void onBrewExtract(InventoryClickEvent event) {

        if (event.isCancelled())
            return;

        if (event.getClickedInventory() == null)
            return;

        // Only listen to brewing inventories
        if (!event.getClickedInventory().getType().equals(InventoryType.BREWING))
            return;

        BrewerInventory brewerInventory = (BrewerInventory) event.getClickedInventory();

        // Only listen to output slot clicks
        if (!isBrewingOutputSlot(event.getSlot()))
            return;

        // Evaluate the item we are extracting, if it isn't there then ignore
        ItemStack extracted = brewerInventory.getItem(event.getSlot());
        if (extracted == null || extracted.getType().equals(Material.AIR))
            return;

        // Determine experience
        LeveledPlayer player = plugin.getEntityService().getPlayerInstance((Player) event.getWhoClicked());
        awardExperience(player, extracted);
    }


}
