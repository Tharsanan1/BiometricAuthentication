import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class SignUpGUI implements ActionListener,KeyListener {
    JFrame signUpFrame;
    JLabel userNameLabel;
    JTextField userNameTextField;
    JLabel passwordLabel;
    JLabel wordToTypeLabel;
    JLabel typeHereLabel;
    JPasswordField passwordTextField;
    JLabel trainKeystrokeLabel;
    JTextField trainKeystrokeTextField;
    JButton signUpButton;
    String[] wordArray = "keystroke dynamics can be used as a biometrics property".split(" ");//keystroke dynamics can be used as a biometrics property
    int nextWordIndex = 0;
    ArrayList<Integer> flyTimeList;
    ArrayList<Integer> stayPressedTimeList;
    ArrayList<Integer> stayReleasedTimeList;
    KeyTimeGrid tempUser;
    boolean convertableFlag;
    boolean dropFlag;
    boolean signUpFlag;
    boolean finalFlag;
    public SignUpGUI(){
        finalFlag = false;
        tempUser = new KeyTimeGrid();
        signUpFrame = new JFrame("SIGN UP PAGE");
        userNameLabel = new JLabel("UserName");
        userNameTextField = new JTextField();
        passwordLabel = new JLabel("Password");
        passwordTextField = new JPasswordField();
        signUpButton = new JButton("SignUp");

        wordToTypeLabel = new JLabel("Word To Type :");
        typeHereLabel = new JLabel("Type Here :");
        signUpButton.addActionListener(this);
        trainKeystrokeLabel = new JLabel("");
        trainKeystrokeTextField = new JTextField();
        userNameLabel.setBounds(10,10,100,20);
        userNameTextField.setBounds(130,10,100,20);
        passwordLabel.setBounds(10,40,100,20);
        passwordTextField.setBounds(130,40,100,20);
        signUpButton.setBounds(130,70,100,20);

        wordToTypeLabel.setBounds(10,110,100,20);
        typeHereLabel.setBounds(10,140,100,20);
        trainKeystrokeLabel.setBounds(130,110,100,20);
        trainKeystrokeLabel.setBackground(Color.white);
        trainKeystrokeLabel.setOpaque(true);
        trainKeystrokeTextField.setBounds(130,140,100,20);
        flyTimeList = new ArrayList<>();
        stayPressedTimeList = new ArrayList<>();
        stayReleasedTimeList = new ArrayList<>();
        convertableFlag = false;
        dropFlag = false;
        signUpFlag = false;
        trainKeystrokeTextField.addKeyListener(new KeyListener() {
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
                if(e.getKeyCode()>=65 || e.getKeyCode()<=90){
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
                        trainKeystrokeTextField.setVisible(false);
                        nextWordIndex = 0;
                        trainKeystrokeLabel.setText("");
                        signUpFlag = true;
                    }
                }
            }
        });
        trainKeystrokeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(dropFlag){
                    setTextEmpty();
                    flyTimeList.clear();
                    stayReleasedTimeList.clear();
                    stayPressedTimeList.clear();
                    return;
                }
                flyTimeList.add((int)System.currentTimeMillis());
                int textfieldLenght = trainKeystrokeTextField.getText().length();
                if(trainKeystrokeLabel.getText().substring((textfieldLenght-1),(textfieldLenght)).equals(trainKeystrokeTextField.getText().substring((textfieldLenght-1),(textfieldLenght)))){
                    if(trainKeystrokeLabel.getText().length() == textfieldLenght){
                        if(textfieldLenght>1){
                            for(int i = 0; i<textfieldLenght-1; i++){
                                tempUser.getFlyTimeGrid()[trainKeystrokeLabel.getText().charAt(i+1)-97][trainKeystrokeLabel.getText().charAt(i)-97] = flyTimeList.get(i+1)-flyTimeList.get(i);
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
                    stayReleasedTimeList.clear();
                    stayPressedTimeList.clear();
                    return;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        //signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setLayout(null);
        signUpFrame.add(signUpButton);
        signUpFrame.add(passwordLabel);
        signUpFrame.add(passwordTextField);
        signUpFrame.add(userNameLabel);
        signUpFrame.add(userNameTextField);
        signUpFrame.add(trainKeystrokeLabel);
        signUpFrame.add(trainKeystrokeTextField);
        signUpFrame.add(wordToTypeLabel);
        signUpFrame.add(typeHereLabel);
        signUpFrame.setSize(500,500);
        signUpFrame.setVisible(true);
        displayWord();

    }
    public KeyTimeGrid getTempUser() {
        return tempUser;
    }

    private void displayWord(){
        if(wordArray.length-1>=nextWordIndex) {
            trainKeystrokeLabel.setText(wordArray[nextWordIndex]);
            nextWordIndex++;
        }
        else{
            nextWordIndex++;
            finalFlag = true;
        }

    }
    private void setTextEmpty() {

        Runnable doHighlight = new Runnable() {
            @Override
            public void run() {
                // your highlight code
                trainKeystrokeTextField.setText("");
            }
        };
        SwingUtilities.invokeLater(doHighlight);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == signUpButton){
            if(userNameTextField.getText().length()>5){
                if(passwordTextField.getText().length()>5){
                    if(signUpFlag){
                        try {
                            if(!User.checkUser(userNameTextField.getText())){
                                User.addUser(new User(userNameTextField.getText(),passwordTextField.getText(),tempUser));
                                Main.sqlite.insertUser(userNameTextField.getText(),passwordTextField.getText(),tempUser.getFlyString(),tempUser.getStayString());
                                signUpFrame.setVisible(false);
                            }
                            else{
                                JOptionPane.showMessageDialog(this.signUpFrame,"user already exist","warning",JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(this.signUpFrame,"finish keystroke training","warning",JOptionPane.WARNING_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this.signUpFrame,"password not valied","warning",JOptionPane.WARNING_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(this.signUpFrame,"Username not valied","warning",JOptionPane.WARNING_MESSAGE);
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
