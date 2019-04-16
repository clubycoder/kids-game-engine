package luby.kids.game.loaders.tiled;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import luby.kids.tiled.MapWrapper;

public class TMXScene extends Node {
    public static String SUFFIX = "-tmx";

    protected AssetManager assetManager;
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    protected AssetInfo assetInfo;
    public AssetInfo getAssetInfo() {
        return this.assetInfo;
    }
    public void setAssetInfo(AssetInfo assetInfo) {
        this.assetInfo = assetInfo;
    }

    protected MapWrapper map;
    public void setMap(MapWrapper map) {
        this.map = map;
    }
    public MapWrapper getMap() {
        return this.map;
    }

    public TMXScene(AssetManager assetManager, AssetInfo assetInfo, MapWrapper map) {
        super(assetInfo.getKey().getName() + SUFFIX);
        setAssetManager(assetManager);
        setAssetInfo(assetInfo);
        setMap(map);
        generate();
    }

    public TMXScene(AssetManager assetManager, String name, MapWrapper map) {
        super(name);
        setAssetManager(assetManager);
        setMap(map);
        generate();
    }

    protected void generate() {
    }
}
