package com.dimension.spr;

import java.util.ArrayList;
//import java.util.LinkedList;
import java.util.List;

class IncreaseSharingBackupMain {
	
    private List<Vertex> nodes;
    private List<Edge> edges,edgePath1,edgePath2;
    private List <List<Edge>> listAllPaths;
	private List <List<Edge>> backupPaths = new ArrayList<List<Edge>>();
    int adj[][]= new int [14][14];
    int source,destination;
	
	public IncreaseSharingBackupMain(List<Edge> edges, List<Vertex> nodes, List<Edge> edgePath1,List<Edge> edgePath2,
			int source,int destination,List <List<Edge>> backupPaths ){
    	this.nodes=new ArrayList<Vertex>();
    	this.nodes=nodes;
    	this.edges=new ArrayList<Edge>(edges);
    	this.edgePath1=new ArrayList<Edge>(edgePath1);
    	this.edgePath2=new ArrayList<Edge>(edgePath2);
    	this.source=source;
    	this.destination=destination;
    	this.backupPaths=backupPaths;
	}
	
	private void initAdjacency(){
		for (int i=0; i<14;++i)
			for (int j=0;j<14;++j)
				adj[i][j]=-1;
				
	}
	private void createAdjacency(){
		for (Edge e1:edges){
			int source = e1.getSource().getId();
			int destination = e1.getDestination().getId();
			adj[source][destination]=1;
			}
		
	}
	
	private void createPathEdge(int path[],int pathIndex){
		int s,d;
		List<Edge> edgePath3= new ArrayList<Edge>();
		if (! edgePath3.isEmpty())
			edgePath3.clear();
		for (int i=0; i<pathIndex-1;++i){
			s = path[i];
			d = path[i+1];
			for (Edge e1:edges){
				if (e1.getSource().getId()== s && e1.getDestination().getId()== d){
					edgePath3.add(e1);
					break;
				}
			}
		}
		listAllPaths.add(edgePath3);
	}
	
	private void getAllPaths(boolean visited[],int path[],int pathIndex,int s,int d){
		visited[s]=true;
		path[pathIndex] = s;
		++pathIndex;
		if (pathIndex == 14)
			return;
		
		if (s == d){
			createPathEdge(path,pathIndex);
		}
		else {
			for (int i=0;i<14;++i){
				if (adj[s][i]==1){
					if (!visited[i])
						getAllPaths(visited,path,pathIndex,i,d);
				}
			}
		}
		--pathIndex;
		visited[s] = false;
		
	}
	public void execute(){
		boolean visited[] = new boolean[14];
		int path[] = new int[14];
		List<Edge> modifiedEdges;
		List<Edge> edgePath3 = new ArrayList<Edge>();
		int costOfAllBackupPaths=0,modBackupCost=0;

		this.listAllPaths=new ArrayList<List<Edge>>();

		initAdjacency();
		createAdjacency();
		//Get the cost of backupPaths in terms of Wavelengths
		for (Edge ep2:edgePath2){
			for (Edge e1:edges){
				if (ep2.getId()==e1.getId())
					costOfAllBackupPaths+=e1.getWeight();
			}
		}
		
		//System.out.println("Cost="+ costOfAllBackupPaths);

		//Initialize the visited array
		for (int i=0;i<14;++i)
			visited[i]=false;
		//Get a set of edges which form backupPaths
		getAllPaths(visited,path,0,source,destination);
		modifiedEdges=new ArrayList<Edge>(edges);
		//Remove all edges which belong to primaryPath1
		modifiedEdges.removeAll(edgePath1);
		//Set 0 for all edges of paths
		for (int i=0;i<listAllPaths.size();++i){
			for (Edge e1:listAllPaths.get(i)){
				for (Edge me1: modifiedEdges){
					if (e1.getId() == me1.getId()){
						me1.setWeight(0);
					}
				}
			}
		}
		//Get a new backupPath
		executeDijkstraAlgorithm exeDijs1 = new executeDijkstraAlgorithm ();
        edgePath3=exeDijs1.execute(nodes,modifiedEdges,new Graph(nodes,modifiedEdges),source,destination);
		
        if (edgePath3==null)  //No shared path exists
        	backupPaths.add(edgePath2);
        else {
        	for (Edge ep3:edgePath3){
        		for (Edge e1:edges){
        			if (ep3.getId()==e1.getId())
        				modBackupCost+=e1.getWeight();
			}
		}
        	//System.out.println("Cost="+ modBackupCost);
        	if (costOfAllBackupPaths < modBackupCost)
        		backupPaths.add(edgePath2);
        	else 
        		backupPaths.add(edgePath3);
        }
		
	}
	
}	
/**
public class IncreaseSharingBackupMain2 {
		
		 private static List<Vertex> nodes;
		 private static List<Edge> edges;

		private static void addLane(String laneId, int sourceLocNo, int destLocNo,int duration) {
			Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration );
	        edges.add(lane);
		}

		public static void main(String [] args){
			int source,destination;
			nodes = new ArrayList<Vertex>();
			edges = new ArrayList<Edge>();
			
	
	    	List<Edge> edgePath1 = new ArrayList<Edge>();
	    	List<Edge> edgePath2 = new ArrayList<Edge>();
			List <List<Edge>> linkDisJointPaths = new ArrayList<List<Edge>>();
			
			List <List<Edge>> primaryPaths = new ArrayList<List<Edge>>();
			List <List<Edge>> backupPaths = new ArrayList<List<Edge>>();
        
			for (int i = 0; i < 14; i++) {
				Vertex location = new Vertex(i, "Node_" + i);
                nodes.add(location);
			}

			addLane("Edge_1", 0, 1, 1);
			addLane("Edge_2", 0, 2, 1);
			addLane("Edge_3", 0, 3, 1);
			addLane("Edge_4", 1, 0, 1);
			addLane("Edge_5", 2, 0, 1);
			addLane("Edge_6", 3, 0, 1);
        
			addLane("Edge_7", 1, 2, 1);
			addLane("Edge_8", 1, 7, 1);
			addLane("Edge_9", 2, 1, 1);
			addLane("Edge_10", 7, 1, 1);
        
			addLane("Edge_11", 2, 5, 1);
			addLane("Edge_12", 5, 2, 1);
        
			addLane("Edge_13", 3, 10, 1);
			addLane("Edge_14", 3, 4, 1);
			addLane("Edge_15", 10,3, 1);
			addLane("Edge_16", 4, 3, 1);
        
			addLane("Edge_17", 4, 5, 1);
			addLane("Edge_18", 4, 6, 1);
			addLane("Edge_19", 5, 4, 1);
			addLane("Edge_20", 6, 4, 1);
        
			addLane("Edge_21", 5, 9, 1);
			addLane("Edge_22", 5, 12, 1);
			addLane("Edge_23", 9, 5, 1);
			addLane("Edge_24", 12, 5, 1);
        
			addLane("Edge_25", 6, 7, 1);
			addLane("Edge_26", 7, 6, 1);
        
			addLane("Edge_27", 7, 8, 1);
			addLane("Edge_28", 8, 7, 1);
        
			addLane("Edge_29", 8, 9, 1);
			addLane("Edge_30", 8, 11, 1);
			addLane("Edge_31", 8, 13, 1);
			addLane("Edge_32", 9, 8, 1);
			addLane("Edge_33", 11, 8, 1);
			addLane("Edge_34", 13, 8, 1);
        
			addLane("Edge_35", 10, 11, 1);
			addLane("Edge_36", 10, 13, 1);
			addLane("Edge_37", 11, 10, 1);
			addLane("Edge_38", 13, 10, 1);
        
			addLane("Edge_39", 11, 12, 1);
			addLane("Edge_40", 12, 11, 1);
        
			addLane("Edge_41", 12, 13, 1);
			addLane("Edge_42", 13, 12, 1);
			

	        source=6;
	        destination=8;

			Graph graph = new Graph(nodes, edges);

			
			SuurabelleAlgo s1 = new SuurabelleAlgo(nodes, edges,graph);
			linkDisJointPaths=s1.execute(source, destination);
			edgePath1 = linkDisJointPaths.get(0);
			edgePath2 = linkDisJointPaths.get(1);
			
			
			//Resetting the weight after S algo is done
			for (Edge ed1: edges)
				ed1.setWeight(1);
			
	        System.out.println("In sharing method");
	      	for (Edge ep1: edgePath1)
	       		System.out.println(ep1.getId()+"\t"+ep1.getSource()+"\t"+ep1.getDestination()+"\t"+ep1.getWeight()); 
	      	primaryPaths.add(edgePath1);

	      	for (Edge ep2: edgePath2)
	       		System.out.println(ep2.getId()+"\t"+ep2.getSource()+"\t"+ep2.getDestination()+"\t"+ep2.getWeight()); 
	      	
	      	IncreaseSharingBackupMain isb1 = new IncreaseSharingBackupMain(edges,nodes,edgePath1,edgePath2,source,destination,backupPaths);
	      	isb1.execute();
	      	System.out.println("In here: "+ backupPaths.size());
			for (int i=0;i<backupPaths.size();++i)
				for (Edge e1:backupPaths.get(i))
					System.out.println(e1.getId());
			
			IncreaseSharingPrimary isp1 = new IncreaseSharingPrimary(edges,nodes,backupPaths,primaryPaths);
			if (primaryPaths.size()>1)
				isp1.execute();
	}
}
**/
