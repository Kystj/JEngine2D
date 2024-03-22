/*
 * Title: EventDispatcher
 * Date: 2023-11-20
 * Author: Kyle St John
 */
package engine.eventsystem;

import engine.settings.EConstants.EventType;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcher {

    private static final Map<EventType, List<EventListener>> listenersMap = new HashMap<>();


    public static void addListener(EventType eventType, EventListener listener) {
        listenersMap.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }


    public static void removeListener(EventType eventType, EventListener listener) {
        listenersMap.getOrDefault(eventType, new ArrayList<>()).remove(listener);
    }


    public static void dispatchEvent(Event event) {
        EventType eventType = event.getEventType();
        List<EventListener> listeners = listenersMap.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public static void dispatchEvent(Event event, GameObject gameObject) {
        EventType eventType = event.getEventType();
        List<EventListener> listeners = listenersMap.getOrDefault(eventType, new ArrayList<>());

        for (EventListener listener : listeners) {
            listener.onEvent(event, gameObject);
        }
    }
}
/* End of EventDispatcher class */