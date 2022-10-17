import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, org.xml.sax.SAXException, NullPointerException {
        Scanner scanner = new Scanner(System.in);
        int[] basketCount = new int[3];
        String[] products = {"Хлеб", "Крупа", "Молоко"};
        int[] prices = {30, 70, 50};
        ClientLog clientLog = new ClientLog();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new File("shop.xml"));
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }

        boolean configLoadEnabled;
        String configLoadFileName;
        String configLoadFormat;
        boolean configSaveEnabled;
        String configSaveFileName;
        String configSaveFormat;
        boolean configLogEnabled;
        String configLogFileName;

        try {
            XPathExpression xp = XPathFactory.newInstance().newXPath().compile("//config/load/enabled");
            configLoadEnabled = Boolean.parseBoolean(xp.evaluate(doc));
            xp = XPathFactory.newInstance().newXPath().compile("//config/load/fileName");
            configLoadFileName = xp.evaluate(doc);
            xp = XPathFactory.newInstance().newXPath().compile("//config/load/format");
            configLoadFormat = xp.evaluate(doc);

            xp = XPathFactory.newInstance().newXPath().compile("//config/save/enabled");
            configSaveEnabled = Boolean.parseBoolean(xp.evaluate(doc));
            xp = XPathFactory.newInstance().newXPath().compile("//config/save/fileName");
            configSaveFileName = xp.evaluate(doc);
            xp = XPathFactory.newInstance().newXPath().compile("//config/save/format");
            configSaveFormat = xp.evaluate(doc);

            xp = XPathFactory.newInstance().newXPath().compile("//config/log/enabled");
            configLogEnabled = Boolean.parseBoolean(xp.evaluate(doc));
            xp = XPathFactory.newInstance().newXPath().compile("//config/log/fileName");
            configLogFileName = xp.evaluate(doc);

        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        Basket basket = null;
        File file = new File(configLoadFileName);
        if (configLoadEnabled && file.exists()) {
            if (configLoadFormat.equals("json")) {
                basket = Basket.loadFromJson(file);
            }
            else {
                basket = Basket.loadFromTxtFile(file);
            }
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
            clientLog.log(productNum, count);
            basket.addToCart(productNum, count);
        }
        if (configSaveEnabled) {
            if (configSaveFormat.equals(".json")) {
                basket.saveJson(new File(configSaveFileName));
            } else if (configSaveFormat.equals(".csv")) {
                basket.saveTxt(new File(configSaveFileName));
            }
        }
        basket.saveJson(file);
        basket.printCart();
        if (configLogEnabled) {
            File logFile = new File(configLogFileName);
            clientLog.exportAsCSV(logFile);
        }
        System.out.println("Итого: " + sumProducts + " " + "рублей.");
        System.out.println("Программа завершена! ");
    }
}