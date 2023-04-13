import java.io.*;
import java.util.Scanner;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;

class Main {
    public static void main(String[] args) {
        ParserXML xml = new ParserXML();
        Scanner scanner = new Scanner(System.in);
        String[] name = {"Гречка", "Яица", "Молоко"};
        int[] price = {100, 70, 80};
        Basket basket = new Basket(price, name);
        ClientLog clientLog = new ClientLog(xml.getFileLog());
        if(xml.getLoadTextOrJson()){
            if(xml.getFileLoad().exists()) {
                basket = Basket.loadFromTxtFile(xml.getFileLoad());
                basket.setFile(xml.getFileLoad());
            }
        }else {
            if(xml.getFileLoad().exists()){
                basket = readJson(xml.getFileLoad().getName());
            }
        }
        System.out.println("Список возможных товаров для покупки :");
        for (int i = 0; i < name.length; i++) {
            System.out.println(i + 1 + "." + name[i] + " " + price[i]);
        }
        while (true) {
            int count = 0;
            System.out.println("Выберите товар и количество или введите `end`");
            String text = scanner.nextLine();
            for (int i = 0; i < text.length(); i++) {
                Character h = text.charAt(i);
                if (h.equals(' ')) {
                    count += 1;
                }
            }
            try {
                if (((count == 1) || (count == 0)) && (text.equals("end") || (Integer.parseInt(text.split(" ", 0)[0]) <= name.length) && (Integer.parseInt(text.split(" ", 0)[0]) > 0))) {
                    if (text.equals("end")) {
                        if(xml.getLoadTextOrJson()) {
                            basket.printCart();
                        }else{
                            System.out.println("Ваша корзина: ");
                            int count1 = 0;
                            for (int i = 0; i < name.length; i++) {
                                if (basket.getAmountList()[i] != 0) {
                                    System.out.println(name[i] + "- " + basket.getAmountList()[i] + " штук " + basket.getAmountList()[i] * price[i] + " цена " + "(" + price[i] + " - стоимость ед.)");
                                    count1 += basket.getAmountList()[i] * price[i];
                                }
                            }
                        }
                        if(xml.writeLog()){
                            clientLog.exportAsCSV(xml.getFileLog());
                        }
                        break;
                    } else {
                        if(!xml.getSaveTextOrJson()){
                            writeJson(basket, Integer.parseInt(text.split(" ", 0)[0]) - 1, Integer.parseInt(text.split(" ", 0)[1]));
                            basket = readJson(xml.getFileLoad().getName());
                        }else{
                            basket.addToCart(Integer.parseInt(text.split(" ", 0)[0]) - 1, Integer.parseInt(text.split(" ", 0)[1]));
                        }
                        if(xml.getSaveLog()){
                            clientLog.log(Integer.parseInt(text.split(" ", 0)[0]) - 1, Integer.parseInt(text.split(" ", 0)[1]));
                        }
                    }
                } else {
                    System.out.println("Введены некорректные данные ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введены некорректные данные");
                continue;
            }
        }
    }

    public static void writeJson(Basket basket, int productNum, int amount) {
        JSONObject obj = new JSONObject();
        File file = new File("basket.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JSONArray arrayAmount = new JSONArray();
        JSONArray arrayPrice = new JSONArray();
        JSONArray arrayName = new JSONArray();
        for (int i = 0; i < basket.getName().length; i++){
            arrayName.add(i, basket.getName()[i]);
            arrayPrice.add(i, basket.getPrice()[i]);
            if(productNum == i){
             arrayAmount.add(i, basket.getAmountList()[i] + amount);
            }else {
                arrayAmount.add(i, basket.getAmountList()[i]);
            }
        }
        obj.put("amount", arrayAmount);
        obj.put("price", arrayPrice);
        obj.put("name", arrayName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            obj.writeJSONString(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket readJson(String fileName) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(fileName));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray arrayAmount = (JSONArray) jsonObject.get("amount");
            JSONArray arrayPrice = (JSONArray) jsonObject.get("price");
            JSONArray arrayName = (JSONArray) jsonObject.get("name");
            int[] price = new int[arrayName.size()];
            int[] amount = new int[arrayName.size()];
            String[] name = new String[arrayName.size()];
            for(int i = 0; i < arrayName.size(); i++){
                price[i] = (int) (long)arrayPrice.get(i);
                amount[i] = (int) (long)arrayAmount.get(i);
                name[i] = (String) arrayName.get(i);
            }
            Basket basket = new Basket(price, name);
            basket.setAmountList(amount);
            return basket;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}