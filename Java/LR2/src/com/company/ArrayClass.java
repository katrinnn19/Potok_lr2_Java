package com.company;
import java.util.Random;
public class ArrayClass {
    private final int number_of_cells; //к-ть ел-в у масиві
    private final int threadNum; //к-ть потоків

    public final int[] arr; //масив елементів


    public ArrayClass(int number_of_cells, int threadNum) { //конструктор
        this.number_of_cells = number_of_cells;
        arr = new int[number_of_cells];
        this.threadNum = threadNum;
        for(int i = 0; i < number_of_cells; i++){ //заповнюємо масив послідовними значеннями
            arr[i] = i;
        }
        Random random = new Random(); //випадковим чином змінюємо деякі значення на протилежні
        arr[random.nextInt(number_of_cells)]*=-1;
    }

    public long OneThreadMin(int startIndex, int finishIndex){ //Знаходження мінімального в заданих межах
        long min =Long.MAX_VALUE;//найбільше можливе значення
        for(int i = startIndex; i < finishIndex; i++){
            if(min>arr[i]){
                min=arr[i];
            }
        }
        return min;
    }

    private long min = 0;

    synchronized private long getMin() {//Чекаємо поки виведуться всі потоки(після чого виведем min)
        while (getThreadCount()<threadNum){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return min;
    }

    synchronized public void collectMin(long min){//Знаходимо min, потік передає зн-ня мін через даний метод
        if(this.min>min){
            this.min = min;
        }
    }

    private int threadCount = 0;//Кількість завершених потоків
    synchronized public void incThreadCount(){
        threadCount++;
        notify();//завершуємо while в getmin()
    }

    private int getThreadCount() { //метод для отр-ня к-ті завершених потоків
        return threadCount;
    }

    public long threadMin(){//метод для пошуку мін зн-я у масиві з вик-ям потоків
        ThreadMin[] threadMins = new ThreadMin[threadNum];//Створюємо масив для потоків
        int len = number_of_cells /threadNum; //Знаходимо крок між межами масиву
        for (int i=0;i<threadNum-1;i++) {
            threadMins[i] = new ThreadMin(len*i, len*(i+1), this); //Створюємо потік в масиві
            threadMins[i].start();  //Запускаємо поток
        }
        threadMins[threadNum-1]= new ThreadMin(len*(threadNum-1), number_of_cells, this);   //Ств-є останній поток в масиві, щоб останні елементи не втрачались
        threadMins[threadNum-1].start(); //Запускаємо поток
        return getMin();
    }
}