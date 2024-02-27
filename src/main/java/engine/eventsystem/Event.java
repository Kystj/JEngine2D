/*
 * Title: Event
 * Date: 2023-11-20
 * Author: Kyle St John
 */
package engine.eventsystem;

import engine.ui.settings.EConstants.EventType;

/**
 * This class represents a generic event with an associated event type, stored as a public field "eventType."
 * It provides a constructor to initialize the event with a specified type and includes a default constructor
 * that sets the event type to a default value, "UserEvent" of the enumerated type "EventType."
 */
public class Event {

    // The type of the event
    private final EventType eventType;

    /**
     * Constructs an event with the designated event type.
     *
     * @param eventType The type of the event.
     */
    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Constructs an event with the default user event type.
     */
    public Event() {
        // Default to EventType.User if no type is specified
        this.eventType = EventType.User;
    }

    /**
     * Gets the type of the event.
     *
     * @return The event type.
     */
    public EventType getEventType() {
        return eventType;
    }
}
/* End of Event class */
