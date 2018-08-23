/*

 */
package AI;

import cosmosquestsolver.OtherThings;
import java.util.Iterator;
import java.util.LinkedList;


//iterator for all permutations of a list (order matters)
//generates permutaions as needed, not all at once
public class PermutationIterator<T> implements Iterator<LinkedList<T>>{//wrapper class to convert all to linked list?
    
    private long numLeft;
    private LinkedList<T> objectsLeft;
    private T currentObject;
    private PermutationIterator iterator;
    
    public PermutationIterator(LinkedList<T> list){
          
        numLeft = (long) OtherThings.fact(list.size());
        objectsLeft = getListCopy(list);
        currentObject = objectsLeft.pop();
        
        if (!objectsLeft.isEmpty()){
            iterator = new PermutationIterator(getListCopy(objectsLeft));
        }
    }

    @Override
    public boolean hasNext() {
        return numLeft != 0;
    }
    
    @Override
    public LinkedList<T> next() {
        if (iterator == null){
            LinkedList<T> singleList = new LinkedList<>();
            singleList.add(currentObject);
            numLeft --;
            return singleList;
        }
        
        if (iterator.hasNext()){
            LinkedList<T> list = iterator.next();
            list.add(0,currentObject);
            numLeft --;
            return list;
        }
        else{//swap out currentObject and return next
            T temp = objectsLeft.pop();
            objectsLeft.add(currentObject);
            currentObject = temp;
            
            iterator = new PermutationIterator(objectsLeft);
            
            LinkedList<T> list = iterator.next();
            list.add(0,currentObject);
            numLeft --;
            return list;
        }
    }
    
    //returns shallow copy of list
    private LinkedList<T> getListCopy(LinkedList<T> list){
        LinkedList<T> newList = new LinkedList<>();
        for (T i : list){
            newList.add(i);
        }
        return newList;
    }
        
    
        
}
