package luby.kids.game.loaders;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;

public class AssetManagerUtils {
    private static final Logger logger = Logger.getLogger(AssetManagerUtils.class.getName());

    public static String fixAssetPath(AssetManager assetManager, String path, String relativeToAssetPath) {
        logger.log(Level.INFO, "Fixing: " + path + ", relativeTo: " + (relativeToAssetPath != null ? relativeToAssetPath : "none"));

        // Fix directory separator
        String fixedPath = path.replace('\\', '/');

        // First try to find it as is
        if (assetManager.locateAsset(new AssetKey<Object>(fixedPath)) != null) {
            return fixedPath;
        }

        // Strip off path back and current references
        {
            String newPath = fixedPath.replace("../", "").replace("./", "");
            if (assetManager.locateAsset(new AssetKey<Object>(newPath)) != null) {
                return newPath;
            }
        }

        // Try finding at root
        if (fixedPath.contains("/")) {
            String newPath = new File(path).getName();
            if (assetManager.locateAsset(new AssetKey<Object>(newPath)) != null) {
                return newPath;
            }
        }

        // Try finding relative to another asset
        if (relativeToAssetPath != null) {
            String fixedRelativeToAssetPath = relativeToAssetPath.replace('\\', '/');
            if (fixedRelativeToAssetPath.contains("/")) {
                String newPath = new File(fixedRelativeToAssetPath).getParentFile().getPath() + "/" + fixedPath;
                if (assetManager.locateAsset(new AssetKey<Object>(newPath)) != null) {
                    return newPath;
                }
            }
        }

        // Give up and let it fail downstream
        return fixedPath;
    }

    public static String fixAssetPath(AssetManager assetManager, String path) {
        return fixAssetPath(assetManager, path, null);
    }
}
