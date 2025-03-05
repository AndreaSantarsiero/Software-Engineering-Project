package it.polimi.ingsw.gc11.model;

public class Player {
    private String username;
    private int coins;
    private int position;
    private Boolean abort;

    public Player(String username) {
        this.username = username;
        coins = 0;
        position = 0;
        abort = false;
    }

    public String getUsername() {
        return username;
    }
    public int getCoins() {
        return coins;
    }
    public void addCoins(int amount) {
        this.coins += coins;
    }
    public void removeCoins(int amount) {
        this.coins -= coins;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int delta) {
        this.position += delta;
    }
}
