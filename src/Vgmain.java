import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Vgmain {
    private List<Shoe> shoeList;
    private List<Customer> customerList;
    private List<Order> orderList;
    private List<OrderShoeMap> orderShoeMapList;

    Vgmain(){

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
        ){
            ShoeLoader shoeLoader = new ShoeLoader(connection);
            shoeList = shoeLoader.getShoeList();
            CustomerLoader customerLoader = new CustomerLoader(connection);
            customerList = customerLoader.getCustomerList();
            OrderLoader orderLoader = new OrderLoader(connection, customerList);
            orderList = orderLoader.getOrderList();
            OrderShoeMapLoader orderShoeMapLoader = new OrderShoeMapLoader(connection, shoeList, orderList);
            orderShoeMapList = orderShoeMapLoader.getOrderShoeMapList();


        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Välj rapport att visa:\n" +
                "1 - Rapport 1\n2 - Rapport 2\n3 - Rapport 3\n4 - Rapport 4\n5 - Rapport 5");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> RapOne(scanner);
            case 2 -> RapTwo();
            case 5 -> RapFive();
            default -> System.out.println(choice + " är inte ett giltigt alternativ");
        }
    }
    private final ShoeInterface sizeSearch = (s, w) -> s.getSize() == Integer.parseInt(w);
    private final ShoeInterface colourSearch = (s, w) -> s.getColour().equalsIgnoreCase(w);
    private final ShoeInterface brandSearch = (s, w) -> s.getBrand().equalsIgnoreCase(w);
    public void searchBy(String searchWord, ShoeInterface shoeInterface){
        orderShoeMapList.stream().filter(s -> shoeInterface.search(s.getShoe(), searchWord))
                .forEach(a -> System.out.println(a.getOrder().getCustomer().getName()+ " - "+a.getOrder().getCustomer().getAddress()));
    }

    public void RapOne(Scanner scanner){
        System.out.println("Vill du söka på:\n1 - Storlek\n2 - Färg\n3 - Märke");
        int choice = scanner.nextInt();
        int searchInt;
        String searchWord;
        switch (choice) {
            case 1 -> {
                System.out.println("Skriv storlek att söka efter: ");
                searchInt = scanner.nextInt();
                searchBy(String.valueOf(searchInt), sizeSearch);
            }
            case 2 -> {
                System.out.println("Skriv färg att söka efter: ");
                searchWord = scanner.next().trim();
                searchBy(searchWord, colourSearch);
            }
            case 3 -> {
                System.out.println("Skriv märke att söka efter: ");
                searchWord = scanner.next().trim();
                searchBy(searchWord, brandSearch);
            }
            default -> System.out.println("Något gick fel!");
        }
    }

    public void RapTwo(){
        Map<Customer, Long> map = orderList.stream().collect(Collectors.groupingBy(c -> c.getCustomer(), Collectors.counting()));
        map.forEach((c, i) -> System.out.println(c.getName() + " Antal ordrar: " + i));
    }

    public void RapFive(){
        Map<String, Long> map = orderShoeMapList.stream().collect(Collectors.groupingBy(s -> s.getShoe().getModelName(), Collectors.counting()));
        map.forEach((s, l) -> System.out.println(s + " Sålts: "+l+" gånger"));

    }

    public static void main(String[] args) {
        Vgmain vgmain = new Vgmain();
    }
}
