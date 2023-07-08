package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity{
    private Texture spriteSheet;
    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> rightAnimation;
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> currentAnimation = downAnimation;
    private static final float PLAYER_MOVE = 0.05f;
    final float FRAME_DURATION = 0.1f; // time between frames
    private float animationTime = 0;
    private final int FRAME_SIZE = 32;



    public Player() {
        super(createTexture());

        // super(new Texture(Gdx.files.internal("grass.png")));
        this.spriteSheet = getTexture();

        // Splice sprite sheet into 8 frames
        TextureRegion[][] temp = TextureRegion.split(spriteSheet, FRAME_SIZE, FRAME_SIZE);
        TextureRegion[] frames = new TextureRegion[8];

        for (int i = 0; i < 8; i++) {
            frames[i] = temp[0][i];
        }

        // Define an animation for each direction
        downAnimation = new Animation<>(FRAME_DURATION, frames[0], frames[1]);
        rightAnimation = new Animation<>(FRAME_DURATION, frames[2], frames[3]);
        upAnimation = new Animation<>(FRAME_DURATION, frames[4], frames[5]);
        leftAnimation = new Animation<>(FRAME_DURATION, frames[6], frames[7]);

        currentAnimation = upAnimation;
    }

    public Vector3 getCentre() {
        return new Vector3(position).add(0.5f, 2f, 0.5f);
    }

    public void draw(SpriteBatch batch, float stateTime) {
        var vec = IsometricUtils.isoToScreen(position);
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, vec.x, vec.y);
    }

    public void handleInput(Player player, float deltaTime) {
        animationTime += deltaTime;

        Vector3 direction = new Vector3();

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.z = -1;
            currentAnimation = leftAnimation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.z = 1;
            currentAnimation = rightAnimation;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction.x = 1;
            currentAnimation = upAnimation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x = -1;
            currentAnimation = downAnimation;
        }

        System.out.println(direction);
        var translation = direction.nor().scl(PLAYER_MOVE);
        player.position.add(translation);
    }

    private static Texture createTexture() {
        return new Texture("shaun-mower-sheet.png");
    }

    // Getter for the texture
    public Texture getTexture() {
        return texture;
    }
}
