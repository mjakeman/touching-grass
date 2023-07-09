package nz.ac.auckland.touchinggrass;

import java.util.LinkedList;
import java.util.Queue;

public class Sequencer {

    static class Action {
        float delay;
        Runnable action;

        public Action(float delay, Runnable action) {
            this.delay = delay;
            this.action = action;
        }
    }

    private float frameTime = 0f;
    boolean isBlocking = false;

    // A queue of Runnable objects, each representing an action to be executed.
    private final Queue<Action> actionQueue;

    // A special "poison pill" action that signals the sequencer to stop.
    private final Action POISON_PILL = new Action(0, () -> {});

    public Sequencer() {
        this.actionQueue = new LinkedList<>();
    }

    // Add a new action to the queue.
    public void addAction(Action action) {
        actionQueue.add(action);
    }

    // Signal the sequencer to stop after all current actions are executed.
    public void stopWhenDone() {
        actionQueue.add(POISON_PILL);
    }

    public boolean step(float deltaTime) {

        // Take the next action from the queue and execute it.
        // If the queue is empty, this will block until an action is added.
        Action action = actionQueue.peek();

//        System.out.printf("Polling %b\n", action != null);

        if (action == null) return isBlocking;

        frameTime += deltaTime;

        if (frameTime < action.delay) return isBlocking;

        action = actionQueue.remove();
        action.action.run();

        System.out.printf("Executed! frameTime=%f%n\n", frameTime);
        frameTime -= action.delay;

        return isBlocking;
    }
}
