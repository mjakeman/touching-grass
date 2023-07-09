package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayScreen extends ScreenAdapter {

    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    IsometricRenderer isometricRenderer;
    ShapeRenderer shapeRenderer;
    ParticleSystem particleSystem;
    HealthBar healthBar;

    MapRenderer mapRenderer;

    Player player;

    float unitScale = 1/32f;
    
    private float stateTime = 0;

    PlayScreen(SpriteBatch batch)
    {
        this.batch = batch;
        healthBar = new HealthBar(batch, 100);
    }

    @Override
    public void show() {
        super.show();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        try {
            player = new Player();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.position = new Vector3(0, 4, 0);

        tiledMap = new TmxMapLoader().load("test-map.tmx");
        mapRenderer = new MapRenderer(tiledMap);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        shapeRenderer = new ShapeRenderer();
        isometricRenderer = new IsometricRenderer();
        particleSystem = new ParticleSystem();

        introAnimation();

    }

    private void introAnimation() {
        Sequencer sequencer = new Sequencer();
        sequencer.start();

        // Add some actions...
        sequencer.addAction(() -> {
            System.out.println("Lock Player");
        });
        sequencer.addAction(() -> {
            System.out.println("Pause");
            pause(2000);

        });
        // Add another action.
        sequencer.addAction(() -> {
            System.out.println("Unlock player");
        });

        // Signal the sequencer to stop after all actions are executed.
        sequencer.stopWhenDone();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stateTime += delta;

        Gdx.gl.glClearColor(0f, 0.6f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        followCamera(delta);
        camera.update();

//        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.render();

        handleCameraInput();
        player.handleInput(player, delta);

        batch.begin();
        mapRenderer.drawGround(batch);
        batch.end();
        healthBar.render();
        player.draw(camera.combined, stateTime);

        // drawDebugLine();
    }

    private void drawDebugLine() {
        Vector2 screenPlayerCentre = IsometricUtils.isoToScreen(player.getCentre());

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red line
        shapeRenderer.line(0, 0, screenPlayerCentre.x, screenPlayerCentre.y);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        img.dispose();
        healthBar.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    public void followCamera(float deltaTime) {
        Vector2 desiredPosition = IsometricUtils.isoToScreen(player.getCentre());

        float lerp = 0.2f;
        Vector3 position = camera.position;
        position.x += (desiredPosition.x - position.x) * lerp * 10 * deltaTime;
        position.y += (desiredPosition.y - position.y) * lerp * 10 * deltaTime;

    }

    public void handleCameraInput() {

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.zoom -= 0.03f;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.zoom += 0.03f;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            System.out.println(camera.position);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

        }

//        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            player.position.z += PLAYER_MOVE;
//        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            player.position.z -= PLAYER_MOVE;
//        }else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            player.position.x += PLAYER_MOVE;
//        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            player.position.x -= PLAYER_MOVE;
//        }
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // This block is executed if the sleep operation is interrupted.
            e.printStackTrace();
        }
    }
}
