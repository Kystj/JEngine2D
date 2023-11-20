/*
 Title: Event
 Date: 2023-11-20
 Author: Kyle St John
 */
package engine.eventsystem;

import engine.settings.EConstants.EventType;

/**
 * This class represents a generic event with an associated event type, stored as a public field "eventType."
 * It provides a constructor to initialize the event with a specified type and includes a default constructor
 * that sets the event type to a default value, "UserEvent" of the enumerated type "EventType."
 */
public class Event {
    private final EventType eventType;

    /**
     * Construct an event with the designated event type
     */
    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Construct an event with the default user event type
     */
    public Event() {
        eventType = EventType.User;
    }

    /**
     * Return Events assigned type
     */
    public EventType getEventType() {
        return eventType;
    }
}
/*End of Event class*/
