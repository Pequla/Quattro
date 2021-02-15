package com.pequla.quattro.modules.commands;

import com.pequla.quattro.game.Player;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class BoardTestCommand implements GuildCommand {
    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {

        Player[][] board = new Player[8][8];

        for (int m = 0; m < 8; m++) {
            for (int n = 0; n < 8; n++) {
                board[m][n] = new Player();
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int m = 0; m < 9; m++) {
            for (int n = 0; n < 9; n++) {
                if (m == 0 && n == 0) {
                    builder.append(":ok_hand:");
                } else {
                    if (n == 0) {
                        builder.append(getEmoji(m - 1));
                    } else if (m == 0) {
                        builder.append(getEmoji(n - 1));
                    } else {
                        builder.append(board[m - 1][n - 1].getAvatarEmoji());

                    }
                }
                if (n != 8) {
                    builder.append(" ");
                }
            }
            builder.append(System.lineSeparator());
        }
        channel.sendMessage(builder.toString()).queue();
    }

    private String getEmoji(int number) {
        switch (number) {
            case 0:
                return ":zero:";
            case 1:
                return ":one:";
            case 2:
                return ":two:";
            case 3:
                return ":three:";
            case 4:
                return ":four:";
            case 5:
                return ":five:";
            case 6:
                return ":six:";
            case 7:
                return ":seven:";
            default:
                return ":warning:";
        }
    }
}
