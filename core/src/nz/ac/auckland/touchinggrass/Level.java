package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.math.Matrix4;

public abstract class Level {
    public abstract Scene setup(PlayScreen screen);
    public abstract void update(PlayScreen screen, Matrix4 projectionMatrix, float deltaTime, float stateTime);
    public abstract void teardown();
    public abstract Player getPlayer();
}
