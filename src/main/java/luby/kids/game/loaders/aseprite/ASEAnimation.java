package luby.kids.game.loaders.aseprite;

import com.google.gson.annotations.SerializedName;
import luby.kids.game.sprites.SpriteAnimation;
import luby.kids.game.sprites.SpriteFrame;
import luby.kids.game.utils.gson.SerializedRequired;

import java.util.ArrayList;
import java.util.List;

/**
 *   "frameTags": [
 *    { "name": "down-walk", "from": 0, "to": 2, "direction": "pingpong" },
 *    { "name": "down-idle", "from": 1, "to": 1, "direction": "forward" },
 *    { "name": "left-walk", "from": 3, "to": 5, "direction": "pingpong" },
 *    { "name": "left-idle", "from": 4, "to": 4, "direction": "forward" },
 *    { "name": "right-walk", "from": 6, "to": 8, "direction": "pingpong" },
 *    { "name": "right-idle", "from": 7, "to": 7, "direction": "forward" },
 *    { "name": "up-walk", "from": 9, "to": 11, "direction": "pingpong" },
 *    { "name": "up-idle", "from": 10, "to": 10, "direction": "forward" }
 *   ],
 */
public class ASEAnimation extends SpriteAnimation {
    @SerializedName("name")
    @SerializedRequired
    private String name;
    @Override
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("from")
    @SerializedRequired
    private int from;
    public int getFrom() {
        return this.from;
    }
    public void setFrom(int from) {
        this.from = from;
    }

    @SerializedName("to")
    @SerializedRequired
    private int to;
    public int getTo() {
        return this.to;
    }
    public void setTo(int to) {
        this.to = to;
    }

    @SerializedName("direction")
    private SpriteAnimation.Direction direction;
    @Override
    public SpriteAnimation.Direction getDirection() {
        return this.direction;
    }
    public void setDirection(SpriteAnimation.Direction direction) {
        this.direction = direction;
    }

    private List<SpriteFrame> frames;
    public void applyFrames(List<ASEFrame> frames) {
        this.frames = new ArrayList<>();
        for (int frameNum = from; frameNum <= to; frameNum++) {
            this.frames.add(frames.get(frameNum));
        }
    }
    @Override
    public List<SpriteFrame> getFrames() {
        return frames;
    }

    @Override
    public String toString() {
        return "{" + name + ", " + from + "-" + to + ", " + direction + "}";
    }
}
