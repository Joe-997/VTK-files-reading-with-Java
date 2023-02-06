import java.util.ArrayList;
import java.util.*;  
import java.io.File;  
import java.io.FileNotFoundException;
import java.util.Scanner;

public class csvDataProcess{
    private ArrayList<csvInterface> colormap;
    private String path;
    private double[] inputvalues;

    public csvDataProcess(String path, double[] inputvalues){
        this.path = path;
        colormap = new ArrayList <csvInterface> ();
        this.inputvalues = inputvalues;
    }
    public ArrayList<csvInterface> csvInterface(){
        return colormap;
    }
    public double[] inputvalues(){
        return inputvalues;
    }
    public void loadColor(){
        try{
            File Obj = new File(path);
            Scanner Reader = new Scanner(Obj);
            Reader.nextLine();
            while (Reader.hasNextLine()){
                 String data = Reader.nextLine();
                double inputvalue;
                double r;
                double g ;
                double b ;
                ArrayList<String> numbers = new ArrayList<>(Arrays.asList(data.split(",")));
                inputvalue = Double.parseDouble(numbers.get(0));
                r = Double.parseDouble(numbers.get(1));
                g = Double.parseDouble(numbers.get(2));
                b = Double.parseDouble(numbers.get(3));
            
                //都是0-1的值，转换0-255
                int red, blue, green;
                red = (int)(r*255 );
                blue = (int)(b*255 );
                green = (int)(g*255 );
                
                colormap.add(new csvInterface(inputvalue,red,green,blue));    //把csv文件的四个值传入colormap
            }
            Reader.close();
        } catch (FileNotFoundException e){
            System.out.println(path + ": Not found");
            e.printStackTrace();
        }        
    }

}