package nz.ac.auckland.touchinggrass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    public MessageDialog(String texturePath, float width, float height) {
        this.dialogTexture = new Texture(texturePath);
        this.dialogFont = new BitmapFont();
        dialogFont.setColor(Color.BLACK);
        this.dialogWidth = width;
        this.dialogHeight = height;
    }

    public void setMessage(String message) {
        this.dialogText = message;
    }

    public void render(SpriteBatch batch) {
        int x = Gdx.graphics.getWidth() - (int)dialogWidth - 150;
        int y = 0;

        batch.draw(dialogTexture, x, y, dialogWidth, dialogHeight);
        dialogFont.draw(batch, dialogText, x + 40, y + dialogHeight / 2 + dialogFont.getLineHeight() / 2 + 40);
    }

    public void dispose() {
        dialogTexture.dispose();
        dialogFont.dispose();
    }
}
