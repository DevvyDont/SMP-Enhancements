package xyz.devvydont.treasureitems.blueprints.tools;

import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.PotentialEnchantmentWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GigadrillPickaxeBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Gigadrill");

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.EFFICIENCY, new PotentialEnchantmentWrapper(Enchantment.EFFICIENCY, 6, 10, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.SILK_TOUCH, new PotentialEnchantmentWrapper(Enchantment.SILK_TOUCH, 1, 1, .5f));
        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 150, 250, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

        return allowedEnchants;
    }

    @Override
    public void randomlyEnchant(ItemStack item, float luckBoost) {
        super.randomlyEnchant(item, luckBoost);
        roundUnbreaking(item);
    }

    public void roundUnbreaking(ItemStack item) {
        // Make sure the unbreakable level is a multiple of 10
        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);
        if (unbreakingLevel == 0)
            return;

        int newUnbreakingLevel = Math.round((unbreakingLevel+4.99f) / 10f) * 10;
        item.removeEnchantment(Enchantment.UNBREAKING);
        item.addUnsafeEnchantment(Enchantment.UNBREAKING, newUnbreakingLevel);
    }

    @Override
    protected List<String> getExtraLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Cannot be " + ChatColor.RED + "upgraded" + ChatColor.GRAY + "!"
        );
    }

    @Override
    public void fix(ItemStack itemStack) {

        // If unbreaking hasn't been put on it yet, put it on
        if (!itemStack.containsEnchantment(Enchantment.UNBREAKING))
            itemStack.addUnsafeEnchantment(Enchantment.UNBREAKING, 250);

        super.fix(itemStack);
    }

    @Override
    public String key() {
        return "gigadrill_pickaxe";
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

        // Hack to spoof 4x durability
        // If we break a block with this pick, we roll a 25% chance to treat this event like normal
        if (Math.random() < .25f)
            return;

        // Otherwise don't let durability damage happen
        event.setCancelled(true);
        event.getBlock().breakNaturally(event.getPlayer().getItemInUse().clone());
    }
}
