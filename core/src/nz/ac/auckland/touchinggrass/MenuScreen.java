package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Stage stage;

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("../assets/menu.png"));

        stage = new Stage();
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

        ImageButton playButton = new ImageButton(playStyle);
        playButton.setBounds(28, 210, 186, 40);

        ImageButton mapsButton = new ImageButton(mapsStyle);
        mapsButton.setBounds(28, 165, 327, 40);

        ImageButton tutorialButton = new ImageButton(tutorialStyle);
        tutorialButton.setBounds(28, 120, 280, 40);

        ImageButton helpButton = new ImageButton(helpStyle);
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
        super.render(delta);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.dispose();
    }
}