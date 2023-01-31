import java.util.Scanner;

public class Login {
    private String name;
    private String password;
    Login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Skriv in namn: ");
        setName(scanner.nextLine().trim());
        System.out.println("Skriv in lösenord: ");
        setPassword(scanner.nextLine().trim());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
