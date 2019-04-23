package luby.kids.game.asset;

import com.jme3.asset.AssetKey;

public class GameAssetKey extends AssetKey {
    public GameAssetKey(String name) {
        super(name);
        extension = AssetKeyUtils.getExtension(name);
    }
}
