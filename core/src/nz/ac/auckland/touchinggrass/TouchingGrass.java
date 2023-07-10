package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TouchingGrass extends Game {

	private SpriteBatch batch;
	private MenuScreen menuScreen;
	private static Music backgroundMusic;

	@Override
	public void create() {
		batch = new SpriteBatch();
		menuScreen = new MenuScreen();
		Gdx.graphics.setWindowedMode(1280, 720);
		setScreen(menuScreen);

		// Load and play the background music
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("m.ogg"));
		backgroundMusic.setLooping(true);
	}

	public static void startBackgroundMusic() {
		backgroundMusic.play();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		// Dispose the background music
		backgroundMusic.dispose();
		super.dispose();
	}
}