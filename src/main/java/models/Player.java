package models;

import lombok.Data;

@Data
public class Player {
    public String username;
    public int health;
    public int damage;
    public int defence;
    public String is_alive;

    public Player(String username, int health, int damage, int defence, String is_alive) {
        this.username = username;
        this.health = health;
        this.damage = damage;
        this.defence = defence;
        this.is_alive = is_alive;
    }
}





