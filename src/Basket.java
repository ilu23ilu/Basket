import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Basket {
    private String[] products;
    private int[] prices;
    private int[] basketCount;

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

    public void saveTxt(File textFile) throws FileNotFoundException {
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
            return new Basket(products, prices, basketCount);
        }
    }
    public void saveJson(File jsonFile) throws IOException {
        try (FileWriter writer = new FileWriter(jsonFile, true)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            writer.write(gson.toJson(this));
        }
    }

    public static Basket loadFromJson(File jsonFile) throws IOException {
        if (jsonFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(jsonFile));) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                return gson.fromJson(br.readLine(), Basket.class);
            }
        }
        return null;
    }
}
