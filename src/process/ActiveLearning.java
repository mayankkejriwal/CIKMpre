package process;

import java.util.*;
import java.io.*;

import evaluateHomo.ImportGoldStandard;


public class ActiveLearning {

	
	//sort in ascending order of |2p-1|
	public static void printSortedFile(String input, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(input));
		ArrayList<String> records=new ArrayList<String>();
		HashMap<Double,HashSet<Integer>> rec=new HashMap<Double,HashSet<Integer>>();
		int count=0;
		while(in.hasNextLine()){
			String r=in.nextLine();
			records.add(r);
			String[] p=r.split("\t");
			String[] p1=p[1].split(" ");
			double d1=Double.parseDouble(p1[0]);
			double d2=Double.parseDouble(p1[1]);/*
			if(!rec.containsKey(Math.abs(d1-d2)))
				rec.put(Math.abs(d1-d2), new HashSet<Integer>());
			rec.get(Math.abs(d1-d2)).add(count);*/
			
			if(!rec.containsKey(d1/d2))
				rec.put(d1/d2, new HashSet<Integer>());
			rec.get(d1/d2).add(count);
			count++;
		}
		
		in.close();
		ArrayList<Double> keys=new ArrayList<Double>(rec.keySet());
		Collections.sort(keys);
		PrintWriter out=new PrintWriter(new File(output));
		for(int j=keys.size()-1; j>=0; j--)
			for(int i:rec.get(keys.get(j)))
				out.println(records.get(i));
		out.close();
	}
	
	public static void makeFiles(String TopFile, int TotalPos, int TotalNeg, ImportGoldStandard gold,String output_pos, String output_neg)throws IOException{
		ArrayList<String> ALpos=new ArrayList<String>();
		ArrayList<String> ALneg=new ArrayList<String>();
		
		
		
		Scanner in2=new Scanner(new FileReader(TopFile));
		ArrayList<String> recs=new ArrayList<String>();
		while(in2.hasNextLine()){
			String line=in2.nextLine();
			recs.add(line);
		}
		in2.close();
		
		for(int i=recs.size()-1; i>=0 && ALneg.size()<TotalNeg; i--){
			ALneg.add(recs.get(i));
		}
		
		for(int i=0; i<=recs.size()-1 && ALpos.size()<TotalPos; i++){
			ALpos.add(recs.get(i));
		}
		
		PrintWriter out1=new PrintWriter(new File(output_pos));
		for(String a:ALpos)
			out1.println(a);
		out1.close();
		
		PrintWriter out2=new PrintWriter(new File(output_neg));
		for(String a:ALneg)
			out2.println(a);
		out2.close();
	}
	
	public static void main(String[] args)throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/dataC5000/";
		//printSortedFile(prefix+"probs_11000_unsup",prefix+"probs_11000_unsup_TopSorted");
		ImportGoldStandard g=new ImportGoldStandard(prefix+"dataC5000.csv",prefix+"GoldStandard",1);
		makeFiles(prefix+"probs_11000_unsup_TopSorted",500,1000,g,prefix+"500",prefix+"1000");
	}
}
