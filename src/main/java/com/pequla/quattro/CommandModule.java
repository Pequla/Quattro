package com.pequla.quattro;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CommandModule extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().trim().split("\\s+");
        TextChannel channel = event.getChannel();
        User user = event.getAuthor();
        if (message[0].equals("q!create")) {
            Game game = new Game(event.getJDA(), channel.getId(), user.getId());
            game.createBoard();
            System.out.println("Game created, id: " + game.getGameId());
            System.out.println("Game will use this channel: " + game.getChannel());
            System.out.println("User " + formatUser(user) + " created a new game in " + formatChannel(channel));
            return;
        }
        if (message[0].equals("q!join")) {
            if (message.length == 2) {
                List<Member> members = event.getMessage().getMentionedMembers();
                if (members.size() == 1) {
                    Member owner = members.get(0);
                    Game game = getGameByOwnerID(owner.getId());
                    if (game != null) {
                        game.addPlayer(user.getId());
                        channel.sendMessage("You have successfully joined " + owner.getEffectiveName() + "'s game").queue();
                        System.out.println("User " + formatUser(user) + " has successfully joined the game");
                        return;
                    } else {
                        channel.sendMessage("User you declared doesn't own a game").queue();
                    }
                } else {
                    channel.sendMessage("You can only join one game, please mention only one user!").queue();
                }
            } else {
                channel.sendMessage("Command format: `q!join <@user>`").queue();
            }
            return;
        }
        if (message[0].equals("q!leave")) {
            Game game = getGameByPlayerID(user.getId());
            if (game != null) {
                game.leave(user.getId());
                System.out.println("User " + formatUser(user) + " left the game");
            } else {
                channel.sendMessage("You are not in the game").queue();
            }
            return;
        }
        if (message[0].equals("q!put")) {
            if (message.length == 3) {
                //todo gg
            } else {
                channel.sendMessage("Command requires 2 arguments: `q!put <m> <n>`").queue();
            }
            return;
        }
        if (message[0].equals("q!games")) {
            StringBuilder content = new StringBuilder("==== Available games ====" + System.lineSeparator());
            Vector<Game> games = Game.getAllGames();
            for (Game game : games) {
                if (channel.getGuild().getChannels().stream().map(GuildChannel::getId).anyMatch(s -> game.getChannel().equals(s))) {
                    content.append(game.toString());
                    content.append(System.lineSeparator());
                }
            }
            content.append("==== Total: ").append(games.size()).append("====");
            channel.sendMessage(content).queue();
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

    private String formatUser(User user) {
        return String.format("%s (%s)", user.getName(), user.getId());
    }

    private String formatChannel(TextChannel channel) {
        Guild guild = channel.getGuild();
        return String.format("%s (%s/%s)", channel.getName(), guild.getName(), guild.getId());
    }
}
