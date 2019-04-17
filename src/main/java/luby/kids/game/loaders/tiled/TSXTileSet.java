package luby.kids.game.loaders.tiled;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.MaterialList;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

import luby.kids.game.loaders.AssetManagerUtils;
import luby.kids.tiled.Animation;
import luby.kids.tiled.Image;
import luby.kids.tiled.TileSetWrapper;
import luby.kids.tiled.TileWrapper;

public class TSXTileSet {
    private static final Logger logger = Logger.getLogger(TSXTileSet.class.getName());

    public static String SUFFIX = "-tsx";

    protected AssetManager assetManager;
    public AssetManager getAssetManager() {
        return this.assetManager;
    }
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    protected AssetInfo assetInfo;
    public AssetInfo getAssetInfo() {
        return this.assetInfo;
    }
    public void setAssetInfo(AssetInfo assetInfo) {
        this.assetInfo = assetInfo;
    }

    protected TileSetWrapper tileSet;
    public TileSetWrapper getTileSet() {
        return this.tileSet;
    }
    public void setTileSet(TileSetWrapper tileSet) {
        this.tileSet = tileSet;
    }

    protected MaterialList materials;
    protected Map<Integer, Geometry> geometries;

    public TileWrapper getTile(int tileId) {
        return tileSet.getTiles().get(tileId);
    }
    public Geometry getGeometry(int tileId) {
        return geometries.get(tileId);
    }

    public TSXTileSet(AssetManager assetManager, AssetInfo assetInfo, TileSetWrapper tileSet) {
        setAssetManager(assetManager);
        setAssetInfo(assetInfo);
        setTileSet(tileSet);
        generate();
    }

    protected Material getMaterial(Image image) {
        if (image == null || image.getSource() == null) {
            return null;
        }
        String path = AssetManagerUtils.fixAssetPath(assetManager, image.getSource());
        logger.log(Level.INFO, "MATERIAL: " + path);
        if (!materials.containsKey(path)) {
            Texture2D texture = null;
            TextureKey textureKey = new TextureKey(path, true);
            textureKey.setGenerateMips(false);
            texture = (Texture2D)assetManager.loadTexture(textureKey);
            texture.setWrap(Texture.WrapMode.Repeat);
            texture.setMagFilter(Texture.MagFilter.Nearest);

            Material material = new Material(assetManager, "TileMap/MatDefs/Tile/Tile.j3md");
            material.setName(path + "-tilemap");
            material.setTexture("ColorMap", texture);
            if (image.getTrans() != null) {
                ColorRGBA transparentColor = MaterialUtils.toColorRGBA(image.getTrans());
                material.setColor("TransColor", transparentColor);
            }

            materials.put(path, material);
        }
        return materials.get(path);
    }

    protected void generate() {
        logger.log(Level.INFO, "TileSet: num-tiles = " + tileSet.getTiles().size());

        materials = new MaterialList();
        geometries = new HashMap<>();
        tileSet.getTiles().forEach((tileId, tile) -> {
            String name = tileSet.getTileSet().getName() + "-" + tile.getTile().getId() + "-tile";
            logger.log(Level.INFO, "TILE[" + tile.getTile().getId() + "]: "
                    + "name = " + name + ", "
                    + "x-offset = " + tile.getOffset().getX() + ", "
                    + "y-offset = " + tile.getOffset().getY()
            );
            Material material = getMaterial(tile.getTile().getImage());
            float xOffset = tile.getOffset().getX();
            float yOffset = tile.getOffset().getY();
            int width = tile.getWidth();
            int height = tile.getHeight();
            Texture2D texture = (Texture2D)(
                    material.getTextureParam("ColorMap") != null ?
                    material.getTextureParam("ColorMap") :
                    material.getTextureParam("Texture")
            ).getTextureValue();
            float textureWidth = texture.getImage().getWidth();
            float textureHeight = texture.getImage().getHeight();

            // Texture coordinates
            float u0 = xOffset / textureWidth;
            float v0 = (textureHeight - yOffset - height) / textureHeight;
            float u1 = (xOffset + width) / textureWidth;
            float v1 = (textureHeight - yOffset) / textureHeight;
            float[] textureCoordinates = new float[] { u0, v0, u1, v0, u1, v1, u0, v1 };

            // Mesh
            Geometry geometry = new Geometry(name, new Quad(width, height, true));
            geometry.getMesh().setBuffer(VertexBuffer.Type.TexCoord, 2, textureCoordinates);
            geometry.setMaterial(material);

            // If this tile has animation frames, attach an animation controller to it
            Animation animation = tile.getTile().getAnimation();
            if (animation != null && animation.getFrame() != null && animation.getFrame().size() > 1) {
                geometry.setBatchHint(Spatial.BatchHint.Never);

                AnimatedTileControl control = new AnimatedTileControl(this, tile);
                geometry.addControl(control);
            }

            geometries.put(tile.getTile().getId(), geometry);
        });
    }
}
