package luby.kids.game.loaders.tiled;

import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import luby.kids.game.GameTest;
import org.junit.Test;

import com.jme3.math.Vector3f;

import luby.kids.game.Game;
import org.lwjgl.Sys;

public class TestTSXLoader {
    private static final Logger logger = Logger.getLogger(TestTSXLoader.class.getName());

    @Test
    public void testTSXLoader() throws Exception {
        final Game game = new GameTest(getClass().getCanonicalName() + "-testTSXLoader") {
            @Override
            public void gameSetup() {
                super.gameSetup();
                set2D();
                cam.setLocation(new Vector3f(0, 0, 0.5f));

                Node tile1 = loadTile("tile-thin-grass.png");
                logger.log(Level.INFO, "A Num Triangles: " + tile1.getTriangleCount());
                tile1.move(0, settings.getHeight() / 2 - 64, 0);
                guiNode.attachChild(tile1);

                TSXTileSet tileSet = (TSXTileSet)assetManager.loadAsset("tileset1.tsx");
                assertEquals(4, tileSet.getTileSet().getTiles().size());
                tileSet.getTileSet().getTiles().keySet().forEach(tileId -> {
                    Node node = new Node(tileId + "-tile");
                    Geometry geometry = tileSet.getGeometry(tileId).clone(true);
                    FloatBuffer uvs = (FloatBuffer)geometry.getMesh().getBuffer(VertexBuffer.Type.TexCoord).getData();
                    System.out.print("GEO[" + tileId + "] "
                            + "name = " + geometry.getName() + ", "
                            + "material = " + geometry.getMaterial().getName() + ", "
                            + "UVs = "
                    );
                    for (int i = 0; i < 8; i += 2) {
                        System.out.print("(" + uvs.get(i) + ", " + uvs.get(i + 1) + "), ");
                    }
                    System.out.println();
                    node.attachChild(geometry);
                    node.move(tileId * 64, settings.getHeight() / 2, 0);
                    guiNode.attachChild(node);
                });
            }
        };
        game.start();
    }

}