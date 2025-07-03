package xyz.devvydont.smprpg.attribute;

public enum AttributeCategory {

    COMBAT("Combat", "Attributes related to damage output and miscellaneous combat aspects"),
    SURVIVABILITY("Survivability", "Attributes that make staying alive easier"),
    MOVEMENT("Movement", "Attributes affect various agility factors"),
    FORAGING("Foraging", "Attributes related to gathering resources and experience"),
    FISHING("Fishing", "Attributes related to fishing"),
    SPECIAL("Special", "Attributes that have unique effects"),;

    public final String DisplayName;
    public final String Description;

    AttributeCategory(String displayName, String description) {
        DisplayName = displayName;
        Description = description;
    }
}
