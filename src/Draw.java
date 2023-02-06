import java.util.ArrayList;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class Draw extends JPanel {
    ArrayList<Point> lines = new ArrayList<Point>();
    ArrayList<Point> vertex;
    ArrayList<Point2D> scaledCoord;
    ArrayList<int[]> cells;
    ArrayList<csvInterface> colormap;   
    
    int type;
    double[] inputvalues;

    public Draw(int width, int height, ArrayList<Point> points, ArrayList<int[]> cells,
            ArrayList<csvInterface> colorMap, int type, double[] inputvalues) {
        this.vertex = points;
        this.cells = cells;
        this.colormap = colorMap;
        this.type = type;
        this.inputvalues = inputvalues;
        
        Point2D minPoint = new Point2D.Double();
        Point2D maxPoint = new Point2D.Double();
        LocatePanel(minPoint, maxPoint);
        scaledCoord = new ArrayList<Point2D>();
        scaleToFitWindow(minPoint, maxPoint, width, height);
    }

    public void paint(Graphics g) {                      
        switch (type){
            case 0: 
            	drawPolygons(g); 
            	break;
            case 1: 
            	drawContourLine1(g); 
            	break;
            case 2:
            	fillColor(g);drawContourLine1(g);
            	break;
            case 3: 
            	drawContourLine2(g);
            	break;
            case 4: 
            	fillColor(g);drawContourLine2(g); 
            	break;
            default: 
            	System.out.println("Please input integer number between 0-4");      
        }
    }

    //Make the graphic fit to the window
    private void LocatePanel(Point2D minPoint, Point2D maxPoint) {
        minPoint.setLocation(Double.MAX_VALUE, Double.MAX_VALUE);
        maxPoint.setLocation(Double.MIN_VALUE, Double.MIN_VALUE);
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i = 0; i < vertex.size(); i++) {
            Point p = vertex.get(i);
            minX = Math.min(minX, p.Coor().getX());
            minY = Math.min(minY, p.Coor().getY());
            maxX = Math.max(maxX, p.Coor().getX());
            maxY = Math.max(maxY, p.Coor().getY());
        }
        minPoint.setLocation(minX, minY);
        maxPoint.setLocation(maxX, maxY);
    }
    private void scaleToFitWindow(Point2D minPoint, Point2D maxPoint, int width, int height) {
        double xdistance = maxPoint.getX() - minPoint.getX();
        double ydistance = maxPoint.getY() - minPoint.getY();
        for (int i = 0; i < vertex.size(); i++) {
            double x = vertex.get(i).Coor().getX();
            double y = vertex.get(i).Coor().getY();
            x = x - minPoint.getX();
            y = y - minPoint.getY();
            x = x * width * 0.7 / xdistance;
            y = -y * height *0.7 / ydistance + height;
            scaledCoord.add(new Point2D.Double(x+80, y-120));
        }
    }
 
    //Get all the triangle polygons' vertex and draw
     private void drawPolygons(Graphics g) {
        	g.setColor(Color.black);
        	for (int i = 0; i < cells.size(); i++) {
        		Polygon p = new Polygon();
        		p = getPolygon(i);
        		g.drawPolygon(p);
            }
     }
     private Polygon getPolygon(int i){
        Polygon p = new Polygon();
        int[] cell = cells.get(i);
        int[] x = new int[3];
        int[] y = new int[3];
        x[0] = (int) scaledCoord.get(cell[0]).getX();
        x[1] = (int) scaledCoord.get(cell[1]).getX();
        x[2] = (int) scaledCoord.get(cell[2]).getX();
        y[0] = (int) scaledCoord.get(cell[0]).getY();
        y[1] = (int) scaledCoord.get(cell[1]).getY();
        y[2] = (int) scaledCoord.get(cell[2]).getY();
       
        p.addPoint(x[0], y[0]);
        p.addPoint(x[1], y[1]);
        p.addPoint(x[2], y[2]);
        return p;
    }
   
    private void drawContourLine1(Graphics g) {
    	for (int i = 0; i < inputvalues.length; i++) {         //Draw the contour line of style 1
    		SetOnes(i);
    		drawContourLineStyle1(i, g);
        }
    }
    private void SetOnes(int i) {                              //Go through all the vertex to determine if it is an "one"
        for (int j = 0; j < vertex.size(); j++) {              //to find the boundary
        	vertex.get(j).Setone(true);
            if (vertex.get(j).Field() > inputvalues[i]) {
            	vertex.get(j).Setone(false);
            }
            if (vertex.get(j).Field() == inputvalues[i]) {
            	vertex.get(j).Setone(true);
            }
            if (inputvalues[i] == 1 && vertex.get(j).Field() >= 0.95) {
            	vertex.get(j).Setone(false);
            }
        }
    }
    private void drawContourLineStyle1(int indexOfColorMap, Graphics g) {
        boolean v1, v2, v3;
        g.setColor(Color.darkGray);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        for (int i = 0; i < cells.size(); i++) {
            int[] cell = cells.get(i);
            int[] x = new int[3];
            int[] y = new int[3];

            x[0] = (int) scaledCoord.get(cell[0]).getX();
            x[1] = (int) scaledCoord.get(cell[1]).getX();
            x[2] = (int) scaledCoord.get(cell[2]).getX();
            y[0] = (int) scaledCoord.get(cell[0]).getY();
            y[1] = (int) scaledCoord.get(cell[1]).getY();
            y[2] = (int) scaledCoord.get(cell[2]).getY();

            v1 = vertex.get(cell[0]).One();
            v2 = vertex.get(cell[1]).One();
            v3 = vertex.get(cell[2]).One();

            if (v1 != v2 && v1 != v3) {
                int xa = (x[0] + x[1]) / 2; int ya = (y[0] + y[1]) / 2;
                int xb = (x[2] + x[0]) / 2; int yb = (y[2] + y[0]) / 2;
                g.drawLine(xa, ya, xb, yb);
            }else if (v2 != v1 && v2 != v3) {
                int xa = (x[0] + x[1]) / 2; int ya = (y[0] + y[1]) / 2;
                int xb = (x[2] + x[1]) / 2; int yb = (y[2] + y[1]) / 2;
                g.drawLine(xa, ya, xb, yb);
            }else if (v3 != v1 && v3 != v2) {
                int xa = (x[0] + x[2]) / 2; int ya = (y[0] + y[2]) / 2;
                int xb = (x[2] + x[1]) / 2; int yb = (y[2] + y[1]) / 2;
                g.drawLine(xa, ya, xb, yb);
            }
        }
    } 
    
    //Draw the contour line of style 2
    private void drawContourLine2(Graphics g) {
    	for (int i = 0; i < inputvalues.length; i++) {
            drawContourLineStyle2(i, g);
        }
    }
    private void drawContourLineStyle2(int index, Graphics g) {
    	g.setColor(Color.darkGray);
    	Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        double[] test = new double[cells.size()];
        for (int i = 0; i < cells.size(); i++) {
    		int[] face = cells.get(i);
    		double s1, s2, s3, averageScalar;
    		s1 = vertex.get(face[0]).Field();
            s2 = vertex.get(face[1]).Field();
            s3 = vertex.get(face[2]).Field();
            averageScalar = (s1 + s2 + s3) /3;
            test[i] = averageScalar;                        
        }

        int[] m = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};     
        for (int i = 0; i < cells.size(); i++) {
            int[] face = cells.get(i);
            int[] x = new int[5];
            int[] y = new int[5];
            x[0] = (int)scaledCoord.get(face[0]).getX();y[0] = (int)scaledCoord.get(face[0]).getY();
            x[1] = (int)scaledCoord.get(face[1]).getX();y[1] = (int)scaledCoord.get(face[1]).getY();
            x[2] = (int)scaledCoord.get(face[2]).getX();y[2] = (int)scaledCoord.get(face[2]).getY();
            x[3] = (x[1] + x[2])/2;
            y[3] = (y[1] + y[2])/2;
            x[4] = (x[0] + x[3])/2;
            y[4] = (y[0] + y[3])/2;
            
            for(int j = 0; j < 12; j++) {
            	if(test[i] >= (m[j]*0.091) && test[i] <= (m[j+1]*0.091) && 
            			inputvalues[index] >= (m[j]*0.091) && inputvalues[index] <= (m[j+1]*0.091)) {
            		g.drawLine(x[0], y[0], x[1], y[1]);g.drawLine(x[1], y[1], x[2], y[2]);g.drawLine(x[2], y[2], x[0], y[0]);
            		g.drawLine(x[0], y[0], x[3], y[3]);g.drawLine(x[1], y[1], x[4], y[4]);g.drawLine(x[2], y[2], x[4], y[4]);
            	}
            }
        }
    }

    //fill the color into the polygons
    private void fillColor(Graphics g) {
        for (int i = 0; i < cells.size(); i++) {
            Polygon p = new Polygon();                 
            int[] face = cells.get(i);
            double averageScalar;                  
            Color averageColor;                       
            p = getPolygon(i);                      
            
            double v1, v2, v3;                  
            v1 = vertex.get(face[0]).Field();
            v2 = vertex.get(face[1]).Field();           
            v3 = vertex.get(face[2]).Field();
            averageScalar = (v1 + v2 + v3)/3;            
            averageColor = findAverageColor(averageScalar);
            g.setColor(averageColor);
            g.fillPolygon(p);
        }
    }
    private Color findAverageColor(double averageScalar){
        Color averageColor;
        int nearest=0;
        double nearestScalar = 2.0;  
        for (int i = 0; i < colormap.size(); i++) {
            double inputvalue = colormap.get(i).Inputvalue();
            if(Math.abs(averageScalar - inputvalue) < nearestScalar) {
                nearestScalar = Math.abs(averageScalar - inputvalue);
                nearest = i;
            }
        }    
        averageColor = colormap.get(nearest).LineColor();
        return averageColor;
    }
}