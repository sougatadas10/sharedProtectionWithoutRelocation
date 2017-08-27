package com.dimension.spr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


class ReadRequestVector1 {
	

	public String execute(BufferedReader br) {
		String curStr;
		try {
			if ((curStr=br.readLine()) != null)
				return (curStr);
			else 
				return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}



}

public class ReadRequestVector {
	
	private static final String FILENAME = "./request.txt";
	public static void main (String[] args){
		FileReader fr=null;
		BufferedReader br=null;
		ReadRequestVector1 rrv = new ReadRequestVector1();
		
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
		}
		catch (IOException e) {
			e.printStackTrace();
			} 
		String[] str=rrv.execute(br).split(" ");
		int source = Integer.parseInt(str[0]);
		int destination= Integer.parseInt(str[1]);
		long startTime=Long.parseLong(str[2]);
		int delayTime=Integer.parseInt(str[3]);
		int requestNumber=Integer.parseInt(str[4]);
		System.out.println(source+"\t"+destination+"\t"+startTime+"\t"+delayTime+"\t"+requestNumber);
		
		str=rrv.execute(br).split(" ");
		source = Integer.parseInt(str[0]);
		destination= Integer.parseInt(str[1]);
		startTime=Long.parseLong(str[2]);
		delayTime=Integer.parseInt(str[3]);
		requestNumber=Integer.parseInt(str[4]);
		System.out.println(source+"\t"+destination+"\t"+startTime+"\t"+delayTime+"\t"+requestNumber);

		str=rrv.execute(br).split(" ");
		source = Integer.parseInt(str[0]);
		destination= Integer.parseInt(str[1]);
		startTime=Long.parseLong(str[2]);
		delayTime=Integer.parseInt(str[3]);
		requestNumber=Integer.parseInt(str[4]);
		System.out.println(source+"\t"+destination+"\t"+startTime+"\t"+delayTime+"\t"+requestNumber);

	}
}
