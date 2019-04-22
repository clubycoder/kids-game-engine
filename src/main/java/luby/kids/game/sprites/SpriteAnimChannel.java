package luby.kids.game.sprites;

import com.jme3.animation.LoopMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;

public class SpriteAnimChannel {
    private SpriteAnimControl control;
    public SpriteAnimControl getControl() {
        return control;
    }
    public void setControl(SpriteAnimControl control) {
        this.control = control;
    }

    public SpriteAnimChannel(SpriteAnimControl control) {
        setControl(control);
    }

    private float time;
    public float getTime() {
        return this.time;
    }
    public void setTime(float time) {
        this.time = time;
    }

    private float speed;
    public float getSpeed() {
        return this.speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private LoopMode loopMode = LoopMode.DontLoop;
    public LoopMode getLoopMode() {
        return loopMode;
    }
    public void setLoopMode(LoopMode loopMode) {
        this.loopMode = loopMode;
    }

    private int frameNum;
    private float frameRemainingDuration;
    public int getFrameNum() {
        return this.frameNum;
    }
    public void setFrameNum(int frameNum) {
        if (frameNum < 0) {
            throw new IllegalArgumentException("Frame number can't be negative");
        }
        this.frameNum = frameNum;
        if (animation != null) {
            if (frameNum > animation.getFrames().size() - 1) {
                throw new IllegalArgumentException("Frame number out of range");
            }
            frameRemainingDuration = animation.getFrames().get(frameNum).getDuration();
        }
    }

    private SpriteAnimation animation;
    private SpriteAnimation.Direction pingpongDirection = SpriteAnimation.Direction.FORWARD;
    public void setAnim(String animName) {
        if (animName == null) {
            throw new IllegalArgumentException("Animation " + animName + " cannot be null");
        } else {
            SpriteAnimation anim = control.getAnimations().get(animName);
            if (anim == null) {
                throw new IllegalArgumentException("Cannot find animation named: '" + animName + "'");
            } else {
                control.notifyAnimChange(this, animName);
                animation = anim;
                setTime(0.0F);
                setSpeed(1.0F);
                pingpongDirection = SpriteAnimation.Direction.FORWARD;
                rewind();
                play();
            }
        }
        notified = false;
    }
    public SpriteAnimation getAnimation() {
        return animation;
    }
    public String getAnimationName() {
        return (animation != null ? this.animation.getName() : null);
    }
    public void rewind() {
        setTime(0.0F);
        if (animation != null && animation.getDirection() == SpriteAnimation.Direction.REVERSE) {
            setFrameNum(animation.getFrames().size() - 1);
        } else {
            setFrameNum(0);
        }
    }
    private boolean isPaused = true;
    public void pause() {
        isPaused = true;
    }
    public void play() {
        isPaused = false;
        notified = false;
    }

    public void reset(boolean rewind) {
        if (rewind) {
            this.rewind();
            update(time);
        }

        animation = null;
        notified = false;
    }

    private boolean notified = false;

    public void update(float tpf) {
        if (animation != null && !isPaused) {
            time += tpf * 1000 * this.speed;
            int prevFrameNum = frameNum;
            boolean done = false;
            boolean cycleDone = false;
            while (time > 0 && !done) {
                frameRemainingDuration -= time;
                if (frameRemainingDuration >= 0) {
                    // All time was used up
                    time = 0.0F;
                } else {
                    // Retrieve the overflow time
                    time = -frameRemainingDuration;
                }
                if (frameRemainingDuration <= 0) {
                    switch (animation.getDirection()) {
                        case REVERSE: {
                            if (frameNum == 0) {
                                cycleDone = true;
                                if (loopMode != LoopMode.DontLoop) {
                                    setFrameNum(animation.getFrames().size() - 1);
                                } else {
                                    done = true;
                                }
                            } else {
                                setFrameNum(frameNum - 1);
                            }
                        } break;
                        case PINGPONG: {
                            if (pingpongDirection == SpriteAnimation.Direction.REVERSE) {
                                if (frameNum == 0) {
                                    cycleDone = true;
                                    pingpongDirection = SpriteAnimation.Direction.FORWARD;
                                    if (loopMode != LoopMode.DontLoop) {
                                        setFrameNum(1);
                                    } else {
                                        done = true;
                                    }
                                } else {
                                    setFrameNum(frameNum - 1);
                                }
                            } else {
                                if (frameNum == animation.getFrames().size() - 1) {
                                    cycleDone = true;
                                    pingpongDirection = SpriteAnimation.Direction.REVERSE;
                                    if (loopMode != LoopMode.DontLoop) {
                                        setFrameNum(animation.getFrames().size() - 2);
                                    } else {
                                        done = true;
                                    }
                                } else {
                                    setFrameNum(frameNum + 1);
                                }
                            }
                        } break;
                        default: {
                            if (frameNum == animation.getFrames().size() - 1) {
                                cycleDone = true;
                                if (loopMode != LoopMode.DontLoop) {
                                    setFrameNum(0);
                                } else {
                                    done = true;
                                }
                            } else {
                                setFrameNum(frameNum + 1);
                            }
                        } break;
                    }
                }
            }
            if (frameNum != prevFrameNum) {
                Node spriteNode = (Node)control.getSpatial();
                // Assume only one piece of geometry for now
                Geometry geom = (Geometry)spriteNode.getChild(0);
                Mesh mesh = geom.getMesh();

                mesh.setBuffer(Type.TexCoord, 2, animation.getFrames().get(frameNum).getTextureCoordinates());
            }
            if (cycleDone) {
                control.notifyAnimDone(this, animation.getName());
            }
        }
    }
}
