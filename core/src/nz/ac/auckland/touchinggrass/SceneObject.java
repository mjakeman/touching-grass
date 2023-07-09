package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public class SceneObject {
    public Vector3 position = new Vector3();
    protected boolean nonBatchable = false;
    protected boolean doesCollision = false;

    public BoundingBox getBoundingBox() {
        return new BoundingBox(position.x, position.z, 1, 1);
    }

    public void draw(Matrix4 projectionMatrix, float stateTime) {

    }

    public void drawBatched(SpriteBatch batch, float stateTime) {

    }
}
