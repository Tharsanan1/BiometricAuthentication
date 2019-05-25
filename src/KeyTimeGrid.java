import javax.swing.*;
import java.awt.event.WindowEvent;

public class KeyTimeGrid {
    private int[][] flyTimeGrid;
    private int[][] stayTimeGrid;
    public KeyTimeGrid(){
        flyTimeGrid = new int[26][26];
        stayTimeGrid = new int[26][27];
    }
    public void addFlyGrid(String s){
        if(s.equals("not found")){
            JOptionPane.showMessageDialog(GUI.currentFrame,"USER DATA CORRUPTED TRY WITH ANOTHER USERNAME","warning",JOptionPane.WARNING_MESSAGE);
            GUI.currentFrame.dispatchEvent(new WindowEvent(GUI.currentFrame, WindowEvent.WINDOW_CLOSING));

        }
        String[] wordArray = s.split(" ");
        int count =0;
        for(int i = 0; i<26; i++){
            for(int j = 0; j<26; j++){

                flyTimeGrid[i][j] = Integer.parseInt(wordArray[count]);
                count++;
            }
        }
    }
    public void addStayGrid(String s){
        String[] wordArray = s.split(" ");
        int len =wordArray.length;
        int count =0;
        for(int i = 0; i<26; i++){
            for(int j = 0; j<27; j++){
                stayTimeGrid[i][j] = Integer.parseInt(wordArray[count]);
                count++;
            }
        }
    }
    public String getFlyString(){
        String s = "";
        int count = 0;
        for(int i = 0; i<26; i++){
            for(int j = 0; j<26; j++){
                s+=flyTimeGrid[i][j]+" ";
                count++;
            }
        }
        int i = count;
        return s;
    }
    public String getStayString(){
        String s = "";
        int count = 0;
        for(int i = 0; i<26; i++){
            for(int j = 0; j<27; j++){
                s+=stayTimeGrid[i][j]+" ";
                count++;
            }
        }

        int i = count;

        return s;
    }

    public int[][] getFlyTimeGrid() {
        return flyTimeGrid;
    }

    public int[][] getStayTimeGrid() {
        return stayTimeGrid;
    }
    public void displayFlyGrid(){
        System.out.println("******************************************************************");
        for(int i = 0; i<26; i++){
            for(int j = 0; j<26; j++){
                System.out.print(flyTimeGrid[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("*********************************************************************");
    }
    public void displayStayTimeGrid(){
        System.out.println("######################################################################");
        for(int i = 0; i<26; i++){
            for(int j = 0; j<27; j++){
                System.out.print(stayTimeGrid[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("#########################################################################");
    }
}
