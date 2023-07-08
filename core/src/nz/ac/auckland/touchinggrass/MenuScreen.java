package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private ImageButton mapsButton;
    private ImageButton tutorialButton;
    private ImageButton helpButton;
    private OrthographicCamera camera;
    private ExtendViewport viewport;

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("../assets/menu.png"));
        cursorPixmap = new Pixmap(Gdx.files.internal("../assets/hand.png"));

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("../assets/play.png"))));
        TextureRegionDrawable mapsDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("../assets/maps.png"))));
        TextureRegionDrawable tutorialDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("../assets/tutorial.png"))));
        TextureRegionDrawable helpDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("../assets/help.png"))));

        ImageButton.ImageButtonStyle playStyle = new ImageButton.ImageButtonStyle();
        playStyle.up = playDrawable;

        ImageButton.ImageButtonStyle mapsStyle = new ImageButton.ImageButtonStyle();
        mapsStyle.up = mapsDrawable;

        ImageButton.ImageButtonStyle tutorialStyle = new ImageButton.ImageButtonStyle();
        tutorialStyle.up = tutorialDrawable;

        ImageButton.ImageButtonStyle helpStyle = new ImageButton.ImageButtonStyle();
        helpStyle.up = helpDrawable;

        playButton = new ImageButton(playStyle);
        playButton.setBounds(28, 210, 186, 40);

        mapsButton = new ImageButton(mapsStyle);
        mapsButton.setBounds(28, 165, 327, 40);

        tutorialButton = new ImageButton(tutorialStyle);
        tutorialButton.setBounds(28, 120, 280, 40);

        helpButton = new ImageButton(helpStyle);
        helpButton.setBounds(28, 75, 80, 40);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen playScreen = new PlayScreen(batch);
                ((Game) Gdx.app.getApplicationListener()).setScreen(playScreen);
            }
        });

        mapsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen playScreen = new PlayScreen(batch);
                ((Game) Gdx.app.getApplicationListener()).setScreen(playScreen);
            }
        });

        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen playScreen = new PlayScreen(batch);
                ((Game) Gdx.app.getApplicationListener()).setScreen(playScreen);
            }
        });

        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen playScreen = new PlayScreen(batch);
                ((Game) Gdx.app.getApplicationListener()).setScreen(playScreen);
            }
        });

        stage.addActor(playButton);
        stage.addActor(mapsButton);
        stage.addActor(tutorialButton);
        stage.addActor(helpButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (viewport.getScreenWidth() != Gdx.graphics.getWidth() || viewport.getScreenHeight() != Gdx.graphics.getHeight()) {
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Adjust button positions based on the new viewport dimensions
            playButton.setBounds(28, viewport.getWorldHeight() - 210, 186, 40);
            mapsButton.setBounds(28, viewport.getWorldHeight() - 165, 327, 40);
            tutorialButton.setBounds(28, viewport.getWorldHeight() - 120, 280, 40);
            helpButton.setBounds(28, viewport.getWorldHeight() - 75, 80, 40);
        }

        boolean hovering = playButton.isOver() || mapsButton.isOver() || tutorialButton.isOver() || helpButton.isOver();

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