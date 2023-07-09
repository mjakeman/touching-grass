package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

import static com.badlogic.gdx.graphics.g2d.Batch.*;
import static com.badlogic.gdx.graphics.g2d.Batch.V4;

public class MapRenderer {

    private final static int NUM_VERTICES = 20;
    protected float[] vertices = new float[NUM_VERTICES];

    private int [][] map;
    private Texture grass;
    private TiledMap tiledMap;


    public MapRenderer(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public void drawGround(SpriteBatch batch) {
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer instanceof TiledMapTileLayer) {
                drawLayer(batch, (TiledMapTileLayer) layer);
            }
        }
    }

    private void drawLayer(SpriteBatch batch, TiledMapTileLayer layer) {
        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        for(int row = layer.getWidth() - 1; row >= 0; row--) {
            for(int col = 0; col <= layer.getWidth() - 1; col++) {
                Vector2 coords = IsometricUtils.isoToScreen(new Vector3(row, 0, col));

                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;

                TiledMapTile tile = cell.getTile();
                if (tile == null) continue;

                TextureRegion region = tile.getTextureRegion();

                float x1 = coords.x;
                float y1 = coords.y;
                float x2 = coords.x + region.getRegionWidth();
                float y2 = coords.y + region.getRegionHeight();

                float u1 = region.getU();
                float v1 = region.getV2();
                float u2 = region.getU2();
                float v2 = region.getV();

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

//                if(cell.getTile() == ) {
//                    batch.draw(grass, coords.x, coords.y, TILE_WIDTH, TILE_HEIGHT);
//                }else {

//                }
            }
        }
    }

    private boolean isGrassTile(TiledMapTile tile) {
        int tileId = tile.getId() - 1;
        return tileId == 7 || tileId == 8 || tileId == 27 || tileId == 28;
    }
    private boolean isFlagTile(TiledMapTile tile) {
        int tileId = tile.getId() - 1;
        return tileId == 127;
    }

    private boolean isMushroomTile(TiledMapTile tile) {
        int tileId = tile.getId() - 1;
        return tileId == 128;
    }

    public void constructGround(Scene scene) {
        int layerIndex = 0;
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer instanceof TiledMapTileLayer) {
                constructLayer(scene, (TiledMapTileLayer) layer, layerIndex++);
            }
        }
    }

    private void constructLayer(Scene scene, TiledMapTileLayer layer, int layerIndex) {
        TiledMapTile dirtTile = tiledMap.getTileSets().getTile(89-1); // dirt
        TiledMapTile flagTile = tiledMap.getTileSets().getTile(128-1); // flag
        TiledMapTile mushroomTile = tiledMap.getTileSets().getTile(129-1); // mushroom

        for (int row = layer.getWidth() - 1; row >= 0; row--) {
            for (int col = 0; col <= layer.getWidth() - 1; col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;

                TiledMapTile tile = cell.getTile();
                if (tile == null) continue;

                // THIS IS GRASS
                if (isGrassTile(tile)) {
                    scene.addObject(new MowableTile(tile, dirtTile, layer.getOpacity(), row, col, layerIndex));
                    continue;
                }

                if (isFlagTile(tile)) {
                    scene.addObject(new FlagTile(tile, flagTile, layer.getOpacity(), row, col, layerIndex));
                    continue;
                }

                if (isMushroomTile(tile)) {
                    scene.addObject(new MushroomTile(tile, mushroomTile, layer.getOpacity(), row, col, layerIndex));
                    continue;
                }

                scene.addObject(new Tile(tile, layer.getOpacity(), row, col, layerIndex));
            }
        }
    }
}
