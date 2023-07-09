package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.graphics.g2d.Batch.*;
import static com.badlogic.gdx.graphics.g2d.Batch.V4;

public class Tile extends SceneObject {

    private static final int NUM_VERTICES = 20;
    final float opacity;
    protected TiledMapTile tile;

    public Tile(TiledMapTile tile, float opacity, int row, int col, int layerY) {
        this.opacity = opacity;
        this.tile = tile;
        this.position = new Vector3(row, layerY, col);

        this.doesCollision = true;
    }

    @Override
    public void draw(Matrix4 projectionMatrix, float stateTime) {
        throw new UnsupportedOperationException("Cannot draw map tiles unbatched. Use drawBatched() instead");
    }

    @Override
    public void drawBatched(SpriteBatch batch, float stateTime) {
        Vector2 coords = IsometricUtils.isoToScreen(position);

        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * opacity);

        TextureRegion region = tile.getTextureRegion();

        float x1 = coords.x;
        float y1 = coords.y;
        float x2 = coords.x + region.getRegionWidth();
        float y2 = coords.y + region.getRegionHeight();

        float u1 = region.getU();
        float v1 = region.getV2();
        float u2 = region.getU2();
        float v2 = region.getV();

        float[] vertices = new float[NUM_VERTICES];

        vertices[X1] = x1;
        vertices[Y1] = y1;
        vertices[C1] = color;
        vertices[U1] = u1;
        vertices[V1] = v1;

        vertices[X2] = x1;
        vertices[Y2] = y2;
        vertices[C2] = color;
        vertices[U2] = u1;
        vertices[V2] = v2;

        vertices[X3] = x2;
        vertices[Y3] = y2;
        vertices[C3] = color;
        vertices[U3] = u2;
        vertices[V3] = v2;

        vertices[X4] = x2;
        vertices[Y4] = y1;
        vertices[C4] = color;
        vertices[U4] = u2;
        vertices[V4] = v1;

        batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
    }
}
