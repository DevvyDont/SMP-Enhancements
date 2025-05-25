package xyz.devvydont.smprpg.attribute;

public enum AttributeCategory {

    COMBAT("Combat"),
    FORAGING("Foraging"),
    FISHING("Fishing"),
    SPECIAL("Special"),;

    public final String DisplayName;

    AttributeCategory(String displayName) {
        DisplayName = displayName;
    }
}
