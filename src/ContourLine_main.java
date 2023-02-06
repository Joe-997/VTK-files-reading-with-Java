import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class ContourLine_main {

  public static void main(String args[]) {
    int width = 600;
    int height = 600;            
    JFrame window = new JFrame();                    
    window.addWindowListener(new WindowAdapter() {           //Set the size of the window 
        public void windowClosing(WindowEvent e) {           //and the basic operations of the window
          System.exit(0);
        }
    }); 
    
    String fileDir = System.getProperty("user.dir");        
    String dir = fileDir;
    if (args.length != 1) {
      System.out.println("Please enter the corresponding number to select the data you prefer.");
      System.out.println("  1-riderr.vtk\n  2-square.vtk\n  3-L.vtk");
      Scanner dtype = new Scanner(System.in);
      int data = dtype.nextInt();
      if(data == 1) {
    	  dir = fileDir + "/riderr.vtk";                     //Let user select a input VTK file
      }else if(data == 2) {
    	  dir = fileDir + "/square.vtk";
      }else if(data == 3) {
    	  dir = fileDir + "/L.vtk";
      }else {
    	  System.err.println("Please enter an integer between [1,3]!");
    	  System.exit(0);
      }
      System.out.println("Please select a drawing mode:\n  0-Polygons\n  1-Contour Line(style 1)\n  "
      		+ "2-Contour Line with filled color(style 1)\n  3-Contour Line(style 2)\n  4-Contour Line with filled color(style 2)");
      //Let user select a preferred result
    //dtype.close();
    }
    
    double[] inputvalues = new double[0];
    Scanner sc = new Scanner(System.in);
    int type = sc.nextInt();
    if(type == 1 || type == 2 || type == 3 || type == 4){
        System.out.println("Please enter how many contour lines you need:");    //Let user enter the number of lines
        int lines = sc.nextInt();                                               //and the input values
        System.out.println("Please enter "+lines+" values between [0,1]:");
        inputvalues = new double[lines];
      
        for(int i=0; i<lines; i++){
          inputvalues[i] = sc.nextDouble();
        }
      }else if(type == 0){
      }else {
    	  System.err.println("Please enter the right value!");
    	  System.exit(0);
      }
    sc.close();
    
  //Load data from VTK file and CSV file.
    vtkDataProcess vtk = new vtkDataProcess(dir);
    vtk.LoadReader();                                                                                    
    csvDataProcess colormap = new csvDataProcess(fileDir + "/CoolWarmFloat257.csv" , inputvalues);                    
    colormap.loadColor();
    
    
  //Draw the result
    Draw drawresult = new Draw(width, height, vtk.Points(), vtk.Faces(), colormap.csvInterface(), type, colormap.inputvalues());
    window.setTitle("Contour Line");
    window.add(drawresult, BorderLayout.CENTER);
    window.pack();
    window.setSize(width, height);
    window.setVisible(true);
    
    
  }
}