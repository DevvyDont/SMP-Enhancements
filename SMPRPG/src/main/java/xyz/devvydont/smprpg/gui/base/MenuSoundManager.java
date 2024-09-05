package xyz.devvydont.smprpg.gui.base;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * A class which holds the sound effects playable by a menu.
 */
public final class MenuSoundManager {
    private MenuSound menuOpen;
    private MenuSound menuClose;
    private MenuSound menuOpenSub;
    private MenuSound menuOpenParent;
    private MenuSound pageNext;
    private MenuSound pagePrevious;
    private MenuSound actionConfirm;
    private MenuSound actionError;
    private final Player player;

    MenuSoundManager(Player player) {
        this.player = player;
        this.setMenuOpen(Sound.BLOCK_AMETHYST_BLOCK_PLACE, 0.5f, 1f);
        this.setMenuClose(Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.3f, 1f);
        this.setMenuOpenSub(Sound.UI_TOAST_IN, 4f, 1f);
        this.setMenuOpenParent(Sound.UI_TOAST_OUT, 4f, 1f);
        this.setPageNext(Sound.ITEM_BOOK_PAGE_TURN, 0.8f, 1f);
        this.setPagePrevious(Sound.ITEM_BOOK_PAGE_TURN, 0.6f, 0.8f);
        this.setActionConfirm(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4f, 1f);
        this.setActionError(Sound.ENTITY_ITEM_BREAK, 0.4f, 1f);
    }

    /**
     * Gets the sound that should be played when the menu is initially opened.
     */
    public void playMenuOpen() {
        this.menuOpen.play();
    }

    /**
     * Sets the sound that should be played when the menu is initially opened.
     */
    public void setMenuOpen(Sound sound, float volume, float pitch) {
        this.menuOpen = new MenuSound(this.player, sound, volume, pitch);
    }

    /**
     * Gets the sound that should be played when the menu is fully closed.
     */
    public void playMenuClose() {
        this.menuClose.play();
    }

    /**
     * Sets the sound that should be played when the menu is fully closed.
     */
    public void setMenuClose(Sound sound, float volume, float pitch) {
        this.menuClose = new MenuSound(this.player, sound, volume, pitch);
    }

    /**
     * Gets the sound that should be played when a sub menu is opened.
     */
    public void playMenuOpenSub() {
        this.menuOpenSub.play();
    }

    /**
     * Sets the sound that should be played when a sub menu is opened.
     */
    public void setMenuOpenSub(Sound sound, float volume, float pitch) {
        this.menuOpenSub = new MenuSound(this.player, sound, volume, pitch);
    }

    /**
     * Gets the sound that should be played when a sub menu returns back to its parent menu.
     */
    public void playMenuOpenParent() {
        this.menuOpenParent.play();
    }

    /**
     * Sets the sound that should be played when a sub menu returns back to its parent menu.
     */
    public void setMenuOpenParent(Sound sound, float volume, float pitch) {
        this.menuOpenParent = new MenuSound(this.player, sound, volume, pitch);
    }

    /**
     * Gets the sound that should be played when is a menu is paginated forward.
     */
    public void playPageNext() {
        this.pageNext.play();
    }

    /**
     * Sets the sound that should be played when is a menu is paginated forward.
     */
    public void setPageNext(Sound sound, float volume, float pitch) {
        this.pageNext = new MenuSound(this.player, sound, volume, pitch);
    }

    /**
     * Gets the sound that should be played when is a menu is paginated backwards.
     */
    public void playPagePrevious() {
        this.pagePrevious.play();
    }

    /**
     * Sets the sound that should be played when is a menu is paginated backwards.
     */
    public void setPagePrevious(Sound sound, float volume, float pitch) {
        this.pagePrevious = new MenuSound(this.player, sound, volume, pitch);
    }

    /**
     * Gets the sound that should be played when an action the player did is confirmed.
     */
    public void playActionConfirm() {
        this.actionConfirm.play();
    }

    /**
     * Sets the sound that should be played when an action the player did is confirmed.
     */
    public void setActionConfirm(Sound sound, float volume, float pitch) {
        this.actionConfirm = new MenuSound(this.player, sound, volume, pitch);
    }

    /**
     * Gets the sound that should be played when an action the player was invalid.
     */
    public void playActionError() {
        this.actionError.play();
    }

    /**
     * Sets the sound that should be played when an action the player was invalid.
     */
    public void setActionError(Sound sound, float volume, float pitch) {
        this.actionError = new MenuSound(this.player, sound, volume, pitch);
    }
}
