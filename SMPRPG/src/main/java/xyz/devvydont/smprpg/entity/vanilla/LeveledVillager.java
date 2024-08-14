package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

public class LeveledVillager extends VanillaEntity {

    public LeveledVillager(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        updateBaseAttribute(AttributeWrapper.DEFENSE.getAttribute(), 30);
    }

    @Override
    public String getDefaultName() {

        if (entity instanceof Villager villager) {
            Villager.Profession profession = villager.getProfession();
            if (!profession.equals(Villager.Profession.NONE))
                return MinecraftStringUtils.getTitledString(profession.key().value());
        }

        return super.getDefaultName();
    }
}
