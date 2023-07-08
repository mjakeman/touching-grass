package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class TouchingGrass extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	IsometricTiledMapRenderer tiledMapRenderer;

	Vector2 startPos = new Vector2(45, 5);

	float unitScale = 1/32f;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

//		camera = new OrthographicCamera();
//		camera.setToOrtho(false, w, h);
//		camera.update();

		tiledMap = new TmxMapLoader().load("isometric-sandbox-map.tmx");
		tiledMapRenderer = new IsometricTiledMapRenderer(tiledMap, unitScale);
		Gdx.input.setInputProcessor(this);

		camera = new OrthographicCamera(Gdx.graphics.getWidth() * unitScale, Gdx.graphics.getHeight() * unitScale);
		camera.position.set(startPos, 0);
		camera.update();

		var batch = tiledMapRenderer.getBatch();
		batch.setShader(null);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
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
