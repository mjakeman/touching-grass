package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private static final float PLAYER_MOVE = 0.05f;

    MapRenderer mapRenderer;

    Player player;
    Player player2;

    float unitScale = 1/32f;

    PlayScreen(SpriteBatch batch)
    {
        this.batch = batch;
    }

    @Override
    public void show() {
        super.show();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        player = new Player();
        player.position = new Vector3(0, 4, 0);
        player2 = new Player();
        player2.position = new Vector3(1, 4, 1);

        mapRenderer = new MapRenderer();

//		camera = new OrthographicCamera();
//		camera.setToOrtho(false, w, h);
//		camera.update();

        tiledMap = new TmxMapLoader().load("isometric-sandbox-map.tmx");
        // Gdx.input.setInputProcessor(this);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        shapeRenderer = new ShapeRenderer();
        isometricRenderer = new IsometricRenderer();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        followCamera(delta);
        camera.update();

//        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.render();

        handleInput();

        batch.begin();
        mapRenderer.drawGround(batch);
        isometricRenderer.draw(batch, player2);
        isometricRenderer.draw(batch, player);
        batch.end();

//        System.out.println(String.format("a %f %f", dest.x, dest.y));

//        Vector3 dest = IsometricUtils.isoToCartesian(new Vector3(10, 10, 0));
//        System.out.println(String.format("b %f %f", dest.x, dest.y));

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red line
        System.out.println(player.getCentre());
		shapeRenderer.line(0, 0, player.getCentre().x, player.getCentre().y);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        img.dispose();
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

    public void handleInput() {

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.zoom -= 0.003f;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.zoom += 0.003f;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            System.out.println(camera.position);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.position.z += PLAYER_MOVE;
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.position.z -= PLAYER_MOVE;
        }else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.position.x += PLAYER_MOVE;
        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.position.x -= PLAYER_MOVE;
        }
    }
}
