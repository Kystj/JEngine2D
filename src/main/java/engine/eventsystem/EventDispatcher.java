/*
 * Title: EventDispatcher
 * Date: 2023-11-20
 * Author: Kyle St John
 */
package engine.eventsystem;

import engine.ui.settings.EConstants.EventType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EventDispatcher class responsible for managing event listeners and dispatching events.
 */
public class EventDispatcher {

    // Map to store event types and their corresponding listeners
    private static final Map<EventType, List<EventListener>> listenersMap = new HashMap<>();

    /**
     * Adds an event listener for a specific event type.
     *
     * @param eventType The type of the event.
     * @param listener  The listener to be added.
     */
    public static void addListener(EventType eventType, EventListener listener) {
        listenersMap.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Removes an event listener for a specific event type.
     *
     * @param eventType The type of the event.
     * @param listener  The listener to be removed.
     */
    public static void removeListener(EventType eventType, EventListener listener) {
        listenersMap.getOrDefault(eventType, new ArrayList<>()).remove(listener);
    }

    /**
     * Dispatches an event to all registered listeners of the corresponding event type.
     *
     * @param event The event to be dispatched.
     */
    public static void dispatchEvent(Event event) {
        EventType eventType = event.getEventType();
        List<EventListener> listeners = listenersMap.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
/* End of EventDispatcher class */