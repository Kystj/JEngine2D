/*
 Title: Viewport
 Date: 2023-11-18
 Author: Kyle St John
 */
package engine.editor;

import engine.UI.EngineWindow;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class Viewport {

    public void tick() {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        int textureId = EngineWindow.get().getFramebufferTexID();
        ImGui.image(textureId, 1000, 1000, 0, 1, 1, 0);

        ImGui.end();
    }
}
/*End of Viewport class*/
