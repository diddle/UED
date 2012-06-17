/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Point;

/**
 *
 * @author Niels Visser
 */
public class CalibrationTranslator {
    
    private Point[] points;
    private Point[] references;
    
    public CalibrationTranslator(Point[] points, Point[] references) {
        this.points = points;
    }
    
    public Point translate(Point p) {
        int leftHeight = points[3].y - points[0].y;
        int rightHeight = points[2].y - points[1].y;
        int topWidth = points[1].x - points[0].x;
        int bottomWidth = points[2].x - points[3].x;
        return null;
    }
}
