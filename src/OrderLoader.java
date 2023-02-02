import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderLoader {
    private List<Order> orderList = new ArrayList<>();
    private List<Customer> customerList;
    OrderLoader(Connection connection, List<Customer> customerList) {
        try (
                Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery("select * from beställning");
            this.customerList = customerList;
            while(resultSet.next()){
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomer(getCustomerFromId(resultSet.getInt("kund_id")));
                order.setDate(resultSet.getString("datum"));
                orderList.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> getOrderList(){
        return orderList;
    }
    private Customer getCustomerFromId(int id){
        return customerList.stream().filter(a -> a.getId() == id).findFirst().get();

    }
}
