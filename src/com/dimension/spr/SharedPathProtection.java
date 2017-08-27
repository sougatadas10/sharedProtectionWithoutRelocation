package com.dimension.spr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.Random;



public class SharedPathProtection {
	
	private static final String FILENAME = "./request.txt";
    int source,destination,primaryWavelengths=0,backupWavelengths=0,sharedPrimaryWavelengths=0,sharedBackupWavelengths=0;
    int numberOfRequests=0;
	volatile List<requestVector> rv = new ArrayList<requestVector>();
	volatile List<requestVector> rvbkp = new ArrayList<requestVector>();
	List <List<Edge>> primaryPaths = new ArrayList<List<Edge>>();
	List <List<Edge>> backupPaths = new ArrayList<List<Edge>>();
	static Graph graph;
	static List<Vertex> nodes;
	static List<Edge> edges;
	FileReader fr=null;
	BufferedReader br=null;
	

	 
	 private static void addLane(String laneId, int sourceLocNo, int destLocNo,int duration) {
			Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration );
	        edges.add(lane);
		}
	
	private void display(){
		int i;
		requestVector r1;

		System.out.println("====================================================================================");
		System.out.println("The request vectors are:");
		//System.out.println("i="+i);
		for (i=0;i<rv.size();++i){
			r1=rv.get(i);
			System.out.print("source=" + r1.source+"\t");
			System.out.print("destination=" + r1.destination+"\t");
			System.out.print("startTime=" + r1.startTime+"\t");
			System.out.print("delayTime=" + r1.delayTime+"\t");
			System.out.print("requestNumber=" + r1.requestNumber+"\t");
			System.out.print("status=" + r1.status+"\t");
			System.out.println("");
		}
		/**
		System.out.println("OLD OUT");
		for (i=0;i<rvbkp.size();++i){
			r1=rvbkp.get(i);
			System.out.print("source=" + r1.source+"\t");
			System.out.print("destination=" + r1.destination+"\t");
			System.out.print("startTime=" + r1.startTime+"\t");
			System.out.print("delayTime=" + r1.delayTime+"\t");
			System.out.print("requestNumber=" + r1.requestNumber+"\t");
			System.out.print("status=" + r1.status+"\t");
			System.out.println("");
		}
		*/
		System.out.println("====================================================================================");
		System.out.println("The primary paths are:");
		for (i=0;i<primaryPaths.size();++i){
			for (Edge e1:primaryPaths.get(i))
				System.out.print(e1.getId()+"\t");
			System.out.println("");
		}

		System.out.println("====================================================================================");
		System.out.println("The backup paths are:");
		for (i=0;i<backupPaths.size();++i){
			for (Edge e1:backupPaths.get(i))
				System.out.print(e1.getId()+"\t");
			System.out.println("");
		}
		System.out.println("====================================================================================");
		
		System.out.println("primaryWavelengths="+primaryWavelengths+"backupWavelengths="+backupWavelengths+
				"sharedPrimaryWavelengths="+sharedPrimaryWavelengths+"sharedBackupWavelengths="+sharedBackupWavelengths);
	}
	
	private void calcTotalWavelengths() {
		int i1=0,j1=0;
		Edge tempEdge;
		boolean flag;
		List<Edge> tempEdgeList1=new ArrayList<Edge>();
		List<Edge> tempEdgeList2=new ArrayList<Edge>();
		
		//primaryWavelengths=0;
		//backupWavelengths=0;
		sharedPrimaryWavelengths=0;
		sharedBackupWavelengths=0;
		
		if (numberOfRequests==1){
			sharedPrimaryWavelengths=primaryPaths.get(0).size();
			sharedBackupWavelengths=backupPaths.get(0).size();
			return;
		}
		//for (i1=0;i1<primaryPaths.size();++i1){
		//	primaryWavelengths=primaryWavelengths+primaryPaths.get(i1).size();
		//}
		//primaryWavelengths=primaryWavelengths+primaryPaths.get(numberOfRequests-1).size();
		
		if (numberOfRequests >= 1){
			for (i1=0;i1<primaryPaths.size();++i1){
				for (j1=0;j1<primaryPaths.get(i1).size();++j1){
					tempEdge=new Edge(primaryPaths.get(i1).get(j1).getId(),primaryPaths.get(i1).get(j1).getSource(),
						primaryPaths.get(i1).get(j1).getDestination(),primaryPaths.get(i1).get(j1).getWeight());
					flag=false;
					if (tempEdgeList1.size()>0){
						for (Edge e2:tempEdgeList1){
							if (e2.getId()==tempEdge.getId())
								flag=true;
						}
					}	
					else
						tempEdgeList1.add(tempEdge);
					if (!flag)
						tempEdgeList1.add(tempEdge);		
				}
			}
		}
		
		
		//sharedPrimaryWavelengths=sharedPrimaryWavelengths+(primaryWavelengths-tempEdgeList1.size());
		sharedPrimaryWavelengths=tempEdgeList1.size();
		
		//for (i1=0;i1<backupPaths.size();++i1){
		//	backupWavelengths=backupWavelengths+backupPaths.get(i1).size();
		//}
		//backupWavelengths=backupWavelengths+backupPaths.get(numberOfRequests-1).size();
		
		if (numberOfRequests > 1){
			for (i1=0;i1<backupPaths.size();++i1){
				for (j1=0;j1<backupPaths.get(i1).size();++j1){
					tempEdge=new Edge(backupPaths.get(i1).get(j1).getId(),backupPaths.get(i1).get(j1).getSource(),
							backupPaths.get(i1).get(j1).getDestination(),backupPaths.get(i1).get(j1).getWeight());
					flag=false;
					if (tempEdgeList2.size()>0){
						for (Edge e2:tempEdgeList2){
							if (e2.getId()==tempEdge.getId())
								flag=true;
						}
					}
					else 
						tempEdgeList2.add(tempEdge);
					if (!flag)
						tempEdgeList2.add(tempEdge);
					}
				} 
			}
		//sharedBackupWavelengths=sharedBackupWavelengths+(backupWavelengths-tempEdgeList2.size());
		sharedBackupWavelengths=tempEdgeList2.size();
	}
	
	private void removeRequest(){
		long presentTime=System.currentTimeMillis();
		requestVector r1=null;
		
		for (int index=0;index<rv.size();++index){
			r1=rv.get(index);
			if (r1.startTime+r1.delayTime<presentTime){
				if (r1.status==1){
					rv.remove(index);
					primaryPaths.remove(index);
					backupPaths.remove(index);
				}
			}
		}

	}
	
	public void generateRequest()throws InterruptedException{
		
		//Random rand = new Random();
		String str;
		String[] arrString;
		//long startTime=0;
		//int delayTime=0;
		
		synchronized(this) {
            while (numberOfRequests<=1000){
            	if (numberOfRequests<=0){
            		try {
            			fr = new FileReader(FILENAME);
            			br = new BufferedReader(fr);
            			}
            		catch (IOException e) {
            			e.printStackTrace();
            			}
            	} 

            	try {
					str=br.readLine();
					if (str==null){
			           System.out.println("***************End of Program******************************");
		            	System.exit(0);
					}
					else {
						arrString = str.split(" ");
						source = Integer.parseInt(arrString[0]);
						destination= Integer.parseInt(arrString[1]);
						//startTime=Long.parseLong(arrString[2]);
						//delayTime=Integer.parseInt(arrString[3]);
						numberOfRequests=Integer.parseInt(arrString[4]);
					}
						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//numberOfRequests = numberOfRequests +1;
            	//if (numberOfRequests==6){
            	//	System.out.println("***************End of Program******************************");
            	//	System.exit(0);
            	//}
            		
            	System.out.println("In Thread1: Start generating Request: " +numberOfRequests );
            	//source = rand.nextInt(10) + 4;
            	//destination = rand.nextInt(4) + 0;
            	//destination = 14;
            	
                long startTime=System.currentTimeMillis();
                double tempDelayTime=startTime%10000;
                int delayTime;
                if (Double.isInfinite(tempDelayTime))
                	delayTime=0;
                else
                	delayTime = (int) tempDelayTime;
                rv.add(new requestVector(source,destination,startTime,(int) Math.exp((delayTime+200)/1000),numberOfRequests));
                rvbkp.add(new requestVector(source,destination,startTime,(int) Math.exp((delayTime+200)/1000),numberOfRequests));
            	if (numberOfRequests>0)
            		notify();
            	wait();
                }
            }
		return;
	}
	
	public void serveRequest()throws InterruptedException {
    	List<Edge> edgePath1 = new ArrayList<Edge>();
    	List<Edge> edgePath2 = new ArrayList<Edge>();
		List <List<Edge>> linkDisJointPaths = new ArrayList<List<Edge>>();
		
    
		synchronized(this){
            while (true){
            	if (numberOfRequests>0){
            		SuurabelleAlgo s1 = new SuurabelleAlgo(nodes, edges,graph);
        			linkDisJointPaths=s1.execute(source, destination);
        			if (linkDisJointPaths.get(0).size() <= linkDisJointPaths.get(1).size()){
        				edgePath1 = linkDisJointPaths.get(0);
        				edgePath2 = linkDisJointPaths.get(1);
        			}
        			else {
        				edgePath1 = linkDisJointPaths.get(1);
        				edgePath2 = linkDisJointPaths.get(0);
        			}
        			//Resetting the weight after S algo is done
        			for (Edge ed1: edges)
        				ed1.setWeight(1);
        			
        	      	primaryPaths.add(edgePath1);

        	      	IncreaseSharingBackupMain isb1 = new IncreaseSharingBackupMain(edges,nodes,edgePath1,edgePath2,source,destination,backupPaths);
        	      	isb1.execute();

        	      	IncreaseSharingPrimary isp1 = new IncreaseSharingPrimary(edges,nodes,backupPaths,primaryPaths,rv);
        			if (primaryPaths.size()>1)
        				isp1.execute();
        			
        			//System.out.println("Before deletion");
        			//display();
        			
        			//int k1=primaryPaths.size()-1;
        			//int k2=primaryPaths.get(k1).size()-1;
	        		//if ((primaryPaths.get(k1).get(k2).getId()== "Edge_43") ||
	        		//	(primaryPaths.get(k1).get(k2).getId()== "Edge_44") ||
	        		//	(primaryPaths.get(k1).get(k2).getId()== "Edge_45") ||
	        		//	(primaryPaths.get(k1).get(k2).getId()== "Edge_46"))
	        		//	primaryPaths.get(k1).remove(k2);
        			
        			rv.get(rv.size()-1).status=1;
        			
        			calcTotalWavelengths();
        			//System.out.println("After deletion"+k);
        			display();
        			removeRequest();
        			
                	notify();
                	wait();

            		}
            	}
            }
                //return;
		}
	
	public static void main(String[] args) throws InterruptedException,IOException{
		
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		for (int i = 0; i < 15; i++) {
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
		
		//addLane("Edge_43", 0, 14, 1);
		//addLane("Edge_44", 1, 14, 1);
		//addLane("Edge_45", 2, 14, 1);
		//addLane("Edge_46", 3, 14, 1);
		
		
		graph = new Graph(nodes, edges);
		
		final SharedPathProtection SPR1=new SharedPathProtection();
		
		Thread t1;
        t1 = new Thread(new Runnable() {
         @Override
       
         public void run(){
                 
                 try{
                        // Thread.sleep(10000);
                         SPR1.generateRequest();
                         }
                 catch(InterruptedException e){
                         e.printStackTrace();
                         }
                 }
         });
		
		Thread t2;
        t2 = new Thread(new Runnable(){
         @Override
         public void run(){
                 try {
                         Thread.sleep(10000);
                         SPR1.serveRequest();
                         }
                 catch(InterruptedException e){
                         e.printStackTrace();
                         }
                 }
         });
		// Start both threads
		t1.start();
		t2.start();
		
		// t1 finishes before t2
		t1.join();
		t2.join();
		

	}

}
