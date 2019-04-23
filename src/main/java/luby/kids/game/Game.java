package luby.kids.game;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

import luby.kids.game.loaders.aseprite.ASELoader;
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
        assetManager.registerLoader(ASELoader.class, "ase.json");
        inputSetup();
    }

    public static class InputActions {
        public static final String UP = "up";
        public static final String DOWN = "down";
        public static final String LEFT = "left";
        public static final String RIGHT = "right";

        public static final String[] all = { UP, DOWN, LEFT, RIGHT };
    }
    protected void inputSetup() {
        inputManager.addMapping(InputActions.UP, new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(InputActions.DOWN, new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping(InputActions.LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(InputActions.RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
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
}
