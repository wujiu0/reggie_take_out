package org.example.reggie.test;

import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        OrderedStream os= new OrderedStream(5);
        os.insert(3, "ccccc"); // 插入 (3, "ccccc")，返回 []
        os.insert(1, "aaaaa"); // 插入 (1, "aaaaa")，返回 ["aaaaa"]
        os.insert(2, "bbbbb"); // 插入 (2, "bbbbb")，返回 ["bbbbb", "ccccc"]
        os.insert(5, "eeeee"); // 插入 (5, "eeeee")，返回 []
        os.insert(4, "ddddd"); // 插入 (4, "ddddd")，返回 ["ddddd", "eeeee"]

    }
}


class OrderedStream {
    String[] values;
    int ptr;

    public OrderedStream(int n) {
        this.values = new String[n + 1];
        this.ptr = 1;
    }

    public List<String> insert(int idKey, String value) {
        List<String> list = new ArrayList<>();
        values[idKey] = value;
        if(values[ptr] != null){
            for(int i = ptr; i < values.length; i++){
                if(values[i] != null){
                    list.add(values[i]);
                } else{
                    ptr = i + 1;
                    break;
                }
            }
        }
        return list;
    }

}
