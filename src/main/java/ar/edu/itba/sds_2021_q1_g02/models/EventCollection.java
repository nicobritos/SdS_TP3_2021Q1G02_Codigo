package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class EventCollection implements Iterable<Event> {
    private final Set<Event> priorityEvents;
    private final Set<Event> otherEvents;

    public EventCollection() {
        this(new HashSet<>(), new HashSet<>());
    }

    public EventCollection(Set<Event> priorityEvents, Set<Event> otherEvents) {
        this.priorityEvents = priorityEvents;
        this.otherEvents = otherEvents;
    }

    public Set<Event> getPriorityEvents() {
        return this.priorityEvents;
    }

    public Set<Event> getOtherEvents() {
        return this.otherEvents;
    }

    public boolean add(Event event) {
        if (event.getEventType().isPriority())
            return this.priorityEvents.add(event);
        else
            return this.otherEvents.add(event);
    }

    public boolean remove(Event event) {
        if (event.getEventType().isPriority())
            return this.priorityEvents.remove(event);
        else
            return this.otherEvents.remove(event);
    }

    public boolean contains(Event event) {
        if (event.getEventType().isPriority())
            return this.priorityEvents.contains(event);
        else
            return this.otherEvents.contains(event);
    }

    public boolean isEmpty() {
        return this.priorityEvents.isEmpty() && this.otherEvents.isEmpty();
    }

    @Override
    public Iterator<Event> iterator() {
        return new EventIterator(this.priorityEvents.iterator(), this.otherEvents.iterator());
    }

    private static class EventIterator implements Iterator<Event> {
        private final Iterator<Event> priorityIterator;
        private final Iterator<Event> otherIterator;

        public EventIterator(Iterator<Event> priorityIterator, Iterator<Event> otherIterator) {
            this.priorityIterator = priorityIterator;
            this.otherIterator = otherIterator;
        }

        @Override
        public boolean hasNext() {
            return this.priorityIterator.hasNext() || this.otherIterator.hasNext();
        }

        @Override
        public Event next() {
            if (!this.hasNext())
                throw new NoSuchElementException();

            if (this.priorityIterator.hasNext())
                return this.priorityIterator.next();
            else
                return this.otherIterator.next();
        }
    }
}
