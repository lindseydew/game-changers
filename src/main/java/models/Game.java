package models;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
public class Game {

    public List<Player> playersArray;


        public Game(Player player, Player enemy){
            playersArray = new ArrayList<Player>();
            playersArray.add(player);
            playersArray.add(enemy);
        }

        public void attack(Player player){
            player.recieve_damage(Math.round(Math.floor(this.random_damage(player))));
        }

        public double random_damage(Player player){
            return (Math.random() * ((player.damage_limit - 5) + 1)) + 5;
        }
}
