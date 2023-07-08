package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class TouchingGrass extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	IsometricTiledMapRenderer tiledMapRenderer;
	ShapeRenderer shapeRenderer;

	Vector2 startPos = new Vector2(0, 0);

	private Matrix4 isoTransform;
	private Matrix4 invIsotransform;

	Player player;

	float unitScale = 1/32f;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		player = new Player();

//		camera = new OrthographicCamera();
//		camera.setToOrtho(false, w, h);
//		camera.update();

		tiledMap = new TmxMapLoader().load("isometric-sandbox-map.tmx");
		tiledMapRenderer = new IsometricTiledMapRenderer(tiledMap, unitScale);
		Gdx.input.setInputProcessor(this);

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(startPos, 0);
		camera.update();

		var tiledMapBatch = tiledMapRenderer.getBatch();
		tiledMapBatch.setShader(null);
		tiledMapBatch.setProjectionMatrix(camera.combined);

		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();
		player.draw(batch);
		batch.end();

//		Vector2 dest = translateIsoToScreen(new Vector2(10, 10));
//		System.out.println(String.format("%f %f", dest.x, dest.y));

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 0, 0, 1); // Red line
//		shapeRenderer.line(0, 0, dest.x, dest.y);
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width * unitScale;
		camera.viewportHeight = height * unitScale;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
