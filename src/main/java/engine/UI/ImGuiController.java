/*
 Title: ImGuiLayer
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.UI;

import engine.editor.Viewport;
import engine.managers.ShortcutHandler;
import engine.widgets.MainMenuBar;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;

/**
 * The main class in charge of the ImGui configurations. Controls set up, style and
 * engines various widgets
 */
public class ImGuiController {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    MainMenuBar mainMenuBar = new MainMenuBar();
    ShortcutHandler shortcutHandler = new ShortcutHandler();
    Viewport viewport = new Viewport();

    /**
     * Initialize ImGui context and configs
     */
    public void initImGui(long glfwWindow) {
        String glslVersion = "#version 330 core";
        setIOConfigs();
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);
    }

    /**
     * Set ImGui specific configurations, backend flags and the init.ini file path
     */
    private void setIOConfigs() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini"); // We don't want to save .ini file
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    /**
     * Update ImGui and its various widgets
     */
    public void tick(float deltaTime) {
        startFrame(deltaTime);
        enableDocking();
        updateViewport();
        updateWidgets();
        ImGui.showDemoWindow();
        endFrame();
    }

    /**
     * Update the viewport and its functions
     */
    private void updateViewport() {
        viewport.tick();
    }

    /**
     * Update all the different IMmGui widgets
     */
    private void updateWidgets() {
        mainMenuBar.tick();
        shortcutHandler.tick();
    }

    /**
     * Enables docking in ImGui and sets the configurations for the docking window
     */
    private void enableDocking() {
        ImBoolean open = new ImBoolean(true);
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;
        ImGui.popStyleVar(2);
        ImGui.begin("Dockspace", open, windowFlags);
        ImGui.dockSpace(ImGui.getID("Dockspace"));
        ImGui.end();
    }

    /**
     * Sets the style for ImGui, begins the new frame and pushes the engiens custom ImGui style
     * options onto the stack
     */
    private void startFrame(final float deltaTime) {
        ImGui.styleColorsClassic();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        pushCustomStyleVars();
    }

    /**
     * Ends the frame and pops the engines custom ImGui style options onto the stack
     */
    private void endFrame() {
        popCustomStyleVars();
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

//        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
//            ImGui.updatePlatformWindows();
//            ImGui.renderPlatformWindowsDefault();
//            GLFW.glfwMakeContextCurrent(backupWindowPtr);
//        }
    }

    /**
     * Cleanup and destroy the ImGui and its context
     */
    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    /**
     * Push custom ImGui style options
     */
    private void pushCustomStyleVars() {
        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 12f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0f, 0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowTitleAlign,0.5f, 0.5f);
    }

    /**
     * Pop custom ImGui style options
     */
    private void popCustomStyleVars() {
        ImGui.popStyleVar(3);
    }
}
/*End of ImGuiLayer class*/
