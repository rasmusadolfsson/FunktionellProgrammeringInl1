public class OrderShoeMap {
    private int id;
    private Order order;
    private Shoe shoe;

    public OrderShoeMap(){

    }
    public OrderShoeMap(int id, Order order, Shoe shoe) {
        this.id = id;
        this.order = order;
        this.shoe = shoe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
    }
}

