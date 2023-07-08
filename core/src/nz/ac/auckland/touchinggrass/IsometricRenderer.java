package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class IsometricRenderer {
    public void draw(SpriteBatch batch, Entity entity) {
        var vec = IsometricUtils.isoToScreen(entity.position);
        batch.draw(entity.texture, vec.x, vec.y);
    }
}
