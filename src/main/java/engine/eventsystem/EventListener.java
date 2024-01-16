package engine.eventsystem;

import engine.objects.GameObject;

/**
 * This interface declares methods for handling events. Implementing classes can choose to implement
 * either the version that takes both a GameObject and an Event parameter or the one that takes only an Event parameter.
 */
public interface EventListener {

    /**
     * Handles an event with both a GameObject and an Event parameter.
     *
     * @param gameObject The GameObject associated with the event.
     * @param event      The Event to handle.
     */
    void onEvent(GameObject gameObject, Event event);

    /**
     * Handles an event with only an Event parameter.
     *
     * @param event The Event to handle.
     */
    void onEvent(Event event);
}