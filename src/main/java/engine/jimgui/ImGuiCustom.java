/*
 Title: ImGuiCustom
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.jimgui;

import imgui.ImGui;

public class ImGuiCustom {

    public static boolean closeButton() {
        return !ImGui.button("Close", 50.0f, 20.0f);
    }
}
/*End of ImGuiCustom class*/
