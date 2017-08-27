package com.dimension.spr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class WriteRequestVector1 {

	private static final String FILENAME = "./request.txt";

	public void execute() {

		int source,destination;
		int delayTime;
		long startTime;
		double tempDelayTime;
		String request;
		FileWriter fw=null;
		BufferedWriter bw=null;
		Random rand = new Random();
		
		try {
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
		}
		catch (IOException e) {
			e.printStackTrace();
			} 

		for (int i=0; i<1000;++i){
			try {
				source = rand.nextInt(10) + 4;
				destination = rand.nextInt(4) + 0;
				startTime=System.currentTimeMillis();
				tempDelayTime=startTime%10000;
				
				if (Double.isInfinite(tempDelayTime))
					delayTime=0;
				else
					delayTime = (int) tempDelayTime;
				
				request=source+" "+destination+" "+ startTime+" "+ delayTime+" "+(i+1);
				
				bw.write(request);
				bw.write("\n");
				System.out.println("Done");
				
			} 
			catch (IOException e) {
				e.printStackTrace();
				} 
		}
		
		
		try {
			if (bw != null)
				bw.close();

			if (fw != null)
				fw.close();

			} 
			catch (IOException ex) {
				ex.printStackTrace();
			}
	}	
}

public class WriteRequestVector{
	
	public static void main (String[] args){
		WriteRequestVector1 wrv = new WriteRequestVector1();
		wrv.execute();
	}
}
