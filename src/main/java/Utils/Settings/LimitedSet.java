package Utils.Settings;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

public class LimitedSet implements Serializable, Iterable<String> {
    private String[] list;

    public LimitedSet(int size) {
        this.list = new String[size];
    }
    public String getLast(){
        return list[0];
    }
    public void add(String item){
        int id = findId(item);

        if(id>=0){
            moveToId(id);
        }else{
            moveToId(list.length);
        }
        list[0] = item;

    }
    private int findId(String item){
        for(int i=0;i<list.length;i++){
            if(list[i]!=null && list[i].equals(item)) return i;
        }
        return -1;
    }
    private void moveToId(int id){
        for(int i=id;i>0;i--){
            String elem = list[i-1];
            if(i<list.length){
                list[i] = elem;
            }
        }
    }

    @Override
    public String toString() {
        return "LimitedSet{" +
                Arrays.toString(list) +
                '}';
    }

    public static void main(String[] args) {
        LimitedSet limitedSet = new LimitedSet(5);

        limitedSet.add("test1");
        limitedSet.add("test2");
        limitedSet.add("test1");
        limitedSet.add("test3");
        limitedSet.add("test3");
        limitedSet.add("test5");
        limitedSet.add("test5");
        limitedSet.add("test2");
        limitedSet.add("test1");
        limitedSet.add("test6");
        limitedSet.add("test3");
        limitedSet.add("test7");

        System.out.println(limitedSet);

    }

    @Override
    public Iterator<String> iterator() {
        return new LimitSetIterator();
    }
    class LimitSetIterator implements Iterator<String> {

        private int index = 0;

        public boolean hasNext() {
            return index < list.length;
        }

        public String next() {
            return list[index++];
        }
    }
}
