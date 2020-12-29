package com.pequla.quattro;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.MarkdownUtil;

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

    public Game(JDA jda, String channel, String id) {
        this.jda = jda;
        this.channel = channel;
        this.players = new Vector<>();
        this.board = new Player[8][8];
        this.inProgress = false;

        Player owner = new Player(id, Player.AvatarType.RED);
        this.owner = owner;
        players.add(owner);
        owner.setGame(this);

        allGames.add(this);
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

    private void printBoard() {
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
        if (inProgress) {
            sendMessage("One of the members (" + id + ") has left the game, game ended");
            allGames.remove(this);
            players.forEach(player -> player.setGame(null));
            inProgress =false;
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
                if (m >= 0 && m < 8 && n >= 0 && n < 8) {
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
                                sendMessage(MarkdownUtil.bold("WINNER FOUND: "+jda.retrieveUserById(p.getId()).complete().getAsMention()));
                                allGames.remove(this);
                                players.forEach(pl -> pl.setGame(null));
                                inProgress = false;
                                sendMessage("To play gain create a game using q!create command!!!");
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
        } else {
            sendMessage("Game hasn't started yet!");
        }
    }

    public void addPlayer(String id) {
        if (!inProgress) {
            if (players.size() < 5) {
                Player player = new Player(id, pickAvatar());
                players.add(player);
                User user = jda.retrieveUserById(id).complete();
                sendMessage("Player " + user.getName() + " (" + user.getId() + ") joined the game " + toString() + " as " + player.getAvatar());
                if (players.size() == 4 && !inProgress) {
                    start();
                }
            }
        } else {
            sendMessage("Game is in progress, you cant join!");
        }
    }

    private void start() {
        sendMessage("Game has started, its red's turn");
        this.onTurn = 0;
        this.inProgress = true;
        printBoard();
    }

    private Player checkForWinner(int m, int n) {
        for (int i = m - 1; i <= m + 1; i++) {
            for (int j = n - 1; j <= n + 1; j++) {
                if (j != n || i != j) {
                    if (board[i][j] == board[m][n]) {
                        return isWinner(i, j, m, n);
                    }
                }
            }
        }
        return null;
    }

    // The whole winner logic
    private Player isWinner(int i, int j, int m, int n) {
        int a = i;
        int b = j;
        int counter = 2;

        for (int k = 0; k < 3; k++) {
            a -= (i - m);
            b -= (j - n);
            if (board[a][b] != board[i][j]) break;
            if (a != m || b != n) counter++;
            if (counter == 4) {
                return board[i][j];
            }
        }

        a = i;
        b = j;
        counter = 2;
        for (int k = 0; k < 3; k++) {
            a += (i - m);
            b += (j - n);
            if (board[a][b] != board[i][j]) break;
            if (a != m || b != n) counter++;
            if (counter == 4) {
                return board[i][j];
            }
        }

        // Not found
        return null;
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
        return "[Game: " + user.getId() + "] Owner: " + user.getName() + " Number of players: " + players.size();
    }
}
