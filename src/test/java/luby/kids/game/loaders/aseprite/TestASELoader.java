package luby.kids.game.loaders.aseprite;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.ModelKey;
import com.jme3.math.Vector3f;
import luby.kids.game.sprites.SpriteAnimChannel;
import luby.kids.game.sprites.SpriteAnimControl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import luby.kids.game.Game;
import luby.kids.game.GameTest;

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

                ASESprite sprite = (ASESprite)assetManager.loadAsset(new ModelKey("characters/Todd.ase-json"));
                assertEquals(1, sprite.getChildren().size());
                System.out.println("SPRITE "
                        + "name = " + sprite.getName() + ", "
                        + "assetName = " + sprite.getAssetInfo().toString() + ", "
                        + "numChildren = " + sprite.getChildren().size()
                );
                sprite.move((float)settings.getWidth() / 2.0f, (float)settings.getHeight() / 2.0f, 0.0f);
                guiNode.attachChild(sprite);
                SpriteAnimControl control = sprite.getControl(SpriteAnimControl.class);
                SpriteAnimChannel channel = control.createChannel();
                channel.setAnim("right-walk");
            }
        };
        game.start();
    }

}

