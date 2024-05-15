/*
 * Title: Event
 * Date: 2023-11-20
 * Author: Kyle St John
 */
package engine.eventsystem;

import engine.world.objects.GameObject;
import engine.world.levels.Level;

/**
 * Defines methods to handle events.
 */
public interface EventListener {

    // Methods

    /**
     * Handles the event associated with a specific game object.
     *
     * @param event      The event to handle.
     * @param gameObject The game object associated with the event.
     */
    void onEvent(Event event, GameObject gameObject);

    /**
     * Handles the event associated with a specific game level.
     *
     * @param event The event to handle.
     * @param level The game level associated with the event.
     */
    void onEvent(Event event, Level level);

    /**
     * Handles the event.
     *
     * @param event The event to handle.
     */
    void onEvent(Event event);
}
