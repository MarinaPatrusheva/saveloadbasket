import java.io.*;

public class Basket {
    private int[] price;
    private int[] amountList;
    private String[] name;
    private File file;

    public Basket(int[] price, String[] name) {
        this.name = name;
        this.price = price;
        amountList = new int[name.length];
    }

    public void addToCart(int productNum, int amount) {
        amountList[productNum] += amount;
        saveTxt(file);
    }

    public void printCart() {
        if (file.exists()) {
            System.out.println("Ваша корзина: ");
            int count = 0;
            for (int i = 0; i < name.length; i++) {
                if (amountList[i] != 0) {
                    System.out.println(name[i] + "- " + amountList[i] + " штук " + amountList[i] * price[i] + " цена " + "(" + price[i] + " - стоимость ед.)");
                    count += amountList[i] * price[i];
                }
            }
            System.out.println(count);
        } else {
            System.out.println("Ваша корзина пуста!");
        }
    }

    private void saveTxt(File textFile) {
        if (textFile.exists()) {
            writeTxt(textFile);
        } else {
            try {
                textFile.createNewFile();
                writeTxt(textFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        try {
            FileReader fr = new FileReader(textFile);
            BufferedReader readerTxt = new BufferedReader(fr);
            StringBuilder ss = new StringBuilder(readerTxt.readLine());
            int count = (int) ss.chars()
                    .filter(cf -> cf == '@')
                    .count();
            int[] price = new int[count];
            String[] name = new String[count];
            int[] amountList = new int[count];
            for (int i = 0; i < count; i++) {
                StringBuilder row = new StringBuilder(ss.substring(ss.indexOf(Integer.toString(i) + "$"), ss.indexOf(Integer.toString(i + 1) + "$")));
                price[i] = Integer.parseInt(new StringBuilder(row.substring(row.indexOf("%"), row.indexOf("="))).deleteCharAt(0).toString());
                amountList[i] = Integer.parseInt(new StringBuilder(row.substring(row.indexOf("-"), row.indexOf("%"))).deleteCharAt(0).toString());
                name[i] = (new StringBuilder(row.substring(row.indexOf("@"), row.indexOf("-"))).deleteCharAt(0)).toString();
            }
            Basket basket = new Basket(price, name);
            basket.setAmountList(amountList);
            return basket;
        } catch (IOException ex) {
            System.out.println("Не удалось прочитать файл");
        }
        return null;
    }

    private void writeTxt(File textFile) {
        try {
            FileWriter writer = new FileWriter(textFile, false);
            for (int i = 0; i < name.length; i++) {
                writer.write(Integer.toString(i) + "$");
                writer.write("@");
                writer.write(name[i]);
                writer.write("-");
                writer.write(Integer.toString(amountList[i]));
                writer.write("%");
                writer.write(Integer.toString(price[i]));
                writer.write("=");
                writer.write(Integer.toString(i + 1) + "$");
            }
            writer.flush();
        } catch (IOException io) {
            System.out.println("Не удалось записать данные");
        }
    }

    protected void setAmountList(int[] amountList) {
        this.amountList = amountList;
    }
    public int[] getPrice(){
        return price;
    }
    public String[] getName(){
        return name;
    }
    public int[] getAmountList(){
        return amountList;
    }

    private static boolean fileEmpty(File textFile) {
        return textFile.length() == 0;
    }
    public void setFile(File file){
        this.file = file;
    }
}