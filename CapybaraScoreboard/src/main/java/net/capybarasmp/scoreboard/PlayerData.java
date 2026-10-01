package net.capybarasmp.scoreboard;

public final class PlayerData {
    public int kills;
    public int deaths;
    public long playtimeSeconds;
    public int lives;

    public PlayerData(int lives) {
        this.lives = lives;
    }
}
