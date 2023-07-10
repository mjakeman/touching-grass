package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainLevel extends Level {

    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    ShapeRenderer shapeRenderer;
    ParticleSystem particleSystem;
    HealthBar healthBar;
    MapRenderer mapRenderer;

    Player player;

    private Level currentLevel;

    private float stateTime = 0;
    private Scene scene;

    private Stage stage;
    private ImageButton backButton;
    private NPCEntity npcEntity;

    private Pixmap cursorPixmap;

    private Sound click;

    @Override
    public Scene setup(PlayScreen screen) {
        cursorPixmap = new Pixmap(Gdx.files.internal("hand.png"));

        float h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));

        scene = new Scene(screen.camera);

        player = new Player();
        player.position = new Vector3(9, 1, 10);
        scene.addObject(player);

        tiledMap = new TmxMapLoader().load("test-map-3d.tmx");
        mapRenderer = new MapRenderer(tiledMap);

        TextureRegionDrawable backDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("back.png"))));
        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.up = backDrawable;

        backButton = new ImageButton(backStyle);
        backButton.setBounds(10, h - 50, 100, 40); // Adjust the position and size of the button here

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                click.play();

                MenuScreen menuScreen = new MenuScreen();
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        stage = new Stage();
        stage.addActor(backButton);
        Gdx.input.setInputProcessor(stage);

        shapeRenderer = new ShapeRenderer();
        particleSystem = new ParticleSystem();

        mapRenderer.constructGround(scene);

        return scene;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(PlayScreen screen, Matrix4 projectionMatrix, float deltaTime, float stateTime) {
        player.handleInput(screen, scene, player, deltaTime);
    }

    @Override
    public void teardown() {

    }
}
