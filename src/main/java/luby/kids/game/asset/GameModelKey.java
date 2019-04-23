package luby.kids.game.asset;

import com.jme3.asset.ModelKey;

public class GameModelKey extends ModelKey {
    public GameModelKey(String name) {
        super(name);
        extension = AssetKeyUtils.getExtension(name);
    }
}
