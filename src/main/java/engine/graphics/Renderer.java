/*
 Title: Renderer
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import engine.utils.engine.ResourceUtils;
import engine.world.components.Sprite;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final List<BatchRenderer> batchList = new ArrayList<>();

    private static Shader activeShader;
    private static final Shader defaultShader = ResourceUtils.getOrCreateShader("C:\\Dev\\StellarSprite2D\\JEngine2D\\shaders\\Default.glsl");
    private static final Shader pickingShader =  ResourceUtils.getOrCreateShader("C:\\Dev\\StellarSprite2D\\JEngine2D\\shaders\\ObjPicker.glsl");


    public void render() {
        // Update the batches
        for (BatchRenderer batch : batchList) {
            batch.render();
        }
    }


    public void addGameObject(GameObject go) {
        Sprite sprite = go.getComponent(Sprite.class);
        if (sprite != null) {
            add(sprite);
        }
    }


    private void add(Sprite sprite) {
        if (!addToExistingBatch(sprite)) {
            createNewBatch(sprite);
        }
    }


    private boolean addToExistingBatch(Sprite sprite) {
        for (BatchRenderer batch : batchList) {
            // Only add sprites of the same z-index onto the same batch
            if (batch.getBatchHasRoom() && batch.getzIndex() == sprite.owningGameObject.getZIndex()) {
                Texture tex = sprite.getSpriteTexture();
                if (tex == null || (batch.hasTexture(tex) || batch.isTexSlotsFull())) {
                    batch.addSpriteToBatch(sprite);
                    return true;
                }
            }
        }
        return false;
    }


    public void removeSpriteFromRenderer(int spriteUID) {
        // Iterate through the batches in reverse order to efficiently remove the sprite
        for (int i = batchList.size() - 1; i >= 0; i--) {
            BatchRenderer batch = batchList.get(i);
            // Remove the sprite from the batch
            batch.removeSpriteFromBatch(spriteUID);
            // If the batch becomes empty after removing the sprite, remove the batch from the renderer
            if (batch.getNumSprites() == 0) {
                batchList.remove(i);
            }
        }
    }


    private void createNewBatch(Sprite sprite) {
        BatchRenderer newBatch = new BatchRenderer(sprite.owningGameObject.getZIndex());
        newBatch.addSpriteToBatch(sprite);
        batchList.add(newBatch);
        // Sorts the batches by their z index to ensure they are in the correct order
        // Can also call .reverseOrder()
        Collections.sort(batchList);
    }


    public static void setPickingShader() {
        activeShader = pickingShader;
    }


    public static void setDefaultShader() {
        activeShader = defaultShader;
    }


    public static Shader getActiveShader() {
        return activeShader;
    }
}
/*End of Renderer class*/
