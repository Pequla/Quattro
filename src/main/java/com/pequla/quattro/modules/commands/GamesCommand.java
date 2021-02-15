package com.pequla.quattro.modules.commands;

import com.pequla.quattro.game.Game;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Vector;

public class GamesCommand implements GuildCommand {
    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        Vector<Game> games = Game.getAllGames();
        if (games.size() != 0) {
            StringBuilder content = new StringBuilder("==== Available games ====" + System.lineSeparator());

            for (Game game : games) {
                if (channel.getGuild().getChannels().stream().map(GuildChannel::getId).anyMatch(s -> game.getChannel().equals(s))) {
                    content.append(game.toString());
                    content.append(System.lineSeparator());
                }
            }
            content.append("==== Total: ").append(games.size()).append(" ====");
            channel.sendMessage(content).queue();
        } else {
            channel.sendMessage("There are currently no active games!").queue();
        }
    }
}
