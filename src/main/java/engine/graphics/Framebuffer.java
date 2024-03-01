/*
 * Title: Framebuffer
 * Date: 2023-11-18
 * Author: Kyle St John
 */

package engine.graphics;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {

    private int framebufferID;
    private Texture textureAttachment;

    public Framebuffer(int width, int height) {
        init(width, height);
    }

    private void init(int width, int height) {
        // Generate framebuffer
        framebufferID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);

        // Create the texture to attach to the framebuffer
        textureAttachment = new Texture(width, height);

        // Attach the texture to color attachment 0
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                textureAttachment.getTextureID(), 0);

        // Create a render buffer to store the depth information
        int renderbufferID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderbufferID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbufferID);

        // Check if the framebuffer is complete
        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE : "Error: Framebuffer is not complete";

        // Unbind the framebuffer to allow rendering to the window
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind(int width, int height) {
        //glViewport(0, 0, width, height);
        glClearColor(1, 1, 1, 0);

        glClear(GL_COLOR_BUFFER_BIT);
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFramebufferID() {
        return framebufferID;
    }

    public int getTextureID() {
        return textureAttachment.getTextureID();
    }
}
/* End of Framebuffer class */