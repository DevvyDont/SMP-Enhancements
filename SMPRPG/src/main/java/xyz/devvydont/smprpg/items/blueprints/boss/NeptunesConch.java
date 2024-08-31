package xyz.devvydont.smprpg.items.blueprints.boss;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Edible;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.FoodUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NeptunesConch extends CustomItemBlueprint implements Listener, Craftable, Edible, Sellable {

    public NeptunesConch(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> lore = new ArrayList<>(super.getDescriptionComponent(meta));
        lore.add(ComponentUtils.getDefaultText("Consume while in an"));
        lore.add(ComponentUtils.getColoredComponent("Ocean Monument", NamedTextColor.AQUA).append(ComponentUtils.getDefaultText(" while ").append(ComponentUtils.getColoredComponent("underwater", NamedTextColor.BLUE))));
        lore.add(ComponentUtils.getDefaultText("to summon an ").append(ComponentUtils.getColoredComponent("Elder Guardian", NamedTextColor.DARK_PURPLE).append(ComponentUtils.getDefaultText("!"))));
        return lore;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.setCategory(CraftingBookCategory.MISC);
        recipe.shape(
                "csc",
                "sds",
                "csc"
        );
        recipe.setIngredient('c', itemService.getCustomItem(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL));
        recipe.setIngredient('s', itemService.getCustomItem(CustomItemType.ENCHANTED_PRISMARINE_SHARD));
        recipe.setIngredient('d', itemService.getCustomItem(Material.DIAMOND));
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.PRISMARINE_CRYSTALS),
                itemService.getCustomItem(Material.PRISMARINE_SHARD)
        );
    }

    @Override
    public FoodComponent getFoodComponent() {
        FoodComponent food = FoodUtil.getVanillaFoodComponent(Material.BREAD);
        food.setEatSeconds(5);
        food.setNutrition(1);
        food.setSaturation(1);
        food.setCanAlwaysEat(true);
        food.addEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20*5*60, 2, true, true), 1.0f);
        return food;
    }

    private boolean isInMonument(Player player) {
        for (GeneratedStructure structure : player.getChunk().getStructures(Structure.MONUMENT))
            if (structure.getBoundingBox().overlaps(player.getBoundingBox()))
                return true;
        return false;
    }

    @EventHandler
    public void onConsumeConch(PlayerItemConsumeEvent event) {

        ItemStack consumedItem = event.getItem();
        if (!isItemOfType(consumedItem))
            return;

        Component refused = ComponentUtils.getErrorMessage("Neptune refused your call!");

        // A player consumed the conch. Are they underwater?
        if (!event.getPlayer().isUnderWater()) {
            event.getPlayer().sendMessage(refused);
            event.getPlayer().sendMessage(ComponentUtils.getGenericMessage(
                    ComponentUtils.getDefaultText("You must be")
                            .append(ComponentUtils.getColoredComponent(" underwater", NamedTextColor.BLUE))
                            .append(ComponentUtils.getDefaultText("!"))
            ));
            event.setCancelled(true);
            return;
        }

        // Are they in a temple?
        if (!isInMonument(event.getPlayer())) {
            event.getPlayer().sendMessage(refused);
            event.getPlayer().sendMessage(ComponentUtils.getGenericMessage(
                    ComponentUtils.getDefaultText("You must be inside an")
                            .append(ComponentUtils.getColoredComponent(" Ocean Monument", NamedTextColor.AQUA))
                            .append(ComponentUtils.getDefaultText("!"))
            ));
            event.setCancelled(true);
            return;
        }

        // Summon the boss!
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
        String playername = SMPRPG.getInstance().getChatService().getPlayerDisplayname(event.getPlayer());
        Bukkit.broadcast(
                ComponentUtils.getGenericMessage(Component.text(playername)
                        .append(ComponentUtils.getDefaultText(" summoned an ")))
                        .append(ComponentUtils.getColoredComponent("Elder Guardian", NamedTextColor.DARK_PURPLE))
                        .append(ComponentUtils.getDefaultText("!"))
        );
        ElderGuardian guardian = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), ElderGuardian.class, CreatureSpawnEvent.SpawnReason.NATURAL);
        guardian.getWorld().createExplosion(guardian, 2.0f, false, false);
    }

    @Override
    public int getWorth() {
        return 2500;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}
