package engine.eventsystem;

import engine.world.objects.GameObject;


public interface EventListener {

    void onEvent(Event event, GameObject gameObject);

    void onEvent(Event event);
}