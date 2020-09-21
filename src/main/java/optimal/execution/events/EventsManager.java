package optimal.execution.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventsManager {
    private final Map<EventType, List<Consumer<? super Event>>> typeToListeners = new HashMap<>();

    public interface Event {
        EventType getEventType();
    };

    public EventsManager() {
        for (EventType eventType : EventType.values()) {
            typeToListeners.put(eventType, new ArrayList<>());
        }
    }

    public void subscribe(EventType eventType, Consumer<? super Event> eventListener) {
        typeToListeners.get(eventType).add(eventListener);
    }

    public void unsubscribe(EventType eventType, Consumer<?> listener) {
        typeToListeners.get(eventType).remove(listener);
    }

    public <T extends Event> void notify(EventType eventType, T data) {
        for (Consumer<? super Event> eventListener : typeToListeners.get(eventType)) {
            eventListener.accept(data);
        }
    }

}
