package engine.eventsystem;

import engine.world.objects.GameObject;
import engine.world.scenes.Scene;


public interface EventListener {

    void onEvent(Event event, GameObject gameObject);

    void onEvent(Event event, Scene scene);

    void onEvent(Event event);
}