/*
 Title: Renderer
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import engine.world.components.Sprite;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The Renderer class is responsible for rendering sprites using BatchRenderer objects.
 */
public class Renderer {

    private final List<BatchRenderer> batchRendererList = new ArrayList<>();

    /**
     * Calls the render() method on each render Batch object in the batch renderer list.
     */
    public void render() {
        // Update the batches
        for (BatchRenderer batch : batchRendererList) {
            batch.render();
        }
    }

    /**
     * Finds a batch with room and adds the sprite to that batch.
     *
     * @param sprite The sprite to be added to a batch.
     */
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

    /**
     * Adds a sprite to a batch if it exists in the provided game object.
     *
     * @param gameObject The game object containing the sprite component.
     */
    public void addSprite(GameObject gameObject) {
        Sprite sprite = gameObject.getComponent(Sprite.class);
        if (sprite != null) {
            addSpriteToBatch(sprite);
        }
    }

    /**
     * Handles the disposal or release of resources associated with rendering.
     * TODO: Implement renderer cleanup code.
     */
    private void cleanup() {
        // TODO: Implement renderer cleanup code
    }
}
/*End of Renderer class*/
