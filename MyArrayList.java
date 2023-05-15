public class MyArrayList<E>{
    private Object[] list;
    private int arraySize;
    private int capacity;
    public MyArrayList(){
        capacity = 10;
        arraySize = 0;
        list = new Object[capacity];
    }
    public void add(E obj){
        if (arraySize < capacity-1)
        {
            list[arraySize] = obj;
            arraySize++;
        }
 
        else
        {
            capacity*=2;
            Object[] temp = new Object[list.length];//make a temp that holds old list
            temp = list;
            list = new Object[capacity];//make a new list with the new capcity
            for (int i = 0; i < temp.length; i++)
            {
                list[i] = temp[i];
            }
            list[arraySize] = obj;
            arraySize++;
        }
    }
    public void add(int index,E obj){
    	if (arraySize < capacity-2){
			Object[] temp = new Object[capacity];
			int count = 0;
			for (int i=0;i<temp.length;i++){
				if (i != index){
					temp[i]=list[count];
					count++;
				}else if (i==index){
					temp[i]=obj;
				}
			}
			arraySize++;
			list=temp;
    	}
    	else {
    		capacity*=2;
    		Object[] temp = new Object[capacity];
    		int count = 0;
			for (int i=0;i<temp.length;i++){
				if (i != index && count<list.length){
					temp[i]=list[count];
					count++;
				}else if (i==index){
					temp[i]=obj;
				}
			}
			arraySize++;
			list = new Object[capacity];
			list = temp;
    		
    	}
    	
    	
    }
    
    public E get(int index){
        return (E) list[index];
    }
    public void remove(int index){
       
        Object[] temp = new Object[arraySize];
        temp = list;
        list = new Object[capacity];
        int count = 0;
        for (int i=0;i<temp.length;i++){
            if (i!=index){
                list[count]=temp[i];
                count++;
            }
        }
        arraySize--;

       
    }
    public E remove(E inp)
    {
        int pos;
        pos=-1;
        Object[] temp = new Object[arraySize];
        for (int i = 0; i < arraySize; i++)
        {
            if (list[i].equals(inp))
            {
                pos = i;
                for (int j = 0; j < arraySize; j++)
                {
                    if (j != pos)
                    {
                        temp[j] = list[j];
                    }
                }
            }
        }
        arraySize--;
        list = temp;
        return (E)list[pos];
    }
 
    public void set(int pos, E inp)
    {
        for (int i = 0; i < arraySize; i++)
        {
            if (i == pos)
            {
                list[i] = inp;
            }
        }
    }
 
    public String toString()
    {
        String s = "";
        int count = 1;
        for (int i = 0; i < arraySize; i++)
        {
            s+= count+""+list[i]+"\n";
            count++;
        }
        return s;
    }
 
    public int size()
    {
        return arraySize;
    }  
    public void scramble(){
    	int rand;
    	for (int i=0;i<arraySize;i++){
    		rand = (int)(Math.random()*arraySize);
    		Object temp = list[rand];
    		list[rand] = list[i];
    		list[i] = temp;
    	}
    }
    
    
}
