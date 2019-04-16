package luby.kids.game.loaders;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;

public class AssetManagerUtils {
    public static String fixAssetPath(AssetManager assetManager, String path) {
        // First try to find it as is
        if (assetManager.locateAsset(new AssetKey<Object>(path)) != null) {
            return path;
        }

        // Strip off path back and current references
        String path2 = path.replace("../", "").replace("./", "");
        if (assetManager.locateAsset(new AssetKey<Object>(path2)) != null) {
            return path2;
        }

        // Give up and let it fail downstream
        return path;
    }
}
