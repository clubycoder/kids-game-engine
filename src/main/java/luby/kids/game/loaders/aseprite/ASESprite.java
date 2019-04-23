package luby.kids.game.loaders.aseprite;

import java.nio.FloatBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gson.*;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;

import luby.kids.game.loaders.AssetManagerUtils;
import luby.kids.game.sprites.SpriteAnimControl;
import luby.kids.game.sprites.SpriteAnimation;
import luby.kids.game.utils.gson.JsonDeserializerWithValidation;

public class ASESprite extends Node {
    private static final Logger logger = Logger.getLogger(ASESprite.class.getName());

    public static String SUFFIX = "-ase";

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

    protected JsonElement aseTree;
    public JsonElement getASETree() {
        return this.aseTree;
    }
    public void setASETree(JsonElement aseTree) {
        this.aseTree = aseTree;
    }

    public ASESprite(AssetManager assetManager, AssetInfo assetInfo, JsonElement aseTree) {
        super(assetInfo.getKey().getName() + SUFFIX);
        setAssetManager(assetManager);
        setAssetInfo(assetInfo);
        setASETree(aseTree);
        generate();
    }

    public ASESprite(AssetManager assetManager, String name, JsonElement aseTree) {
        super(name);
        setAssetManager(assetManager);
        setASETree(aseTree);
        generate();
    }

    protected Material getMaterial(String spriteSheetPath) {
        String path = AssetManagerUtils.fixAssetPath(assetManager, spriteSheetPath, assetInfo.getKey().getName());
        logger.log(Level.INFO, "MATERIAL: " + path);
        Texture2D texture = null;
        TextureKey textureKey = new TextureKey(path, true);
        textureKey.setGenerateMips(false);
        texture = (Texture2D)assetManager.loadTexture(textureKey);
        texture.setWrap(Texture.WrapMode.Repeat);
        texture.setMagFilter(Texture.MagFilter.Nearest);

        Material material = new Material(assetManager, "Sprite/MatDefs/SpriteSheet/SpriteSheet.j3md");
        material.setName(path + "-spritesheet");
        material.setTexture("ColorMap", texture);

        return material;
    }

    protected void generate() {
        if (!aseTree.isJsonObject()) {
            throw new IllegalArgumentException("ASE tree is not an object");
        }
        JsonObject aseObject = aseTree.getAsJsonObject();

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(ASERect.class, new JsonDeserializerWithValidation<ASERect>())
                .registerTypeAdapter(ASESize.class, new JsonDeserializerWithValidation<ASESize>())
                .registerTypeAdapter(ASEAnimation.class, new JsonDeserializerWithValidation<ASEAnimation>())
                .registerTypeAdapter(ASEMeta.class, new JsonDeserializerWithValidation<ASEMeta>())
                .registerTypeAdapter(ASEFrame.class, new JsonDeserializerWithValidation<ASEFrame>())
                .create();

        if (!aseObject.has("meta") || !aseObject.get("meta").isJsonObject()) {
            throw new IllegalArgumentException("ASE is missing meta or not an object");
        }
        ASEMeta aseMeta = gson.fromJson(aseObject.get("meta"), ASEMeta.class);

        if (!aseObject.has("frames") || (!aseObject.get("frames").isJsonObject() && !aseObject.get("frames").isJsonArray())) {
            throw new IllegalArgumentException("ASE is missing frames or is not an object or array");
        }
        List<ASEFrame> aseFrames = new ArrayList<>();
        if (aseObject.get("frames").isJsonArray()) {
            aseObject.get("frames").getAsJsonArray().forEach(jsonElement -> {
                aseFrames.add(gson.fromJson(jsonElement, ASEFrame.class));
            });
        } else {
            JsonObject aseFramesObject = aseObject.get("frames").getAsJsonObject();
            aseFramesObject.keySet().forEach(frameKey -> {
                aseFrames.add(gson.fromJson(aseFramesObject.get(frameKey), ASEFrame.class));
            });
        }
        if (aseFrames.size() < 1) {
            throw new IllegalArgumentException("ASE must have at least 1 frame");
        }

        String name = assetInfo.toString() + "-sprite-ase";
        logger.log(Level.INFO, "SPRITE: "
            + "name = " + name + ", "
            + "meta = " + aseMeta + ", "
            + "frames = [\n  " + aseFrames.stream().map(ASEFrame::toString).collect(Collectors.joining("\n  ")) + "\n]"
        );

        // Material
        Material material = getMaterial(aseMeta.getImagePath());

        // Base settings
        float textureWidth = aseMeta.getImageSize().getWidth();
        float textureHeight = aseMeta.getImageSize().getHeight();
        int firstFrameWidth = aseFrames.get(0).getFrame().getWidth();
        int firstFrameHeight = aseFrames.get(0).getFrame().getHeight();

        // Texture Coordinates for each frame
        aseFrames.forEach(frame -> {
            float xOffset = frame.getFrame().getX();
            float yOffset = frame.getFrame().getY();
            int width = frame.getFrame().getWidth();
            int height = frame.getFrame().getHeight();
            float u0 = xOffset / textureWidth;
            float v0 = (textureHeight - yOffset - height) / textureHeight;
            float u1 = (xOffset + width) / textureWidth;
            float v1 = (textureHeight - yOffset) / textureHeight;
            FloatBuffer textureCoordinates = BufferUtils.createFloatBuffer(u0, v0, u1, v0, u1, v1, u0, v1);
            frame.setTextureCoordinates(textureCoordinates);
        });

        // Mesh
        Geometry geometry = new Geometry(name, new Quad(
                firstFrameWidth,
                firstFrameHeight,
                true
        ));
        geometry.getMesh().setBuffer(VertexBuffer.Type.TexCoord, 2, aseFrames.get(0).getTextureCoordinates());
        geometry.setMaterial(material);
        geometry.setBatchHint(Spatial.BatchHint.Never);
        geometry.move(-(firstFrameWidth / 2.0F), 0.0F, 0.0F);

        // Create the animation control
        SpriteAnimControl control = new SpriteAnimControl();
        control.createChannel();
        addControl(control);

        List<ASEAnimation> aseAnimations = aseMeta.getAnimations();
        // If there are no defined animations add the default one
        if (aseAnimations == null || aseAnimations.size() == 0) {
            aseAnimations = new ArrayList<>();
            ASEAnimation aseAnimation = new ASEAnimation();
            aseAnimation.setName("default");
            aseAnimation.setFrom(0);
            aseAnimation.setTo(aseFrames.size() - 1);
            aseAnimation.setDirection(SpriteAnimation.Direction.FORWARD);
            aseAnimations.add(aseAnimation);
        }
        // Apply frames to animations
        aseAnimations.forEach(animation -> {
            animation.applyFrames(aseFrames);
            control.addAnimation(animation);
        });

        attachChild(geometry);
    }
}
