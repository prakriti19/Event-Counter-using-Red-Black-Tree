import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class bbst {

	//creating the global root for RBTRee
	static Node root;
	
	//Node class of RBTree
	private class Node{
		int id;
		int count;
		String colour;
		Node left;
		Node right;
		Node parent;
	
		Node(){
			colour = "Black";
			left = null;
			right = null;
			parent = null;
		}
		//Initializing the node with values
		Node(int key, int val, String col){
			id = key;
			count = val;
			colour = col;
			left = nil;
			right = nil;
			parent = nil;
		}
		
	}
	
	//using the global value to count the inRange Nodes
	private class Count{
		int count =0;
	}
	
	//creating the sentinal node to be put below last level
	Node nil = new Node();
	
	//Inserting the node as a normal BST
	public Node insertBST(Node node, Node add ){
		if(node==nil){
			//inserting the node at the correct position
			return add;
		}
		if(add.id <node.id){
			//going to the left if the node to be inserted is smaller than the current node
			node.left = insertBST(node.left,add);
			//pointing the node just inserted to its parent
			node.left.parent = node;
		}else{
			//going right in case the node is equal or bigger
			node.right = insertBST(node.right,add);
			//attaching the parent
			node.right.parent = node;
		}
		
		return node;
	}
	
	//Function to perform the left rotation
	public void rotateLeft(Node node){
		Node ptrRight = node.right;
		node.right = ptrRight.left;
		
		if(ptrRight.left != nil){
			ptrRight.left.parent = node;
		}
		ptrRight.parent = node.parent;
		
		if(node.parent==nil){
			root = ptrRight;
		}else if( node == node.parent.left){
			node.parent.left = ptrRight;
		}else{
			node.parent.right = ptrRight;
		}
		
		ptrRight.left = node;
		node.parent = ptrRight;
	}
	
	//function to perform right rotation
	public void rotateRight(Node node){
		Node ptrLeft = node.left;
		node.left = ptrLeft.right;
		
		if(ptrLeft.right != nil){
			ptrLeft.right.parent = node;
		}
		
		ptrLeft.parent = node.parent;
		
		if(ptrLeft.parent==nil){
			root = ptrLeft;
		}else if(node == node.parent.left){
			node.parent.left = ptrLeft;
		}else{
			node.parent.right = ptrLeft;
		}
		
		ptrLeft.right = node;
		node.parent = ptrLeft;
	}
	
	//fixing the node as per RB tree property after inserting it.
	void fixViolation(Node node){
		Node parentPtr = nil;
		Node grParentPtr = nil;
		
		while( node != root && node.colour != "Black" && node.parent.colour == "Red" && node.parent.parent != nil){
			
			parentPtr = node.parent;
			grParentPtr = node.parent.parent;
			//case A
			//parent of node is the left child of grandparent
			if(parentPtr == grParentPtr.left){
				Node unclePtr = grParentPtr.right;
				
				//case 1 
				//Uncle is red in color
				if(unclePtr != nil && unclePtr.colour == "Red"){
					grParentPtr.colour = "Red";
					parentPtr.colour = "Black";
					unclePtr.colour = "Black";
					node = grParentPtr;
				}else{
					//case 2
					//of node is the right child of its parent
					if(node == parentPtr.right){
						rotateLeft(parentPtr);
						node = parentPtr;
						parentPtr = node.parent;
					}
					
					//case 3
					//node is the left child of its parent left rotation is required
					rotateRight(grParentPtr);
					String temp = parentPtr.colour;
					parentPtr.colour = grParentPtr.colour;
					grParentPtr.colour = temp;
					node = parentPtr;
				}
			}
			
			//case B
			//parent of the node is the right child of grand parent
			if(parentPtr == grParentPtr.right){
				Node unclePtr = grParentPtr.left;
				//case 1
				//uncle pointer is red
				if(unclePtr != nil && unclePtr.colour == "Red"){
					grParentPtr.colour = "Red";
					parentPtr.colour = "Black";
					unclePtr.colour = "Black";
					node = grParentPtr;
				}else{
					//case 2
					//node is the left child of its parent right rotation is required
					if(node == parentPtr.left){
						rotateRight(parentPtr);
						node = parentPtr;
						parentPtr = node.parent;
					}
					//case 3
					//node is the right child of its  parent left rotation is required
					rotateLeft(grParentPtr);
					String temp = parentPtr.colour;
					parentPtr.colour = grParentPtr.colour;
					grParentPtr.colour = temp;
					node = parentPtr;
					
				}
			}
			
		}
		
		root.colour = "Black";
	}
	
	//Function to insert the node as BST and fix it using the fix Violation
	public void insert(int key, int val){
		Node add = new Node(key,val,"Red");
		insertBST(root,add);
		fixViolation(add);
	}
	
	//Function to search for the node in the tree
		public Node search(int key) {
			 Node node=root;
	        while (node!= nil){
	            if(node.id==key){
	            	return node;
	            }
	            else if(node.id<key){
	            	node=node.right;
	            }
	            else{
	            	node=node.left;
	            }    
	        }
	        //key not found in the tree
	        return nil;
	    }
		
	//function the swipe the two nodes
	public void transferLink(Node one, Node two){ 
		//System.out.println(" in transferLink");
		if (one.parent == nil){
			//System.out.println(" u is root " + u.id + " v is " + v.id);
	        root=two;
	    }else if(one==one.parent.right){
	    	one.parent.right = two;    
	    }else{
	    	one.parent.left = two;
	    }
	        
	        two.parent = one.parent;
	}

	//Function to delete the node from the tree
	public void delete(Node node){
		//System.out.println(" in delete");
	    Node tempNode=node;
	    Node other;
	    String checkColour = tempNode.colour;
	    if(node.left==nil){
	    	other=node.right;
	        transferLink(node,node.right);
	    }
	    else if(node.right==nil){
	    	other=node.left;
	        transferLink(node,node.left);
	    }
	    else{
	    	tempNode=node.right;
	        while(tempNode.left!=nil)
	        	tempNode=tempNode.left;
	        checkColour=tempNode.colour;
	        other=tempNode.right;
	        if(tempNode.parent==node)
	        	other.parent=tempNode;
	        else{
	        	transferLink(tempNode,tempNode.right);
	        	tempNode.right=node.right;
	        	tempNode.right.parent=tempNode;
	        }
	        transferLink(node,tempNode);
	        tempNode.left=node.left;
	        tempNode.left.parent=tempNode;
	        tempNode.colour=node.colour;
	    }
	    //if the node to be deleted has back color we caused a violation and is needed to be fixed
	    if(checkColour=="Black")
	    	fixViolationDelete(other);
	}

	//one the node is deleted and the RB Tree property is violated we call the fix function to make is RB Tree again
	//we look for the sibling node and perform the operation based on the sibling of the current node

	public void fixViolationDelete(Node node){ 
		
	    while(node!=root && node.colour=="Black"){
	        if(node==node.parent.left){
	        	//if the node is the left child and sibling is in the right
	            Node sibling=node.parent.right;
	            //check if the color of sibling is red then exchange the color with parent and rotate left 
	            if(sibling.colour == "Red"){
	            	sibling.colour = "Black";
	                node.parent.colour = "Red";
	                rotateLeft(node.parent);
	                sibling=node.parent.right;
	            }
	            if(sibling.left.colour=="Black" && sibling.right.colour =="Black"){
	            	//if both the children of sibling are black, change the color of the sibling and transfer the problem up the parent
	            	sibling.colour = "Red";
	                node=node.parent;
	            }
	            else{ 
	                if(sibling.right.colour=="Black"){
	                	//if the left child of the sibling in red exchange the color of the sibling's child with sibling and color the child black
	                	// and rotate right
	                	sibling.left.colour = "Black";
	                	sibling.colour = "Red";
	                    rotateRight(sibling);
	                    sibling=node.parent.right;
	                }
	                //followed my the left rotation
	                sibling.colour = node.parent.colour;
	                node.parent.colour = "Black";
	                sibling.right.colour = "Black";
	                rotateLeft(node.parent);
	                node=root;
	            }
	        }
	        else{
	        	//node is the right child of the parent and sibling is the left child
	            Node sibling=node.parent.left;
	            
	            if(sibling.colour == "Red"){
	            	sibling.colour = "Black";
	                node.parent.colour = "Red";
	                rotateRight(node.parent);
	                sibling=node.parent.left;
	            }
	            
	            if(sibling.right.colour=="Black" && sibling.left.colour=="Black"){
	            	sibling.colour = "Red";
	                node=node.parent;
	            }else{
	                if(sibling.left.colour=="Black"){
	                	sibling.right.colour= "Black";
	                	sibling.colour = "Red";
	                    rotateLeft(sibling);
	                    sibling=node.parent.left;
	                }
	                
	                sibling.colour = node.parent.colour;
	                node.parent.colour = "Black";
	                sibling.left.colour = "Black";
	                rotateRight(node.parent);
	                node=root;
	            }
	        }
	    }
	    //we color the node black
	    node.colour = "Black";
	}	

	//function to search for a node in the tree, if present then delete the node
	public void deleteNode(int key){
		//System.out.println(" in delete Node ");
		Node delNode = search(key);
		if(delNode != nil){
			//System.out.println(" del " + delNode.id);
			delete(delNode);
		}else{
			System.out.println(" Node not in RB Tree");
		}
	}

	// This function is to find the node with the smallest key that is bigger than the given id
	public int findNext(Node root, int key){
		int res = key;
		while(root != nil){
			if(root.id <= key){
				root = root.right;
			}
			if(root != nil && root.id > key){
				res = root.id;
				root=root.left;
			}
		}
		
		return res;
	}
	
	
	// In case the Id is present in the key, the previus node is the inorder predecessor,
	//this function performs the task for finding the Inorder predecessor
	public Node getPrev(Node root, int key, Node prev){
		if(root==nil){
			return nil;
		}
		
		if(key==root.id){
			if(root.left != nil){
				Node temp = root.left;
				while(temp.right != nil){
					temp = temp.right;
				}
				prev = temp;
			}
			return prev;
		}
		
		if(key>root.id){
			prev = root;
			prev = getPrev(root.right,key,prev);
		}else{
			prev = getPrev(root.left,key,prev);
		}
		
		return prev;
	}
	
	//This function performs the task of finding the biggest node that is smaller than the given ID
	public int findPrev(Node root, int key){
		int res = key;
		while(root != nil){
			if(key==root.id){
				return root.left.id;
			}
			if(root.id > key ){
				root = root.left;
			}
			if( root != nil && root.id < key){
				res = root.id;
				root = root.right;
			}
			
		}
		return res;
	}
	
	//In case of finding the next of an ID already present in the tree, we need to find the inorder successor 
	//This function performs the task of finding the inorder successor in the tree
	public Node getNext(Node root, int key, Node next){
		if(root == nil){
			return nil;
		}
		
		if(key==root.id){
			//System.out.println(" next for " + root.id);
			if(root.right != nil){
				Node temp = root.right;
				while(temp.left != nil){
					temp = temp.left;
				}
				next = temp;
				//System.out.println(" next is " + next.id);
			}
			return next;
		}
			
		if(key < root.id){
			next = root;
				next = getNext(root.left,key,next);
		}else{
			next = getNext(root.right,key,next);
		}
			return next;
	}
	
	
	//This the wrapper fucntion to find the next node for a given ID
	//If the node is present in the tree, we find its inorder successor else, we perform a binary search to get the node 
	//which is smallest node in the tree which is bigger than the given node
	public void Next(int key){
		Node res;
		if(search(key)!=nil){
			res = getNext(root,key,nil);
		}else{
			int next = findNext(root,key);
			res = getNode(root,next);
		}
		
		if(res != nil){
			System.out.println( res.id + " " + res.count);
		}else{
			System.out.println("0 0");
		}
	}
	
	//This the wrapper function to find the previous node for a given ID
	//If the node is present in the tree, we find its inorder predecessor else, we perform a binary search to get the node 
	//which is biggest node in the tree which is smaller than the given node
	
	public void Previous(int key){
		if(search(key)!=nil){
			Node res = getPrev(root,key,nil);
			if(res != nil){
				System.out.println(res.id + " " + res.count);
			}else{
				System.out.println("0 0");
			}
		}else{
			int prev = findPrev(root,key);
			Node res = getNode(root,prev);
			if(res != nil){
				System.out.println(res.id + " " + res.count);
			}else{
				System.out.println("0 0");
			}
		}
	}
	
	//This function finds the nodes which lie in the given range and sum up the counts of these nodes and returns the total sum
	
	public int getInRange(Node root, int id1, int id2){
		
		if(root==nil){
			return 0;
		}
		
		/*if(root.id >= id1 && root.id <= id2){
			//if(root.id==350) System.out.println(" in range " + root.id + " with count "  + root.count);
			count.count = count.count+root.count;
		}*/
		if(root.id==id1&&root.id==id2)
            return root.count;

        if(id1<=root.id&&id2>=root.id){
        
            return root.count+getInRange(root.left,id1,id2)+getInRange(root.right,id1,id2);
        }

		
        else if(id1<root.id){
            return getInRange(root.left,id1,id2);

        }
        else {
            return getInRange(root.right, id1, id2);
        }

	}
	
	//This is the wrapper function for the inRange function 
	public void InRange(int id1, int id2){
		if(id1<findMin()){
			id1 = findMin();
		}
				
		if(id2>findMax()){
			id2 = findMax();
		}
		Count c1 = new Count();
		int res = getInRange(root,id1,id2);
		System.out.println(res);
	}
	
	//get the minimum ID in the tree
	public int findMin(){
		if(root==nil){
			return 0;
		}
		Node temp = root;
		while(temp.left != nil){
			temp=temp.left;
		}
		return temp.id;
	}
	
	//get the maximum ID in the tree
	public int findMax(){
		if(root==nil){
			return 0;
		}
		Node temp = root;
		while(temp.right != nil){
			temp = temp.right;
		}
		return temp.id;
	}
	
	//Get the count of the given ID
	
	public int getCount(Node root, int key){
		if(root==nil){
			return 0;
		}
		
		if(root.id == key){
			return root.count;
		}
		
		if(key < root.id){
			return getCount(root.left,key);
		}else{
			return getCount(root.right,key);
		}
	}
	
	//Wrapper function to get the count of the given ID
	public void Count(int key){
		int res = getCount(root,key);
		System.out.println(res);
	}
	
	//Function to perform the increase key function in case the ID is present in the tree
	public Node increaseKey(Node root, int key, int incVal){
		if(root==nil){
			return nil;
		}
		
		if(root.id == key){
			int temp = root.count+incVal;
			root.count = temp;
			return root;
		}
		
		if(key<root.id){
			root = increaseKey(root.left,key,incVal);
		}else{
			root = increaseKey(root.right,key,incVal);
		}
		
		return root;
	}
	
	//wrapper function for increase key if present and insert a new node in case the key is not present in the tree which the given
	//value to increase as a count of that ID
	public void Increase(int key, int incVal){
		Node res = increaseKey(root,key,incVal);
		if(res==nil){
			insert(key,incVal);
			res = getNode(root,key);
			System.out.println(res.count);
		}else{
		
			System.out.println(res.count);
		}
	}

	//function to perform the decrease key operation in case the key is present in the tree
	public Node decreaseKey(Node root, int key, int decVal){
		if(root==nil){
			return nil;
		}
		
		if(root.id == key){
			int temp = root.count-decVal;
			root.count = temp;
			return root;
		}
		
		if(key<root.id){
			root = decreaseKey(root.left,key,decVal);
		}else{
			root = decreaseKey(root.right,key,decVal);
		}
		
		return root;
	}
	
	//Wrapper function for the decrease key operation which decreases the count if the key is present in the tree
	//if the new count returned is less than 1 it calls the deleteNode method to delete the tree
	public void Reduce(int key, int incVal){
		Node temp = decreaseKey(root,key,incVal);
		
		if(temp != nil){
			if(temp.count <=0){
				deleteNode(key);
				System.out.println("0");
			}else{
				System.out.println(temp.count);
			}
		}else{
			System.out.println("0");
		}
	}
	//Funtion to find the node from a given node with a given id
	
	public Node getNode(Node root, int id){
		if(root==nil){
			return nil;
		}
		
		if(root.id == id){
			return root;
		}
		if(id < root.id){
			root = getNode(root.left,id);
		}else{
			root = getNode(root.right,id);
		}
		
		return root;
	}
	
	//Function to preform a level order travesal on the tree
	public void levelOrder(Node node){
		ArrayList<Node> queue = new ArrayList<Node>();
		
		if(node==nil){
			return;
		}
		if(node != nil){
			queue.add(node);
		}
		
		while(!queue.isEmpty()){
			Node temp = (Node)queue.get(0);
			System.out.println(" ID: " + temp.id + " Count: " + temp.count + " Colour: " + temp.colour);
			queue.remove(0);
			if(temp.left != nil){
				queue.add(temp.left);
			}
			if(temp.right != nil){
				queue.add(temp.right);
			}
		}
		
	}
	
	//function to perform the preorder travesal on the tree
	public void preorder(Node node){
		if(node == nil){
			return;
		}
		System.out.println(" ID: " + node.id + " Colour: " + node.colour + " Count: " + node.count);
		preorder(node.left);
		preorder(node.right);
	}
	
	//function to build the tree from the given sorted array in O(n) time
	//The function takes the middle element as the root of the tree and recursively calls
	//the function to get the left and the right sub tree
	public Node buildTree(int arr[][], int start, int end, int level, int height){
		if(start>end) return nil;
		int mid = start+(end-start)/2;
		Node root; 
		if(level==height-1){
			root = new Node(arr[mid][0],arr[mid][1],"Red");
		}else{
			root = new Node(arr[mid][0],arr[mid][1],"Black");		
		}
		root.left = buildTree(arr,start,mid-1,level+1,height);
		root.left.parent=root;
		root.right = buildTree(arr,mid+1,end,level+1,height);
		root.right.parent=root;
		return root;
	} 
		
	//function calculates the height of the tree
	public int height(Node root){
		if(root==nil)return 0;
		return Math.max(height(root.left),height(root.right))+1;
	}
	
	//once the tree is created, we need to color the last level nodes to red 
	//this function performs the task of going to the last level and coloring the nodes as red
	public void colorRedLastLevel(Node root,int level){
		if(level==1){
			root.colour = "Red";
		}else if(level>1){
			colorRedLastLevel(root.left,level-1);
			colorRedLastLevel(root.right,level-1);
		}
		
	}
	
	public void buildInitialTree(int arr[][], int size){
		int height = (int) Math.ceil((Math.log(size))/Math.log(2));
		root = buildTree(arr, 0, arr.length-1,0,height);
		//int height = height(root);
		//colorRedLastLevel(root, height);

	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
			bbst rbt = new bbst();
		
			//File f1 = new File("C:\\Users\\Prakriti\\Downloads\\ads_project\\test_10000000.txt");
				//System.out.println(" file name " + args[0]);
			File f1 = new File(args[0]);
			FileReader input = new FileReader(f1);
			BufferedReader in = new BufferedReader(input);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
				line = in.readLine();
				int size = Integer.parseInt(line);
				int arr[][] = new int[size][2];
				int i=0;
				while(line != null){
					line = in.readLine();
					if( line != null){
						int key = Integer.parseInt(line.split(" ")[0]);
						int val = Integer.parseInt(line.split(" ")[1]);
						arr[i][0]=key;
						arr[i][1]=val;
						i++;
					
					}
				}
				rbt.buildInitialTree(arr,size);
				
				input.close();
				in.close();
				//System.out.println(" tree build");
				String line1;
				while ((line1 = br.readLine()) != null) {
					//System.out.println(" line " + line);
	            	if(line1.isEmpty())
	            		continue;
	            	//System.out.println(" line ");
	            	StringTokenizer stringTokenizer = new StringTokenizer(line1, " ");
	            	
	            	while (stringTokenizer.hasMoreElements()) {
					  
	            	String command = stringTokenizer.nextElement().toString();
	            	int param1 ;
			    	int param2;
			    		               
	               
		        //        System.out.println(command);
		                switch (command.toString()) {
		                    case "increase":
		                    	param1 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                    	param2 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                        
		                        //System.out.println(Integer.parseInt(y.toString()));
		                        rbt.Increase(param1, param2);
		                        break;
		                    case "reduce":
		                    	param1 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                    	param2 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                        rbt.Reduce(param1,param2);
		                        break;
		                    case "count":
		                    	param1 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                        rbt.Count(param1);
		                        break;
		                    case "inrange":
		                    	param1 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                    	param2 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                        rbt.InRange(param1, param2);
		                        break;
		                    case "next":
		                    	param1 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                        rbt.Next(param1);
		                        break;
	
		                    case "previous":
		                    	param1 = Integer.parseInt(stringTokenizer.nextElement().toString());
		                        rbt.Previous(param1);
		                        break;
		                    case "quit":
						    	System.exit(0);
						    	break;
						    default : 
						    	System.out.println(" incorrect Input");
						    	System.exit(0);
						    	break;
		                }
	            	}
	            }
				
				//File f2 = new File("C:\\Users\\Prakriti\\Downloads\\ads_project\\commands.txt");
				//File f2 = new File(args[1]);
				//FileReader input2 = new FileReader(f2);
				//BufferedReader in2 = new BufferedReader(input2);
				//String s2 = in2.readLine();
				/*while(s != null){
					s = in.readLine();
					if(s!=null){
						switch(s.split(" ")[0]){
					    case "increase" :
					    	//System.out.println(" increase ");
					    	int key = Integer.parseInt(s.split(" ")[1]);
					    	int incVal = Integer.parseInt(s.split(" ")[2]);
					    	//System.out.println(" key " + key + " incVal " + incVal);
					    	rbt.Increase(key, incVal);
					       break; 
					       case "reduce" :
					    	 //System.out.println(" reduce ");
					    	key = Integer.parseInt(s.split(" ")[1]);
					    	incVal = Integer.parseInt(s.split(" ")[2]);
					    	//System.out.println( " key " + key  + " incVal " + incVal);
					    	rbt.Reduce(key, incVal);
					       break;
					    case "count" :
					    	//System.out.println("count ");
					    	key = Integer.parseInt(s.split(" ")[1]);
					    	//System.out.println(" key " + key);
					    	rbt.Count(key);
					    	break;
					    case "inrange" :
					    	//System.out.println(" in range ");
					    	key = Integer.parseInt(s.split(" ")[1]);
					    	int key2 = Integer.parseInt(s.split(" ")[2]);
					    	//System.out.println(" key " + key + " val " + key2);
					    	rbt.InRange(key, key2);
					    	break;
					    case "next" :
					    	//System.out.println(" next");
					    	key = Integer.parseInt(s.split(" ")[1]);
					    	//System.out.println(" key " + key);
					    	rbt.Next(key);
					    	break;
					    case "previous" :
					    	//System.out.println(" prev ");
					    	key = Integer.parseInt(s.split(" ")[1]);
					    	//System.out.println(" find prev of " + key);
					    	//System.out.println(" node in tree " + rbt.getNode(root, key).id);
					    	rbt.Previous(key);
					    	break;
					    case "quit":
					    	System.exit(0);
					    	break;
					    default : 
					    	System.out.println(" incorrect Input");
						}
					}*/
		
	}

}
