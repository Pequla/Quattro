package com.pequla.quattro.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.time.Instant;
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
    private boolean inProgress;

    public Game(JDA jda, String channel, String id, String name, String iconUrl) {
        this.jda = jda;
        this.channel = channel;
        this.players = new Vector<>();
        this.board = new Player[8][8];
        this.inProgress = false;

        Player owner = new Player(id, name, iconUrl, Player.AvatarType.RED);
        this.owner = owner;
        players.add(owner);
        owner.setGame(this);

        allGames.add(this);
        sendMessage("Game successfully created, owner: " + owner.getName() + " " + owner.getAvatarEmoji());
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

    private void printBoard() {
        StringBuilder builder = new StringBuilder();
        for (int m = 0; m < 9; m++) {
            for (int n = 0; n < 9; n++) {
                if (m == 0 && n == 0) {
                    builder.append(":ok_hand:");
                } else {
                    if (n == 0) {
                        builder.append(getEmoji(m - 1));
                    } else if (m == 0) {
                        builder.append(getEmoji(n - 1));
                    } else {
                        builder.append(board[m - 1][n - 1].getAvatarEmoji());

                    }
                }
                if (n != 8) {
                    builder.append(" ");
                }
            }
            builder.append(System.lineSeparator());
        }
        sendMessage(builder.toString());
    }

    public void leave(String id) {
        if (inProgress) {
            sendMessage("One of the members (" + id + ") has left the game, game ended");
            allGames.remove(this);
            players.forEach(player -> player.setGame(null));
            inProgress = false;
        } else {
            if (owner.getId().equals(id)) {
                sendMessage("Owner left the game, everyone will get kicked");
                allGames.remove(this);
                players.forEach(player -> player.setGame(null));
                inProgress = false;
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
    }

    public void put(String id, int m, int n) {
        if (inProgress) {
            Player playerOnTurn = players.elementAt(onTurn);
            if (playerOnTurn.getId().equals(id)) {
                if (isInRange(m, n)) {
                    if (board[m][n].getAvatar() == Player.AvatarType.EMPTY) {
                        for (Player player : players) {
                            if (player.getId().equals(id)) {
                                board[m][n] = player;
                                sendMessage(String.format("You moved to [%s][%s]", m, n));
                                printBoard();
                                if (onTurn == 3) {
                                    onTurn = 0;
                                } else {
                                    onTurn++;
                                }

                                // Check for a winner
                                Player p = checkForWinner(m, n);
                                if (p != null) {
                                    // Yes there is a winner
                                    sendMessage(new EmbedBuilder()
                                            .setColor(Color.CYAN)
                                            .setAuthor(p.getName(), null, p.getIconUrl())
                                            .setTitle(MarkdownUtil.bold("WINNER FOUND"))
                                            .setDescription("Winner was found, to play again create a new game using q!create command")
                                            .addField("Winner:", p.getName(), false)
                                            .addField("Game owner:", owner.getName(), false)
                                            .setTimestamp(Instant.now())
                                            .build());
                                    allGames.remove(this);
                                    players.forEach(pl -> pl.setGame(null));
                                    inProgress = false;
                                }
                                return;
                            }
                        }
                    } else {
                        sendMessage("Field is already occupied! Please chose another one!");
                    }
                } else {
                    sendMessage("Cords must be in range from 0 to 7");
                }
            } else {
                sendMessage("You need to be on turn, currently " + playerOnTurn.getName() + " (" + playerOnTurn.getAvatarEmoji() + ") is on turn");
            }
        } else {
            sendMessage("Game hasn't started yet!");
        }
    }

    public void addPlayer(String id, String name, String iconUrl) {
        if (!inProgress) {
            if (players.size() < 5) {
                Player player = new Player(id, name, iconUrl, pickAvatar());
                players.add(player);
                player.setGame(this);
                sendMessage("Player " + name + " (" + id + ") joined the game " + toString() + " as " + player.getAvatar());
                if (players.size() == 4 && !inProgress) {
                    start();
                }
            }
        } else {
            sendMessage("Game is in progress, you cant join!");
        }
    }

    private void start() {
        sendMessage("Game has started, its red's (" + owner.getName() + ") turn");
        this.onTurn = 0;
        this.inProgress = true;
        printBoard();
    }

    private Player checkForWinner(int m, int n) {
        Player player = null;
        if (isInRange(m, n)) {
            player = checkHorizontal(m, n);
            if (player == null) {
                player = checkVertical(m, n);
            }
            if (player == null) {
                player = checkDiagonalDown(m, n);
            }
            if (player == null) {
                player = checkDiagonalUp(m, n);
            }
        }
        return player;
    }

    private Player checkHorizontal(int m, int n) {
        int counter = 1;
        Player player = board[m][n];

        //Going up
        for (int i = 1; i < 4; i++) {
            if (board[m - i][n] == player && isInRange(m - i, n)) {
                counter++;
            } else {
                break;
            }
        }

        //Going down
        if (counter != 4) {
            for (int i = 1; i < 4; i++) {
                if (board[m + i][n] == player && isInRange(m + i, n)) {
                    counter++;
                    if (counter == 4) {
                        return player;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return player;
        }

        return null;
    }

    private Player checkVertical(int m, int n) {
        int counter = 1;
        Player player = board[m][n];

        //Going left
        for (int i = 1; i < 4; i++) {
            if (board[m][n - i] == player && isInRange(m, n - i)) {
                counter++;
            } else {
                break;
            }
        }

        //Going right
        if (counter != 4) {
            for (int i = 1; i < 4; i++) {
                if (board[m][n + i] == player && isInRange(m, n + i)) {
                    counter++;
                    if (counter == 4) {
                        return player;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return player;
        }

        return null;
    }

    private Player checkDiagonalDown(int m, int n) {
        int counter = 1;
        Player player = board[m][n];

        //Left
        for (int i = 1; i < 4; i++) {
            if (board[m - i][n - i] == player && isInRange(m - i, n - i)) {
                counter++;
            } else {
                break;
            }
        }

        //Right
        if (counter != 4) {
            for (int i = 1; i < 4; i++) {
                if (board[m + i][n + i] == player && isInRange(m + i, n + i)) {
                    counter++;
                    if (counter == 4) {
                        return player;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return player;
        }

        return null;
    }

    private Player checkDiagonalUp(int m, int n) {
        int counter = 1;
        Player player = board[m][n];

        //Left
        for (int i = 1; i < 4; i++) {
            if (board[m + i][n - i] == player && isInRange(m + i, n - i)) {
                counter++;
            } else {
                break;
            }
        }

        //Right
        if (counter != 4) {
            for (int i = 1; i < 4; i++) {
                if (board[m - i][n + i] == player && isInRange(m - i, n + i)) {
                    counter++;
                    if (counter == 4) {
                        return player;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return player;
        }

        return null;
    }

    private boolean isInRange(int m, int n) {
        return m >= 0 && m < 8 && n >= 0 && n < 8;
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

    private void sendMessage(MessageEmbed message) {
        TextChannel tc = jda.getTextChannelById(channel);
        if (tc != null) {
            tc.sendMessage(message).queue();
        }
    }

    private String getEmoji(int number) {
        switch (number) {
            case 0:
                return ":zero:";
            case 1:
                return ":one:";
            case 2:
                return ":two:";
            case 3:
                return ":three:";
            case 4:
                return ":four:";
            case 5:
                return ":five:";
            case 6:
                return ":six:";
            case 7:
                return ":seven:";
            default:
                return ":warning:";
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
        return "[Game: " + owner.getId() + "] Owner: " + owner.getName() + " Number of players: " + players.size();
    }
}
