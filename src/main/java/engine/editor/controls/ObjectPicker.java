/*
 Title: PickingTexture
 Date: 2024-03-21
 Author: Kyle St John
 */
package engine.editor.controls;

import engine.debugging.info.Logger;

import static org.lwjgl.opengl.GL30.*;

public class ObjectPicker {
    private int frameBufferId;

    public ObjectPicker(int width, int height) {
        init(width, height);
    }


    public void init(int width, int height) {
        // Generate framebuffer
        frameBufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);

        int pickingTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, pickingTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0,
                GL_RGB, GL_FLOAT, 0);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                pickingTextureId, 0);

        glReadBuffer(GL_NONE);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            try {
                assert false : "Error: Framebuffer is not complete";
            } catch (AssertionError e) {
                System.err.println("Framebuffer is not complete");
                Logger.error("Framebuffer is not complete");
            }
        }

        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }


    public void bind() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBufferId);
    }


    public void unbind() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }


    public int readObjectIDByPixel(int x, int y) {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferId);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        float[] pixels = new float[3];
        glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels);

        return (int)(pixels[0]) - 1;
    }
}
/*End of PickingTexture class*/
