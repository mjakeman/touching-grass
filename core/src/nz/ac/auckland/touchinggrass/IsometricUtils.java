package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IsometricUtils {
    public static final int TILE_WIDTH = 34;
    public static final int TILE_HEIGHT = 34;

    public static Vector2 isoToScreen(Vector3 ptIso) {
        int scaleX = 16;  // Adjust this value for different scaling
        int scaleY = 16;
        int scaleZ = 8;  // Adjust this value for different scaling

        float screenX = scaleX * (ptIso.x + ptIso.z);
        float screenY = scaleZ * (ptIso.x - ptIso.z);// + (scaleY * ptIso.y);

        return new Vector2(screenX, screenY);
    }

}
