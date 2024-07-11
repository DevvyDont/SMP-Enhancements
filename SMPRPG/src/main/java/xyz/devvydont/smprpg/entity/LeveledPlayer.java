package xyz.devvydont.smprpg.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.util.formatting.Symbols;

public class LeveledPlayer extends LeveledEntity {


    private TextDisplay info;

    public LeveledPlayer(SMPRPG plugin, LivingEntity entity) {
        super(plugin, entity);
    }

    @Override
    public String getClassKey() {
        return "player";
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public String getDefaultName() {
        return entity.getName();
    }

    @Override
    public int getDefaultLevel() {
        return getLevel();
    }

    @Override
    public int getLevel() {
        // Check out the gear that is on this player and figure out an average power level

        Player p = (Player) entity;

        double averageGearLevel = 0;
        int gearConsiderations = 0;
        ItemStack[] gear = {p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots(), p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand()};
        for (ItemStack item : gear) {

            if (item == null || item.getType().equals(Material.AIR))
                continue;

            SMPItemBlueprint blueprint = plugin.getItemService().getBlueprint(item);
            if (!(blueprint instanceof Attributeable attributeable))
                continue;

            averageGearLevel += attributeable.getPowerRating();
            gearConsiderations++;
        }

        // If we are not wearing a full set of armor, pretend we are (missing armor = worse power rating)
        gearConsiderations = Math.max(4, gearConsiderations);
        averageGearLevel = averageGearLevel / gearConsiderations;

        return Math.max(1, (int)averageGearLevel);
    }

    /**
     * The amount of base health a player has
     *
     * @return
     */
    public int getBaseHealth() {
        return 100;
    }

    /**
     * The amount of hearts to render for the player, certain aspects can increase this
     *
     * @return
     */
    public int getHealthScale() {
        return 20;
    }

    @Override
    public void updateAttributes() {

        // Update max health to 100 while maintaining their current HP
        double percent = getHealthPercentage();
        updateBaseAttribute(Attribute.GENERIC_MAX_HEALTH, getBaseHealth());
        setHealthPercentage(percent);

        // Make sure we aren't overloading their UI with hearts
        getPlayer().setHealthScale(getHealthScale());
        getPlayer().setHealthScaled(true);
    }

    @Override
    public void updateNametag() {

        // Players will use the text display thingies
        if (!hasSecondaryNametag())
            spawnSecondaryNametag();

        int hp;
        if (getHp() <= 0)
            hp = 0;
        else if (getHp() > 0 && getHp() < 1)
            hp = 1;
        else
            hp = (int)getHp();
        int maxHp = (int)Math.round(getMaxHp());

        TextColor hpTextColor = getChatColorFromHealth(getHp(), maxHp);
        info.text(Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text(Symbols.POWER + getLevel()).color(NamedTextColor.YELLOW))
                .append(Component.text("]").color(NamedTextColor.GRAY))
                .append(Component.text(" " + hp).color(hpTextColor))
                .append(Component.text("/").color(NamedTextColor.GRAY))
                .append(Component.text(maxHp).color(NamedTextColor.GREEN))
                .append(Component.text(Symbols.HEART).color(NamedTextColor.DARK_RED)));
    }

    @Override
    public void cleanup() {
        super.cleanup();
        if (hasSecondaryNametag())
            killSecondaryNametag();
    }

    @Override
    public void setLevel(int level) {
        // Does nothing
    }

    @Override
    public void resetLevel() {
        // Does nothing
    }

    @Override
    public void dimNametag() {
        // Does nothing
    }

    @Override
    public void brightenNametag() {
        // does nothing
    }

    public TextDisplay spawnSecondaryNametag() {

        // Kill the old one if it exists
        killSecondaryNametag();

        // Spawn a new TextDisplay and mount it on the player
        info = entity.getWorld().spawn(entity.getLocation(), TextDisplay.class, e -> {
            e.setSeeThrough(true);
            e.setPersistent(false);
            e.setBillboard(Display.Billboard.CENTER);
        });
        entity.addPassenger(info);
        return info;
    }

    public void killSecondaryNametag() {
        if (!hasSecondaryNametag()) {
            info = null;
            return;
        }

        info.remove();
        info = null;
    }

    public boolean hasSecondaryNametag() {
        return info != null && info.isValid();
    }

    public Player getPlayer() {
        return (Player) entity;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 1.0;
    }
}
