package engine.utils.imgui;

import engine.graphics.Texture;
import engine.utils.engine.ResourceUtils;

public interface GConstants {

    int COLUMN_WIDTH_MEDIUM = 100;
    float SLIDER_SPEED = 0.3f;

    Texture LOCK_BUTTON_TEXTURE = ResourceUtils.getOrCreateTexture("assets/buttons/locked.png");
    Texture UNLOCK_BUTTON_TEXTURE = ResourceUtils.getOrCreateTexture("assets/buttons/unlocked.png");
    Texture GRID_BUTTON_TEXTURE = ResourceUtils.getOrCreateTexture("assets/buttons/grid.png");
    Texture LAUNCH_BUTTON_TEXTURE = ResourceUtils.getOrCreateTexture("assets/buttons/launch.png");
    Texture PLAY_BUTTON_TEXTURE = ResourceUtils.getOrCreateTexture("assets/buttons/play.png");
    Texture STOP_BUTTON_TEXTURE = ResourceUtils.getOrCreateTexture("assets/buttons/stop.png");
    Texture WIREFRAME_BUTTON_TEXTURE = ResourceUtils.getOrCreateTexture("assets/buttons/frame.png");



}
