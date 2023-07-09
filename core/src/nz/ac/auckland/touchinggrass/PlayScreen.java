package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PlayScreen extends ScreenAdapter {

    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    ParticleSystem particleSystem;
    HealthBar healthBar;
    MapRenderer mapRenderer;

    Player player;
    Entity entity;

    float unitScale = 1/32f;

    private float stateTime = 0;
    private Scene scene;

    private Stage stage;
    private ImageButton backButton;

    private Pixmap cursorPixmap;

    PlayScreen(SpriteBatch batch)
    {
        this.batch = batch;
        healthBar = new HealthBar(batch, 100);
    }

    @Override
    public void show() {
        super.show();

        cursorPixmap = new Pixmap(Gdx.files.internal("../assets/hand.png"));

        float h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        try {
            player = new Player();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.position = new Vector3(9, 2, 6);

        tiledMap = new TmxMapLoader().load("story-intro.tmx");
        mapRenderer = new MapRenderer(tiledMap);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.35f;
        camera.update();

        TextureRegionDrawable backDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("../assets/back.png"))));
        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.up = backDrawable;

        backButton = new ImageButton(backStyle);
        backButton.setBounds(10, h - 50, 100, 40); // Adjust the position and size of the button here

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuScreen menuScreen = new MenuScreen();
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        stage = new Stage();
        stage.addActor(backButton);
        Gdx.input.setInputProcessor(stage);

        shapeRenderer = new ShapeRenderer();
        particleSystem = new ParticleSystem();

        scene = new Scene();
        scene.addObject(player);

        mapRenderer.constructGround(scene);
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
        player.handleInput(scene, player, delta);

        boolean hovering = backButton.isOver();

        if (hovering) {
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));
        } else {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

        batch.begin();
//        mapRenderer.drawGround(batch);
        batch.end();
        healthBar.render();
        // player.draw(camera.combined, stateTime);
        scene.draw(camera.combined, stateTime);

        stage.draw();

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