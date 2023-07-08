package nz.ac.auckland.touchinggrass;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import nz.ac.auckland.touchinggrass.IsometricUtils;

public class MapRenderer {

    public static final int TILE_WIDTH = 34;
    public static final int TILE_HEIGHT = 34;

    private int [][] map;
    private Texture grass;
    //private Texture sky;


    public MapRenderer() {
        grass = new Texture(Gdx.files.internal("grass.png"));
        //sky = new Texture(Gdx.files.internal("sky.png"));
        map = generateMap();
    }

    public void drawGround(SpriteBatch batch) {
        for(int row = map.length - 1; row >= 0; row--) {
            for(int col = map.length - 1; col >= 0; col--) {
                Vector2 coords = IsometricUtils.isoToScreen(new Vector3(row, 0, col));

                if(map[row][col] == 1) {
                    batch.draw(grass, coords.x, coords.y, TILE_WIDTH, TILE_HEIGHT);
                }else {

                }
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
