package geekbrains.jthree.practice01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Box<T extends Fruit> {

    private static final int MAX_VALUE = 20;

    private int capacity;
    private List<T> boxContent = new ArrayList<>();

    // Конструктор пустой коробки
    Box() {
        newBoxInit();
    }

    // Конструктор сразу наполняемой коробки
    Box(T... something) {
        newBoxInit();
        putSomething(something);
    }

    // Начальная настройка коробки
    private void newBoxInit() {
        capacity = MAX_VALUE;
    }

    // Пополнение коробки
    public void putSomething(T... something) {
        for (int i = 0; i < something.length; i++) {
            //if (capacity - (contentWeight + something[i].getWeight()) < 0.0f) break;
            if (boxContent.size() == capacity) break;
            else {
                Collections.addAll(boxContent,something[i]);
            }
        }
    }

    // Изъятие из коробки
    public T extractOne() {
        T exObj = boxContent.get(boxContent.size()-1);
        boxContent.remove(boxContent.size()-1);
        return exObj;
    }

    // Получить вес содержимого
    public float getBoxWeight() {
        float weight = 0.0f;
        for (int i = 0; i < boxContent.size(); i++) {
            weight += boxContent.get(i).getWeight();
        }
        return weight;
    }

    // Сравнение коробок по весу
    public boolean compare(Box<T> box) {
        return Math.abs(this.getBoxWeight() - box.getBoxWeight()) < 0.01;
    }

    // Перекладывание из коробки в коробку
    public void addFromAnotherBox(Box<T> box) {
        while (this.boxContent.size() < capacity && box.getValue() > 0) {
            this.putSomething(box.extractOne());
        }
    }

    // Получить количество объектов в коробке
    public int getValue() {
        return boxContent.size();
    }
}
