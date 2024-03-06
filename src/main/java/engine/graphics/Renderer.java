/*
 Title: Renderer
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import engine.world.components.Sprite;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final List<BatchRenderer> batchList = new ArrayList<>();

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
            if (batch.getBatchHasRoom() && batch.getzIndex() == sprite.owningGameObject.getzIndex()) {
                Texture tex = sprite.getSpriteTexture();
                if (tex == null || (batch.hasTexture(tex) || batch.isTexSlotsFull())) {
                    batch.addSpriteToBatch(sprite);
                    return true;
                }
            }
        }
        return false;
    }

    private void createNewBatch(Sprite sprite) {
        BatchRenderer newBatch = new BatchRenderer(sprite.owningGameObject.getzIndex());
        newBatch.addSpriteToBatch(sprite);
        batchList.add(newBatch);
        // Sorts the batches by their z index to ensure they are in the correct order
        // Can also call .reverseOrder()
        Collections.sort(batchList);
    }
}
/*End of Renderer class*/
