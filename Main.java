package geekbrains.jthree.practice02;

public class Main {

    public final Object monitor = new Object();
    public volatile char letter = 'A';

    public static void main (String[] args) {
        Main test = new Main();
        Thread aThread = new Thread(() -> {
            test.printA();
        } );
        Thread bThread = new Thread(() -> {
            test.printB();
        });
        Thread cThread = new Thread(() -> {
            test.printC();
        });
        aThread.start();
        bThread.start();
        cThread.start();
    }

    public void printA() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letter != 'A') monitor.wait();
                    System.out.print("A");
                    letter = 'B';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letter != 'B') monitor.wait();
                    System.out.print("B");
                    letter = 'C';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letter != 'C') monitor.wait();
                    System.out.print("C");
                    letter = 'A';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
