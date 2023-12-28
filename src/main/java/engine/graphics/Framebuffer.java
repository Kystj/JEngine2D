/*
 Title: Framebuffer
 Date: 2023-11-18
 Author: Kyle St John
 */

package engine.graphics;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {

    private int frameBufferID;
    private Texture textureAttachment;

    public Framebuffer(int width, int height) {
        init(width, height);
    }

    private void init(int width, int height) {
        // Generate framebuffer
        frameBufferID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);

        // Create the texture to attach to the framebuffer
        this.textureAttachment = new Texture(width, height);

        // Creates a frame buffer for a 2d texture and attaches it to color attachment 0
        // using the texID for the data it will store
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                this.textureAttachment.getTextureID(), 0);

        // Create render buffer to store the depth info
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32,width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE : "Error: Framebuffer is not complete";
        // This is crucial to allow the GPU to render to the window. Think front back buffer.
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind() {
        glClearColor(1,1,1,0);
        glClear(GL_COLOR_BUFFER_BIT);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
    }

    public void unBind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFboID() {
        return frameBufferID;
    }

    public int getFBTextureID() {
        return textureAttachment.getTextureID();
    }
}
/*End of Framebuffer class*/

