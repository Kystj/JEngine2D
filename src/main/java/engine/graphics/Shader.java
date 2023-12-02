/*
 Title: Shader
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

/**
 * The main shader class for the engine. Preforms the shader compilation, linking
 * and error checking
 */
public class Shader {

    private String vertexShaderSrc;
    private String fragmentShaderSrc;
    private int vertexShaderID, fragmentShaderID;
    private int shaderProgramID;
    private boolean isShaderBound = false;

    public Shader(String filepath) {
        init(filepath);
    }

    /**
     * Load the vertex and fragment shader information from a specified file
     */
    protected void init(String filepath) {
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexShaderSrc = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentShaderSrc = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexShaderSrc = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentShaderSrc = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    /**
     * Call to to compile and link shader methods
     */
    public void compileAndLinkShaders() {
        compile();
        link();
    }


    /**
     * Compile the vertex and fragment shaders and check for errors in the compilation process
     */
    private void compile() {
        // Create vertex shader then compile and check for errors
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSrc);
        glCompileShader(vertexShaderID);
        checkShaderStatus(vertexShaderID, "Vertex");

        // Create the fragment shader then compile and check for errors
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSrc);
        glCompileShader(fragmentShaderID);
        checkShaderStatus(fragmentShaderID, "Fragment");
    }

    /**
     * Link the shaders and create a shader program
     */
    private void link() {
        // Create the shader program
        shaderProgramID = glCreateProgram();

        //Attach the vertex and fragment shaders
        glAttachShader(shaderProgramID, vertexShaderID);
        glAttachShader(shaderProgramID, fragmentShaderID);

        // Link the shaders together and form the final shader program
        glLinkProgram(shaderProgramID);

        // Check for errors in linking
        int linkingSuccess = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (linkingSuccess == GL_FALSE) {
            int len = glGetShaderi(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Linking of shaders failed");
            System.out.println(glGetShaderInfoLog(shaderProgramID, len));
            assert false : "";
        }
        // Preform cleanup on the shader objects
        cleanup();
    }

    /**
     * Tell openGl to use this shader program
     */
    public void use() {
        if (!isShaderBound) {
            glUseProgram(shaderProgramID);
            isShaderBound = true;
        }
    }

    public void uploadVec4f(String uniformName, Vector4f vec) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform4f(uniformLocation, vec.x, vec.y, vec.z, vec.w);
    }

    /**
     * Get the location of and upload a texture uniform variable
     */
    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }


    /**
     * Check if a shader has compiled successfully
     */
    private void checkShaderStatus(int shaderID, String shaderType) {
        int compilationSuccess = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (compilationSuccess == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + shaderType + " shader compilation failed.");
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }
    }

    /**
     * Delete the shaders after they have been linked into the program object
     */
    private void cleanup() {
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
    }

    /**
     * Removes the current shader set in glUseProgram and sets it to 0 (no shader)
     */
    public void detatch() {
        glUseProgram(0);
        isShaderBound = false;
    }

    //TODO: Delete this
    public int getShaderProgramID() {
        return shaderProgramID;
    }
}
/*End of Shader class*/
