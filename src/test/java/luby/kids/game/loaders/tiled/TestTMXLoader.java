package luby.kids.game.loaders.tiled;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jme3.scene.Node;
import com.jme3.math.Vector3f;

import luby.kids.game.asset.GameModelKey;
import luby.kids.game.Game;
import luby.kids.game.GameTest;

public class TestTMXLoader {
    private static final Logger logger = Logger.getLogger(TestTMXLoader.class.getName());

    @Test
    public void testTMXLoader() throws Exception {
        final Game game = new GameTest(getClass().getCanonicalName() + "-testTMXLoader") {
            @Override
            public void gameSetup() {
                super.gameSetup();
                set2D();
                cam.setLocation(new Vector3f(0, 0, 0.5f));

                TMXScene map = (TMXScene)assetManager.loadAsset(new GameModelKey("map-test1.tmx"));
                assertEquals(2, map.getChildren().size());
                System.out.println("MAP "
                        + "name = " + map.getName() + ", "
                        + "assetName = " + map.getAssetInfo().toString() + ", "
                        + "numChildren = " + map.getChildren().size() + ", "
                        + "map = " + (map.getMap() != null ? "not-null" : "null")
                );
                for (int layerNum = 0; layerNum < map.getChildren().size(); layerNum++) {
                    Node layer = (Node)map.getChild(layerNum);
                    System.out.println("LAYER[" + layerNum + "] "
                            + "name = " + layer.getName() + ", "
                            + "numChildren = " + layer.getChildren().size()
                    );
                }
                // map.move(0, (float)settings.getHeight() / 2.0f, 0.0f);
                map.move(64.0f, -map.getHeight() + (float)settings.getHeight() - 64.0f, 0.0f);
                guiNode.attachChild(map);
            }
        };
        game.start();
    }

}

