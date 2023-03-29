import java.io.File;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        File file = new File("Basket.txt");
        Scanner scanner = new Scanner(System.in);
        String[] name = {"Гречка", "Яица", "Молоко"};
        int[] price = {100, 70, 80};
        Basket basket = new Basket(price, name);
        if(file.exists()) {
            basket = Basket.loadFromTxtFile(file);
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
                        basket.printCart();
                        break;
                    } else {
                        basket.addToCart(Integer.parseInt(text.split(" ", 0)[0]) - 1, Integer.parseInt(text.split(" ", 0)[1]));
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
}