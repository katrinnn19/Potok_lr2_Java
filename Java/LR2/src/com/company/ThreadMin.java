package com.company;

public class ThreadMin extends Thread {
    private final int startIndex;//початкой індекс для обробки потоком
    private final int finishIndex;//кінцевий індекс для обробки потоком
    private final ArrayClass arrClass;

    public ThreadMin(int startIndex, int finishIndex, ArrayClass arrClass) {
        this.startIndex = startIndex;
        this.finishIndex = finishIndex;
        this.arrClass = arrClass;
    }

    @Override
    public void run() {
        long min = arrClass.OneThreadMin(startIndex, finishIndex);//Знаходимо мінімальний елемент в межах start i finish
        arrClass.collectMin(min); // Передаємо мінімальне значення для запису
        arrClass.incThreadCount();//Відмічуємо, що поток завершено
    }
}