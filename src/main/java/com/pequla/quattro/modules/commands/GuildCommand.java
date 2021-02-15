package com.pequla.quattro.modules.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public interface GuildCommand {
    void execute(Member member, TextChannel channel, List<Member> mentioned, String[] args);
}
