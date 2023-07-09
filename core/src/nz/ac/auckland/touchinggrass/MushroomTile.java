package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class MushroomTile extends Tile {
    TiledMapTile mushroom;
    public MushroomTile(TiledMapTile tile, TiledMapTile mushroom, float opacity, int row, int col, int layerY) {
        super(tile, opacity, row, col, layerY);
        this.mushroom = mushroom;
    }

    public void destroy() {
        this.tile = null;
    }
}
