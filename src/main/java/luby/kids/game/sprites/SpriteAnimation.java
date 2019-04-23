package luby.kids.game.sprites;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public abstract class SpriteAnimation {
    public abstract String getName();

    public enum Direction {
        @SerializedName("forward")
        FORWARD,
        @SerializedName("reverse")
        REVERSE,
        @SerializedName("pingpong")
        PINGPONG
    }
    public Direction getDirection() {
        return Direction.FORWARD;
    }

    public abstract List<SpriteFrame> getFrames();
}
