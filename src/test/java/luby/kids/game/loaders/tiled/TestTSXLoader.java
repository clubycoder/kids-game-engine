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

                TSXTileSet tileSet = (TSXTileSet)assetManager.loadAsset("tileset1.tsx");
                assertEquals(4, tileSet.getTileSet().getTiles().size());
                tileSet.getTileSet().getTiles().keySet().forEach(tileId -> {
                    Node node = new Node(tileId + "-tile");
                    Geometry geometry = tileSet.getGeometry(tileId).clone(true);
                    FloatBuffer uvsBuffer = (FloatBuffer)geometry.getMesh().getBuffer(VertexBuffer.Type.TexCoord).getData();

                    System.out.print("GEO[" + tileId + "] "
                            + "name = " + geometry.getName() + ", "
                            + "material = " + geometry.getMaterial().getName() + ", "
                            + "UVs = "
                    );
                    for (int i = 0; i < 8; i += 2) {
                        System.out.print("(" + uvsBuffer.get(i) + ", " + uvsBuffer.get(i + 1) + "), ");
                    }
                    System.out.println();

                    assertEquals("tileset1-" + tileId + "-tile", geometry.getName());
                    assertEquals("tileset1.png-tilemap", geometry.getMaterial().getName());
                    float[] uvs = new float[8];
                    for (int i = 0; i < 8; i += 1) {
                        uvs[i] = uvsBuffer.get(i);
                    }
                    float[] uvsExpected = (
                            tileId == 0 ? new float[] { 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f } : (
                            tileId == 1 ? new float[] { 0.5f, 0.5f, 1.0f, 0.5f, 1.0f, 1.0f, 0.5f, 1.0f } : (
                            tileId == 2 ? new float[] { 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 0.5f, 0.0f, 0.5f } :
                                          new float[] { 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.5f }
                    )));
                    assertArrayEquals(uvsExpected, uvs, 0.0f);

                    node.attachChild(geometry);
                    node.move(tileId * 64, (float)settings.getHeight() / 2.0f, 0.0f);
                    guiNode.attachChild(node);
                });
            }
        };
        game.start();
    }

}