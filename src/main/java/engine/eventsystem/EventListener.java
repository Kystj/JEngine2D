package engine.eventsystem;

import engine.world.objects.GameObject;
import engine.world.levels.Level;


public interface EventListener {

    void onEvent(Event event, GameObject gameObject);

    void onEvent(Event event, Level level);

    void onEvent(Event event);
}