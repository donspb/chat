package geekbrains.jthree.practice01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Перестановка элементов массива.
        System.out.println("Перестановка элементов:");
        Integer[] ints = new Integer[5];
        for (int i = 0; i < 5; i++) ints[i] = i;
        try {
            changer(ints, 2, 4);
            for (int i = 0; i < 5; i++) System.out.println(ints[i]);
        } catch (Exception e) {
            System.out.println("Elements changing error:");
            e.printStackTrace();
        }

        // 2. Массив в ArrayList
        System.out.println("Массив в лист:");
        String[] strArray = {"1", "2", "3", "4", "5"};
        ArrayList<String> aList = arrayToString(strArray);
        for (String string : aList ) {
            System.out.println(string);
        }

        testBoxes();

    }

    // 1. Перестановка элементов массива.
    public static void changer(Object[] arr,int a,int b) {
        Object tempE = arr[a];
        arr[a] = arr[b];
        arr[b] = tempE;
    }

    // 2. Массив в ArrayList.
    public static <T> ArrayList<T> arrayToString(T[] array) {
        ArrayList<T> backList = new ArrayList<>();
        Collections.addAll(backList, array);
        return backList;
    }

    // Тестирование работы с коробками
    public static void testBoxes() {

        System.out.println("--------------------\nBox test section\n--------------------");
        Box<Apple> AppleBox1 = new Box<>();
        Box<Apple> AppleBox2 = new Box<>();
        Box<Apple> AppleBox3 = new Box<>(new Apple(), new Apple(), new Apple(), new Apple());

        AppleBox1.putSomething(new Apple(), new Apple(), new Apple(), new Apple());
        Apple[] apples = new Apple[21];
        for (int i = 0; i < apples.length ; i++) apples[i] = new Apple();
        AppleBox2.putSomething(apples);

        System.out.println("First Box: " + AppleBox1.getValue());
        System.out.println("Second Box: " + AppleBox2.getValue());
        System.out.println("Third Box: " + AppleBox3.getValue());

        System.out.println("Are first and second equal? " + AppleBox1.compare(AppleBox2));
        System.out.println("Are first and third equal? " + AppleBox1.compare(AppleBox3));

        AppleBox1.addFromAnotherBox(AppleBox2);
        AppleBox2.addFromAnotherBox(AppleBox3);
        System.out.println("First Box: " + AppleBox1.getValue());
        System.out.println("Second Box: " + AppleBox2.getValue());
        System.out.println("Third Box: " + AppleBox3.getValue());
        
    }

}
