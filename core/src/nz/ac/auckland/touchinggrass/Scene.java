package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    List<SceneObject> objects;
    SpriteBatch batch;

    public Scene() {
        objects = new ArrayList<>();
        batch = new SpriteBatch();
    }

    public void addObject(SceneObject sceneObject) {
        objects.add(sceneObject);
    }

    public void draw(Matrix4 projectionMatrix, float stateTime) {
        objects.sort(this::closer);
        batch.begin();
        batch.setProjectionMatrix(projectionMatrix);
        for (var object : objects) {
            if (object.nonBatchable) {
                batch.end();

                object.draw(projectionMatrix, stateTime);

                batch.begin();
                batch.setProjectionMatrix(projectionMatrix);
            } else {
                object.drawBatched(batch, stateTime);
            }
        }
        batch.end();
    }

    private float nearness(SceneObject object) {
        return -0.5f*object.position.x + object.position.z;
    }

    private int closer(SceneObject a, SceneObject b) {
        if (a.position.y == b.position.y) {
            return nearness(a) > nearness(b)
                    ? 1
                    : -1;
        }

        return a.position.y > b.position.y
                ? 1
                : -1;
    }

}
