package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayScreen extends ScreenAdapter {

    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    ParticleSystem particleSystem;
//    HealthBar healthBar;
    MapRenderer mapRenderer;

    Player player;

    private Level currentLevel;

    private float stateTime = 0;
    private Scene scene;

    private Stage stage;
    private ImageButton backButton;
    private NPCEntity npcEntity;

    private Pixmap cursorPixmap;
    public Sequencer sequencer;

    public MessageDialog messageDialog;

    private HashMap<Sequencer, Boolean> effectSequencers;

    private Sound click;

    public void addEffectSequencer(Sequencer effectSequencer) {
        effectSequencer.addAction(new Sequencer.Action(0, () -> {
            effectSequencers.put(effectSequencer, true);
        }));
        effectSequencers.put(effectSequencer, false);
    }

    PlayScreen(SpriteBatch batch)
    {
        this.batch = batch;
        this.effectSequencers = new HashMap<Sequencer, Boolean>();
//        healthBar = new HealthBar(batch, 100);
    }

    @Override
    public void show() {
        super.show();

        TouchingGrass.startBackgroundMusic();

        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));

        // Setup UI
        cursorPixmap = new Pixmap(Gdx.files.internal("hand.png"));

        float h = Gdx.graphics.getHeight();

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

        // Setup Level
        currentLevel = new IntroLevel();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.35f;
        camera.update();

        scene = currentLevel.setup(this);

        sequencer = new Sequencer();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stateTime += delta;

        Gdx.gl.glClearColor(0f, 0.6f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        batch.setProjectionMatrix(camera.combined);

        scene.draw(camera.combined, stateTime);

        followCamera(delta);
        camera.update();

        if (messageDialog != null) {
//            System.out.println("Drawing message box");
            messageDialog.render();
        }

        if (sequencer.step(delta))
            return;

        for (Sequencer effectSequencer : effectSequencers.keySet()) {
            if (effectSequencers.get(effectSequencer)) continue;
            effectSequencer.step(delta); // don't allow blocking?
        }

        effectSequencers.entrySet().removeIf(Map.Entry::getValue);

        currentLevel.update(this, camera.combined, delta, stateTime);
        scene.update(delta);

        boolean hovering = backButton.isOver();

        if (hovering) {
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));
        } else {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

//        healthBar.render();
        stage.draw();

        handleCameraInput();
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
//        healthBar.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    public void followCamera(float deltaTime) {
        Vector2 desiredPosition = IsometricUtils.isoToScreen(currentLevel.getPlayer().getCentre());

        float lerp = 0.2f;
        Vector3 position = camera.position;
        position.x += (desiredPosition.x - position.x) * lerp * 10 * deltaTime;
        position.y += (desiredPosition.y - position.y) * lerp * 10 * deltaTime;

    }

    public void handleCameraInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            float sign = camera.zoom > 0 ? 1 : -1;
            camera.zoom = Math.abs(camera.zoom) - 0.03f;
            camera.zoom = (float) Math.max(0.1, Math.abs(camera.zoom)) * sign;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            float sign = camera.zoom > 0 ? 1 : -1;
            camera.zoom = Math.abs(camera.zoom) + 0.03f;
            camera.zoom = (float) Math.min(2, Math.abs(camera.zoom)) * sign;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            System.out.println(camera.position);
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

    void loadLevel(Level level) {
        currentLevel.teardown();
        scene = level.setup(this);
        currentLevel = level;
    }


}