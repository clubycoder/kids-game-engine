package luby.kids.game;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

import luby.kids.game.loaders.tiled.TMXLoader;
import luby.kids.game.loaders.tiled.TSXLoader;

public class Game extends SimpleApplication {
    public Game() {
        super();

        resolveSettings(null);
    }

    public Game(String title) {
        super();

        AppSettings initialSettings = new AppSettings(true);
        initialSettings.setTitle(title);
        resolveSettings(initialSettings);
    }

    public Game(String title, int width, int height) {
        super();

        AppSettings initialSettings = new AppSettings(true);
        initialSettings.setTitle(title);
        initialSettings.setWidth(width);
        initialSettings.setHeight(height);
        resolveSettings(initialSettings);
    }

    public Game(String title, AppSettings initialSettings) {
        super();

        initialSettings.setTitle(title);
        resolveSettings(initialSettings);
    }

    public Game(AppSettings initialSettings) {
        super();

        resolveSettings(initialSettings);
    }

    protected void resolveSettings(AppSettings initialSettings) {
        if (initialSettings != null) {
            if (settings == null) {
                setSettings(initialSettings);
            } else {
                settings.mergeFrom(initialSettings);
            }
        }
        if (settings == null) {
            setSettings(new AppSettings(true));
        }
        setShowSettings(false);
        setDisplayDebug(true);
    }

    public void setDisplayDebug(boolean enabled) {
        setDisplayStatView(enabled);
        setDisplayFps(enabled);
    }

    public void set2D() {
        cam.setParallelProjection(true);
        getFlyByCamera().setEnabled(false);
    }

    public void set3D() {
        cam.setParallelProjection(false);
        getFlyByCamera().setEnabled(true);
    }

    public void gameSetup() {
        assetManager.registerLoader(TSXLoader.class, "tsx");
        assetManager.registerLoader(TMXLoader.class, "tmx", "tmx.gz");
    }

    public void gameUpdate(float timePassed) {
    }

    public void gameDraw(RenderManager renderManager) {
    }

    @Override
    public void simpleInitApp() {
        gameSetup();
    }

    @Override
    public void simpleUpdate(float tpf) {
        gameUpdate(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        gameDraw(rm);
    }

    public Node loadTile(String name) {
        Node node = new Node("tile/" + name);

        // Load picture
        Picture pic = new Picture("tile-texture/" + name);
        Texture2D tex = (Texture2D)assetManager.loadTexture(name);
        pic.setTexture(assetManager, tex, true);

        // Adjust picture
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width);
        pic.setHeight(height);

        // Add a material to the picture
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);

        // Attach the picture to the node and return it
        node.attachChild(pic);
        return node;
    }
}
