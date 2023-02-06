import java.awt.geom.Point2D;

public class Point {
    private double scalarVaule;
    private Point2D coordinate;
    private boolean one;

    public Point(Double x, Double y) {
        this.coordinate = new Point2D.Double(x, y);
        scalarVaule = 0.0;
        one = true;
    }

    public void setValue(double value) {
        this.scalarVaule = value;
    }

    public void Setone(boolean zero) {
        one = zero;
    }

    public Point2D Coor() {
        return coordinate;
    }

    public double Field() {
        return scalarVaule;
    }

    public boolean One() {
        return one;
    }

}