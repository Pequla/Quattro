package com.pequla.quattro;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Game {

    private static final Vector<Game> allGames = new Vector<>();

    private final JDA jda;
    private final String channel;
    private final Player owner;
    private final Vector<Player> players;
    private final Player[][] board;
    private int onTurn;

    public Game(JDA jda, String channel, String id) {
        this.jda = jda;
        this.channel = channel;
        Player owner = new Player(id, Player.AvatarType.RED);
        this.owner = owner;
        this.players = new Vector<>();
        players.add(owner);
        this.board = new Player[8][8];
        sendMessage("Game successfully created, owner: " + owner.getId());
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public void createBoard() {
        for (int m = 0; m < 8; m++) {
            for (int n = 0; n < 8; n++) {
                board[m][n] = new Player();
            }
        }
        sendMessage("Board has been initialised");
        printBoard();
    }

    public void printBoard() {
        StringBuilder builder = new StringBuilder();
        for (int m = 0; m < 8; m++) {
            for (int n = 0; n < 8; n++) {
                builder.append(board[m][n].getAvatarEmoji());
                if (n != 7) {
                    builder.append(" ");
                }
            }
            builder.append(System.lineSeparator());
        }
        sendMessage(builder.toString());
    }

    public void leave(String id) {
        if (owner.getId().equals(id)) {
            sendMessage("Owner left the game, everyone will get kicked");
            allGames.remove(this);
            players.forEach(player -> player.setGame(null));
            return;
        }
        players.forEach(player -> {
            if (player.getId().equals(id)) {
                players.remove(player);
                player.setGame(null);
                sendMessage("Successfully left the game");
            }
        });
    }

    public void put(String id, int m, int n) {
        Player playerOnTurn = players.elementAt(onTurn);
        if (playerOnTurn.getId().equals(id)) {
            if (m >= 0 && m < 8 && n >= 0 && n < 8) {
                for (Player player : players) {
                    if (player.getId().equals(id)) {
                        board[m][n] = player;
                        sendMessage(String.format("You moved to [%s][%s]", m, n));
                        printBoard();
                        if (onTurn==3) {
                            onTurn = 0;
                        } else {
                            onTurn++;
                        }
                        return;
                    }
                }
            } else {
                sendMessage("Cords must be in range from 0 to 7");
            }
        } else {
            sendMessage("You need to be on turn, currently " + playerOnTurn.getAvatarEmoji() + " is on turn");
        }
    }

    public void addPlayer(String id) {
        if (players.size() < 5) {
            Player player = new Player(id, pickAvatar());
            players.add(player);
            User user = jda.retrieveUserById(id).complete();
            sendMessage("Player " + user.getName() + " (" + user.getId() + ") joined the game " + toString() + " as " + player.getAvatar());
            if (players.size() == 4) {
                start();
            }
        }
    }

    private void start() {
        sendMessage("Game has started, its red's turn");
        this.onTurn = 0;
        printBoard();
    }

    private Player.AvatarType pickAvatar() {
        List<Player.AvatarType> avatars = players.stream().map(Player::getAvatar).collect(Collectors.toList());
        if (!avatars.contains(Player.AvatarType.GREEN)) {
            return Player.AvatarType.GREEN;
        }
        if (!avatars.contains(Player.AvatarType.BLUE)) {
            return Player.AvatarType.BLUE;
        }
        if (!avatars.contains(Player.AvatarType.YELLOW)) {
            return Player.AvatarType.YELLOW;
        }
        return null;
    }

    private void sendMessage(String message) {
        TextChannel tc = jda.getTextChannelById(channel);
        if (tc != null) {
            tc.sendMessage(message).queue();
        }
    }

    public String getGameId() {
        return owner.getId();
    }

    public String getChannel() {
        return channel;
    }

    public Player getOwner() {
        return owner;
    }

    public static Vector<Game> getAllGames() {
        return allGames;
    }

    @Override
    public String toString() {
        User user = jda.retrieveUserById(owner.getId()).complete();
        return "[Game: " + user.getId() + "] Owner: " + user.getName() + "Number of players: " + players.size();
    }
}
