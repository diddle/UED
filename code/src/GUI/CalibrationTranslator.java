/*	Copyright Niels Visser 2012
 *	
 *	This file is part of MusicTable.
 *	
 *	MusicTable is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	MusicTable is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with MusicTable.  If not, see <http://www.gnu.org/licenses/>.
 */
package GUI;

import java.awt.Point;

public class CalibrationTranslator {

    private Point[] points;
    private Point[] references;
    private static final int numIterations = 20;
    private static final int realScreenWidth = 1024;
    private static final int realScreenHeight = 768;

    public CalibrationTranslator(Point[] points, Point[] references) {
        this.points = points;
        this.references = references;
        int referenceWidth = references[1].x - references[0].x;
        int referenceHeight = references[2].y - references[1].y;
        double widthFactor = (double)(realScreenWidth - referenceWidth) / (2d * referenceWidth);
        double heightFactor = (double)(realScreenHeight - referenceHeight) / (2d * referenceHeight);
        Point p1 = interpolate(points[0], points[3], -heightFactor);
        Point p2 = interpolate(points[1], points[2], -heightFactor);
        Point p3 = interpolate(points[1], points[0], -widthFactor);
        Point p4 = interpolate(points[2], points[3], -widthFactor);
        Point p5 = interpolate(points[2], points[1], -heightFactor);
        Point p6 = interpolate(points[3], points[0], -heightFactor);
        Point p7 = interpolate(points[3], points[2], -widthFactor);
        Point p8 = interpolate(points[0], points[1], -widthFactor);
        this.points[0] = intersect(p1, p2, p7, p8);
        this.points[1] = intersect(p3, p4, p1, p2);
        this.points[2] = intersect(p5, p6, p3, p4);
        this.points[3] = intersect(p7, p8, p5, p6);
    }

    public Point translate(Point p) {
        double relativeDistanceFromBottom = getRelativeDistanceFromBottom(points[0], points[3], points[1], points[2], p);
        double relativeDistanceFromLeft = getRelativeDistanceFromBottom(points[1], points[0], points[2], points[3], p);
        int newX = (int) Math.round((double) realScreenWidth * relativeDistanceFromLeft);
        int newY = (int) Math.round((double) realScreenHeight * relativeDistanceFromBottom);
        System.out.println("oldX: " + p.x + " newX: " + newX);
        System.out.println("oldY: " + p.y + " newY: " + newY);
        Point result = new Point(newX, newY);
        return result;
    }

    private double getRelativeDistanceFromBottom(Point leftTop, Point leftBottom, Point rightTop, Point rightBottom, Point target) {
        double stepSize = 0.5;
        boolean goUp = true;
        double pos = 0d;
        for (int i = 0; i < numIterations; i++) {
            pos = goUp ? pos + stepSize : pos - stepSize;
            Point p1 = interpolate(leftBottom, leftTop, pos);
            Point p2 = interpolate(rightBottom, rightTop, pos);
            goUp = isLeft(p1, p2, target);
            stepSize /= 2d;
        }
        return pos;
    }

    private Point interpolate(Point p1, Point p2, double pos) {
        Point result = new Point();
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        result.x = (int) Math.round((double) dx * pos) + p1.x;
        result.y = (int) Math.round((double) dy * pos) + p1.y;
        return result;
    }

    private boolean isLeft(Point a, Point b, Point c) {
        return ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)) > 0;
    }

    private Point intersect(
            Point p1, Point p2,
            Point p3, Point p4) {
        int x1 = p1.x;
        int x2 = p2.x;
        int x3 = p3.x;
        int x4 = p4.x;
        int y1 = p1.y;
        int y2 = p2.y;
        int y3 = p3.y;
        int y4 = p4.y;
        int d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        int xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        int yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point(xi, yi);
    }
}
