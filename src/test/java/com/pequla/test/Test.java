package com.pequla.test;

import com.pequla.quattro.Player;

public class Test {
    public static void main(String[] args) {
        Player[][] tabletest = new Player[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tabletest[i][j] = new Player();
            }
        }

        tabletest[6][7] = new Player("", Player.AvatarType.BLUE);
        System.out.println(tabletest[6][7].getAvatarEmoji());
        System.out.println(tabletest[7][7].getAvatarEmoji());
    }
}
