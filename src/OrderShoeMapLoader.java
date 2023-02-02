import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderShoeMapLoader {
    private List<OrderShoeMap> orderShoeMapList = new ArrayList<>();
    private List<Order> orderList;
    private List<Shoe> shoeList;

    OrderShoeMapLoader(Connection connection, List<Shoe> shoeList, List<Order> orderList) {
        try (
                Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery("select * from Map1");
            this.orderList = orderList;
            this.shoeList = shoeList;
            while(resultSet.next()){
                OrderShoeMap orderShoeMap = new OrderShoeMap();
                orderShoeMap.setId(resultSet.getInt("id"));
                orderShoeMap.setOrder(getOrderFromId(resultSet.getInt("beställnings_id")));
                orderShoeMap.setShoe(getShoeFromId(resultSet.getInt("sko_id")));
                orderShoeMapList.add(orderShoeMap);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderShoeMap> getOrderShoeMapList(){
        return orderShoeMapList;
    }
    private Shoe getShoeFromId(int id){
        return shoeList.stream().filter(s -> s.getId() == id).findFirst().get();
    }
    private Order getOrderFromId(int id){
        return orderList.stream().filter(s -> s.getId() == id).findFirst().get();
    }
}
