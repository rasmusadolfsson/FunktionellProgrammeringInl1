import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoeLoader {
    public List<Shoe> shoeList = new ArrayList<>();
    ShoeLoader(Connection connection) {
        try (
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery("select * from sko");
            while (resultSet.next()) {
            Shoe shoe = new Shoe();
            shoe.setId(resultSet.getInt("id"));
            shoe.setBrand(resultSet.getString("märke"));
            shoe.setModelName(resultSet.getString("modellnamn"));
            shoe.setPrice(resultSet.getInt("pris"));
            shoe.setAmount(resultSet.getInt("antal"));
            shoe.setColour(getColourFromId(resultSet.getInt("färg")));
            shoe.setSize(getSizeFromId(resultSet.getInt("storlek")));
            shoeList.add(shoe);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    private String getColourFromId(int id){
        return switch (id) {
            case 1 -> "Vit";
            case 2 -> "Svart";
            case 3 -> "Röd";
            case 4 -> "Grå";
            default -> null;
        };
    }
    private int getSizeFromId(int id){
        return switch (id){
            case 1 -> 37;
            case 2 -> 39;
            case 3 -> 41;
            case 4 -> 43;
            case 5 -> 45;
            default -> -1;
        };
    }
}
