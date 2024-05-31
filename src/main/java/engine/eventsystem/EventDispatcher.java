/*
 * Title: EventDispatcher
 * Date: 2023-11-20
 * Author: Kyle St John
 */
package engine.eventsystem;

import engine.utils.engine.EConstants;
import engine.world.levels.Level;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventDispatcher {

    private static final Map<EConstants.EventType, List<EventListener>> LISTENER_MAP = new HashMap<>();

    public static void addListener(EConstants.EventType eventType, EventListener listener) {
        LISTENER_MAP.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }


    public static void removeListener(EConstants.EventType eventType, EventListener listener) {
        LISTENER_MAP.getOrDefault(eventType, new ArrayList<>()).remove(listener);
    }


    public static void dispatchEvent(Event event) {
        EConstants.EventType eventType = event.getEventType();
        List<EventListener> listeners = LISTENER_MAP.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }


    public static void dispatchEvent(Event event, GameObject gameObject) {
        EConstants.EventType eventType = event.getEventType();
        List<EventListener> listeners = LISTENER_MAP.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event, gameObject);
        }
    }


    public static void dispatchEvent(Event event, Level level) {
        EConstants.EventType eventType = event.getEventType();
        List<EventListener> listeners = LISTENER_MAP.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event, level);
        }
    }
}
/* End of EventDispatcher class */