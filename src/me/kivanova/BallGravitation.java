package me.kivanova;

import acm.graphics.GObject;
import acm.graphics.GOval;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class BallGravitation extends WindowProgram {

    /* Width and height of application window in pixels */
    public static final int APPLICATION_WIDTH = 600;
    public static final int APPLICATION_HEIGHT = 600;

    /* Gravitational acceleration. */
    private static final double GRAVITY = 0.425;

    /* Elasticity. */
    private static final double ELASTICITY = 0.75;

    /* The pause time of animation.  */
    private static final long PAUSE_TIME = 30;

    /* The amount of max bounce. */
    private static final int MAX_BOUNCE = 10;

    /* The start time. */
    private long startTime;

    /* The map contains the threads of the GOval. */
    private final Map<GOval, Thread> threads = new HashMap<>();

    /* The map contains the animation type of the GOval. */
    private final Map<GOval, AnimationType> animationTypes = new HashMap<>();

    public void run() {
        addMouseListeners();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /* Calculate the start time. */
        startTime = e.getWhen();
        /* If found the ball on the canvas. */
        GObject selectedObject = getElementAt(e.getX(), e.getY());
        if (selectedObject instanceof GOval) {
            /* Stop the previous thread. */
            stopPreviousThread((GOval) selectedObject);
            /* Change the value to animation type. */
            changeAnimationType((GOval) selectedObject);
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        /* Get the current object at the click location.*/
        GObject elementAt = getElementAt(event.getX(), event.getY());
        /* The current GOval. */
        /* If there is no oval at the click location, will create the new oval. */
        GOval oval = (elementAt != null) ? (GOval) elementAt :
                createBall(event.getWhen() - startTime, event.getX(), event.getY());
        /* Display the bounce ball animation. */
        bounceBallAnimation(oval);
    }

    /**
     * Display the bounce ball animation
     * depending on the animation type.
     *
     * @param oval The ball to bounce.
     */
    private void bounceBallAnimation(GOval oval) {
        Thread thread = new Thread(() -> {
            try {
                /* The velocity y-axis. */
                double dy = 0;
                /* The bounce amount. */
                int bounce = 0;
                while (bounce != MAX_BOUNCE) {
                    /* Move the ball by the current velocity. */
                    oval.move(0, dy);
                    if (animationTypes.get(oval) == AnimationType.BOUNCE_DOWN) {
                        /* Add the gravity. */
                        dy += GRAVITY;
                        if (isDownBounce(oval, dy)) {
                            dy *= -ELASTICITY;
                            bounce++;
                        }
                    } else {
                        /* Add the gravity. */
                        dy -= GRAVITY;
                        if (isUpBounce(oval, dy)) {
                            dy *= -ELASTICITY;
                            bounce++;
                        }
                    }
                    Thread.sleep(PAUSE_TIME);
                }
            } catch (InterruptedException e) {
                /*  no-op: The end of animation. */
            }
        });
        /* Add the new thread to threads map. */
        threads.put(oval, thread);
        thread.start();
    }

    /**
     * @param oval The current oval.
     * @param dy   The velocity y-axis.
     * @return does the bounce down?
     */
    private boolean isDownBounce(GOval oval, double dy) {
        return oval.getY() + oval.getHeight() >= getHeight() && dy > 0;
    }

    /**
     * @param oval The current oval.
     * @param dy   The velocity y-axis.
     * @return does the bounce up?
     */
    private boolean isUpBounce(GOval oval, double dy) {
        return oval.getY() <= 0 && dy < 0;
    }

    /**
     * If the thread depending on the GOval was found,
     * this thread will be stopped.
     */
    private void stopPreviousThread(GOval oval) {
        /* Find the thread depending on the GOval. */
        Thread currentThread = threads.get(oval);
        if (currentThread != null) {
            /* Stop the current thread. */
            currentThread.interrupt();
        } else {
            System.out.println("The thread depending on the GOval does not found.");
        }
    }

    /**
     * Create the ball object.
     *
     * @param clickTime the click time.
     * @param coordX    The center coord x-axis.
     * @param coordY    The center coord y-axis.
     * @return the oval represents the ball object.
     */
    private GOval createBall(long clickTime, double coordX, double coordY) {
        Ball ball = new Ball((int) clickTime, coordX, coordY);
        /* Draw the GOval, which represents the Ball. */
        add(ball.getOval());
        /* Add the new value to animation type map. */
        /* The default animation is "bounce down". */
        animationTypes.put(ball.getOval(), AnimationType.BOUNCE_DOWN);
        return ball.getOval();
    }

    /**
     * Change the value to animation type.
     *
     * @param oval the selected oval.
     */
    private void changeAnimationType(GOval oval) {
        switch (animationTypes.get(oval)) {
            case BOUNCE_DOWN -> animationTypes.put(oval, AnimationType.BOUNCE_UP);
            case BOUNCE_UP -> animationTypes.put(oval, AnimationType.BOUNCE_DOWN);
        }
    }
}
