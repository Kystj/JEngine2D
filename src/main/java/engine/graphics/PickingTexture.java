/*
 Title: PickingTexture
 Date: 2024-03-21
 Author: Kyle St John
 */
package engine.graphics;

import static org.lwjgl.opengl.GL30.*;

public class PickingTexture {
    private int pickingTextureId;
    private int fbo;
    private int depthTexture;

    public PickingTexture(int width, int height) {
        // The width and height will be the same size as the game window
        if (!init(width, height)) {
            assert false : "Error initializing picking texture";
        }
    }

    public boolean init(int width, int height) {
        // Generate framebuffer
        // - Consider making a factory that allows frame buffers to be created with different options
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // Create the texture to render the data to, and attach it to our framebuffer
        pickingTextureId = glGenTextures(); // Generates the textureID
        glBindTexture(GL_TEXTURE_2D, pickingTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // X direction
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // Y direction
        // We want openGL to select the closest pixel not belnd. This is important to get
        //accurate pixel selection
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0,
                GL_RGB, GL_FLOAT, 0); // Storing rgb data as a 32 bit float

//        The glFramebufferTexture2D function specifically attaches a 2D texture to a specified
//        attachment point of the currently bound framebuffer object. This means that subsequent
//        rendering operations will write pixel data to the attached texture instead of the
//        default framebuffer.
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                this.pickingTextureId, 0);

        // Create the texture object for the depth buffer
        glEnable(GL_TEXTURE_2D);
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0,
                GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
                GL_TEXTURE_2D, depthTexture, 0);

        // This code sequence is commonly used when performing off-screen rendering, render-to-texture, or multi-pass
        // rendering techniques where the primary focus is on writing to the framebuffer rather than reading its contents.
        glReadBuffer(GL_NONE); // Set to none because we are writing to the framebuffer not reading from it
        glDrawBuffer(GL_COLOR_ATTACHMENT0); // Sets the active draw buffer to the first color attachment for rendering.
        // Every time draw is called, the frameBuffer will draw to gl_color_attachment0

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Error: Framebuffer is not complete";
            return false;
        }

        // Unbind the texture and framebuffer
        // - This is important to ensure any subsequent calls do not affect the texture or framebuffer
        // - we have been working with here
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        return true;
    }


    // Binds the frame buffer for drawing
    public void enableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
    }

    // Unbinds the frame buffer
    public void disableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    //    GL_FRAMEBUFFER represents the entire FBO for both reading and drawing operations.
//    GL_DRAW_FRAMEBUFFER is the target for drawing or rendering operations.
//    GL_READ_FRAMEBUFFER is the target for reading operations within the FBO.
    public int readPixel(int x, int y) {
        // Bind the frame buffer to be read from. This allows to access its data and manipulate
        // it or store it further
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        float pixels[] = new float[3];
        glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels);

        // The -1 ensures that picking that does land on an object will return an id of -1
        return (int)(pixels[0]) - 1;
    }
}
/*End of PickingTexture class*/
