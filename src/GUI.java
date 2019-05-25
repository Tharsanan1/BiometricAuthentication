import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class GUI  implements KeyListener,ActionListener {
    JFrame loginFrame;
    JLabel userNameLabel;
    JTextField userNameTextField;
    JLabel passwordLabel;
    JLabel wordDisplaylabel;
    JLabel wordToTypeLabel;
    JLabel typeHereLabel;
    JPasswordField passwordTextField;
    JTextField phraseTestTextField;
    static JFrame currentFrame;
    JButton signInButton;
    JButton signUpButton;
    JButton refreshButton;
    ArrayList<Integer> flyTimeList;
    ArrayList<Integer> stayPressedTimeList;
    ArrayList<Integer> stayReleasedTimeList;
    String[] wordArray = "keystroke dynamics can be used as a biometrics property".split(" ");
    KeyTimeGrid tempUser;
    int nextWordIndex = 0;
    SignUpGUI signUpFrame;
    boolean convertableFlag = false;
    boolean dropFlag = false;
    boolean signInFlag;
    boolean finalFlag;
    GUI signInFrame;
     public GUI(){
         finalFlag = false;
         signInFrame = this;
         tempUser = new KeyTimeGrid();
         flyTimeList = new ArrayList<>();
         stayPressedTimeList = new ArrayList<>();
         stayReleasedTimeList = new ArrayList<>();
         loginFrame = new JFrame("SIGN IN PAGE");
         currentFrame = this.loginFrame;
         userNameLabel = new JLabel("UserName");
         userNameTextField = new JTextField();
         passwordLabel = new JLabel("Password");
         passwordTextField = new JPasswordField();
         signInButton = new JButton("SignIn");
         signUpButton = new JButton("SignUp");
         refreshButton = new JButton("Refresh Window");
         wordDisplaylabel = new JLabel("");
         wordDisplaylabel.setBackground(Color.white);
         wordDisplaylabel.setOpaque(true);
         wordToTypeLabel = new JLabel("Word To Type :");
         typeHereLabel = new JLabel("Type Here :");
         phraseTestTextField = new JTextField();
         userNameLabel.setBounds(10,10,100,20);
         userNameTextField.setBounds(130,10,100,20);
         passwordLabel.setBounds(10,50,100,20);
         passwordTextField.setBounds(130,50,100,20);
         signInButton.setBounds(130,80,100,20);
         signUpButton.setBounds(10,270,220,20);
         wordToTypeLabel.setBounds(10,140,100,20);
         wordDisplaylabel.setBounds(130,140,100,20);
         typeHereLabel.setBounds(10,170,100,20);
         phraseTestTextField.setBounds(130,170,100,20);
         refreshButton.setBounds(10,240,220,20);
         signUpButton.addActionListener(this);
         signInButton.addActionListener(this);
         refreshButton.addActionListener(this);
         loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         loginFrame.setLayout(null);
         loginFrame.add(signInButton);
         loginFrame.add(signUpButton);
         loginFrame.add(refreshButton);
         loginFrame.add(passwordLabel);
         loginFrame.add(passwordTextField);
         loginFrame.add(userNameLabel);
         loginFrame.add(userNameTextField);
         loginFrame.add(wordDisplaylabel);
         loginFrame.add(phraseTestTextField);
         loginFrame.add(wordToTypeLabel);
         loginFrame.add(typeHereLabel);
         loginFrame.setSize(500,500);
         loginFrame.setVisible(true);
         displayWord();
         signInFlag = false;

         phraseTestTextField.addKeyListener(new KeyListener() {
             @Override
             public void keyTyped(KeyEvent e) {

             }

             @Override
             public void keyPressed(KeyEvent e) {
                 if(e.getKeyCode()>=65 && e.getKeyCode()<=90){
                     stayPressedTimeList.add((int)System.currentTimeMillis());
                 }
                 else{
                     stayPressedTimeList.clear();
                     stayReleasedTimeList.clear();
                     dropFlag = true;
                 }
             }

             @Override
             public void keyReleased(KeyEvent e) {
                 System.out.println("staypressed time list size: "+stayPressedTimeList.size());
                 if(dropFlag){
                     dropFlag = false;
                     return;
                 }
                 if(e.getKeyCode()>=65 && e.getKeyCode()<=90){
                     stayReleasedTimeList.add((int)System.currentTimeMillis());
                 }
                 else{
                     stayPressedTimeList.clear();
                     stayReleasedTimeList.clear();
                     return;
                 }

                 if(convertableFlag){
                     int length = wordArray[nextWordIndex-2].length();
                     System.out.println("lenght : "+length);
                     for(int i = 0; i<length; i++){
                         if(i!=length-1){
                             System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$added value : "+(stayReleasedTimeList.get(stayPressedTimeList.size()-length+i)-stayPressedTimeList.get(stayPressedTimeList.size()-length+i)));
                             tempUser.getStayTimeGrid()[wordArray[nextWordIndex-2].charAt(i)-97][wordArray[nextWordIndex-2].charAt(i+1)-97]=stayReleasedTimeList.get(stayPressedTimeList.size()-length+i)-stayPressedTimeList.get(stayPressedTimeList.size()-length+i);

                         }
                         else{
                             System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$added value : "+(stayReleasedTimeList.get(stayPressedTimeList.size()-length+i)-stayPressedTimeList.get(stayPressedTimeList.size()-length+i)));
                             tempUser.getStayTimeGrid()[wordArray[nextWordIndex-2].charAt(i)-97][0] = stayReleasedTimeList.get(stayPressedTimeList.size()-length+i)-stayPressedTimeList.get(stayPressedTimeList.size()-length+i);
                         }
                     }
                     convertableFlag = false;
                     System.out.print("list sizes: "+stayPressedTimeList.size()+"  "+stayReleasedTimeList.size());
                     for(int k = 0; k<stayPressedTimeList.size(); k++){
                         System.out.print(stayPressedTimeList.get(k)+"  ");
                         System.out.print(stayReleasedTimeList.get(k));
                         System.out.println();
                     }
                     stayPressedTimeList.clear();
                     stayReleasedTimeList.clear();
                     tempUser.displayStayTimeGrid();
                     if(finalFlag){
                         wordDisplaylabel.setText("");
                         phraseTestTextField.setVisible(false);
                         nextWordIndex = 0;
                         signInFlag = true;
                     }
                 }
             }
         });
         phraseTestTextField.getDocument().addDocumentListener(new DocumentListener() {
             @Override
             public void insertUpdate(DocumentEvent e) {
                 flyTimeList.add((int)System.currentTimeMillis());
                 int textfieldLenght = phraseTestTextField.getText().length();
                 if(wordDisplaylabel.getText().substring((textfieldLenght-1),(textfieldLenght)).equals(phraseTestTextField.getText().substring((textfieldLenght-1),(textfieldLenght)))){
                     if(wordDisplaylabel.getText().length() == textfieldLenght){
                         if(textfieldLenght>1){
                             for(int i = 0; i<textfieldLenght-1; i++){
                                 tempUser.getFlyTimeGrid()[wordDisplaylabel.getText().charAt(i+1)-97][wordDisplaylabel.getText().charAt(i)-97] = flyTimeList.get(i+1)-flyTimeList.get(i);
                             }
                             tempUser.displayFlyGrid();
                         }
                         convertableFlag = true;
                         displayWord();
                         setTextEmpty();
                         flyTimeList.clear();
                         System.out.println("My Name is :- " + Thread.currentThread().getName());
                     }
                 }
                 else{
                     setTextEmpty();
                     flyTimeList.clear();
                     dropFlag = true;
                     stayReleasedTimeList.clear();
                     stayPressedTimeList.clear();
                 }
             }

             @Override
             public void removeUpdate(DocumentEvent e) {
                 if(dropFlag){
                     setTextEmpty();
                     flyTimeList.clear();
                     dropFlag = true;
                     stayReleasedTimeList.clear();
                     stayPressedTimeList.clear();
                 }
             }

             @Override
             public void changedUpdate(DocumentEvent e) {

             }
         });
     }

    private void displayWord(){
        if(wordArray.length-1>=nextWordIndex) {
            wordDisplaylabel.setText(wordArray[nextWordIndex]);
            nextWordIndex++;
        }
        else{
            finalFlag = true;
            nextWordIndex++;
        }

    }
    private void setTextEmpty() {

        Runnable doHighlight = new Runnable() {
            @Override
            public void run() {
                // your highlight code
                phraseTestTextField.setText("");
            }
        };
        SwingUtilities.invokeLater(doHighlight);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refreshButton){
            new GUI();
            loginFrame.setVisible(false);
        }
        if(e.getSource() == signUpButton){
            signUpFrame = new SignUpGUI();
        }
        if(e.getSource() == signInButton) {
            try {
                if(User.getUser(userNameTextField.getText(),passwordTextField.getText())!=null){
                    if(signInFlag) {
                        User user = User.getUser(userNameTextField.getText(), passwordTextField.getText());
                        int flyTimeKeys = 0;
                        int stayTimeKeys = 0;
                        int flyTimeDeviation = 0;
                        int stayTimeDeviation = 0;
                        for (int i = 0; i < 10; i++) {
                            System.out.println("@");
                        }
                        tempUser.displayStayTimeGrid();
                        tempUser.displayFlyGrid();

                        System.out.println("()()()()()()()()()()()()())()()()()()()()()()()()()()())()()()()()()()()()()()()()()()()()()()())()()())()()()()()()()()()");
                        user.getKeyTimeGrid().displayStayTimeGrid();
                        user.getKeyTimeGrid().displayFlyGrid();
                        for (int i = 0; i < 10; i++) {
                            System.out.println("@");
                        }
                        System.out.println("flydeviationflydeviationflydeviationflydeviationflydeviationflydeviationflydeviationflydeviationflydeviation");
                        for (int i = 0; i < 26; i++) {
                            for (int j = 0; j < 26; j++) {
                                if (tempUser.getFlyTimeGrid()[i][j] - user.getKeyTimeGrid().getFlyTimeGrid()[i][j] != 0) {
                                    flyTimeKeys++;
                                }
                                flyTimeDeviation += Math.sqrt((tempUser.getFlyTimeGrid()[i][j] - user.getKeyTimeGrid().getFlyTimeGrid()[i][j]) * (tempUser.getFlyTimeGrid()[i][j] - user.getKeyTimeGrid().getFlyTimeGrid()[i][j]));
                                System.out.print(tempUser.getFlyTimeGrid()[i][j] - user.getKeyTimeGrid().getFlyTimeGrid()[i][j]);
                                System.out.print(" ");
                            }
                            System.out.println();
                        }
                        System.out.println("flydeviationflydeviationflydeviationflydeviationflydeviationflydeviationflydeviationflydeviationflydeviationflydeviation");
                        System.out.println("staydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviation");
                        for (int i = 0; i < 26; i++) {
                            for (int j = 0; j < 27; j++) {
                                if (tempUser.getStayTimeGrid()[i][j] - user.getKeyTimeGrid().getStayTimeGrid()[i][j] != 0) {
                                    stayTimeKeys++;
                                }
                                stayTimeDeviation += Math.sqrt((tempUser.getStayTimeGrid()[i][j] - user.getKeyTimeGrid().getStayTimeGrid()[i][j]) * (tempUser.getStayTimeGrid()[i][j] - user.getKeyTimeGrid().getStayTimeGrid()[i][j]));
                                System.out.print(tempUser.getStayTimeGrid()[i][j] - user.getKeyTimeGrid().getStayTimeGrid()[i][j]);
                                System.out.print(" ");
                            }
                            System.out.println();
                        }
                        System.out.println("staydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviationstaydeviation");
                        double avgFlyTimeDeviation = 0;
                        double avgStayTimeDeviation = 0;
                        if (flyTimeKeys != 0) {
                            avgFlyTimeDeviation = (float) flyTimeDeviation / flyTimeKeys;
                        }
                        if (stayTimeKeys != 0) {
                            avgStayTimeDeviation = (float) stayTimeDeviation / stayTimeKeys;
                        }
                        System.out.println("avgFlytimeDeviation: " + avgFlyTimeDeviation + " avgStayTimeDeviation: " + avgStayTimeDeviation);
                        if(avgFlyTimeDeviation<40 && avgStayTimeDeviation<20){
                            JOptionPane.showMessageDialog(this.loginFrame,"YOU ARE LOGGING IN","warning",JOptionPane.WARNING_MESSAGE);
                            this.loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
                        }
                        else{
                            JOptionPane.showMessageDialog(this.loginFrame,"TRY KEYSTROKE AGAIN","warning",JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(this.loginFrame,"pass keystroke typing","warning",JOptionPane.WARNING_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this.loginFrame,"user not exist","warning",JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
