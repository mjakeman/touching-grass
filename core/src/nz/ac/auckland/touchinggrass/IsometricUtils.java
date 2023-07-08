package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IsometricUtils {
    public static final int TILE_WIDTH = 34;
    public static final int TILE_HEIGHT = 34;

    public static Vector2 isoToScreen(Vector3 ptIso) {
        int scaleX = 16;  // Adjust this value for different scaling
        int scaleY = 8;  // Adjust this value for different scaling

        float screenX = scaleX * (ptIso.x - ptIso.z);
        float screenY = scaleY * ((ptIso.x + ptIso.z) + ptIso.y);

        return new Vector2(screenX, screenY);
    }

    public static Vector3 cartesianToIso(Vector3 pt) {
        var tempPt = new Vector3(0, pt.y, 0);
        tempPt.x = (pt.x - pt.z) * (TILE_WIDTH / 2f);
        tempPt.z = (pt.x + pt.z) * (TILE_HEIGHT / 4f);
        return tempPt;
    }

}
