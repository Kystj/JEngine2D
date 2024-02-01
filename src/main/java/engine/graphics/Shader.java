/*
 Title: Shader
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

/**
 * The Shader class represents a shader program in OpenGL.
 * It loads vertex and fragment shader sources from a file and sets them for compilation.
 */
public class Shader {

    // Shader source code for vertex and fragment shaders
    private String vertexShaderSrc;
    private String fragmentShaderSrc;

    // Shader IDs for vertex and fragment shaders
    private int vertexShaderID;
    private int fragmentShaderID;

    // Shader program ID after linking
    private int shaderProgramID;

    // Flag to check if the shader is bound
    private boolean isShaderBound = false;

    /**
     * Constructs a Shader object by initializing shaders from a specified file.
     *
     * @param filepath The file path to the shader source file.
     */
    public Shader(String filepath) {
        init(filepath);
    }

    /**
     * Initializes the shader by reading shader source from the file and extracting vertex and fragment shaders.
     *
     * @param filepath The file path to the shader source file.
     */
    protected void init(String filepath) {
        try {
            // Read the shader source from the file
            String source = new String(Files.readAllBytes(Paths.get(filepath)));

            // Split the source based on the '#type' keyword to separate vertex and fragment shaders
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Extract the patterns and set shader sources
            String firstPattern = extractPattern(source, 0);
            String secondPattern = extractPattern(source, source.indexOf("#type", firstPattern.length()));

            setShaderSource(firstPattern, splitString[1]);
            setShaderSource(secondPattern, splitString[2]);
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Unable to load file: '" + filepath + "'";
        }
    }

    /**
     * Extracts the shader type pattern (e.g., 'vertex' or 'fragment') from the source code.
     *
     * @param source        The source code of the shader.
     * @param startingIndex The starting index to search for the pattern.
     * @return The extracted pattern.
     */
    private String extractPattern(String source, int startingIndex) {
        int index = startingIndex + "#type".length() + 1;
        int eol = source.indexOf("\r\n", index);
        return source.substring(index, eol).trim();
    }

    /**
     * Sets the shader source based on the specified pattern ('vertex' or 'fragment').
     *
     * @param pattern      The shader type pattern.
     * @param shaderSource The source code of the shader.
     * @throws IOException If the specified pattern is neither 'vertex' nor 'fragment'.
     */
    private void setShaderSource(String pattern, String shaderSource) throws IOException {
        if (pattern.equals("vertex")) {
            vertexShaderSrc = shaderSource;
        } else if (pattern.equals("fragment")) {
            fragmentShaderSrc = shaderSource;
        } else {
            throw new IOException("Error: Unknown shader type - " + pattern);
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

    public void uploadVec2f(String uniformName, Vector2f vec) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform2f(uniformLocation, vec.x, vec.y);
    }

    public void uploadVec3f(String uniformName, Vector3f vec) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform3f(uniformLocation, vec.x, vec.y, vec.z);
    }

    /**
     * Get the location of and upload a texture uniform variable
     */
    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    /**
     * Get the location of and upload a Matrix4f uniform variable
     */
    public void uploadMat4f(String varName, Matrix4f mat4) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    /**
     * Get the location of and upload an array of ints as a uniform variable
     */
    public void uploadIntArray(String varName, int[] intArray) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1iv(varLocation, intArray);
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
    public void detach() {
        glUseProgram(0);
        isShaderBound = false;
    }
}
/*End of Shader class*/
