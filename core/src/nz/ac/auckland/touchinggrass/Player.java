package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Objects;

public class Player extends Entity{
    private final Animation<TextureRegion> leftAnimation;
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> rightAnimation;
    private final Animation<TextureRegion> upAnimation;
    private static final float PLAYER_MOVE = 0.05f;
    final float FRAME_DURATION = 0.1f; // time between frames
    private static final int FRAME_SIZE = 32;
    private Direction direction;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final ParticleSystem particleSystem;

    public Player() throws InterruptedException {
        super(createTexture());

        // super(new Texture(Gdx.files.internal("grass.png")));
        Texture spriteSheet = getTexture();

        this.spriteBatch = new SpriteBatch();
        this.particleSystem = new ParticleSystem();
        this.shapeRenderer = new ShapeRenderer();

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

        direction = Direction.UP;
    }

    public Vector3 getCentre() {
        return new Vector3(position).add(0.5f, 2f, 0.5f);
    }

    public Vector3 getExhaust() {
        var centre = getCentre();
        float offset = 0.5f;
        return switch (direction) {
            case LEFT -> centre.add(0, 0, offset);
            case RIGHT -> centre.add(0, 0, -offset);
            case UP -> centre.add(-offset, 0, 0);
            case DOWN -> centre.add(offset, 0, 0);
        };
    }

    private Animation<TextureRegion> getCurrentAnimation() {
        return switch (direction) {
            case UP -> upAnimation;
            case DOWN -> downAnimation;
            case LEFT -> leftAnimation;
            case RIGHT -> rightAnimation;
        };
    }

    public void draw(Matrix4 projectionMatrix, float stateTime) {
        drawExhaust(projectionMatrix, stateTime);
        drawPlayer(projectionMatrix, stateTime);
    }

    private void drawPlayer(Matrix4 projectionMatrix, float stateTime) {
        spriteBatch.setProjectionMatrix(projectionMatrix);

        spriteBatch.begin();
        var vec = IsometricUtils.isoToScreen(position);
        TextureRegion currentFrame = getCurrentAnimation().getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, vec.x, vec.y);
        spriteBatch.end();
    }

    private void drawExhaust(Matrix4 projectionMatrix, float stateTime) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        particleSystem.update(shapeRenderer, stateTime);
    }

    public void handleInput(Player player, float deltaTime) {
        Vector3 orientation = new Vector3();

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            orientation.z = -1;
            direction = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            orientation.z = 1;
            direction = Direction.RIGHT;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            orientation.x = 1;
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            orientation.x = -1;
            direction = Direction.DOWN;
        }

        var translation = orientation.nor().scl(PLAYER_MOVE);
        player.position.add(translation);

        Vector2 screenPlayerCentre = IsometricUtils.isoToScreen(player.getCentre());
        particleSystem.emit((int)(100 * deltaTime), new Color(43f/256, 115f/256, 30f/256, 1.0f), screenPlayerCentre.x, screenPlayerCentre.y);

    }

    private static Texture createTexture() {
        return new Texture("shaun-mower-sheet.png");
    }

    // Getter for the texture
    public Texture getTexture() {
        return texture;
    }

}
