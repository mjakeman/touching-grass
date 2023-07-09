package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Example usage
 *
 * private MessageDialog messageDialog;
 *
 * messageDialog = new MessageDialog("cloud.png", 200, 100);
 * messageDialog.setMessage("This is a helpful hint!");
 *
 * messageDialog.render(batch);
 *
 * messageDialog.dispose();
 */

public class MessageDialog {
    private Texture dialogTexture;
    private BitmapFont dialogFont;
    private String dialogText;

    private float dialogWidth = 200;
    private float dialogHeight = 100;
    private RoundedRectRenderer shapeRenderer;
    private SpriteBatch batch;

    public MessageDialog(String texturePath, float width, float height) {
        this.dialogTexture = new Texture(texturePath);
        this.dialogFont = new BitmapFont();
        dialogFont.setColor(Color.BLACK);
        dialogFont.getData().setScale(2);
        this.dialogWidth = width;
        this.dialogHeight = height;
        this.shapeRenderer = new RoundedRectRenderer();
        this.shapeRenderer.setAutoShapeType(true);
        this.batch = new SpriteBatch();
    }

    public void setMessage(String message) {
        this.dialogText = message;
    }

    public void render() {
        int x = Gdx.graphics.getWidth() - (int)dialogWidth - 150;
        int y = 0;

//        batch.draw(dialogTexture, x, y, dialogWidth, dialogHeight);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.roundedRect(40, 40, 800, 150, 10);
        shapeRenderer.end();
        batch.begin();
        dialogFont.draw(batch, dialogText, 120, 120);
        batch.end();
    }

    public void dispose() {
        dialogTexture.dispose();
        dialogFont.dispose();
    }
}
