package optimal.execution.events;

import optimal.execution.ResultEntity;

public class ResultEntityObtainedEvent implements EventsManager.Event {
    private final EventType myType;
    private final ResultEntity resultEntity;

    public ResultEntityObtainedEvent(EventType myType, ResultEntity resultEntity) {
        this.myType = myType;
        this.resultEntity = resultEntity;
    }

    @Override
    public EventType getEventType() {
        return myType;
    }

    public ResultEntity getResultEntity() {
        return resultEntity;
    }
}
