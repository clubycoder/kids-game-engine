package luby.kids.game.loaders.aseprite;

import com.google.gson.annotations.SerializedName;
import luby.kids.game.utils.gson.SerializedRequired;

public class ASESize {
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
       return width + "x" + height;
   }
}
