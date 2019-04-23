package luby.kids.game.loaders.aseprite;

import java.util.logging.Logger;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;

import luby.kids.game.Game;
import luby.kids.game.GameTest;
import luby.kids.game.asset.GameModelKey;
import luby.kids.game.sprites.SpriteAnimChannel;
import luby.kids.game.sprites.SpriteAnimControl;

public class TestASELoader {
    private static final Logger logger = Logger.getLogger(TestASELoader.class.getName());

    @Test
    public void testASELoader() throws Exception {
        final Game game = new GameTest(getClass().getCanonicalName() + "-testASELoader") {
            @Override
            public void gameSetup() {
                super.gameSetup();
                set2D();
                cam.setLocation(new Vector3f(0, 0, 0.5f));

                final ASESprite sprite = (ASESprite)assetManager.loadAsset(new GameModelKey("characters/Todd.ase.json"));
                assertEquals(1, sprite.getChildren().size());
                System.out.println("SPRITE "
                        + "name = " + sprite.getName() + ", "
                        + "assetName = " + sprite.getAssetInfo().toString() + ", "
                        + "numChildren = " + sprite.getChildren().size()
                );
                sprite.move((float)settings.getWidth() / 2.0f, (float)settings.getHeight() / 2.0f, 0.0f);
                sprite.scale(4.0F, 4.0F, 0.0F);
                guiNode.attachChild(sprite);
                SpriteAnimControl control = sprite.getControl(SpriteAnimControl.class);
                final SpriteAnimChannel channel = control.createChannel();
                channel.setAnim("right-walk");

                ActionListener actionListener = new ActionListener() {
                    @Override
                    public void onAction(String name, boolean isPressed, float tpf) {
                        switch (name) {
                            case InputActions.UP: channel.setAnim("up-walk"); break;
                            case InputActions.DOWN: channel.setAnim("down-walk"); break;
                            case InputActions.LEFT: channel.setAnim("left-walk"); break;
                            case InputActions.RIGHT: channel.setAnim("right-walk"); break;
                        }
                    }
                };
                inputManager.addListener(actionListener, InputActions.all);
            }
        };
        game.start();
    }

}

