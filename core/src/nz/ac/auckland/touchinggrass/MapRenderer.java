package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
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


    public MapRenderer() {
        tiledMap = new TmxMapLoader().load("isometric-sandbox-map.tmx");
        grass = new Texture(Gdx.files.internal("grass.png"));
        //sky = new Texture(Gdx.files.internal("sky.png"));
        map = generateMap();
    }

    public void drawGround(SpriteBatch batch) {
        for (var layer : tiledMap.getLayers()) {
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

                var cell = layer.getCell(col, row);
                if (cell == null) continue;

                var tile = cell.getTile();
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            map = generateMap();
        }
    }

    private int[][] generateMap(){
        Random r = new Random();
        int rSize = 50;//r.nextInt(25);

        if(rSize < 10) {
            rSize = 10;
        }

        int[][] map = new int[rSize][rSize];

        for(int row = 0; row < map.length; row++) {
            for(int col = 0; col < map.length; col++) {
                int num = r.nextInt(10);

                if(num > 7) {
                    map[row][col] = 0;
                }else {
                    map[row][col] = 1;
                }
            }
        }

        return map;
    }
}
