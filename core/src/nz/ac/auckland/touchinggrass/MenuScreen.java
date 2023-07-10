package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MenuScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Stage stage;
    private Pixmap cursorPixmap;
    private ImageButton playButton;
    private ImageButton helpButton;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private Sound click;

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("menu.png"));
        cursorPixmap = new Pixmap(Gdx.files.internal("hand.png"));

        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play.png"))));
        TextureRegionDrawable helpDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("help.png"))));

        ImageButton.ImageButtonStyle playStyle = new ImageButton.ImageButtonStyle();
        playStyle.up = playDrawable;

        ImageButton.ImageButtonStyle helpStyle = new ImageButton.ImageButtonStyle();
        helpStyle.up = helpDrawable;

        playButton = new ImageButton(playStyle);
        playButton.setBounds(54, 320, 372, 80);

        helpButton = new ImageButton(helpStyle);
        helpButton.setBounds(54, 230, 160, 80);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                click.play();

                PlayScreen playScreen = new PlayScreen(batch);
                ((Game) Gdx.app.getApplicationListener()).setScreen(playScreen);
            }
        });

        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                click.play();

                HelpScreen helpScreen = new HelpScreen();
                ((Game) Gdx.app.getApplicationListener()).setScreen(helpScreen);
            }
        });

        stage.addActor(playButton);
        stage.addActor(helpButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (viewport.getScreenWidth() != Gdx.graphics.getWidth() || viewport.getScreenHeight() != Gdx.graphics.getHeight()) {
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Adjust button positions based on the new viewport dimensions
            playButton.setBounds(54, viewport.getWorldHeight() - 320, 372, 80);
            helpButton.setBounds(54, viewport.getWorldHeight() - 230, 160, 80);
        }

        boolean hovering = playButton.isOver() || helpButton.isOver();

        if (hovering) {
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));
        } else {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.dispose();
        cursorPixmap.dispose();
    }
}