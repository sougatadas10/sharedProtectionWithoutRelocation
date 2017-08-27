package com.dimension.spr;

public class requestVector {

	
		int source,destination,delayTime,requestNumber,status;
		long startTime;
		
		public requestVector(int source,int destination,long startTime,int delayTime,int requestNumber ){
			this.source=source;
			this.destination=destination;
			this.startTime=startTime;
			this.delayTime=delayTime;
			this.requestNumber=requestNumber;
			status=0;
		}
}
