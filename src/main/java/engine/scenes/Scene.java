/*
 Title: Scene
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.scenes;

import engine.graphics.Renderer;

public class Scene {

    Renderer renderer;

    public Scene(long glfwWindow) {
        this.renderer = new Renderer(glfwWindow);
    }

    public void tick() {

    }

    public void render() {
        renderer.render();
    }

    public void cleanup() {

    }
}
/*End of Scene class*/
