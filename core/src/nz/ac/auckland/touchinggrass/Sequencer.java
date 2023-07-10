package nz.ac.auckland.touchinggrass;

import java.util.LinkedList;
import java.util.Queue;

public class Sequencer {

    static class BaseAction {
        float delay;
    }

    static class Action extends BaseAction {
        Runnable action;

        public Action(float delay, Runnable action) {
            this.delay = delay;
            this.action = action;
        }
    }

    interface TransitionCallback {
        void callback(float start, float end, float progress);
    }

    static class Transition extends BaseAction {
        float delay;
        float start;
        float end;
        TransitionCallback callback;

        public Transition(float delay, float start, float end, TransitionCallback callback) {
            this.delay = delay;
            this.callback = callback;
            this.start = start;
            this.end = end;
        }
    }

    private float frameTime = 0f;
    boolean isBlocking = false;

    // A queue of Runnable objects, each representing an action to be executed.
    private final Queue<BaseAction> actionQueue;

    // A special "poison pill" action that signals the sequencer to stop.
    private final Action POISON_PILL = new Action(0, () -> {});

    public Sequencer() {
        this.actionQueue = new LinkedList<>();
    }

    // Add a new action to the queue.
    public void addAction(BaseAction action) {
        actionQueue.add(action);
    }

    // Signal the sequencer to stop after all current actions are executed.
    public void stopWhenDone() {
        actionQueue.add(POISON_PILL);
    }

    public boolean step(float deltaTime) {

        // Take the next action from the queue and execute it.
        // If the queue is empty, this will block until an action is added.
        BaseAction action = actionQueue.peek();

        if (action == null) return isBlocking;

        frameTime += deltaTime;

        if (frameTime < action.delay) {
            return isBlocking;
        }

//        System.out.println(frameTime);

        if (action instanceof Action) {
            Action realAction = (Action) action;
            actionQueue.remove();
            realAction.action.run();
            frameTime = 0;
        } else if (action instanceof Transition) {
            Transition transition = (Transition) action;
            float progress = 1 - ((transition.delay - frameTime) / transition.delay);

            if (progress <= 1) {
                transition.callback.callback(transition.start, transition.end, progress);
                return isBlocking;
            }

            actionQueue.remove();
            frameTime = 0;
        }

        return isBlocking;
    }
}
