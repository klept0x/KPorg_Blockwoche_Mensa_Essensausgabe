package plotter.src.main.java.view;

import plotter.src.main.java.model.CustomPoint;

import java.util.List;

/**
 * Interface to control the GraphPlotter.
 */
public interface GraphPlotter {
    /**
     * Replaces or adds the dataPoints list.
     * @param points the data points.
     */
    void addPointList(List<CustomPoint> points);

    /**
     * Adds a point to the current diagram.
     * @param point
     */
    void addPoint(CustomPoint point);
}
