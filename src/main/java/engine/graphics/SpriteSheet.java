/*
 Title: SpriteSheet
 Date: 2024-01-04
 Author: Kyle St John
 */
package engine.graphics;

import engine.components.Sprite;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private final Texture spriteSheetTexture;

    private final List<Sprite> spriteSheet = new ArrayList<>();
    private final int spriteWidth;
    private final int spriteHeight;
    private final int spriteSpacing;
    private final int spritesPerSheet;

    //TODO: Add multiple constructors for common sprite sizes and spacing
    public SpriteSheet(Texture spriteSheet, int spriteWidth, int spriteHeight, int spriteSpacing, int spritesPerSheet) {
        this.spriteSheetTexture = spriteSheet;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spriteSpacing = spriteSpacing;
        this.spritesPerSheet = spritesPerSheet;
        extractSprites();
    }

    private void extractSprites() {
        float invTextureWidth = 1.0f / spriteSheetTexture.getTextureWidth();
        float invTextureHeight = 1.0f / spriteSheetTexture.getTextureHeight();

        int currentX = 0;
        int currentY = spriteSheetTexture.getTextureHeight() - spriteHeight;

        for (int i = 0; i < spritesPerSheet; i++) {
            float topY = (currentY + spriteHeight) * invTextureHeight;
            float rightX = (currentX + spriteWidth) * invTextureWidth;
            float leftX = currentX * invTextureWidth;
            float bottomY = currentY * invTextureHeight;

            Vector2f[] spriteCoordinates =  {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(spriteSheetTexture, spriteCoordinates);
            this.spriteSheet.add(sprite);

            currentX += spriteWidth + spriteSpacing;
            if (currentX >= spriteSheetTexture.getTextureWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spriteSpacing;
            }
        }
    }

    public int spriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int numOfSprites() {
        return spriteSheet.size();
    }

    public Sprite getSprite(int spriteIndex) {
        return spriteSheet.get(spriteIndex);
    }

    public String getFilePathOfTexture() {
        return spriteSheetTexture.getFilePath();
    }

    public int getSpriteSpacing() {
        return spriteSpacing;
    }
}
/*End of SpriteSheet class*/
