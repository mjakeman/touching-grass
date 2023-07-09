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

    public SceneObject testAABBCollisions(SceneObject test) {
        var bbox1 = test.getBoundingBox();
        for (var object : objects) {
            var bbox2 = object.getBoundingBox();
            if (!object.doesCollision) continue;
            if (object.position.y != test.position.y) continue;

            if (bbox1.x < bbox2.x + bbox2.width &&
                bbox1.x + bbox1.width > bbox2.x &&
                bbox1.y < bbox2.y + bbox2.height &&
                bbox1.y + bbox1.height > bbox2.y
            ) {
                // Collision detected!
                return object;
            }
        }

        return null;
    }

}
