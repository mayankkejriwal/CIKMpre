package evaluateHomo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import christen.TFIDF;

public class ImportGoldStandard {

	HashMap<Integer,Integer> duplicate_records; //no transitive closure complications allowed: only disjoint duplicates!
	ArrayList<HashSet<Integer>> clusters;	//we don't do an index thing here
	public int num_dups=0; //the total number of duplicate pairs present
	public int total_pairs=0;
	public TFIDF data;
	boolean cluster_gold;
	//For TFIDF, we assume schema line is missing. 
	   // if opt is 1, then clustering otherwise ordinary pairing
		public  ImportGoldStandard(String dataset, String goldfile, int opt) throws IOException{
			data=new TFIDF(dataset, false);
			total_pairs=data.corpussize*(data.corpussize-1)/2;
			Scanner in=new Scanner(new File(goldfile));	//the gold standard file
			
			if(opt==1){
				cluster_gold=true;
				clusters=new ArrayList<HashSet<Integer>>();
				while(in.hasNextLine()){
					String[] d=in.nextLine().split(" ");
					HashSet<Integer> tmp=new HashSet<Integer>();
					for(int i=0; i<d.length; i++)
						tmp.add(Integer.parseInt(d[i]));
					clusters.add(tmp);
				}
				for(int i=0; i<clusters.size(); i++)
					num_dups+=(clusters.get(i).size()*(clusters.get(i).size()-1)*0.5);
			}
			else{
				cluster_gold=false;
				duplicate_records=new HashMap<Integer,Integer>();
				while(in.hasNextLine()){
					String[] d=in.nextLine().split(" ");
					duplicate_records.put(Integer.valueOf(d[0]), Integer.valueOf(d[1]));
				}
				num_dups=duplicate_records.size();
			}
			
			
			in.close();
		}
		
		public boolean contains(int i, int j){
			if(!cluster_gold){
				if(duplicate_records.containsKey(i))
					if((int)duplicate_records.get(i)==j)
						return true;
					else return false;
				else if(duplicate_records.containsKey(j))
					if((int)duplicate_records.get(j)==i)
						return true;
					else return false;
				else
					return false;
			}
			else{
				for(int p=0; p<clusters.size(); p++)
					if(clusters.get(p).contains(i)&&clusters.get(p).contains(j))
						return true;
				return false;
			}
		}
		
		public HashMap<Integer,Integer> retRandomDups(int num){
			if(cluster_gold)
				System.out.println("error! Wrong function retRandomDups called");
			if(num>duplicate_records.keySet().size()){
				return duplicate_records;
			}
			HashMap<Integer,Integer> res=new HashMap<Integer,Integer>();
			Random p=new Random(System.currentTimeMillis());
			ArrayList<Integer> q=new ArrayList<Integer>(duplicate_records.keySet());
			while(num>0){
				int d=p.nextInt(q.size());
				if(res.containsKey((q.get(d))))
					continue;
				else
					res.put(q.get(d), duplicate_records.get(q.get(d)));
				num--;
			}
			return res;
			
			
			
		}
		
		//for simplicity, might not return exactly the number called for, but will return at least that.
		public ArrayList<HashSet<Integer>> retRandomDupsCluster(int num){
			if(!cluster_gold)
				System.out.println("error! Wrong function retRandomDupsCluster called");
			if(num>num_dups){
				return clusters;
			}
			ArrayList<HashSet<Integer>> res=new ArrayList<HashSet<Integer>>();
			ArrayList<Integer> q=new ArrayList<Integer>();
			for(int i=0; i<clusters.size(); i++)
				q.add(i);
			Random p=new Random(System.currentTimeMillis());
			
			while(num>0){
				int d=p.nextInt(q.size());
				res.add(clusters.get(q.get(d)));
				num-=(clusters.get(q.get(d)).size()*(clusters.get(q.get(d)).size()-1)/2);
				q.remove((int) d);
			}
			return res;
			
			
			
		}
		
		//use for both cluster and duplicates
		public HashMap<Integer,Integer> retRandomNonDups(int num){
			
			HashMap<Integer,Integer> res=new HashMap<Integer,Integer>();
			Random p=new Random(System.currentTimeMillis());
			
			while(num>0){
				int d1=p.nextInt(data.corpussize);
				int d2=p.nextInt(data.corpussize);
				if(d1==d2||contains(d1,d2))
					continue;
				else if((res.containsKey(d1)&&res.get(d1).equals(d2))||(res.containsKey(d2)&&res.get(d2).equals(d1)))
					continue;
				else
					res.put(d1,d2);
				num--;
			}
			return res;
			
		}
	
}
