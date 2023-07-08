package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

public class Entity {
    Vector3 position;
    Texture texture;

    public Entity(Texture texture) {
        this.texture = texture;
        this.position = new Vector3(0, 0, 0);
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
