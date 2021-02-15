package com.pequla.quattro.modules.commands;

import com.pequla.quattro.game.Game;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Vector;

public class JoinCommand implements GuildCommand {

    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        if (args.length == 1) {
            if (mentioned.size() == 1) {
                Member owner = mentioned.get(0);
                Game game = getGameByOwnerID(owner.getId());
                if (game != null) {
                    User user = member.getUser();
                    game.addPlayer(user.getId(), user.getName(), user.getEffectiveAvatarUrl());
                    channel.sendMessage("You have successfully joined " + owner.getEffectiveName() + "'s game").queue();
                    System.out.println("User " + member.getEffectiveName() + " has successfully joined the " + owner.getEffectiveName() + "'s game");
                } else {
                    channel.sendMessage("User you declared doesn't own a game").queue();
                }
            } else {
                channel.sendMessage("You can only join one game, please mention only one user!").queue();
            }
        } else {
            channel.sendMessage("Command format: `q!join <@user>`").queue();
        }
    }

    private Game getGameByOwnerID(String id) {
        Vector<Game> games = Game.getAllGames();
        for (Game g : games) {
            if (g.getOwner().getId().equals(id)) {
                return g;
            }
        }
        return null;
    }
}
