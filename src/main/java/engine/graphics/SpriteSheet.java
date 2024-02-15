/*
 Title: SpriteSheet
 Date: 2024-01-04
 Author: Kyle St John
 */
package engine.graphics;

import engine.world.components.Sprite;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * The SpriteSheet class represents a sprite sheet, which is a grid of individual sprites.
 */
public class SpriteSheet {

    private final Texture spriteSheetTexture;
    private final List<Sprite> spriteSheet = new ArrayList<>();

    private final int spriteWidth;
    private final int spriteHeight;
    private final int spriteSpacing;
    private final int spritesPerSheet;

    private final String assetType;

    /**
     * Constructs a SpriteSheet with the given parameters.
     *
     * @param spriteSheet     The texture representing the sprite sheet.
     * @param spriteWidth     The width of each individual sprite in pixels.
     * @param spriteHeight    The height of each individual sprite in pixels.
     * @param spriteSpacing   The spacing between each sprite in pixels.
     * @param spritesPerSheet The number of sprites per sheet.
     * @param assetType       The type of asset associated with this sprite sheet.
     */
    public SpriteSheet(Texture spriteSheet, int spriteWidth, int spriteHeight,
                       int spriteSpacing, int spritesPerSheet, String assetType) {
        this.spriteSheetTexture = spriteSheet;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spriteSpacing = spriteSpacing;
        this.spritesPerSheet = spritesPerSheet;
        this.assetType = assetType;
        extractSprites();
    }

    /**
     * Extracts individual sprites from the sprite sheet based on provided parameters.
     */
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

            Vector2f[] spriteCoordinates = {
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

    /**
     * Gets the width of each individual sprite in pixels.
     *
     * @return The sprite width.
     */
    public int getSpriteWidth() {
        return spriteWidth;
    }

    /**
     * Gets the height of each individual sprite in pixels.
     *
     * @return The sprite height.
     */
    public int getSpriteHeight() {
        return spriteHeight;
    }

    /**
     * Gets the total number of sprites in the sprite sheet.
     *
     * @return The number of sprites.
     */
    public int numOfSprites() {
        return spriteSheet.size();
    }

    /**
     * Gets a specific sprite from the sprite sheet.
     *
     * @param spriteIndex The index of the sprite.
     * @return The sprite at the specified index.
     */
    public Sprite getSprite(int spriteIndex) {
        return spriteSheet.get(spriteIndex);
    }

    /**
     * Gets the file path of the texture associated with the sprite sheet.
     *
     * @return The file path of the texture.
     */
    public String getFilePathOfTexture() {
        return spriteSheetTexture.getFilePath();
    }

    /**
     * Gets the spacing between each sprite in pixels.
     *
     * @return The sprite spacing.
     */
    public int getSpriteSpacing() {
        return spriteSpacing;
    }

    /**
     * Gets the type of asset associated with this sprite sheet.
     *
     * @return The asset type.
     */
    public String getAssetType() {
        return assetType;
    }
}
/*End of SpriteSheet class*/