package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class InterfaceReforge extends PrivateInterface {

    public static final int INPUT_SLOT = 13;
    public static final int BUTTON_SLOT = 31;

    public InterfaceReforge(SMPRPG plugin, Player owner) {
        super(plugin, owner);
    }

    public int getReforgeCost(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> 250;
            case UNCOMMON -> 500;
            case RARE -> 1000;
            case EPIC -> 2500;
            case LEGENDARY -> 5000;
            case MYTHIC -> 10000;
            case DIVINE -> 25000;
            default -> 50000;
        };
    }

    public int getBalance() {
        return SMPRPG.getInstance().getEconomyService().getMoney(owner);
    }

    public void spendMoney(int cost) {
        SMPRPG.getInstance().getEconomyService().takeMoney(owner, cost);
        Component taken = ComponentUtils.create(EconomyService.formatMoney(cost), NamedTextColor.GOLD);
        Component bal = ComponentUtils.create(EconomyService.formatMoney(getBalance()), NamedTextColor.GOLD);
        owner.sendMessage(ComponentUtils.alert(
                taken.append(ComponentUtils.create(" has been taken from your account. Your balance is now ")).append(bal)
        ));
    }

    public ItemStack getAnvilButton() {

        ItemStack input = getItem(INPUT_SLOT);
        ItemStack anvil = InterfaceUtil.getNamedItem(Material.ANVIL, ComponentUtils.create("Roll Random Reforge!", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();

        // Has nothing been input yet?
        if (input == null || input.getType().equals(Material.AIR)) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("Input an item to reforge!", NamedTextColor.WHITE));
            anvil.editMeta(meta -> {
                meta.lore(ComponentUtils.cleanItalics(lore));
            });
            return anvil;
        }

        SMPItemBlueprint blueprint = plugin.getItemService().getBlueprint(input);

        // Is this item not able to receive a reforge?
        if (getRandomReforge(blueprint.getItemClassification(), blueprint.getReforgeType(input.getItemMeta())).getType().equals(ReforgeType.ERROR)) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("This item cannot be reforged!", NamedTextColor.RED));
            anvil.editMeta(meta -> {
                meta.lore(ComponentUtils.cleanItalics(lore));
            });
            return anvil.withType(Material.BARRIER);
        }

        // Valid item
        ItemRarity rarity = blueprint.getRarity(input);
        int cost = getReforgeCost(rarity);
        int bal = getBalance();
        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.create("Click to reforge this item!", NamedTextColor.GRAY));
        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.create("Cost: ").append(ComponentUtils.create(EconomyService.formatMoney(cost), NamedTextColor.GOLD)));
        lore.add(ComponentUtils.create("Your balance: ").append(ComponentUtils.create(EconomyService.formatMoney(bal), NamedTextColor.GOLD)));
        if (cost > bal)
            lore.add(ComponentUtils.create("Insufficient funds! You cannot afford this!", NamedTextColor.RED));
        if (blueprint.isReforged(input.getItemMeta())) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("NOTE: Previous reforge will be overwritten!", NamedTextColor.RED));
        }
        anvil.editMeta(meta -> {
            meta.lore(ComponentUtils.cleanItalics(lore));
            if (bal >= cost)
                meta.setEnchantmentGlintOverride(true);
        });

        return anvil;
    }

    @Override
    public int getNumRows() {
        return 5;
    }

    /**
     * Randomly rolls a reforge.
     *
     * @param classification The classification of the item that is being reforged.
     * @param exclude The reforge type to exclude when rolling a reforge. Can be null to consider all available reforges.
     * @return An randomly selected instance of a registered ReforgeBase singleton that is compatible with the classification.
     */
    public @NotNull ReforgeBase getRandomReforge(ItemClassification classification, @Nullable ReforgeType exclude) {

        // Construct a list of reforges to choose from by looping through all reforges and analyzing its compatibility.
        List<ReforgeBase> choices = new ArrayList<>();
        for (ReforgeType type : ReforgeType.values()) {

            // Do we want to exclude this reforge?
            if (type.equals(exclude))
                continue;

            // Is this reforge allowed to be rolled in a reforge station?
            if (!type.isRollable())
                continue;

            // Is this reforge allowed for this item type?
            if (!type.isAllowed(classification))
                continue;

            // Valid!
            choices.add(SMPRPG.getInstance().getItemService().getReforge(type));
        }

        // If we found no valid reforges, default to the error reforge type. Error reforge type should be handled by caller
        if (choices.isEmpty())
            return SMPRPG.getInstance().getItemService().getReforge(ReforgeType.ERROR);

        // Return a random choice
        return choices.get((int) (Math.random()*choices.size()));
    }

    public void reforge() {

        // Check if we have an item in the input
        ItemStack item = getItem(INPUT_SLOT);
        if (item == null)
            return;

        // Check if this item is able to store attributes. Reforges can't add attributes to attributeless items!
        SMPItemBlueprint blueprint = plugin.getItemService().getBlueprint(item);
        if (!(blueprint instanceof Attributeable attributeable))
            return;

        // Analyze the current reforge on the gear and determine if we can even roll another reforge without erroring
        ReforgeType currentReforgeType = blueprint.getReforgeType(item.getItemMeta());
        ReforgeBase newReforge = getRandomReforge(blueprint.getItemClassification(), currentReforgeType);
        boolean success = !newReforge.getType().equals(ReforgeType.ERROR);

        // Determine if we can afford this reforge
        int cost = getReforgeCost(blueprint.getRarity(item));
        if (getBalance() < cost)
            success = false;

        // Apply reforge and take their money if we had no issues
        if (success) {
            newReforge.apply(item);
            spendMoney(cost);
            LeveledPlayer player = SMPRPG.getInstance().getEntityService().getPlayerInstance(owner);
            player.getMagicSkill().addExperience((blueprint.getRarity(item).ordinal()+1) * attributeable.getPowerRating() / 10);
        }

        Location soundOrigin = owner.getLocation().add(owner.getLocation().getDirection().normalize().multiply(2));
        owner.getWorld().playSound(soundOrigin, success ? Sound.BLOCK_ANVIL_USE : Sound.ENTITY_VILLAGER_NO, .5f, .75f);
        blueprint.updateMeta(item);
    }

    @Override
    public void initializeDefaultState() {
        super.initializeDefaultState();
        fill(InterfaceUtil.getInterfaceBorder());
        clearSlot(INPUT_SLOT);
        setSlot(BUTTON_SLOT, getAnvilButton());
        inventoryView.setTitle("Tool Reforging");
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        super.handleInventoryClick(event);

        // Treat click events as a whitelist style
        event.setCancelled(true);

        if (event.getClickedInventory() == null)
            return;

        // Update the anvil button on the next tick to react to the state of the GUI
        Bukkit.getScheduler().runTaskLater(SMPRPG.getInstance(), () -> setSlot(BUTTON_SLOT, getAnvilButton()), 0L);

        // If we are clicking in the player inventory allow it to happen
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            event.setCancelled(false);
            return;
        }

        // If we are clicking in the input slot allow it to happen
        if (event.getClickedInventory().equals(inventory) && event.getSlot() == INPUT_SLOT) {
            event.setCancelled(false);
            return;
        }

        // If we left clicked the reforge button then reforge
        if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == BUTTON_SLOT) {
            reforge();
            return;
        }
    }

    @Override
    public void handleInventoryClose(InventoryCloseEvent event) {
        super.handleInventoryClose(event);

        transferItemToPlayer(INPUT_SLOT);
    }
}
