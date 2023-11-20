/*
 Title: Scene
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.scene;

import engine.graphics.Renderer;

public class Scene {

    Renderer renderer;

    public Scene() {
        this.renderer = new Renderer();
    }

    public void tick() {

    }

    public void init() {
        renderer.init();
    }

    public void render() {
        renderer.render();
    }

    public void cleanup() {

    }
}
/*End of Scene class*/
