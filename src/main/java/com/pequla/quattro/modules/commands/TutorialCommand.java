package com.pequla.quattro.modules.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class TutorialCommand implements GuildCommand {

    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        channel.sendMessage(new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(MarkdownUtil.bold("GAME TUTORIAL"))
                .setDescription("You can find more information about this board game here: https://github.com/Pequla/Quattro/blob/main/README.md")
                .setTimestamp(Instant.now())
                .build()).queue();
    }
}
