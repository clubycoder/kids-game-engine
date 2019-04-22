package luby.kids.game.sprites;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
