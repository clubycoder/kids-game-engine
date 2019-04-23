package luby.kids.game.loaders.aseprite;

import java.nio.FloatBuffer;

import com.google.gson.annotations.SerializedName;

import luby.kids.game.sprites.SpriteFrame;
import luby.kids.game.utils.gson.SerializedRequired;

/**
 * {
 *     "frame": { "x": 0, "y": 0, "w": 32, "h": 32 },
 *     "rotated": false,
 *     "trimmed": false,
 *     "spriteSourceSize": { "x": 0, "y": 0, "w": 32, "h": 32 },
 *     "sourceSize": { "w": 32, "h": 32 },
 *     "duration": 250
 * }
 */
public class ASEFrame implements SpriteFrame {
    @SerializedName("frame")
    @SerializedRequired
    private ASERect frame;
    public ASERect getFrame() {
        return this.frame;
    }
    public void setFrame(ASERect frame) {
        this.frame = frame;
    }

    @SerializedName("spriteSourceSize")
    @SerializedRequired
    private ASERect spriteSourceSize;
    public ASERect getSpriteSourceSize() {
        return this.spriteSourceSize;
    }
    public void setSpriteSourceSize(ASERect spriteSourceSize) {
        this.spriteSourceSize = spriteSourceSize;
    }

    @SerializedName("sourceSize")
    @SerializedRequired
    private ASESize sourceSize;
    public ASESize getSourceSize() {
        return this.sourceSize;
    }
    public void setSourceSize(ASESize sourceSize) {
        this.sourceSize = sourceSize;
    }

    @SerializedName("duration")
    @SerializedRequired
    private int duration;
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    private FloatBuffer textureCoordinates;
    public FloatBuffer getTextureCoordinates() {
        return this.textureCoordinates;
    }
    public void setTextureCoordinates(FloatBuffer textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }

    @Override
    public String toString() {
        return "{" + frame + ", " + spriteSourceSize + ", " + sourceSize + ", " + duration + "}";
    }
}
