package models;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Game {


    public List<Player> playersArray;


        public Game(Player player, Player enemy){
            playersArray = new ArrayList<Player>();
            playersArray.add(player);
            playersArray.add(enemy);
        }

        public void attack(Player player, Player enemy){
            enemy.recieve_damage(Math.round(Math.floor(this.random_damage(player))));
        }

        public double random_damage(Player player){
            Random rand = new Random();
            int random = (int)(Math.random() * player.damage_limit + 1);
            return (random);
        }

}
