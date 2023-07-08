package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity{

    public Player() {
        super(new Texture(Gdx.files.internal("grass.png")));
    }

    public Vector3 getCentre() {
        return new Vector3(position).add(0.5f, 2f, 0.5f);
    }
}
