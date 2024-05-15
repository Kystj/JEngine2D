/*
 * Title: EventDispatcher
 * Date: 2023-11-20
 * Author: Kyle St John
 */
package engine.eventsystem;

import engine.utils.EConstants.EventType;
import engine.world.levels.Level;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages event listeners and dispatches events to registered listeners.
 */
public class EventDispatcher {

    // Fields

    /**
     * Map to store event types and their corresponding listeners.
     */
    private static final Map<EventType, List<EventListener>> LISTENER_MAP = new HashMap<>();

    // Public Methods

    /**
     * Adds an event listener for the specified event type.
     *
     * @param eventType The type of event to listen for.
     * @param listener  The event listener to add.
     */
    public static void addListener(EventType eventType, EventListener listener) {
        LISTENER_MAP.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Removes an event listener for the specified event type.
     *
     * @param eventType The type of event to remove the listener from.
     * @param listener  The event listener to remove.
     */
    public static void removeListener(EventType eventType, EventListener listener) {
        LISTENER_MAP.getOrDefault(eventType, new ArrayList<>()).remove(listener);
    }

    /**
     * Dispatches the given event to all listeners registered for its type.
     *
     * @param event The event to dispatch.
     */
    public static void dispatchEvent(Event event) {
        EventType eventType = event.getEventType();
        List<EventListener> listeners = LISTENER_MAP.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    /**
     * Dispatches the given event to all listeners registered for its type,
     * associated with a specific game object.
     *
     * @param event      The event to dispatch.
     * @param gameObject The game object associated with the event.
     */
    public static void dispatchEvent(Event event, GameObject gameObject) {
        EventType eventType = event.getEventType();
        List<EventListener> listeners = LISTENER_MAP.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event, gameObject);
        }
    }

    /**
     * Dispatches the given event to all listeners registered for its type,
     * associated with a specific game level.
     *
     * @param event The event to dispatch.
     * @param level The game level associated with the event.
     */
    public static void dispatchEvent(Event event, Level level) {
        EventType eventType = event.getEventType();
        List<EventListener> listeners = LISTENER_MAP.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event, level);
        }
    }
}
/* End of EventDispatcher class */