package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class TutorialLevel extends Level {

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

        tiledMap = new TmxMapLoader().load("tutorial-map.tmx");
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
