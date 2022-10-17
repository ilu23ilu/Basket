import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, NullPointerException {
        Scanner scanner = new Scanner(System.in);
        int[] basketCount = new int[3];
        String[] products = {"Хлеб", "Крупа", "Молоко"};
        int[] prices = {30, 70, 50};
        ClientLog clientLog = new ClientLog();
        File file = new File("basket.json");
        Basket basket = null;
        if (file.exists()) {
            basket = Basket.loadFromJson(file);
        } else {
            basket = new Basket(products, prices, basketCount);
        }
        System.out.println("Список возможных товаров для покупки: ");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт.");
        }
        int sumProducts = 0;
        int productNum = 0;
        int count = 0;

        while (true) {
            System.out.println("Выберите товар и количество или введите 'end'");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");
            productNum = Integer.parseInt(parts[0]) - 1;
            count = Integer.parseInt(parts[1]);
            int sum = count * prices[productNum];
            sumProducts += sum;
            basket.addToCart(productNum, count);
            clientLog.log(productNum, count);


        }
        basket.saveJson(file);
        basket.printCart();
        clientLog.exportAsCSV(file);
        System.out.println("Итого: " + sumProducts + " " + "рублей.");
        System.out.println("Программа завершена! ");
    }
}