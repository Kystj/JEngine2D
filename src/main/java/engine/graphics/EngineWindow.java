package engine.graphics;/*
 Title: EWindow
 Date: 2024-04-01
 Author: Kyle St John
 */
/*
 Title: EngineWindow
 Date: 2023-11-06
 Author: Kyle St John
 */


import engine.debug.draw.DebugRenderer;
import engine.debug.info.DebugLogger;
import engine.editor.controls.ImGuiController;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.io.KeyInputs;
import engine.io.MouseInputs;
import engine.world.levels.TestLevel;
import engine.editor.GameEditor;
import engine.utils.EConstants;
import engine.utils.MathUtils;
import engine.world.objects.GameObject;
import engine.world.levels.Level;
import org.joml.Vector2f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

import static engine.utils.EConstants.DEFAULT_ASPECT_RATIO;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class EngineWindow implements EventListener {

    private static EngineWindow engine = null;

    private long glfwWindow;
    private int windowWidth;
    private int windowHeight;
    private float aspectRatio;

    public static float FPS;
    public static float FRAME_TIME;
    private float DELTA_TIME = -1;

    private Framebuffer framebuffer;
    private boolean isWireFrameEnabled = false;

    private final ImGuiController ImGui_Controller = new ImGuiController();
    public static GameEditor Game_Editor;
    
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
        this.aspectRatio =  DEFAULT_ASPECT_RATIO;

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

        // Add the various events the EngineWindow should respond to and register it as a listener
        EventDispatcher.addListener(EConstants.EventType.Play, this);
        EventDispatcher.addListener(EConstants.EventType.Stop, this);
        EventDispatcher.addListener(EConstants.EventType.Launch, this);
        EventDispatcher.addListener(EConstants.EventType.Wire_Frame, this);

        // Initialize ImGui functionality
        ImGui_Controller.initImGui(glfwWindow);

        // Load and initialize the editor
        Game_Editor = new GameEditor();
        Game_Editor.init();
        Game_Editor.loadNewScene(new TestLevel());
    }


    public void tick() {
        float lastUpdateTime = (float) glfwGetTime();
        float accumulatedTime = 0.0f;
        float timePerFrame = 0.0f;
        int frames = 0;
        final int NUM_FRAMES_TO_AVERAGE = 100; // Number of frames to average for FPS calculation
        DELTA_TIME = 1.0f / 120.0f; // Fixed logical time step

        while (!glfwWindowShouldClose(glfwWindow)) {
            float currentTime = (float) glfwGetTime();
            float elapsedTime = currentTime - lastUpdateTime;
            lastUpdateTime = currentTime;

            accumulatedTime += elapsedTime;
            timePerFrame += elapsedTime;

            pollUserEvents();

            // Update logic at fixed rate
            while (accumulatedTime >= DELTA_TIME) {
                tick(DELTA_TIME);
                accumulatedTime -= DELTA_TIME;
            }

            // Render
            render();
            glfwSwapBuffers(glfwWindow);

            frames++;

            // Calculate FPS after a certain number of frames
            if (frames >= NUM_FRAMES_TO_AVERAGE) {
                FPS = frames / FRAME_TIME; // fps is a global variable used to pass metrics around
                // Reset frame time and frame count
                frames = 0;
                FRAME_TIME = timePerFrame; // frameTime is a global variable used to pass metrics around
                timePerFrame = 0.0f;
            }
        }
        closeEngine();
    }


    private void tick(float deltaTime) {
        DebugRenderer.tick();
        // Update game logic here with the fixed deltaTime
        Game_Editor.tick(deltaTime);
    }


    private void render() {
        Game_Editor.renderEditor();
        // Render the editor scene to the framebuffer
        this.framebuffer.use(windowWidth, windowHeight);
        clear();
        if (isWireFrameEnabled) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        DebugRenderer.render();
        Renderer.setDefaultShader();
        Game_Editor.renderScene();//currentScene.render();
        this.framebuffer.detatch();

        if (isWireFrameEnabled) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        // Render/Update the ImGui components
        ImGui_Controller.render();
    }


    public float getAspectRatio() {
        return aspectRatio;
    }


    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
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
        DebugLogger.info("Launching StellarSprite2D....\n"
                + "\t...Screen Dimensions: " + this.windowWidth + " x " + this.windowHeight +
                "\n\t...Aspect Ratio: " + MathUtils.decimalToAspectRatio((float) windowWidth / windowHeight)
                + "\n\t...Ready!", true);
    }


    private void closeEngine() {
        if (glfwGetKey(glfwWindow, GLFW_KEY_ESCAPE) == GLFW_PRESS &&
                glfwGetKey(glfwWindow, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {

            ImGui_Controller.destroy();
            glfwSetWindowShouldClose(glfwWindow, true);
            System.out.println("Engine terminated");
        }
    }


    public boolean isWireFrameEnabled() {
        return isWireFrameEnabled;
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
        glClearColor(0, 0, 0, 1.0f);
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }


    public float getDeltaTime() {
        return DELTA_TIME;
    }


    @Override
    public void onEvent(Event event, GameObject gameObject) {

    }

    @Override
    public void onEvent(Event event, Level level) {

    }


    @Override
    public void onEvent(Event event) {
        if (event.getEventType() == EConstants.EventType.Wire_Frame) {
            isWireFrameEnabled = !isWireFrameEnabled;
        }
    }
}