package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class MowableTile extends Tile {
    TiledMapTile dirt;
    public MowableTile(TiledMapTile tile, TiledMapTile dirt, float opacity, int row, int col, int layerY) {
        super(tile, opacity, row, col, layerY);
        this.dirt = dirt;
    }

    public void mow() {
        this.tile = dirt;
    }
}
