package com.dimension.spr;

import java.util.ArrayList;
import java.util.List;

public class IncreaseSharingPrimary {
	
    private List<Vertex> nodes;
    private List<Edge> edges;
	private List <List<Edge>> primaryPaths = new ArrayList<List<Edge>>();
	private List <List<Edge>> backupPaths = new ArrayList<List<Edge>>();
	List<requestVector> r1 = new ArrayList<requestVector>();
  //  int adj[][]= new int [14][14];

	
	public IncreaseSharingPrimary(List<Edge> edges, List<Vertex> nodes,List <List<Edge>> backupPaths,List <List<Edge>> primaryPaths,List<requestVector> r ){
    	this.nodes=new ArrayList<Vertex>();
    	this.edges= new ArrayList<Edge>(edges);
    	this.nodes=nodes;
    	this.primaryPaths=primaryPaths;
    	this.backupPaths=backupPaths;
    	this.r1=r;
	}
	
	private boolean checkLinkJoint(int i, List<Edge> altEdgePath){
		int k1=0,k2=0;
		
		for (k1=0; k1 < primaryPaths.get(i).size();++k1 ){
			for (k2=0;k2<altEdgePath.size();++k2){
				if (altEdgePath.get(k2).getId()== primaryPaths.get(i).get(k1).getId()){
					return true;
				}
			}
		}
		
		return false;
		
	}
	private boolean checkLinkDisJoint(int i,int j){
		boolean flag=true;
		for (int k1=0; k1 < backupPaths.get(i).size() && flag;++k1 ){
			for (int k2=0;k2<backupPaths.get(j).size();++k2){
				if (backupPaths.get(i).get(k1).getId()== backupPaths.get(j).get(k2).getId()){
					flag=false;
					break;
				}
			}
		}
		return flag;
	}
	
	public void execute(){
		int source=0,destination=0,oldCost=0,newCost=0;
		boolean linkDisJoint,linkJoint;
	//	Edge edg1;
		List<Edge> primaryListEdges;
		List<Edge> modifiedListEdges = new ArrayList<Edge>(edges);
		List<Edge> altEdgePath = new ArrayList<Edge>();
		
		for (int i=0;i<backupPaths.size()-1;++i){
			source=r1.get(i).source;
			destination=r1.get(i).destination;

			for (Edge e1:backupPaths.get(i))
				oldCost+=e1.getWeight();
			for (int j=i+1;j<backupPaths.size();++j){
				linkDisJoint = checkLinkDisJoint(i,j);
				if (!linkDisJoint){
					primaryListEdges = new ArrayList<Edge>(primaryPaths.get(j));
					for (Edge ed1: edges)
						ed1.setWeight(1);
					
					modifiedListEdges.removeAll(primaryListEdges);
					
					executeDijkstraAlgorithm exeDijs1 = new executeDijkstraAlgorithm ();
			        altEdgePath=exeDijs1.execute(nodes,modifiedListEdges,new Graph(nodes,modifiedListEdges),source,destination);
			        if (altEdgePath == null) { //no path exists
			        	return;
			        }
			        else {
			        	for (Edge e1:altEdgePath)
			        		newCost+=e1.getWeight();
			        	
			        	if (newCost<oldCost){
			        		if (altEdgePath.size()==0)
			        			return;
			        		//if ((altEdgePath.get(altEdgePath.size()-1).getId()== "Edge_43") ||
			        		//	(altEdgePath.get(altEdgePath.size()-1).getId()== "Edge_44") ||
			        		//	(altEdgePath.get(altEdgePath.size()-1).getId()== "Edge_45") ||
			        		//	(altEdgePath.get(altEdgePath.size()-1).getId()== "Edge_46"))
			        		//	return;
			        		linkJoint=false;
			        		linkJoint = checkLinkJoint(i,altEdgePath);
			        		if (linkJoint)
			        			return;
			        		primaryPaths.remove(i);
			        		primaryPaths.add(i, altEdgePath);
			        	}
			        }	
				}
			}
		}
				
	}

	
}
