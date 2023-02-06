import java.util.*;  
import java.io.File;  
import java.io.FileNotFoundException;
import java.util.Scanner;

public class vtkDataProcess{
    private ArrayList <Point> points;
    private ArrayList <int[]> cells;
    private int numberOfPoints;
    private int numberOfFaces;
    private String path;
    private boolean startLoadingPoint;
    private boolean startLoadingFace;
    private boolean startScalarField;

    public vtkDataProcess(String path){
        this.path = path;
        numberOfFaces = 0;
        numberOfPoints = 0;
        startLoadingFace = false;
        startLoadingPoint = false;
        startScalarField = false;
        points = new ArrayList <Point>();
        cells = new ArrayList <int[]> ();
    }
    public ArrayList <Point> Points(){
        return points;
    }
    public int NumberOfPoints(){
        return numberOfPoints;
    }
    public int NumberOfFaces(){
        return numberOfFaces;
    }
    public ArrayList<int[]> Faces(){
        return cells;
    }
 
    public void LoadReader(){
        try{
            File Obj = new File(path);
            Scanner Reader = new Scanner(Obj);
            while (Reader.hasNextLine()){                                      //每一循环读入vtk文件的一行内容
                String data = Reader.nextLine();
                LoadPoints(data, Reader);                                     //用该方法读取点数据
                LoadFaces(data, Reader);
                LoadScalarField(data, Reader);
            }
            Reader.close();
        } catch (FileNotFoundException e){
            System.out.println(path + ": Not found");
            e.printStackTrace();
        }
    }
 
    private void LoadPoints(String data,  Scanner Reader){                     //关键字截取处理points部分
        startLoadingPoint = data.indexOf("POINTS ") !=-1? true: false;
        if(startLoadingPoint){ 
            String [] words = data.split(" ", 3);
            for (String word : words){                                           //搜寻关键字后这一行如果有数字就读入
                if(isNumeric(word)) numberOfPoints = Integer.valueOf(word);
            }
            
            double x, y, z;
            for(int i = 0; i <numberOfPoints; i++){                              //读入的整数代表points个数，做循环读入每一点坐标
                while(Reader.hasNextDouble()){
                    x = Reader.hasNextDouble()? Reader.nextDouble(): 0;
                    y = Reader.hasNextDouble()? Reader.nextDouble(): 0;
                    z = Reader.hasNextDouble()? Reader.nextDouble(): 0;
                    points.add(new Point(x,y));                                     //每一点的数据存入double数组points
                }
            }
        }
        
    }

    private void LoadFaces(String data, Scanner Reader){                            //处理cells部分
        startLoadingFace = data.indexOf("CELLS ") !=-1? true: false;                //3代表三边形，三个值连续着代表相对边长
        if(startLoadingFace){
            
            if(Reader.hasNextInt()) numberOfFaces = Reader.nextInt();
            int a, b, c;
            for(int i = 0; i < numberOfFaces; i++){
                while(Reader.hasNextInt()){
                    
                    a = Reader.hasNextInt()? Reader.nextInt(): 0;
                    b = Reader.hasNextInt()? Reader.nextInt(): 0;
                    c = Reader.hasNextInt()? Reader.nextInt(): 0;
                    int[] face = {a, b, c};
                    cells.add(face);                                               //存入整形数组cells
                    if(Reader.hasNextInt())Reader.nextInt();                          
                }
            }
        }
        
    }
  
    private void LoadScalarField(String data, Scanner Reader){                       //处理vtk最后一部分标量场，与points对应
        double maxd = Double.MIN_VALUE;
        startScalarField = data.indexOf("LOOKUP_TABLE ") !=-1? true: false;
        if(startScalarField){
            int count = 0;
            for(count=0; count < numberOfPoints;count++){
               if(Reader.hasNextDouble()) points.get(count).setValue(Reader.nextDouble());
               if(maxd < points.get(count).Field()) maxd =  points.get(count).Field();  //循环所有点，把最大的scalar值赋给maxd
            }
            int maxin = (int)Math.round(maxd);
            //normalize scalar to range 0-1
            for(int i=0; i < numberOfPoints;i++){                                   //把标量场每一点设为[0,1]内
                double value =  points.get(i).Field();  
                //System.out.println(maxd);
                points.get(i).setValue(value/maxd);
             }
        }
        //System.out.println(points);
    }
 
    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }


}