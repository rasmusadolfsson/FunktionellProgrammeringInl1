import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerLoader {
    private final List<Customer> customerList = new ArrayList<>();

    CustomerLoader(Connection connection) {
        try (
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery("select * from kund");
            while(resultSet.next()){
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("namn"));
                customer.setAddress(resultSet.getString("adress"));
                customer.setCity(resultSet.getString("ort"));
                customer.setPassword(resultSet.getString("lösenord"));
                customerList.add(customer);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public List<Customer> getCustomerList(){
        return customerList;
    }
}
