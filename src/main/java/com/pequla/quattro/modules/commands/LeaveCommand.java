package com.pequla.quattro.modules.commands;

import com.pequla.quattro.game.Game;
import com.pequla.quattro.game.Player;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class LeaveCommand implements GuildCommand {

    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        Game game = getGameByPlayerID(member.getId());
        if (game != null) {
            game.leave(member.getId());
            System.out.println("User " + member.getEffectiveName() + " left the game");
        } else {
            channel.sendMessage("You are not in the game").queue();
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
