package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class FlagTile extends Tile {
    TiledMapTile flag;
    public FlagTile(TiledMapTile tile, TiledMapTile flag, float opacity, int row, int col, int layerY) {
        super(tile, opacity, row, col, layerY);
        this.flag = flag;
    }

    public void destroy() {
        this.tile = null;
    }
}
