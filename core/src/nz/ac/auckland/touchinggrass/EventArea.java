package nz.ac.auckland.touchinggrass;

import jdk.jfr.Event;

public class EventArea extends GameObject {

    private boolean done = false;

    interface IEventAction {
        void handle(EventArea area);
    }

    public IEventAction onEnter;

    public EventArea(IEventAction onEnter) {
        this.doesCollision = true;
        this.onEnter = (area) -> {
            if (done) return;
            done = true;
            onEnter.handle(area);
        };
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(position.x - 1, position.z - 1, 3, 3);
    }
}
