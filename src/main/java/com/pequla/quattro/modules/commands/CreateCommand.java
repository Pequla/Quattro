package com.pequla.quattro.modules.commands;

import com.pequla.quattro.game.Game;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class CreateCommand implements GuildCommand {

    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        User user = member.getUser();
        Game game = new Game(channel.getJDA(), channel.getId(), user.getId(), user.getName(), user.getEffectiveAvatarUrl());
        game.createBoard();
        System.out.println("Game created, id: " + game.getGameId());
        System.out.println("Game will use this channel: " + game.getChannel());
        System.out.println("User " + member.getEffectiveName() + " created a new game in " + channel.getName());
    }
}
