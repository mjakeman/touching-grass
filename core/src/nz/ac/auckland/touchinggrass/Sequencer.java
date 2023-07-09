package nz.ac.auckland.touchinggrass;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sequencer extends Thread {

    // A queue of Runnable objects, each representing an action to be executed.
    private final BlockingQueue<Runnable> actionQueue;

    // A special "poison pill" action that signals the sequencer to stop.
    private final Runnable POISON_PILL = () -> {};

    public Sequencer() {
        this.actionQueue = new LinkedBlockingQueue<>();
    }

    // Add a new action to the queue.
    public void addAction(Runnable action) {
        actionQueue.add(action);
    }

    // Signal the sequencer to stop after all current actions are executed.
    public void stopWhenDone() {
        actionQueue.add(POISON_PILL);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Take the next action from the queue and execute it.
                // If the queue is empty, this will block until an action is added.
                Runnable action = actionQueue.take();

                // If the action is the poison pill, exit the loop.
                if (action == POISON_PILL) break;

                action.run();
            } catch (InterruptedException e) {
                // If the thread was interrupted, exit the loop.
                break;
            }
        }
    }
}
