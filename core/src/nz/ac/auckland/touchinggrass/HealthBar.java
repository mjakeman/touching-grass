package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthBar {
    private SpriteBatch spriteBatch;
    private Pixmap pixmap;
    private Texture texture;
    private float maxHealth;
    private float currentHealth;
    private float barWidth;
    private float barHeight;

    public HealthBar(SpriteBatch batch, float maxHealth) {
        this.spriteBatch = batch;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.barWidth = Gdx.graphics.getWidth() / 3f;
        this.barHeight = 20;

        pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        texture = new Texture(pixmap);
    }

    public void setHealth(float health) {
        this.currentHealth = health;
    }

    public void render() {
        float healthRatio = currentHealth / maxHealth;
        int healthBarWidth = (int) (barWidth * healthRatio);
        int x = Gdx.graphics.getWidth() - (int)barWidth - 10; // added padding from the right edge of the screen

        // border
        int padding = 2; // padding size
        pixmap.setColor(0, 0, 0, 1); // Black color for border
        pixmap.fillRectangle(x - padding, Gdx.graphics.getHeight() - (int)barHeight - padding - 20,
                (int)barWidth + 2 * padding, (int)barHeight + 2 * padding);

        // health bar
        pixmap.setColor(1, 0, 0, 1); // Red color for health
        pixmap.fillRectangle(x, Gdx.graphics.getHeight() - (int)barHeight - 20, healthBarWidth, (int)barHeight);


        texture.dispose();
        texture = new Texture(pixmap);

        spriteBatch.begin();
        spriteBatch.draw(texture, 0, 0);
        spriteBatch.end();
    }


    public void dispose() {
        texture.dispose();
        pixmap.dispose();
    }
}
