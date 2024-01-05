#type vertex
#version 330 core
// These values come from our vertex array buffer
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

// fColor comes from the vertex
in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main()
{
    if (fTexId > 0) {
        // Must cast to int to index into uTextures[]
        int id = int(fTexId);
        // By using fColor we can add things like tints to our game
        color = fColor * texture(uTextures[id], fTexCoords);
        //color = vec4(fTexCoords, 0, 1);
    } else {
        // Use reserve slot for color
        color = fColor;
    }
}