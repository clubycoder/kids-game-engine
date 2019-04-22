package luby.kids.game.sprites;

public interface SpriteAnimEventListener {
    void onSpriteAnimCycleDone(SpriteAnimControl control, SpriteAnimChannel channel, String animName);

    void onSpriteAnimCycleChange(SpriteAnimControl control, SpriteAnimChannel channel, String animName);
}
