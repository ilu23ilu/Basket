import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Basket implements Serializable {
    String[] products;
    int[] prices;
    int[] basketCount;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.basketCount = new int[3];
    }

    public Basket(String[] products, int[] prices, int[] basketCount) {
        this.products = products;
        this.prices = prices;
        this.basketCount = basketCount;
    }

    public void addToCart(int productNum, int amount) {
        basketCount[productNum] += amount;
    }

    public void printCart() {
        System.out.println("Ваша корзина :");
        for (int i = 0; i < basketCount.length; i++) {
            if (basketCount[i] != 0) {
                System.out.println(products[i] + " " + basketCount[i] + "шт." + " " + prices[i] + "руб/шт " + " " + basketCount[i] * prices[i] + " " + "в сумме. ");
            }
        }
    }

    void saveTxt(File textFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (String product : products) {
                out.print(product + " ");

            }
            out.println();
            for (int price : prices) {
                out.print(price + " ");
            }
            out.println();
            for (int count : basketCount) {
                out.print(count + " ");
            }
            out.println();
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        try (Scanner scanner = new Scanner(new FileInputStream(textFile))) {
            String[] products = scanner.nextLine().split(" ");
            int[] prices = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            int[] basketCount = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            return new Basket(products, prices);
        }
    }

    public void saveBin(File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Basket loadFromBinFile(File file) {
        Basket basket;
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            basket = (Basket) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }
}
