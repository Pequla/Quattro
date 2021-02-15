package com.pequla.quattro.modules.commands;

import com.pequla.quattro.game.Game;
import com.pequla.quattro.game.Player;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class PutCommand implements GuildCommand {
    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        if (args.length == 2) {
            Game game = getGameByPlayerID(member.getId());
            if (game != null) {
                try {
                    int m = Integer.parseInt(args[0]);
                    int n = Integer.parseInt(args[1]);
                    game.put(member.getId(), m, n);
                } catch (NumberFormatException e) {
                    channel.sendMessage(":warning: You need to input round numbers in range from 0 to 7").queue();
                }
            } else {
                channel.sendMessage("You are not in the game").queue();
            }
        } else {
            channel.sendMessage("Command requires 2 arguments: `q!put <m> <n>`").queue();
        }
    }

    private Game getGameByPlayerID(String id) {
        List<Player> players = new ArrayList<>();
        Game.getAllGames().forEach(game -> players.addAll(game.getPlayers()));
        for (Player player : players) {
            if (player.getId().equals(id)) {
                return player.getGame();
            }
        }
        return null;
    }
}
