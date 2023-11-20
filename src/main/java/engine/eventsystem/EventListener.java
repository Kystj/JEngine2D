package engine.eventsystem;

import engine.objects.GameObject;

/**
 * This interface declares two methods for handling events, where the first method expects both a GameObject and an Event
 * parameter, while the second method takes only an Event parameter.
 */
public interface EventListener {
    void onEvent(GameObject gameObject, Event event);
    void onEvent(Event event);
}
