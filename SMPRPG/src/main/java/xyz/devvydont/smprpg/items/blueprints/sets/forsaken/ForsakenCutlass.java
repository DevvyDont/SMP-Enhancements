package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ForsakenCutlass extends CustomAttributeItem implements Listener, IHeaderDescribable, ICraftable, IBreakableEquipment {

    public static final int WITHER_APPLY_CHANCE = 20;
    public static final int WITHER_APPLY_SECONDS = 10;

    public ForsakenCutlass(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        List<Component> components = new ArrayList<>();
        components.add(ComponentUtils.EMPTY);
        components.add(AbilityUtil.getAbilityComponent("Necrotic (Passive)"));
        components.add(ComponentUtils.create("Attacks have a ").append(ComponentUtils.create(WITHER_APPLY_CHANCE + "%", NamedTextColor.GREEN)).append(ComponentUtils.create(" chance to")));
        components.add(ComponentUtils.create("apply the ").append(ComponentUtils.create("withered", NamedTextColor.DARK_RED)).append(ComponentUtils.create(" effect for ").append(ComponentUtils.create(WITHER_APPLY_SECONDS + "s", NamedTextColor.GREEN))));

        return components;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(Material.NETHERITE_SWORD)-10),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.3)
        );
    }

    @Override
    public int getPowerRating() {
        return ForsakenArmorSet.POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SWORD;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }


    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(" s ", " s ", " r ");
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        recipe.setIngredient('s', ItemService.generate(ForsakenArmorSet.CRAFTING_COMPONENT));
        recipe.setIngredient('r', ItemService.generate(CustomItemType.OBSIDIAN_TOOL_ROD));
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                ItemService.generate(Material.NETHER_STAR)
        );
    }

    @Override
    public int getMaxDurability() {
        return ForsakenArmorSet.DURABILITY;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void __onAttackWithCutlass(CustomEntityDamageByEntityEvent event) {

        // Can the attacked entity have potion effects?
        if (!(event.getDamaged() instanceof LivingEntity attacked))
            return;

        // Did the attacker use the cutlass?
        if (!(event.getDealer() instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        if (!isItemOfType(living.getEquipment().getItemInMainHand()))
            return;

        // The cutlass was used. Apply withering if we roll for it and it is not already applied
        if (attacked.hasPotionEffect(PotionEffectType.WITHER))
            return;

        // RNG roll
        if (Math.random() * 100 > WITHER_APPLY_CHANCE)
            return;

        // Apply!
        attacked.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,  WITHER_APPLY_SECONDS*20, 0, true, true));
        attacked.getWorld().playSound(attacked.getLocation(), Sound.ENTITY_WITHER_HURT, .5f, 1.5f);
        attacked.getWorld().spawnParticle(Particle.ASH, attacked.getEyeLocation(), 5);
    }

}
