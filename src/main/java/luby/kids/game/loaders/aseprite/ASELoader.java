package luby.kids.game.loaders.aseprite;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jme3.asset.*;

public class ASELoader implements AssetLoader {
    private static final Logger logger = Logger.getLogger(ASELoader.class.getName());

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
            ASESprite sprite = (ASESprite)load(assetInfo, assetStream);
            sprite.setName(assetInfo.getKey().getName());
            return sprite;
        } finally {
            if (assetStream != null) {
                assetStream.close();
            }
        }
    }

    public Object load(AssetInfo assetInfo, InputStream assetStream) throws IOException {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement aseTree = jsonParser.parse(new InputStreamReader(assetStream));
            return new ASESprite(assetManager, assetInfo, aseTree);
        } catch(Exception e) {
            throw new IOException("Error loading ASE", e);
        }
    }
}
