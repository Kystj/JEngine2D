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
    private BatchRenderer batchRenderer = new BatchRenderer();

    /**
     * Call the render() method on each render Batch object in the batch renderer list
     */
    public void render() {
        batchRenderer.render();
    }

    public void addSpriteToBatch(Sprite sprite) {
        boolean added = false;
        for (BatchRenderer batch : batchRendererList) {
            if (!batch.isBatchFull()) {
                batch.addQuad(sprite);
                added = true;
                break;
            }
        }

        if (!added) {
            BatchRenderer batch = new BatchRenderer();
            batch.init();
            batchRendererList.add(batch);
            batch.addQuad(sprite);
        }
    }

    public void addSprite(GameObject gameObject) {
        Sprite sprite = gameObject.getComponent(Sprite.class);
        if (sprite != null) {
            addSpriteToBatch(sprite);
        }
    }

    public void cleanup() {
        // TODO: Implement renderer clean up code
    }
}
/*End of renderer class*/
