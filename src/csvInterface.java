import java.awt.Color;

public class csvInterface {
    private double inputvalue;
    private Color color;
    public csvInterface(double inputvalue, int r, int b, int g){
        this.inputvalue = inputvalue;
        this.color = new Color( r,g,b,255);  //a不透明
    }
    public double Inputvalue(){
        return inputvalue;
    }
    public Color LineColor(){
        return color;
    }
    
}
