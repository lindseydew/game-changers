package models;

import lombok.Data;

@Data
    public class Players {
        private String user_ID;
        private String user_name;
        private String name;
        private String password;
        private int high_score;

        public Players(String user_ID, String username, String full_name, String password, int high_score) {
            this.user_ID = user_ID;
            this.user_name = username;
            this.name = full_name;
            this.password = password;
            this.high_score = high_score;
        }

    }
