package it.polimi.ingsw.gc11.model;

public class Player {
    private String username;
    private int coins;
    private int position;
    private Boolean abort;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getCoins() {
        return coins;
    }
    public void addCoins(int coins) {
        this.coins += coins;
    }
    public void removeCoins(int coins) {
        this.coins -= coins;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
}
