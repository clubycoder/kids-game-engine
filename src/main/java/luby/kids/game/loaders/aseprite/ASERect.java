package luby.kids.game.loaders.aseprite;

import com.google.gson.annotations.SerializedName;

import luby.kids.game.utils.gson.SerializedRequired;

public class ASERect {
    @SerializedName("x")
    @SerializedRequired
    private int x;
    public int getX() {
        return this.x;
    }
    public void setX(int x) {
        this.x = x;
    }

    @SerializedName("y")
    @SerializedRequired
    private int y;
    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }

    @SerializedName("w")
    @SerializedRequired
    private int width;
    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    @SerializedName("h")
    @SerializedRequired
    private int height;
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "(" + x  + ", " + y + ") - (" + (x + width) + ", " + (y + height) + ")";
    }
}
