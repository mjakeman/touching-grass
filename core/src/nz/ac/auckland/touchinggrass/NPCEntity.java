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

import java.util.List;
import java.util.function.Supplier;

public class NPCEntity extends Entity {
    private final Animation<TextureRegion> leftAnimation;
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> rightAnimation;
    private final Animation<TextureRegion> upAnimation;
    private static final float PLAYER_MOVE = 0.05f;
    final float FRAME_DURATION = 0.1f; // time between frames
    private static final int FRAME_SIZE = 32;
    public Direction direction;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final ParticleSystem particleSystem;

    public NPCEntity(Supplier<Texture> textureSupplier) {
        super(textureSupplier.get());

        nonBatchable = true;
        doesCollision = true;

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

    @Override
    public BoundingBox getBoundingBox() {
        float size = 0.7f;
        return new BoundingBox(position.x + 0.15f, position.z + 0.15f, size, size);
    }

    public Vector3 getCentre() {
        return new Vector3(position).add(0.5f, 0.5f, 0.5f);
    }

//    public Vector3 getExhaust() {
//        Vector3 centre = getCentre();
//        float offset = 0.5f;
//        return switch (direction) {
//            case LEFT -> centre.add(0, 0, offset);
//            case RIGHT -> centre.add(0, 0, -offset);
//            case UP -> centre.add(-offset, 0, 0);
//            case DOWN -> centre.add(offset, 0, 0);
//        };
//    }

    private Animation<TextureRegion> getCurrentAnimation() {
        switch (direction) {
            case UP:
                return upAnimation;
            case DOWN:
                return downAnimation;
            case LEFT:
                return leftAnimation;
            case RIGHT:
                return rightAnimation;
            default:
                throw new IllegalArgumentException("Unexpected value: " + direction);
        }
    }


    @Override
    public void update(float deltaTime) {
        Vector2 screenPlayerCentre = IsometricUtils.isoToScreen(getCentre());
        particleSystem.emit((int)(100 * deltaTime), new Color(43f/256, 115f/256, 30f/256, 1.0f), screenPlayerCentre.x, screenPlayerCentre.y);
    }

    @Override
    public void draw(Matrix4 projectionMatrix, float stateTime) {
        drawExhaust(projectionMatrix, stateTime);
        drawPlayer(projectionMatrix, stateTime);
    }

    private void drawPlayer(Matrix4 projectionMatrix, float stateTime) {
        spriteBatch.setProjectionMatrix(projectionMatrix);

        spriteBatch.begin();
        Vector2 vec = IsometricUtils.isoToScreen(position);
        TextureRegion currentFrame = getCurrentAnimation().getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, vec.x, vec.y);
        spriteBatch.end();
    }

    private void drawExhaust(Matrix4 projectionMatrix, float stateTime) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        particleSystem.update(shapeRenderer, stateTime);
    }

    private List<SceneObject> getGroundMaterial(Scene scene) {
        Vector3 translation = new Vector3(1.0f, 1, -1.0f);
        position.sub(translation);
        List<SceneObject> ground = scene.testAABBCollisions(this);
        position.add(translation);

        return ground;
    }

    // Getter for the texture
    public Texture getTexture() {
        return texture;
    }

}
