package luby.kids.game.loaders.tiled;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.*;

import luby.kids.tiled.TiledReader;

public class TMXLoader implements AssetLoader {
    private static final Logger logger = Logger.getLogger(TMXLoader.class.getName());

    protected AssetManager assetManager;

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        this.assetManager = assetInfo.getManager();

        AssetKey<?> assetKey = assetInfo.getKey();
        if (!(assetKey instanceof ModelKey)) {
            throw new AssetLoadException("Invalid asset key");
        }

        InputStream assetStream = null;
        try {
            assetStream = assetInfo.openStream();
            TMXScene scene = (TMXScene)load(assetInfo, assetStream);
            scene.setName(assetInfo.getKey().getName());
            return scene;
        } finally {
            if (assetStream != null) {
                assetStream.close();
            }
        }
    }

    public Object load(AssetInfo assetInfo, InputStream assetStream) throws IOException {
        TiledReader reader = new TiledReader();
        try {
            return new TMXScene(assetManager, assetInfo, reader.readTMX(assetStream));
        } catch(Exception e) {
            throw new IOException("Error loading TMX", e);
        }
    }
}
