import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class test {
    test(){
        Scanner scanner = new Scanner(System.in);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/properties.properties"));
        } catch (IOException e) {
            System.out.println("Något gick fel vid hämtning av properties");
            e.printStackTrace();
        }
        try(Connection connection = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password"));

            CallableStatement callableStatement = connection.prepareCall("call AddToCart(?,?,?)")
        ){
            CustomerLoader customerLoader = new CustomerLoader(connection);
            List<Customer> customerList = customerLoader.getCustomerList();
            int customerId = -1;
            boolean loggedIn = false;
            while(!loggedIn){
                Login login = new Login();
                List<Customer> customer = customerList.stream().filter(c -> c.getName().equalsIgnoreCase(login.getName()) && c.getPassword().equals(login.getPassword())).toList();
                if(customer.size() == 1){
                    customerId = customer.get(0).getId();
                    loggedIn = true;
                    customer.forEach(c -> System.out.println("Inloggad som: " + c.getName()));
                }else System.out.println("Fel namn eller lösenord!");
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        test test = new test();
    }
}
