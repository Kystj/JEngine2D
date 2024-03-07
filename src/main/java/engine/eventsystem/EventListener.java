package engine.eventsystem;

import engine.world.objects.GameObject;


public interface EventListener {

    void onEvent(GameObject gameObject, Event event);

    void onEvent(Event event);
}