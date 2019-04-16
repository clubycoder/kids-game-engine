package luby.kids.game.loaders.tiled;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;

import luby.kids.tiled.TiledReader;

public class TSXLoader implements AssetLoader {
    private static final Logger logger = Logger.getLogger(TSXLoader.class.getName());

    protected AssetManager assetManager;

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        this.assetManager = assetInfo.getManager();

        InputStream assetStream = null;
        try {
            assetStream = assetInfo.openStream();
            TSXTileSet tileSet = (TSXTileSet)load(assetInfo, assetStream);
            return tileSet;
        } finally {
            if (assetStream != null) {
                assetStream.close();
            }
        }
    }

    public Object load(AssetInfo assetInfo, InputStream assetStream) throws IOException {
        TiledReader reader = new TiledReader();
        try {
            return new TSXTileSet(assetManager, assetInfo, reader.readTSX(assetStream));
        } catch(Exception e) {
            throw new IOException("Error loading TSX", e);
        }
    }
}
