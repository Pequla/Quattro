package com.pequla.quattro.modules.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class HelpCommand implements GuildCommand {

    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        channel.sendMessage(new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(MarkdownUtil.bold("COMMAND LIST"))
                .addField("q!create", "Creates a new game", false)
                .addField("q!join", "Joins a mentioned user's game", false)
                .addField("q!leave", "Leaves a game", false)
                .addField("q!put", "Places your avatar on the board", false)
                .addField("q!games", "Lists all the available games in the guild", false)
                .addField("q!tutorial", "Displays a link for the tutorial", false)
                .addField("q!invite", "Displays a bot invite link", false)
                .setTimestamp(Instant.now())
                .build()).queue();
    }
}
