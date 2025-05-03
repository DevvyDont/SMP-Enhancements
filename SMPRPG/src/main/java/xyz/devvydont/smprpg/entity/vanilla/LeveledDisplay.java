package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledDisplay extends VanillaEntity<Display> {

    public LeveledDisplay(Display entity) {
        super(entity);
    }

    public LeveledDisplay(TextDisplay entity) {
        super(entity);
    }

    public LeveledDisplay(ItemDisplay entity) {
        super(entity);
    }

    public LeveledDisplay(BlockDisplay entity) {
        super(entity);
    }

    @Override
    public void updateNametag() {
        super.updateNametag();
        _entity.setCustomNameVisible(false);
    }

    @Override
    public int getDefaultLevel() {
        return 1;
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public double calculateBaseHealth() {
        return 5;
    }
}
