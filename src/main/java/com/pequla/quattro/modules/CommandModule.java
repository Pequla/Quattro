package com.pequla.quattro.modules;

import com.pequla.quattro.modules.commands.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class CommandModule extends ListenerAdapter {

    private final HashMap<String, GuildCommand> map;

    public CommandModule(String prefix) {
        this.map = new HashMap<>();
        map.put(prefix + "create", new CreateCommand());
        map.put(prefix + "c", new CreateCommand());
        map.put(prefix + "join", new JoinCommand());
        map.put(prefix + "j", new JoinCommand());
        map.put(prefix + "leave", new LeaveCommand());
        map.put(prefix + "l", new LeaveCommand());
        map.put(prefix + "put", new PutCommand());
        map.put(prefix + "p", new PutCommand());
        map.put(prefix + "games", new GamesCommand());
        map.put(prefix + "g", new GamesCommand());
        map.put(prefix + "test", new BoardTestCommand());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            Message message = event.getMessage();
            String[] asArray = message.getContentRaw().trim().split("\\s+");
            GuildCommand command = map.get(asArray[0].toLowerCase());
            if (command != null) {
                command.execute(event.getMember(), event.getChannel(), message.getMentionedMembers(), Arrays.copyOfRange(asArray, 1, asArray.length));
            }
        }
    }
}
