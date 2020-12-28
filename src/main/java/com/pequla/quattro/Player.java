package com.pequla.quattro;


public class Player {

    public enum AvatarType {
        EMPTY,RED, GREEN, BLUE, YELLOW
    }

    private String id;
    private final AvatarType avatar;
    private Game game;

    public Player(String id, AvatarType avatar) {
        this.id = id;
        this.avatar = avatar;
    }

    public Player() {
        this.avatar = AvatarType.EMPTY;
    }

    public String getId() {
        return id;
    }

    public AvatarType getAvatar() {
        return avatar;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean inGame() {
        if (game != null) {
            return true;
        }
        return false;
    }

    public String getAvatarEmoji() {
        if (avatar == AvatarType.RED) {
            return ":red_square:";
        }
        if (avatar == AvatarType.GREEN) {
            return ":green_square:";
        }
        if (avatar == AvatarType.BLUE) {
            return ":blue_square:";
        }
        if (avatar == AvatarType.YELLOW) {
            return ":green_square:";
        }
        return ":36393F:";
    }
}
