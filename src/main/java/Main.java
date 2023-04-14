import java.util.Scanner;
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
                if(!Basket.fileEmpty(xml.getFileLoad())){
                basket = basket.readJson(xml.getFileLoad().getName());
                }
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
                            basket.writeJson(basket, Integer.parseInt(text.split(" ", 0)[0]) - 1, Integer.parseInt(text.split(" ", 0)[1]), xml.getFileSave().getName());
                            basket = basket.readJson(xml.getFileLoad().getName());
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
}