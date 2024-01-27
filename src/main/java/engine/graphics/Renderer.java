/*
 Title: renderer
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import engine.components.Sprite;
import engine.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Renderer class for the engine
 */
public class Renderer {

    private final List<BatchRenderer> batchRendererList = new ArrayList<>();

    /**
     * Call the render() method on each render Batch object in the batch renderer list
     */
    public void render() {
        // Update the batches
        for (BatchRenderer batch : batchRendererList) {
            batch.render();
        }
    }

    /** Find a batch with room and add the sprite to that batch */
    private void addSpriteToBatch(Sprite sprite) {
        BatchRenderer batch = batchRendererList.stream()
                .filter(BatchRenderer::getBatchHasRoom)
                .findFirst()
                .orElseGet(() -> {
                    BatchRenderer newBatch = new BatchRenderer();
                    batchRendererList.add(newBatch);
                    return newBatch;
                });

        batch.addSpriteToBatch(sprite);
    }

    /** This method adds a sprite to a batch if it exists in the provided game object. */
    public void addSprite(GameObject gameObject) {
        Sprite sprite = gameObject.getComponent(Sprite.class);
        if (sprite != null) {
            addSpriteToBatch(sprite);
        }
    }


    /** Handles the disposal or release of resources associated with rendering. */
    private void cleanup() {
        // TODO: Implement renderer clean up code
    }
}
/*End of renderer class*/
