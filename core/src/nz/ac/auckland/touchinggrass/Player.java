package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.controllers.Controllers;

import java.util.List;
import java.util.stream.StreamSupport;

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
    private boolean shouldEnlargeSprite;

    private Sound soundEffect;

    public Player() {
        super(createTexture());

        nonBatchable = true;

        soundEffect = Gdx.audio.newSound(Gdx.files.internal("../assets/lawnmower.ogg"));

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

    @Override
    public BoundingBox getBoundingBox() {
        var size = shouldEnlargeSprite ? 2.3f : 0.7f; ;
        return new BoundingBox(position.x + 0.15f, position.z + 0.15f, size, size);
    }

    public Vector3 getCentre() {
        return new Vector3(position).add(0.5f, 0.5f, 0.5f);
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

    @Override
    public void draw(Matrix4 projectionMatrix, float stateTime) {
        drawExhaust(projectionMatrix, stateTime);
        drawPlayer(projectionMatrix, stateTime);
    }

    private void drawPlayer(Matrix4 projectionMatrix, float stateTime) {
        spriteBatch.setProjectionMatrix(projectionMatrix);

        spriteBatch.begin();
        var vec = IsometricUtils.isoToScreen(position);
        TextureRegion currentFrame = getCurrentAnimation().getKeyFrame(stateTime, true);

        float scale = shouldEnlargeSprite ? 2.5f : 1.0f; // Scale factor is 2.5 if the sprite should be enlarged, otherwise 1.0
        float width = currentFrame.getRegionWidth() * scale; // Calculate new width
        float height = currentFrame.getRegionHeight() * scale; // Calculate new height

        spriteBatch.draw(currentFrame, vec.x, vec.y, width, height);
        spriteBatch.end();
    }

    private void drawExhaust(Matrix4 projectionMatrix, float stateTime) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        particleSystem.update(shapeRenderer, stateTime);
    }

    private List<SceneObject> getGroundMaterial(Scene scene) {
        var translation = new Vector3(1.0f, 1, -1.0f);
        position.sub(translation);
        var ground = scene.testAABBCollisions(this);
        position.add(translation);

        return ground;
    }

    private boolean handleCollision(Scene scene, List<SceneObject> objects) {
        for (var object : objects) {
            if (object instanceof FlagTile flag) {
                OrthographicCamera camera = scene.getCamera();
                Sequencer sequencer = new Sequencer();
                sequencer.start();
                sequencer.addAction(() ->
                        camera.zoom = -camera.zoom
                );
                sequencer.addAction(() ->
                        sequencer.pause(15000)
                );
                sequencer.addAction(() ->
                        camera.zoom = -camera.zoom
                );
                sequencer.stopWhenDone();
                scene.removeObject(flag);
                return true;
            }
            else if (object instanceof MushroomTile mushroom) {
                Sequencer sequencer = new Sequencer();
                sequencer.start();
                sequencer.addAction(() ->
                        setToggleEnlargeSprite(true)
                );
                sequencer.addAction(() ->
                        sequencer.pause(20000)
                );
                sequencer.addAction(() ->
                        setToggleEnlargeSprite(false)
                );
                sequencer.stopWhenDone();
                scene.removeObject(mushroom);

                return true;
            }
        }

        return objects.isEmpty();
    }

    public void handleInput(Scene scene, Player player, float deltaTime) {
        Vector3 orientation = new Vector3();

//        System.out.println(Controllers.getControllers().size);

        if (Controllers.getControllers().size > 0) {
            Controller controller = Controllers.getControllers().first();

            Vector3 horizontalMovement = new Vector3();
            Vector3 verticalMovement = new Vector3();

            var axisLeft = controller.getAxis(controller.getMapping().axisLeftX);
            if (axisLeft > 0.2) {
                horizontalMovement.x = 1;
                horizontalMovement.z = 1;
            } else if (axisLeft < -0.2) {
                horizontalMovement.x = -1;
                horizontalMovement.z = -1;
            }

            var axisRight = controller.getAxis(controller.getMapping().axisLeftY);
            if (axisRight > 0.2) {
                verticalMovement.z = 1;
                verticalMovement.x = -1;
            } else if (axisRight < -0.2) {
                verticalMovement.z = -1;
                verticalMovement.x = 1;
            }

            if (axisLeft > 0 && axisRight > 0) {
                direction = Direction.RIGHT;
            } else if (axisLeft < 0 && axisRight < 0) {
                direction = Direction.LEFT;
            } else if (axisLeft > 0 && axisRight < 0) {
                direction = Direction.UP;
            } else if (axisLeft < 0 && axisRight > 0) {
                direction = Direction.DOWN;
            }


            // Axis proximity determination
//            if (Math.abs(axisLeft) > Math.abs(axisRight) && axisLeft > 0 && axisRight < 0) {
//                direction = Direction.UP;
//            } else if (Math.abs(axisRight) > Math.abs(axisLeft) && axisLeft > 0 && axisRight > 0) {
//                direction = Direction.LEFT;
//            } else if (Math.abs(axisLeft) > Math.abs(axisRight) && axisLeft < 0 && axisRight < 0) {
//                direction = Direction.DOWN;
//            } else if (Math.abs(axisRight) > Math.abs(axisLeft) && axisLeft < 0 && axisRight > 0) {
//                direction = Direction.RIGHT;
//            }

            System.out.println(axisLeft);
            System.out.println(axisRight);

            orientation = horizontalMovement.add(verticalMovement);
        }

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

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            soundEffect.play(0.1f);
        } else {
            soundEffect.stop();
        }

        var translation = orientation.nor().scl(PLAYER_MOVE);
        var translationX = new Vector3(translation.x, 0, 0);
        var translationZ = new Vector3(0, 0, translation.z);

        // Collision detection
        player.position.add(translationX);
        var checkForCollision = scene.testAABBCollisions(player);
        if (!handleCollision(scene, checkForCollision)) {
            player.position.sub(translationX);
        }

        player.position.add(translationZ);
        checkForCollision = scene.testAABBCollisions(player);
        if (!handleCollision(scene, checkForCollision)) {
            player.position.sub(translationZ);
        }

        var ground = getGroundMaterial(scene);
        for (var tile : ground) {
            if (tile instanceof MowableTile mowable) {
                mowable.mow();
            }
        }

        Vector2 screenPlayerCentre = IsometricUtils.isoToScreen(player.getCentre());
        particleSystem.emit((int)((shouldEnlargeSprite ? 1000 : 100) * deltaTime), new Color(43f/256, 115f/256, 30f/256, 1.0f), screenPlayerCentre.x + (shouldEnlargeSprite ? 12.0f : 0.0f), screenPlayerCentre.y + (shouldEnlargeSprite ? 12.0f : 0.0f));

    }

    private static Texture createTexture() {
        return new Texture("shaun-mower-sheet.png");
    }

    // Getter for the texture
    public Texture getTexture() {
        return texture;
    }

    public void setToggleEnlargeSprite(boolean shouldEnlargeSprite) {
        this.shouldEnlargeSprite = shouldEnlargeSprite;
    }
}
