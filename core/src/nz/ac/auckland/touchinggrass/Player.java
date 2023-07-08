package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity{

    public Player() {
        super(new Texture(Gdx.files.internal("grass.png")));
    }
}
