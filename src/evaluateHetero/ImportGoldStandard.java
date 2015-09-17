package evaluateHetero;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import christen.TFIDF;

public class ImportGoldStandard {

	HashMap<Integer,HashSet<Integer>> duplicate_records; 
	
	public int num_dups=0; //the total number of duplicate pairs present
	public int total_pairs=0;
	public TFIDF data1;
	public TFIDF data2;
	
	//For TFIDF, we assume schema line is missing. 
	   // if opt is 1, then clustering otherwise ordinary pairing
		public  ImportGoldStandard(String dataset1, String dataset2, String goldfile) throws IOException{
			data1=new TFIDF(dataset1, false);
			data2=new TFIDF(dataset2, false);
			total_pairs=data1.corpussize*data2.corpussize;
			Scanner in=new Scanner(new File(goldfile));	//the gold standard file
			
			
				
				duplicate_records=new HashMap<Integer,HashSet<Integer>>();
				while(in.hasNextLine()){
					String[] d=in.nextLine().split(" ");
					int i1=Integer.valueOf(d[0]);
					if(!duplicate_records.containsKey(i1))
						duplicate_records.put(i1,new HashSet<Integer>());
					duplicate_records.get(i1).add(Integer.valueOf(d[1]));
				}
				in.close();
				
				for(int i:duplicate_records.keySet())
					num_dups+=duplicate_records.get(i).size();
		}
		
		public boolean contains(int i, int j){
			
				if(duplicate_records.containsKey(i))
					if(duplicate_records.get(i).contains(j))
						return true;
					
				
				return false;
			
			
		}
		
		public HashMap<Integer,HashSet<Integer>> retRandomDups(int num){
			
			if(num>duplicate_records.keySet().size()){
				return duplicate_records;
			}
			HashMap<Integer,HashSet<Integer>> res=new HashMap<Integer,HashSet<Integer>>();
			Random p=new Random(System.currentTimeMillis());
			ArrayList<Integer> q=new ArrayList<Integer>(duplicate_records.keySet());
			while(num>0){
				int d=p.nextInt(q.size());
				if(res.containsKey((q.get(d))))
					continue;
				else
					res.put(q.get(d), duplicate_records.get(q.get(d)));
				num-=duplicate_records.get(q.get(d)).size();
			}
			return res;
			
			
			
		}
		
		//careful
		public HashMap<Integer,Integer> retRandomNonDups(int num){
			
			HashMap<Integer,Integer> res=new HashMap<Integer,Integer>();
			Random p=new Random(System.currentTimeMillis());
			
			while(num>0){
				int d1=p.nextInt(data1.corpussize);
				int d2=p.nextInt(data2.corpussize);
				if(d1==d2||contains(d1,d2))
					continue;
				else if((res.containsKey(d1)&&res.get(d1).equals(d2)))
					continue;
				else
					res.put(d1,d2);
				num--;
			}
			return res;
			
		}
	
}
