package luby.kids.game.sprites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class SpriteAnimControl extends AbstractControl implements Cloneable {
    private Map<String, SpriteAnimation> animations = new HashMap<>();
    public Map<String, SpriteAnimation> getAnimations() {
        return this.animations;
    }
    public void setAnimations(Map<String, SpriteAnimation> animations) {
        this.animations = animations;
    }
    public void addAnimation(SpriteAnimation animation) {
        animations.put(animation.getName(), animation);
    }
    public void addAnimation(String name, SpriteAnimation animation) {
        animations.put(name, animation);
    }
    public void addAnimations(List<SpriteAnimation> animations) {
        animations.forEach(this::addAnimation);
    }
    public void addAnimations(Map<String, SpriteAnimation> animations) {
        animations.forEach(this::addAnimation);
    }

    public SpriteAnimControl() {
    }
    public SpriteAnimControl(List<SpriteAnimation> animations) {
        addAnimations(animations);
    }
    public SpriteAnimControl(Map<String, SpriteAnimation> animations) {
        setAnimations(animations);
    }

    private List<SpriteAnimEventListener> listeners = new ArrayList<>();

    public void addListener(SpriteAnimEventListener listener) {
        if (this.listeners.contains(listener)) {
            throw new IllegalArgumentException("The given listener is already registered in this control");
        } else {
            this.listeners.add(listener);
        }
    }
    public void removeListener(SpriteAnimEventListener listener) {
        if (!this.listeners.remove(listener)) {
            throw new IllegalArgumentException("The given listener is not registered in this control");
        }
    }
    public void clearListeners() {
        this.listeners.clear();
    }

    void notifyAnimDone(SpriteAnimChannel channel, String animName) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            listeners.get(i).onSpriteAnimCycleDone(this, channel, animName);
        }
    }
    void notifyAnimChange(SpriteAnimChannel channel, String animName) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            listeners.get(i).onSpriteAnimCycleChange(this, channel, animName);
        }
    }

    private List<SpriteAnimChannel> channels = new ArrayList<>();
    public List<SpriteAnimChannel> getChannels() {
        return this.channels;
    }
    public void setChannels(List<SpriteAnimChannel> channels) {
        this.channels = channels;
    }
    public SpriteAnimChannel createChannel() {
        // For now limit to a single channel
        SpriteAnimChannel channel;
        if (channels.size() > 0) {
            channel = channels.get(0);
        } else {
            channel = new SpriteAnimChannel(this);
            channels.add(channel);
        }
        return channel;
    }

    @Override
    protected void controlUpdate(float tpf) {
        channels.forEach(channel -> channel.update(tpf));
    }

    @Override
    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {
    }

    public Object clone() {
        SpriteAnimControl control = new SpriteAnimControl(animations);
        return control;
    }
}
