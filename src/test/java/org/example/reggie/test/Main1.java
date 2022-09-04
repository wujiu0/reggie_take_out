package org.example.reggie.test;

public class Main1 {
    public static void main(String[] args) {
        MyCircularDeque circularDeque = new MyCircularDeque(5); // 设置容量大小为3
        circularDeque.insertFront(7);                    // 返回 true
        circularDeque.insertLast(0);                    // 返回 true
        circularDeque.getFront();                    // 返回 true
        circularDeque.insertLast(3);                    // 已经满了，返回 false
        circularDeque.getFront();                // 返回 2
        circularDeque.insertFront(9);                        // 返回 true
        circularDeque.getRear();                    // 返回 true
        circularDeque.getFront();                    // 返回 true
        circularDeque.getFront();                // 返回 4
        circularDeque.deleteLast();
        circularDeque.getRear();
    }


}

class MyCircularDeque {
    int head;
    int tail;
    int[] elem;

    public MyCircularDeque(int k) {
        elem = new int[k + 1];
    }

    public boolean insertFront(int value) {
        if (!this.isFull()) {
            elem[head] = value;
            head = (head - 1 + elem.length) % elem.length;
            return true;
        }
        return false;
    }

    public boolean insertLast(int value) {
        if (!this.isFull()) {
            elem[tail] = value;
            tail = (tail + 1) % elem.length;
            return true;
        }
        return false;
    }

    public boolean deleteFront() {
        if (!this.isEmpty()) {
            head = (head + 1) % elem.length;
            return true;
        }
        return false;
    }

    public boolean deleteLast() {
        if (!this.isEmpty()) {
            tail = (tail - 1 + elem.length) % elem.length;
            return true;
        }
        return false;
    }

    public int getFront() {
        if (!this.isEmpty()) {
            return this.elem[(head + 1) % elem.length];
        }
        return -1;
    }

    public int getRear() {
        if (!this.isEmpty()) {
            return this.elem[(tail - 1 + elem.length) % elem.length];
        }
        return -1;
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public boolean isFull() {
        return (tail + 1) % elem.length == head;
    }
}



