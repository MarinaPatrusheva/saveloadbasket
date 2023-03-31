import java.io.*;

public class Basket implements Serializable{
    private static final long serialVersionUID = 1;
    private int[] price;
    private int[] amountList;
    private String[] name;
    private File file = new File("basket.bin");

    public Basket(int[] price, String[] name) {
        this.name = name;
        this.price = price;
        amountList = new int[name.length];
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addToCart(int productNum, int amount) {
        if(!fileEmpty(file)) {
            for (int i = 0; i < name.length; i++) {
                amountList[i] = loadFromBinFile(file).amountList[i];
            }
        }
        amountList[productNum] += amount;
        saveBin(file);
    }

    public void printCart() {
        if (file.exists()) {
            System.out.println("Ваша корзина: ");
            int count = 0;
            for (int i = 0; i < this.name.length; i++) {
                if (this.amountList[i] != 0) {
                    System.out.println(this.name[i] + "- " + this.amountList[i] + " штук " + (this.amountList[i] * this.price[i]) + " цена " + "(" + this.price[i] + " - стоимость ед.)");
                    count += this.amountList[i] * this.price[i];
                }
            }
            System.out.println(count);
        } else {
            System.out.println("Ваша корзина пуста!");
        }
    }
    public void saveBin(File file){
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Basket loadFromBinFile(File file){
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Basket basket = (Basket) objectInputStream.readObject();
            objectInputStream.close();
            return basket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean fileEmpty(File file){
        return file.length() == 0;
    }
    protected void setAmountList(int[] amountList){
        this.amountList = amountList;
    }
}