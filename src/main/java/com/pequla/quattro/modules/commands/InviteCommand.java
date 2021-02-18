package com.pequla.quattro.modules.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class InviteCommand implements GuildCommand {

    @Override
    public void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args) {
        channel.sendMessage(new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(MarkdownUtil.bold("BOT INVITE LINK"))
                .setDescription("You can invite the bot to your server using this link: https://quattro.pequla.com/")
                .setTimestamp(Instant.now())
                .build()).queue();
    }
}
