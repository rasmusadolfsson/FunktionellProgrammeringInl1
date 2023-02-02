import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class main {
    main() {
        Scanner scanner = new Scanner(System.in);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/properties.properties"));
        } catch (IOException e) {
            System.out.println("Något gick fel vid hämtning av properties");
            e.printStackTrace();
        }
        boolean loggedIn = false;
        while (!loggedIn) {
            Login login = new Login();
            try (Connection connection = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password"));

                 PreparedStatement statement = connection.prepareStatement("select * from kund where namn = ? and lösenord = ?");
                 CallableStatement callableStatement = connection.prepareCall("call AddToCart(?,?,?)")
            ) {
                statement.setString(1, login.getName());
                statement.setString(2, login.getPassword());

                System.out.println(login.getName());
                System.out.println(login.getPassword());

                ResultSet resultSet = statement.executeQuery();
                int customerId = -1;
                while (resultSet.next()) {
                    customerId = resultSet.getInt("id");

                    System.out.println(customerId);
                }
                if(customerId == -1){
                    System.out.println("Felaktigt namn eller lösenord!");
                    break;
                }
                ShoeLoader shoeLoader = new ShoeLoader(connection);

                shoeLoader.shoeList.stream().filter(a -> a.getAmount() > 0).forEach(s -> System.out.println(s.getId() + " - " + s.getBrand() + " - " + s.getModelName() + " - " + s.getPrice() + " - " +
                        s.getAmount() + " - " + s.getColour() + " - " + s.getSize()));

                int shoeChoice;
                System.out.println("Skriv in nummer för att välja sko: ");
                shoeChoice = scanner.nextInt();
                int orderChoice;
                System.out.println("Vill du lägga till i förra beställningen eller göra en ny? ");
                System.out.println("1 - Förra\n2 - Ny");
                orderChoice = scanner.nextInt();

                if (orderChoice == 1) {
                    int orderId = -1;
                    try (PreparedStatement statement1 = connection.prepareStatement("select * from beställning where kund_id = ?")) {
                        statement1.setInt(1, customerId);
                        ResultSet resultSet2 = statement1.executeQuery();
                        while(resultSet2.next()){
                            if(resultSet2.isLast()){
                                orderId = resultSet2.getInt("id");
                            }
                        }
                        callableStatement.setInt(1, customerId);
                        callableStatement.setInt(2, orderId);
                        callableStatement.setInt(3, shoeChoice);
                        callableStatement.executeQuery();
                        System.out.println("Sko tillagd i beställning!");

                    } catch (SQLException e) {
                        System.out.println("Kunde inte lägga in sko i beställning!");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        callableStatement.setInt(1, customerId);
                        callableStatement.setString(2, null);
                        callableStatement.setInt(3, shoeChoice);
                        callableStatement.executeQuery();
                        System.out.println("Sko tillagd i beställning!");
                    } catch (SQLException e) {
                        System.out.println("Kunde inte lägga in sko i beställning!");
                        e.printStackTrace();
                    }
                }


                loggedIn = true;
            } catch (SQLException e) {
                System.out.println("Felaktig namn eller lösenord!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        main main = new main();
    }
}
