package luby.kids.game.loaders.aseprite;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import luby.kids.game.utils.gson.SerializedRequired;

/**
 * "meta": {
 *   "app": "http://www.aseprite.org/",
 *   "version": "1.3-dev",
 *   "image": "Todd.spritesheet.png",
 *   "format": "RGBA8888",
 *   "size": { "w": 96, "h": 128 },
 *   "scale": "1",
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
 *   "layers": [
 *    { "name": "Sprite Sheet", "opacity": 255, "blendMode": "normal" }
 *   ],
 *   "slices": [
 *   ]
 *  }
 */
public class ASEMeta {
    @SerializedName("app")
    private String app;
    public String getApp() {
        return this.app;
    }
    public void setApp(String app) {
        this.app = app;
    }

    @SerializedName("version")
    private String version;
    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    @SerializedName("image")
    private String imagePath;
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @SerializedName("size")
    @SerializedRequired
    private ASESize imageSize;
    public ASESize getImageSize() {
        return this.imageSize;
    }
    public void setImageSize(ASESize imageSize) {
        this.imageSize = imageSize;
    }

    @SerializedName("frameTags")
    private List<ASEAnimation> animations;
    public List<ASEAnimation> getAnimations() {
        return this.animations;
    }
    public void setAnimations(List<ASEAnimation> animations) {
        this.animations = animations;
    }

    @Override
    public String toString() {
        StringBuilder animationsString = new StringBuilder();
        if (animations != null && animations.size() > 0) {
            animationsString.append("{");
            animations.forEach(animation -> {
                animationsString.append("    " + animation.getName() + " = " + animation);
            });
            animationsString.append("  }");
        } else {
            animationsString.append("none");
        }
        return "{\n"
                + "  app = " + (app != null ? app : "unknown") + ",\n"
                + "  version = " + (version != null ? version : "unknown") + ",\n"
                + "  imagePath = " + (imagePath != null ? imagePath : "none") + ",\n"
                + "  imageSize = " + imageSize + ",\n"
                + "  animations = " + animationsString.toString() + ",\n"
                + "}";
    }
}
