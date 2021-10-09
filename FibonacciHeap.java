import java.util.ArrayList;
import java.util.Collections;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
	public static int totalLinks =0; 
	static int totalCuts =0;
	protected HeapNode min;
	protected int size;
	protected HeapNode first;
	int number_of_trees;
	int number_of_marks; 
	
	public FibonacciHeap() 
	{
		this.min = null;
		this.size=0;
		this.first = null;
		this.number_of_trees=0;
		this.number_of_marks=0;
	}
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
	public boolean isEmpty()
    {
    	if(size==0) {
    		return true;
    	}
    	return false;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * 
    * Returns the new node created. 
    */
    public HeapNode insert(int key)
    {    
    	HeapNode node = new HeapNode(key);
    	if(this.isEmpty()) 
    	{
    		this.first = node;
    		this.min = node;
    	}
    	else if(key<this.min.getKey()) 
    	{
    		this.min = node;
    	}
    	this.size++;
    	this.number_of_trees++;//always insert add 1 for the tree number 
    	node.prev = this.first.prev;
    	node.next = this.first;
    	this.first.prev.next = node;
    	this.first.prev = node;
    	this.first = node;
    	return node;
    }

    private void changeParentToNull(HeapNode x,int rank) 
    {
    	HeapNode minChild = x.child;
    	for(int i=rank;i>0;i--) 
    	{
    		minChild.parent=null;
    		minChild=minChild.next;
    	}
    	
    }     


   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin() {
    	
    	if(this.size==1) 
        	{
        		if(this.min.mark) 
        		{
        			number_of_marks--;
        		}
        		this.size=0;
        		this.first=null;
        		this.min=null;
        		this.number_of_trees=0;
        		return;
        	}
    	if(this.min.child==null) 
    	{
    		this.min.prev.next=this.min.next;
    		this.min.next.prev=this.min.prev;
    		 if(this.min.getKey()==this.first.getKey()) 
    		{
    			this.first=this.first.next;
    			//this.min = null;
    		}
    	}
    	else 
    	{
    		if(this.min==this.first) 
    		{
    			this.first=this.min.child;
    		}
        	changeParentToNull(this.min,this.min.rank);
        	this.min.next.prev = this.min.child.prev;//conect 19 to 35
        	this.min.child.prev.next = this.min.next; //conect 35 to 19 
        	this.min.prev.next = this.min.child;//conect 39 to 40 
        	this.min.child.prev= this.min.prev;//conect 40 to 39 
    	}
    	Consolidating_linking(this); //do Successive Linking after min deletion 
    	find_new_minimum(this);
    	this.size--; //delete one node
     	return; 
    }


	
    
//find the new minimum after delete min 
private void find_new_minimum(FibonacciHeap fibonacciHeap) 
{
	this.number_of_trees=0;
	HeapNode newmin = this.first;
	this.number_of_trees++;
	HeapNode temp = this.first.next;
	while(temp.getKey()!=this.first.getKey()) 
	{
		this.number_of_trees++;
		if(temp.getKey()<newmin.getKey()) 
		{
			newmin=temp;
		}
		if(temp==temp.next) 
		{
			return;
		}
		temp = temp.next;
	}
	this.min=newmin;
}




//this method need to link  every tree in thr same rank 
   private void Consolidating_linking(FibonacciHeap fibonacciHeap) 
   {
	   int counter = 0;
	   HeapNode[] arr = new HeapNode[50];
	   HeapNode node =this.first;
	   HeapNode curr = node.next;
	   arr[node.rank] = node;
	   node.next = node;
	   node.prev = node;
	   
	   while(curr.getKey()!=this.first.getKey()) 
	   {
		   int currRank = curr.rank;
		   //HeapNode temp = curr.next;
		   HeapNode linked = curr;
		   curr=curr.next;
		   while(arr[currRank]!=null) 
		   {
				   linked = link(arr[currRank],linked);
				   arr[currRank] = null;
				   currRank = linked.rank;   

		   }
		   if(arr[currRank]==null) 
		   {
			   arr[currRank] = linked;
		   }
	   }
	   HeapNode first = null;
	   for(int i=0; i<arr.length; i++) 
	   {
		if(arr[i]!=null) {//find the first baket
			first = arr[i];
			this.first=first;
			counter= i+1;
			break;
		}
	   }
		HeapNode temp = first;
		while(counter < arr.length) {
			 if(arr[counter]!=null) {
				 temp.next=arr[counter];
				 arr[counter].prev=temp;
				 temp=temp.next;
			 }
			 counter++;
		}
		first.prev=	temp;
		temp.next=first;
   }
	   

   
   
   private HeapNode link(HeapNode x1, HeapNode x2) {
	   totalLinks++;
	   HeapNode toBeReturned = null;
	   if(x1.getKey()<x2.getKey()) 
	   {//x1 is the smaller key will be the root 
		   toBeReturned =x1;
		   x1.rank++;
		   x1.next=x1;
		   x1.prev=x1;
		   HeapNode x1FirstChild =x1.child;
		   if(x1FirstChild!=null) 
		   {
			   x1.child=x2;
			   x2.parent=x1;
			   HeapNode x1LastChild = x1FirstChild.prev;
			   x2.next = x1FirstChild;
			   x1LastChild.next=x2;
			   x2.prev=x1LastChild;
			   x1FirstChild.prev =x2; 
		   }
		   else 
		   {
			   x1.child=x2;
			   x2.parent=x1;
			   x2.next=x2;
			   x2.prev=x2;
		   }
	   }
	   else 
	   { // x2 is the smaller key will be the root 
		   toBeReturned=x2;
		   x2.rank++;
		   x2.next=x2;
		   x2.prev=x2;
		   HeapNode x2FirstChild =x2.child;
		   if(x2FirstChild!=null) 
		   {
			   x2.child=x1;
			   x1.parent=x2;
			   HeapNode x2LastChild = x2FirstChild.prev;
			   x1.next = x2FirstChild;
			   x2LastChild.next=x1;
			   x1.prev=x2LastChild;
			   x2FirstChild.prev =x1;
		   }
		   else 
		   {
			   x2.child =x1;
			   x1.parent=x2;
			   x1.next=x1;
			   x1.prev=x1;
		   } 
	   }
	return toBeReturned;   
   }

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	this.number_of_trees=this.number_of_trees+heap2.number_of_trees;
    	this.number_of_marks=this.number_of_marks+heap2.number_of_marks;
    	
    	if(this.min.getKey()>heap2.min.getKey()) {//update the minimum (the root) to be the minimum between the trees
    		this.min=heap2.min; 
    	}
    	this.size=this.size+heap2.size;
    	HeapNode last_this = this.first.prev;//the last node in this tree that conact to the first node 
    	HeapNode last_heap2 = heap2.first.prev;//the last node in heap2 tree that conact to the first node 
    	if(last_this!=null&&last_heap2!=null) {
    	last_this.next=heap2.first;
    	heap2.first.prev=last_this;
    	last_heap2.next=this.first;
    	this.first.prev=last_heap2;
    	}
    	
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
	//int[] arr = new int[(int) Math.log(this.size)]; option
	int[] arr = new int[this.size];
	arr[first.rank]++;//put 1 in the rank of the first tree 
	HeapNode starter = this.first.next;
	while(starter.getKey()!=this.first.getKey()) {//until we back to the start 
		arr[starter.rank]++;
		starter=starter.next;
	}
	
        return arr; 
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {   
    	if(this.min!=null) 
    	{
    		decreaseKey(x,Integer.MAX_VALUE);
    		//decreaseKey(x, 2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2);
    		//decreaseKey(x, this.findMin().getKey()+1+x.getKey());
        	this.deleteMin();
    	}  	
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.key = x.getKey()-delta;
    	if(x.parent!=null && x.getKey()<x.parent.getKey())
    	{
    		cascadingCut(x);
    	}
    	if(x.getKey()<this.min.getKey()) 
    	{
    		this.min = x;
    	}
    }
    
    public void cascadingCut(HeapNode x) 
    {
    	HeapNode y = x.parent;
    	cut(x);
    	if(y.parent!=null) 
    	{
    		if(y.mark==false) 
    		{
    			y.mark=true;
    			this.number_of_marks++;
    		}
    		else //y is marl
    		{
    			cascadingCut(y);
    		}
    	}	
    }
    
    public void cut(HeapNode x) 
    {
    	this.number_of_trees++;
    	totalCuts++;
    	HeapNode y = x.parent;
    	x.parent=null;
    	if(x.mark ==true) {
    	x.mark = false;
		this.number_of_marks--;
    	}
    	y.rank--;
    	if(x.next == x) 
    	{
    		y.child = null;
    	}
    	else 
    	{
    		y.child = x.next;
    		x.prev.next = x.next;
    		x.next.prev = x.prev;
    	}
    	x.next =this.first;
        x.prev=this.first.prev;
        first.prev.next=x;
        first.prev=x;
        first=x;
    }



   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    
    public int potential() {
    	
		return this.number_of_trees + 2*(this.number_of_marks);
    	
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return totalLinks; 
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCuts; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k*deg(H)). 
    * You are not allowed to change H.
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {   
    	if(H.isEmpty()) 
    	{
        	int[] arr = new int[0];
    		return arr;
    	}
    	int[] arr = new int[k];
    	FibonacciHeap newHeap = new FibonacciHeap(); //making a new fibHeap
    	HeapNode node = H.first;
    	newHeap.insert(node.getKey());
    	H.addAllSons(newHeap,node,0,k);
    	for(int i=0;i<k;i++) 
    	{
    		arr[i] = newHeap.min.getKey();
    		newHeap.deleteMin();
    	}
    	return arr;
    
   }
    
    private void addAllSons(FibonacciHeap newHeap, HeapNode node, int floor, int k) 
    {
    	boolean finishedFloor = false;
    	HeapNode firstOnFloor = node.child;
    	HeapNode son = firstOnFloor;
    	while(!finishedFloor) 
    	{
        	newHeap.insert(son.getKey());
    		if(floor+1<=k) 
        	{
    			if(son.child!=null) 
            	{
            		addAllSons(newHeap, son, floor+1, k);
            	}
        	}
    		son=son.next;
    		if(son == firstOnFloor) 
    		{
    			finishedFloor=true;
    		}	
    	}
    }
    
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode
    {
    	public int key;
    	protected int rank;
    	protected boolean mark;
    	protected HeapNode child;
    	protected HeapNode next;
    	protected HeapNode prev;
    	protected HeapNode parent;
  	
    	public HeapNode(int key) 
    	{
    		this.key = key;
    		this.rank = 0;
    		this.mark = false;
    		this.child = null;
    		this.next = this;
    		this.prev = this;
    		this.parent = null;
    	}
    	public int getKey() 
    	{
    		return this.key;
    	}
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static  void printHeapFib(FibonacciHeap heap) {
    	
        System.out.println("-----------------------------------------------");
        if(heap.size==0) {
        	System.out.println("()");
        	return;
        }
        String[] list = new String[heap.size()*10];
        for (int i = 0; i < heap.size(); i++) {

            list[i] = "";
        }
        Integer level = 0;
        printHeapFib(heap.findMin(), list, level);
        for (int i = 0; i < heap.size(); i++) {
            if (list[i]!="")
                System.out.println(list[i]);
        }
        System.out.println("-----------------------------------------------");
    }

    public  static void printHeapFib(HeapNode node, String[] list, Integer level) {

        list[level] += "(";
        if (node == null) {
            list[level] += ")";
            return;
        } else {
            HeapNode temp = node;
            do {
                list[level] += temp.getKey();
                HeapNode k = temp.child;
                printHeapFib(k, list, level + 1);
                list[level] +=getChain("=");
                temp = temp.next;
            } while (temp != node);
            list[level] += ")";
        }


    }
    private static String getChain(String link){
        int count =2;;
        String str="";
        if (count==0){
            return "";
        }
        for (int i=0;i<count/3+1;i++){
            str+=link;
        }
        return str+">";
    }
    public static void insertN(FibonacciHeap h1,int n) {
        insertN( h1,1, n);

    }


    public static void insertN(FibonacciHeap h1,int start, int amount) {
        ArrayList<Integer> mylist = new ArrayList();
        for(int i = start; i <  start+amount; i++) {

            mylist.add(i);
        }
        Collections.shuffle(mylist);
        for (int i = 0; i < amount; i++) {
            h1.insert(mylist.get(i));

        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    ///////////
    static void test0() 
    {
        String test = "test0";
        FibonacciHeap fibonacciHeap = new FibonacciHeap();
          
        fibonacciHeap.insert(12); 
        fibonacciHeap.insert(11);
        fibonacciHeap.insert(10);
        fibonacciHeap.insert(9);
        fibonacciHeap.insert(8);
        fibonacciHeap.insert(7); 
        fibonacciHeap.insert(6); 
        fibonacciHeap.insert(5); 
        fibonacciHeap.insert(4); 
        fibonacciHeap.insert(3); 
        fibonacciHeap.insert(2); 
        fibonacciHeap.insert(1); 
 
        fibonacciHeap.deleteMin();
        
		FibonacciHeap.printHeapFib(fibonacciHeap);
        for (int i = 0; i <20; i++) 
        {
            if (fibonacciHeap.findMin().getKey() != i) 
            {
                System.out.println("damn, min is "+fibonacciHeap.findMin().getKey()+" and i is "+i);
                return;
            }
            fibonacciHeap.deleteMin();
            System.out.println("after deleting min:");
    		FibonacciHeap.printHeapFib(fibonacciHeap);
 		   System.out.println("finish");


        }
    }
    //////////
    
    public static void main(String[] args) {
        //test0();
    }
}

