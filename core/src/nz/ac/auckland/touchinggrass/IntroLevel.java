package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

public class IntroLevel extends Level {

    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    ShapeRenderer shapeRenderer;
    ParticleSystem particleSystem;
    MapRenderer mapRenderer;

    Player player;
    private Scene scene;
    private NPCEntity npcEntity;


    @Override
    public Scene setup(PlayScreen screen) {
        scene = new Scene(screen.camera);

        player = new Player();
        player.position = new Vector3(9, 2, 6);
        scene.addObject(player);

        npcEntity = new NPCEntity(() -> new Texture("old-mower-sheet.png"));
        npcEntity.position = new Vector3(21, 2, 7);
        npcEntity.direction = Direction.DOWN;
        scene.addObject(npcEntity);

        var eventArea = new EventArea((obj) -> {

            var sequencer = screen.sequencer;

            sequencer.addAction(new Sequencer.Action(0, () -> sequencer.isBlocking = true));

            sequencer.addAction(new Sequencer.Action(3, () -> sequencer.isBlocking = false));

//            var messageDialog = new MessageDialog("cloud.png", 200, 100);
//            messageDialog.setMessage("This is a helpful hint!");
//            SpriteBatch batch = new SpriteBatch();
//            batch.begin();
//            messageDialog.render(batch);
//            batch.end();
//            messageDialog.dispose();
        });
        eventArea.position = new Vector3(21, 2, 7);
        scene.addObject(eventArea);

        tiledMap = new TmxMapLoader().load("story-intro.tmx");
        mapRenderer = new MapRenderer(tiledMap);

        shapeRenderer = new ShapeRenderer();
        particleSystem = new ParticleSystem();

        mapRenderer.constructGround(scene);

        return scene;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(Matrix4 projectionMatrix, float deltaTime, float stateTime) {
        player.handleInput(scene, player, deltaTime);
    }

    @Override
    public void teardown() {

    }
}
