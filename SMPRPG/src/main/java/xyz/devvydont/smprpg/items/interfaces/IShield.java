package xyz.devvydont.smprpg.items.interfaces;

public interface IShield {

    /**
     * How much damage should be blocked when blocking?
     * @return a double representing damage resistance percent
     */
    double getDamageBlockingPercent();

    /**
     * How long in ticks should the shield have a delay for?
     * @return an integer representing 1/20th seconds
     */
    int getShieldDelay();

}
