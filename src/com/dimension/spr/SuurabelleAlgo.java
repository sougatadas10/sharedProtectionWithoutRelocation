package com.dimension.spr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class SuurabelleAlgo {
	private Graph graph;
    private List<Vertex> nodes;
    private List<Edge> edges;
    int [][] cost = new int [14][14];

    public SuurabelleAlgo(List<Vertex> nodes,List<Edge> edges, Graph graph){
    	this.nodes=new ArrayList<Vertex>(nodes);
    	this.edges=new ArrayList<Edge>(edges);
    	this.graph=graph;
    }
    
    private List <Edge> createmodifiedgraph(List<Edge> edgePath1,List<Edge> edgePath2){
     	List <Edge> valuesToRemove = new ArrayList<Edge>();
    	
    	for (Edge ep1: edgePath1){
    		valuesToRemove.add(ep1);
    	}
    	
    	edgePath2.removeAll(valuesToRemove);
       	return edgePath2;   	
    }
    
    
    private List<Edge> recalculateWeight(List<Edge> edgePath1,int source){
    	int sourceVertexId, destinationVertexId;
    	List<Edge> modifiedEdges = new ArrayList<Edge>(edges);

        for (Edge ed1:modifiedEdges){
        	if (ed1.getWeight()>0){
        		sourceVertexId=ed1.getSource().getId();
        		destinationVertexId=ed1.getDestination().getId();
        		ed1.setWeight(ed1.getWeight()- cost[source][destinationVertexId]+cost[source][sourceVertexId]);
        	}
        }

        return modifiedEdges;
    	
    }
    
    public void calculateCost(){
    	LinkedList<Edge> edgePath = new LinkedList<Edge>();
    	executeDijkstraAlgorithm e2 = new executeDijkstraAlgorithm ();
    	int totalcost;
    	
    	for (int i=0; i<14;++i){	
    		for (int j=0;j<14;++j){
    			if (i==j)
    				cost[i][j]=0;
    			else{
    				totalcost=0;
    				if (!edgePath.isEmpty())
    					edgePath.clear();
    				edgePath=e2.execute(nodes, edges, graph, i, j);
    		        for (Edge ed1:edgePath){
    		        	totalcost=totalcost+ed1.getWeight();
    		        }
    		        cost[i][j]=totalcost;
    			}
    				
    		}
    	}

    }
    
    public List <List<Edge>> execute (int source,int destination){
    	List<Edge> edgePath1,edgePath2,modifiedEdges,modifiedEdges1,tempEdgePath1;
    	List <List<Edge>> linkDisJointPaths = new ArrayList<List<Edge>>();
    	Graph modifiedGraph;
    	Edge tEdge;
    	int index1=0,index2=0,j=0;;
    	boolean flag;
    	
    	executeDijkstraAlgorithm e1 = new executeDijkstraAlgorithm ();
    	calculateCost();
//Step 1: Calculate shortest path between source and destination    	
        edgePath1=new ArrayList<Edge>(e1.execute(nodes,edges,graph,source,destination));
//Step 2: Calculate the modified weight for all edges        
        modifiedEdges=new ArrayList<Edge>(recalculateWeight(edgePath1,source)); 
//Step 3: Create modified graph
        modifiedEdges1=new ArrayList<Edge>(createmodifiedgraph(edgePath1,modifiedEdges));
        modifiedGraph = new Graph(nodes, modifiedEdges1);
//Step 4: Find the shortest path in the modified graph         
        edgePath2=new ArrayList<Edge>(e1.execute(nodes,modifiedEdges1,modifiedGraph,source,destination));
//Step5: Discard the reversed of edges of path1 & path2. Interchange the remaining edges to give 2 edge disjoint path
        flag=false;
        for (index1=0;index1<edgePath1.size()&&!flag;++index1){
        	for (index2=0;index2<edgePath2.size();++index2){
        		if(edgePath1.get(index1).getSource()==edgePath2.get(index2).getDestination() && 	
        			edgePath1.get(index1).getDestination()==edgePath2.get(index2).getSource()){
        				flag=true;
        				break;
        		}
        	}
        }
        if (flag){
        	tempEdgePath1=new ArrayList<Edge>(edgePath1);
        	int len = edgePath1.size()-index1;
        	for (int i=edgePath1.size()-1;i>len;--i)
        		edgePath1.remove(i);
        	j=index1;
        	for (int i=index2;i<edgePath2.size();++i){
        		tEdge=new Edge(edgePath2.get(i).getId(),edgePath2.get(i).getSource(),
        				edgePath2.get(i).getDestination(),edgePath2.get(i).getWeight());
        		edgePath1.add(tEdge);
        		++j;
        	}
        	len = edgePath2.size()-index2;
        	for (int i=edgePath2.size()-1;i>len;--i)
        		edgePath2.remove(i);
        	
        	j=index2;
        	for (int i=index1;i<tempEdgePath1.size();++i){
        		tEdge=new Edge(tempEdgePath1.get(i).getId(),tempEdgePath1.get(i).getSource(),
        				tempEdgePath1.get(i).getDestination(),tempEdgePath1.get(i).getWeight());
        		edgePath2.add(tEdge);
        		++j;
        	}


        }
//Step 5 ends        
      	linkDisJointPaths.add(edgePath1);
      	linkDisJointPaths.add(edgePath2);
      	return linkDisJointPaths; 
    }
    
}

/**
public class SuurabelleAlgo {
	
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
        
        SuurabelleAlgoDemo s1 = new SuurabelleAlgoDemo(nodes, edges,graph);
        s1.execute(source, destination);
	}

}
*/
