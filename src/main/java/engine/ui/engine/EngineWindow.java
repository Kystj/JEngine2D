/*
 Title: EngineWindow
 Date: 2023-11-06
 Author: Kyle St John
 */

package engine.ui.engine;

import engine.debug.draw.DebugRenderer;
import engine.debug.logger.Logger;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.graphics.Framebuffer;
import engine.graphics.OrthographicCamera;
import engine.io.KeyInputs;
import engine.io.MouseInputs;
import engine.settings.EConstants;
import engine.utils.MathUtils;
import engine.world.objects.GameObject;
import engine.world.scenes.EditorScene;
import engine.world.scenes.Scene;
import org.joml.Vector2f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class EngineWindow implements EventListener {

    private static EngineWindow engine = null;

    private long glfwWindow;

    private int windowWidth;
    private int windowHeight;

    private float defaultAspectRatio = 16.0f / 9.0f;

    private Framebuffer framebuffer;

    private boolean isFrameModeOn = false;

    private final ImGuiController imGuiController = new ImGuiController();

    private int engineMode = 0; // 0: Editor. 1: Full play
    private Scene currentScene;

    private float deltaTime = -1;

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
        logLaunchInfo();


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

    private void loadEngineConfigs() {
        // Initialize the frame buffer
        this.framebuffer = new Framebuffer(windowWidth, windowHeight);
        glViewport(0, 0, windowWidth, windowHeight);

        // Initialize ImGui functionality
        imGuiController.initImGui(glfwWindow);

        // Add the various events the EngineWindow should respond to and register it as a listener
        EventDispatcher.addListener(EConstants.EventType.Play, this);
        EventDispatcher.addListener(EConstants.EventType.Stop, this);
        EventDispatcher.addListener(EConstants.EventType.Launch, this);
        EventDispatcher.addListener(EConstants.EventType.Wire_Frame, this);

        // Load the Editor Scene at launch
        changeScene(0);
    }


    public void tick() {
        double startTime = glfwGetTime();
        double endTime;

        while (!glfwWindowShouldClose(glfwWindow)) {

            pollUserEvents();

            DebugRenderer.tick();

            if (deltaTime >= 0) {
                updateEngineMode(deltaTime);
            }
            glfwSwapBuffers(glfwWindow);

            endTime = glfwGetTime();
            deltaTime = (float) (endTime - startTime);
            startTime = endTime;

            closeEngine();
        }
    }

    public void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new EditorScene();
                currentScene.init();
                break;
            case 1:
                System.out.println("Initializing scene");
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }


    private void updateEngineMode(float deltaTime) {
        if (KeyInputs.keyPressed(GLFW_KEY_ESCAPE)) {
            engineMode = 0;
        }

        switch (engineMode) {
            case 0:
                editorModeTick(deltaTime);
                break;
            case 1:
                fullPlayModeTick(deltaTime);
                break;
            default:
                assert false : "No play mode selected";
        }
    }

    // TODO: Separate into separate functions and implement time step correctly
    private void fullPlayModeTick(float deltaTime) {
        clear();
        currentScene.render();
        currentScene.tick(deltaTime);
    }

    // TODO: Separate into separate functions and implement time step correctly
    private void editorModeTick(float deltaTime) {
        // Safety check
        if (engineMode == 0) {
            this.framebuffer.bind(windowWidth, windowHeight);
            clear();
            if (isFrameModeOn) {
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            }

            // Render the editor scene to the framebuffer
            DebugRenderer.render();
            currentScene.render();

            // Renderer to the framebuffer
            currentScene.tick(deltaTime);
            this.framebuffer.unbind();

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

            // Update the ImGui components
            imGuiController.tick(deltaTime);
        }
    }

    public float getDefaultAspectRatio() {
        return defaultAspectRatio;
    }


    public void setDefaultAspectRatio(float defaultAspectRatio) {
        this.defaultAspectRatio = defaultAspectRatio;
    }

    public OrthographicCamera getCamera() {
        return currentScene.getOrthoCamera();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    private void pollUserEvents() {
        glfwPollEvents();
    }

    public void terminateProgram() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

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


    private void logLaunchInfo() {
        Logger.info("Screen Dimensions: " + this.windowWidth + " x " + this.windowHeight);
        Logger.info("Aspect Ratio: " + MathUtils.decimalToAspectRatio((float) windowWidth / windowHeight));
    }


    private void closeEngine() {
        if (glfwGetKey(glfwWindow, GLFW_KEY_ESCAPE) == GLFW_PRESS &&
                glfwGetKey(glfwWindow, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {

            imGuiController.destroy();
            glfwSetWindowShouldClose(glfwWindow, true);
            System.out.println("Engine terminated");
        }
    }

    public boolean isFrameModeOn() {
        return isFrameModeOn;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getFramebufferTexID() {
        return framebuffer.getTextureID();
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }


    private void clear() {
        glClearColor(0,0,0,1.0f);
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }


    public float getDeltaTime() {
        return deltaTime;
    }


    @Override
    public void onEvent(GameObject gameObject, Event event) {

    }


    @Override
    public void onEvent(Event event) {
        switch (event.getEventType()) {
            case Play:
                engineMode = 0;
                System.out.println("Playing...");
                break;
            case Stop:
                System.out.println("Stopping...");
                break;
            case Launch:
                engineMode = 1;
                System.out.println("Launching full play mode...");
                break;
            case Wire_Frame:
                isFrameModeOn = !isFrameModeOn;
                break;
        }
    }
}
/*End of EngineWindow class*/