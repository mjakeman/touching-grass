package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Entity extends SceneObject {
    Texture texture;

    public Entity(Texture texture) {
        this.texture = texture;
        this.position = new Vector3(0, 0, 0);
    }

    @Override
    public void draw(Matrix4 projectionMatrix, float stateTime) {
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(projectionMatrix);

        var vec = IsometricUtils.isoToScreen(position);
        batch.draw(texture, vec.x, vec.y);

        batch.end();
    }

    @Override
    public void drawBatched(SpriteBatch batch, float stateTime) {
        var vec = IsometricUtils.isoToScreen(position);
        batch.draw(texture, vec.x, vec.y);
    }
}
