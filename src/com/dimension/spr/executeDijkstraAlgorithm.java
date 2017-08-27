package com.dimension.spr;


import java.util.LinkedList;
import java.util.List;


class executeDijkstraAlgorithm {

        private LinkedList<Edge> getEdges(List<Edge> edges,LinkedList<Vertex> path){
    	Vertex source,destination;
    	LinkedList<Edge> edgePath = new LinkedList<Edge>();
    	
    	for (int i=0; i<path.size()-1;++i){
    		source = path.get(i);
    		destination = path.get(i+1);
    		for (Edge e1:edges){
    			if (e1.getSource()==source && e1.getDestination()==destination){
    				edgePath.add(e1);
    			}
    		}
    	}
    	
    	return edgePath;
    }
        
    public LinkedList<Edge> execute(List<Vertex> nodes,List<Edge> edges,Graph graph,int source, int destination){
   
    	LinkedList<Edge> edgePath = new LinkedList<Edge>();
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(source));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(destination));
        if (path!= null)
        	edgePath = getEdges(edges,path);
        if (path==null)
        	return null;
        else
        	return edgePath;
    }
    
    
}
