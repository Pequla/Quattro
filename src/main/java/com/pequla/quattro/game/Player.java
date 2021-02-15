package com.pequla.quattro.game;

public class Player {

    public enum AvatarType {
        EMPTY, RED, GREEN, BLUE, YELLOW
    }

    private String id;
    private String name;
    private String iconUrl;
    private final AvatarType avatar;
    private Game game;

    public Player(String id, String name, String iconUrl, AvatarType avatar) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
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

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
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
            return ":yellow_square:";
        }
        return ":black_circle:";
    }
}
