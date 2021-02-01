package ru.geekbrains.jthree.practice05;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    private static Semaphore entrance;

    public Tunnel(int cars) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        entrance = new Semaphore(cars);
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                entrance.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                entrance.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
