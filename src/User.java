import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    String name;
    String password;
    KeyTimeGrid keyTimeGrid;
    static ArrayList<User> userlist = new ArrayList<>();
    User(String name, String password, KeyTimeGrid keyTimeGrid){
        this.keyTimeGrid = keyTimeGrid;
        this.name = name;
        this.password = password;
    }
    public static void addUser(User user){
        userlist.add(user);
    }

    public String getName() {
        return name;
    }

    public KeyTimeGrid getKeyTimeGrid() {
        return keyTimeGrid;
    }

    public String getPassword() {
        return password;
    }

    public static User getUser(String userName, String password) throws SQLException {

        for(int i=0; i<userlist.size(); i++){
            if(userlist.get(i).getName().equals(userName) && userlist.get(i).getPassword().equals(password)){
                return userlist.get(i);
            }
        }
        if(Main.sqlite.checkUser(userName)){
            String flyString = Main.sqlite.getFlyGrid(userName,password);
            String stayString = Main.sqlite.getStayGrid(userName,password);
            System.out.println(stayString.length());
            System.out.println(flyString.length());
            KeyTimeGrid temp = new KeyTimeGrid();
            temp.addFlyGrid(flyString);
            temp.addStayGrid(stayString);
            User tempUser = new User(userName,password,temp);
            User.addUser(tempUser);
            return tempUser;
        };

        return null;
    }
    public static boolean checkUser(String userName) throws SQLException {
        for(int i=0; i<userlist.size(); i++){
            if(userlist.get(i).getName().equals(userName)){
                return true;
            }
        }

        return false;
    }
}
