package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public class SceneObject {
    public Vector3 position;
    protected boolean nonBatchable = false;

    public void draw(Matrix4 projectionMatrix, float stateTime) {

    }

    public void drawBatched(SpriteBatch batch, float stateTime) {

    }
}
