package luby.kids.game.loaders.tiled;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import luby.kids.game.loaders.AssetManagerUtils;
import luby.kids.tiled.MapWrapper;
import luby.kids.tiled.TileLayerWrapper;
import luby.kids.tiled.TileSet;
import luby.kids.tiled.TileWrapper;

public class TMXScene extends Node {
    private static final Logger logger = Logger.getLogger(TMXScene.class.getName());

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

    public float getWidth() {
        return (float)map.getMap().getWidth() * (float)map.getMap().getTilewidth();
    }
    public float getHeight() {
        return (float)map.getMap().getHeight() * (float)map.getMap().getTileheight();
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
        TSXTileSet[] tileSets = new TSXTileSet[map.getMap().getTileset().size()];
        List<TileSet> mapTileSets = new ArrayList<>(map.getMap().getTileset());
        mapTileSets.sort(Comparator.comparingInt(TileSet::getFirstgid).reversed());
        map.getMap().getTileset().sort(Comparator.comparingInt(TileSet::getFirstgid).reversed());
        for (int tileSetNum = 0; tileSetNum < mapTileSets.size(); tileSetNum++) {
            TileSet mapTileSet = mapTileSets.get(tileSetNum);
            String path = AssetManagerUtils.fixAssetPath(assetManager, mapTileSet.getSource());
            logger.log(Level.INFO, "Loading map tile-set " + path);
            TSXTileSet tileSet = (TSXTileSet)assetManager.loadAsset(path);
            tileSets[tileSetNum] = tileSet;
        }

        float mapWidth = getWidth();
        float mapHeight = getHeight();

        TileLayerWrapper[] layers = map.getTileLayers();
        for (int layerNum = 0; layerNum < layers.length; layerNum++) {
            TileLayerWrapper layer = layers[layerNum];
            Node layerNode = new Node(getName() + "-layer" + layerNum);
            layerNode.move(
                    (layer.getLayer().getOffsetx() != null ? (float)layer.getLayer().getOffsetx() : 0.0f),
                    -(layer.getLayer().getOffsety() != null ? (float)layer.getLayer().getOffsety() : 0.0f),
                    layerNum
            );
            int[][] globalTileIds = layer.getTileIds();
            for (int y = 0; y < globalTileIds.length; y++) {
                if (globalTileIds[y] != null) {
                    for (int x = 0; x < globalTileIds[y].length; x++) {
                        int globalTileId = globalTileIds[y][x];
                        if (globalTileId > 0) {
                            boolean found = false;
                            int tileSetNum = 0;
                            while (!found && tileSetNum < mapTileSets.size()) {
                                TileSet mapTileSet = mapTileSets.get(tileSetNum);
                                if (globalTileId >= mapTileSet.getFirstgid()) {
                                    int tileId = globalTileId - mapTileSet.getFirstgid();
                                    logger.log(Level.INFO, "TILE ID: " + globalTileId + " / " + tileId + " in " + tileSets[tileSetNum].getAssetInfo().toString());
                                    Geometry geometry = tileSets[tileSetNum].getGeometry(tileId).clone(true);
                                    TileWrapper tile = tileSets[tileSetNum].getTileSet().getTiles().get(tileId);
                                    float tileX = (float) x * (float) map.getMap().getTilewidth();
                                    float tileY = mapHeight - ((float)(y + 1) * (float) map.getMap().getTileheight());
                                    geometry.move(tileX, tileY, 0.0f);
                                    layerNode.attachChild(geometry);
                                    found = true;
                                }
                                tileSetNum++;
                            }
                        }
                    }
                }
            }
            attachChild(layerNode);
        }
    }
}
