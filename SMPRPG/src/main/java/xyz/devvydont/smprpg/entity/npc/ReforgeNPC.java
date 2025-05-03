package xyz.devvydont.smprpg.entity.npc;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.NPCEntity;
import xyz.devvydont.smprpg.gui.items.MenuReforge;

public class ReforgeNPC extends NPCEntity<Villager> {

    public ReforgeNPC(Entity entity, CustomEntityType type) {
        super(entity, type);
    }

    public ReforgeNPC(Villager entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();
        _entity.setProfession(Villager.Profession.TOOLSMITH);
    }

    @Override
    public void handleInteract(Player player) {
        new MenuReforge(player).openMenu();
        _entity.getWorld().playSound(_entity.getLocation(), Sound.ENTITY_VILLAGER_YES, 0.5F, 0.75F);
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
