package me.kivanova;

import acm.graphics.GOval;
import acm.graphics.GPoint;

import java.awt.*;

/**
 * This class keeps the Ball Object.
 */
class Ball {

    /* Time limits. */
    private static final int MAX_TIME = 3000;

    /* The max ball radius. */
    private static final double MAX_RADIUS = 100.0;

    /* The color model */
    private static final int COLOR_MODEL = 255;

    /* The start x and y coordinates.  */
    private final GPoint points;

    /* The ball radius. */
    private final double radius;

    /* The ball color. */
    private final Color ballColor;

    /* The GOval represents to this Ball. */
    private final GOval oval;

    Ball(int paramCoef, double centerX, double centerY) {
        this.radius = calculateRadius(paramCoef);
        this.ballColor = calculateColor(this.radius, paramCoef);
        this.points = new GPoint(centerX - this.radius, centerY - this.radius);
        this.oval = new GOval(points.getX(), points.getY(), 2 * radius, 2 * radius);
        this.oval.setColor(ballColor);
        this.oval.setFilled(true);
    }

    public GPoint getPoints() {
        return points;
    }

    public double getRadius() {
        return radius;
    }

    public Color getBallColor() {
        return ballColor;
    }

    public GOval getOval() {
        return oval;
    }

    /**
     * Calculate the ball radius.
     *
     * @param paramCoef The coefficient to calculate the ball parameters.
     * @return the ball radius.
     */
    private double calculateRadius(int paramCoef) {
        return (paramCoef > MAX_TIME)
                ? MAX_RADIUS
                : MAX_RADIUS * (double) paramCoef / MAX_TIME;
    }

    /**
     * Calculate the ball color.
     *
     * @param radius     The ball radius.
     * @param paramCoef The coefficient to calculate the ball parameters.
     * @return the ball color.
     */
    private Color calculateColor(double radius, int paramCoef) {
        if (paramCoef > MAX_TIME) {
            return Color.BLACK;
        } else {
            int levelColor = COLOR_MODEL - (int) (radius * COLOR_MODEL / MAX_RADIUS);
            return new Color(levelColor, levelColor, levelColor);
        }
    }
}
