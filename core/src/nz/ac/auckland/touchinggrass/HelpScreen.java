package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class HelpScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Stage stage;
    private Pixmap cursorPixmap;
    private ImageButton homeButton;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private Sound click;

    @Override
    public void show() {

        float w = Gdx.graphics.getWidth();

        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));

        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("helpmenu.png"));
        cursorPixmap = new Pixmap(Gdx.files.internal("hand.png"));

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        TextureRegionDrawable homeDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("home.png"))));

        ImageButton.ImageButtonStyle homeStyle = new ImageButton.ImageButtonStyle();
        homeStyle.up = homeDrawable;

        homeButton = new ImageButton(homeStyle);
        homeButton.setBounds(w/2 - 80, 165, 160, 80);

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                click.play();

                MenuScreen menuScreen = new MenuScreen();
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        stage.addActor(homeButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (viewport.getScreenWidth() != Gdx.graphics.getWidth() || viewport.getScreenHeight() != Gdx.graphics.getHeight()) {
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Adjust button positions based on the new viewport dimensions
            homeButton.setBounds(viewport.getWorldWidth() / 2 - 80, viewport.getWorldHeight() - 165, 160, 80);
        }

        boolean hovering = homeButton.isOver();

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
