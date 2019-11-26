package models;

import lombok.Data;

@Data
public class Player {

    public String username;
    public int health;
    public int damage_limit;
    public int defence;
    public String is_alive;
    public int coins;

    public Player(String username, int health, int damage_limit, int defence, String is_alive, int coins) {
        this.username = username;
        this.health = health;
        this.damage_limit = damage_limit;
        this.defence = defence;
        this.is_alive = is_alive;
        this.coins = coins;
    }


    public void recieve_damage(double damage){
        health -= damage;
    }

    public void attack(Player player){
        player.recieve_damage(Math.round(Math.floor(player.random_damage())));
    }

    public double random_damage(){
        return (Math.random() * ((damage_limit - 5) + 1)) + 5;
    }

    public void Heal(){
        health = health + 10;
    }

    public void increase_damage(){
        damage_limit = damage_limit + 5;
    }

    public void increase_defence(){
        defence = defence + 5;
    }

}






