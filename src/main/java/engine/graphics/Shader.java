/*
 Title: Shader
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import static org.lwjgl.opengl.GL20.*;

/**
 * The main shader class for the engine. Preforms the shader compilation, linking
 * and error checking
 */
public class Shader {

    String vertexShaderSrc = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
            "}";

    String fragmentShaderSrc = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
            "} ";

    int vertexShaderID, fragmentShaderID;

    int shaderProgramID;

    /**
     * Compile the vertex and fragment shaders and check for errors in the compilation process
     */
    public void compile() {
        // Create vertex shader then compile and check for errors
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSrc);
        glCompileShader(vertexShaderID);
        checkShaderStatus(vertexShaderID);

        // Create the fragment shader then compile and check for errors
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSrc);
        glCompileShader(vertexShaderID);
        checkShaderStatus(fragmentShaderID);
    }

    /**
     * Link the shaders and create a shader program
     */
    public void link() {
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
    }

    /**
     * Tell openGl to use this shader program
     */
    public void use() {
        glUseProgram(shaderProgramID);
    }

    /**
     * Check if a shader has compiled successfully
     */
    private void checkShaderStatus(int shaderID) {
        int compilationSuccess = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (compilationSuccess == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }
    }
}
/*End of Shader class*/
