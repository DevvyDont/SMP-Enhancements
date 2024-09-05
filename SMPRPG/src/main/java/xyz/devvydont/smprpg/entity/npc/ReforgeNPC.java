package xyz.devvydont.smprpg.entity.npc;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.NPCEntity;
import xyz.devvydont.smprpg.gui.MenuReforge;

public class ReforgeNPC extends NPCEntity {

    public ReforgeNPC(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    public Villager getVillager() {
        return (Villager) entity;
    }

    @Override
    public void setup() {
        super.setup();
        getVillager().setProfession(Villager.Profession.TOOLSMITH);
    }

    @Override
    public void handleInteract(Player player) {
        new MenuReforge(player).openMenu();
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_VILLAGER_YES, 0.5F, 0.75F);
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public String getEntityName() {
        return "Gear Blacksmith";
    }

}
