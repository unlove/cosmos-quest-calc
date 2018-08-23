/*

 */
package AI;

import cosmosquestsolver.OtherThings;
import java.util.Iterator;
import java.util.LinkedList;

//iterator for all combinations (order doesn't matter) of a specific nunber (nCr).
//generates combinations as they are needed, not all at once.
//gives combinations involving the items at the front of the list first
// basic rersive idea: for nCr, return all combinations for (n-1)C(r-1), then add the last element in the list
public class CombinationIterator<T> implements Iterator<LinkedList<T>>{
        
        
    private int numElements;
    private int guarenteedIndex;
    private long numLeft;
    private LinkedList<T> list;
    private CombinationIterator iterator;
    
    public CombinationIterator(LinkedList<T> list, int numElements){//use list instead of linkedList? no, performance, numElements > list.size?
        
        this.numElements = numElements;
        guarenteedIndex = numElements - 1;
        numLeft = OtherThings.nCr(list.size(),numElements);
        this.list = list;
        if (list.size() > numElements && numElements > 0){
            iterator = new CombinationIterator(getFrontCopy(list,numElements-1),numElements - 1);
        }
    }
        
    @Override
    public boolean hasNext() {
        return numLeft > 0;
    }
    
    @Override
     public LinkedList<T> next() {
        if (numElements <= 0){
            numLeft --;
            return new LinkedList<>();
        }
        if (iterator == null){
            numLeft --;
            return list;
        }
        
        if (iterator.hasNext()){
            LinkedList<T> tempList = iterator.next();
            tempList.add(list.get(guarenteedIndex));
            numLeft --;
            return tempList;
        }
        else{//swap iterators
            guarenteedIndex ++;
            iterator = new CombinationIterator(getFrontCopy(list,guarenteedIndex),numElements - 1);//?
            LinkedList<T> tempList = iterator.next();
            tempList.add(list.get(guarenteedIndex));
            numLeft --;
            return tempList;
        }
    }
        
    private LinkedList<T> getFrontCopy(LinkedList<T> list, int numToCopy){
        int numCopied = 0;
        LinkedList<T> newList = new LinkedList<>();
        for (T i : list){
            if (numCopied >= numToCopy){
                break;
            }
            newList.add(i);
            numCopied ++;
            
        }
        return newList;
    }
        
}