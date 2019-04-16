package luby.kids.game.loaders.tiled;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.logging.Logger;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.control.AbstractControl;

import luby.kids.tiled.Frame;
import luby.kids.tiled.TileWrapper;

public class AnimatedTileControl extends AbstractControl {
	static Logger logger = Logger.getLogger(AnimatedTileControl.class.getName());

	private TSXTileSet tileSet;
	private TileWrapper tile;
	private List<Frame> frames;
	private int currentFrameIndex;
	private float unusedTime;

	public AnimatedTileControl(TSXTileSet tileSet, TileWrapper tile) {
		this.tileSet = tileSet;
		this.tile = tile;
		this.frames = tile.getTile().getAnimation().getFrame();
		currentFrameIndex = 0;
		unusedTime = 0f;
	}

	@Override
	protected void controlUpdate(float tpf) {
		float ms = tpf * 1000;
		unusedTime += ms;
		Frame frame = frames.get(currentFrameIndex);
		int previousTileId = frame.getTileid();

		while (frame.getDuration() > 0 && unusedTime > frame.getDuration()) {
			unusedTime -= frame.getDuration();
			currentFrameIndex = (currentFrameIndex + 1) % frames.size();

			frame = frames.get(currentFrameIndex);
		}

		if (previousTileId != frame.getTileid()) {
			Geometry geom = (Geometry)spatial;
			Mesh mesh = geom.getMesh();

			Mesh frameMesh = tileSet.getGeometry(frame.getTileid()).getMesh();
			FloatBuffer data = (FloatBuffer)frameMesh.getBuffer(Type.TexCoord).getData();
			mesh.setBuffer(Type.TexCoord, 2, data);
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	public Object clone() {
		AnimatedTileControl control = new AnimatedTileControl(tileSet, tile);
		return control;
	}
}
