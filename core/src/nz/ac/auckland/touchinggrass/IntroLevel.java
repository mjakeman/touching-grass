package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

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
        player.position = new Vector3(8, 2, 7);
        scene.addObject(player);

        npcEntity = new NPCEntity(() -> new Texture("old-mower-sheet.png"));
        npcEntity.position = new Vector3(21, 2, 7);
        npcEntity.direction = Direction.DOWN;
        scene.addObject(npcEntity);

        EventArea eventArea = new EventArea((obj) -> {

            Sequencer sequencer = screen.sequencer;
            float originalZoom = screen.camera.zoom;

            sequencer.addAction(new Sequencer.Action(0, () -> sequencer.isBlocking = true));
            sequencer.addAction(new Sequencer.Transition(1, screen.camera.zoom, 0.15f, (s, e, p) -> {
                screen.camera.zoom = MathUtils.lerp(s, e, p);
            }));
            sequencer.addAction(new Sequencer.Action(0, () -> {
                screen.messageDialog = new MessageDialog("cloud.png", 500, 200);
                screen.messageDialog.setMessage("Bryan the NPC: Hello there young mower!");
            }));
            sequencer.addAction(new Sequencer.Action(2, () -> {
                screen.messageDialog.dispose();
                screen.messageDialog = new MessageDialog("cloud.png", 500, 200);
                screen.messageDialog.setMessage("Bryan the NPC: You have been asleep for 50 years");
            }));
            sequencer.addAction(new Sequencer.Action(2, () -> {
                screen.messageDialog.dispose();
                screen.messageDialog = new MessageDialog("cloud.png", 500, 200);
                screen.messageDialog.setMessage("Bryan the NPC: There is grass everywhere!");
            }));
            sequencer.addAction(new Sequencer.Action(2, () -> {
                screen.messageDialog.dispose();
                screen.messageDialog = new MessageDialog("cloud.png", 500, 200);
                screen.messageDialog.setMessage("Bryan the NPC: Set forth and mow all the grass");
            }));
            sequencer.addAction(new Sequencer.Action(2, () -> {
                screen.messageDialog.dispose();
                screen.messageDialog = new MessageDialog("cloud.png", 500, 200);
                screen.messageDialog.setMessage("Bryan the NPC: Go through that portal to continue");
            }));
            sequencer.addAction(new Sequencer.Action(2, () -> {
                screen.messageDialog.dispose();
                screen.messageDialog = null;
            }));

            sequencer.addAction(new Sequencer.Transition(1, 0.15f, originalZoom, (s, e, p) -> {
                screen.camera.zoom = MathUtils.lerp(s, e, p);
                sequencer.isBlocking = false;
            }));

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

        eventArea = new EventArea((obj) -> {
            screen.loadLevel(new MainLevel());
        });
        eventArea.position = new Vector3(4, 2, 7);
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
    public void update(PlayScreen screen, Matrix4 projectionMatrix, float deltaTime, float stateTime) {
        player.handleInput(screen, scene, player, deltaTime);
    }

    @Override
    public void teardown() {

    }
}
