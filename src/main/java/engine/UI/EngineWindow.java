/*
 Title: EngineWindow
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.UI;


import engine.graphics.Framebuffer;
import engine.inputs.KeyInputs;
import engine.inputs.MouseInputs;
import engine.managers.SceneManager;
import org.joml.Vector2f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The main window class for JEngine2D
 */
public class EngineWindow {

    private static EngineWindow engine = null;

    private long glfwWindow;
    private int windowWidth;
    private int windowHeight;

    Framebuffer framebuffer;

    /**
     * ImGui controller
     */
    private final ImGuiController imGuiController = new ImGuiController();

    /**
     * Create a EngineWindow instance using the singleton pattern
     */
    public static EngineWindow get() {
        if (EngineWindow.engine == null) {
            EngineWindow.engine = new EngineWindow();
        }
        return EngineWindow.engine;
    }

    /**
     * Run JEngine2D
     */
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        tick();

        terminateProgram();
    }

    /**
     * Initialize the engine and openGL
     */
    private void init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints();

        // Sets the version of GLFW to use. Both min and max versions will use 3.3.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // This hint tells OpenGL to create an OpenGL context that is forward compatible
        // with newer versions of openGL.
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        this.windowWidth = (int) getDefaultScreenSize().x;
        this.windowHeight = (int) getDefaultScreenSize().y;

        // Prints the Window size to the console
        displayWindowDimensions();


        // Create the Window
        String title = "JEngine2D";
        glfwWindow = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
        if (glfwWindow == NULL)
            throw new IllegalStateException("Failed to create the GLFW window");

        // Create CallBacks: must be called from the main thread.
        glfwSetKeyCallback(glfwWindow, KeyInputs::keyCallBack);
        glfwSetCursorPosCallback(glfwWindow, MouseInputs::mousePosCallback);
        glfwSetScrollCallback(glfwWindow, MouseInputs::mouseScrollCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseInputs::mouseButtonCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            get().setWindowWidth(newWidth);
            get().setWindowHeight(newHeight);
        });


        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Enable alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        loadEngineConfigs();
    }

    /**
     * Load the engine configurations at program start up Initialize ImGui.
     */
    private void loadEngineConfigs() {
        // TODO: Improve this
        this.framebuffer = new Framebuffer(windowWidth, windowHeight);
        glViewport(0, 0, windowWidth, windowHeight);


        imGuiController.initImGui(glfwWindow);
        SceneManager.changeScene(glfwWindow);
    }

    /**
     * Begin the engines update loop
     */
    public void tick() {

        double startTime = glfwGetTime();
        double endTime;
        float deltaTime = -1;

        while (!glfwWindowShouldClose(glfwWindow)) {

            pollUserEvents();

            editorModeTick(deltaTime);
            //renderPlay(deltaTime);

            glfwSwapBuffers(glfwWindow);

            endTime = glfwGetTime();
            deltaTime = (float) (endTime - startTime);
            startTime = endTime;

            closeEngine();
        }
    }

    /**
     * Render and play the game without the editor (Plays the game in full screen mode)
     */
    private void playMode(float deltaTime) {
        SceneManager.renderScene();
    }

    /**
     * Render the game with the editor (The game can still be played from the viewport)
     */
    private void editorModeTick(float deltaTime) {
        this.framebuffer.bind();
        SceneManager.renderScene();
        this.framebuffer.unBind();
        imGuiController.tick(deltaTime);
    }

    /**
     * Check for user input
     */
    private void pollUserEvents() {
        glfwPollEvents();
    }

    /**
     * Terminate and cleanup
     */
    public void terminateProgram() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /**
     * Gets the default screen size of the monitor
     */
    private Vector2f getDefaultScreenSize() {
        // Get the primary monitor
        long primaryMonitor = glfwGetPrimaryMonitor();

        Vector2f size = new Vector2f();

        // The glfwGetVideoMode() function in GLFW retrieves the current video mode of a monitor.
        // A video mode describes the screen's resolution, color depth (bits per pixel),
        // refresh rate, and other related information.
        GLFWVidMode vidMode = glfwGetVideoMode(primaryMonitor);

        if (vidMode == null) {
            throw new NullPointerException("GLFWVidMode is null. Cannot retrieve screen dimensions. " +
                    "Ensure the primary window is assigned.");
        }
        size.x = vidMode.width();
        size.y = vidMode.height();
        return size;
    }

    /**
     * Prints the window dimensions to the console
     */
    private void displayWindowDimensions() {
        System.out.println("Screen Width: " + this.windowWidth);
        System.out.println("Screen Height: " + this.windowHeight);
        System.out.println("Aspect Ratio: " + (float) windowWidth / windowHeight);
    }

    /**
     * Frames per second counter
     */
    private void FPSCounter(float deltaTime) {
        System.out.println("FPS: " + (int) (1.0 / deltaTime));
    }

    /**
     * Terminate the engine and cleanup ImGui
     */
    private void closeEngine() {
        // TODO: Change this to a function and use a modifier
        if (glfwGetKey(glfwWindow, GLFW_KEY_ESCAPE) == GLFW_PRESS &&
                glfwGetKey(glfwWindow, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {

            imGuiController.destroy();
            glfwSetWindowShouldClose(glfwWindow, true);
            System.out.println("Engine terminated");
        }
    }

    /**
     * Get a reference to the glfw window
     */
    public long getWindowPointer() {
        return glfwWindow;
    }

    /**
     * Get window width
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * Get window height
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * Get the texture bound to the EngineWindows Framebuffer object
     */
    public int getFramebufferTexID() {
        return framebuffer.getFBTextureID();
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }
}
/*End of EngineWindow class*/
