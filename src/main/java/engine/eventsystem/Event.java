/*
 * Title: Event
 * Date: 2023-11-20
 * Author: Kyle St John
 */
package engine.eventsystem;

import engine.utils.EConstants.EventType;

/**
 * Represents a generic event with an associated event type.
 */
public class Event {

    // Fields

    /**
     * The type of the event.
     */
    private final EventType eventType;

    // Constructors

    /**
     * Constructs an event with the specified event type.
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

    // Public methods

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