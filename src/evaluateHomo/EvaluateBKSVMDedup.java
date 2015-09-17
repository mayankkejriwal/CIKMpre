package evaluateHomo;

import java.io.*;
import java.util.*;

import features.GenerateFeaturesFile;
import general.Parameters;
import libsvm.*;

public class EvaluateBKSVMDedup {
	
		
		ImportGoldStandard gold;	//must be explicitly for 'pairs' not 'clusters'
		GenerateBlocks block_obj;
		
		private HashMap<String, HashSet<Integer>> blocks;
		
		
		ArrayList<String> tuples;
		
		
		public EvaluateBKSVMDedup(String BKFile, ArrayList<String> tuples)throws IOException{
			this.gold=null;
			this.tuples=tuples;
			
			ArrayList<String> BKs=new ArrayList<String>();
			Scanner in=new Scanner(new FileReader(BKFile));
			String[] disjuncts=null;
			if(in.hasNextLine())
				disjuncts=in.nextLine().split("\t");
			for(String disjunct:disjuncts)
				BKs.add(disjunct);
			block_obj=new GenerateBlocks(BKs, null);
			in.close();
			
			
			build_blocks();
		}
		
		public EvaluateBKSVMDedup(String BKFile, ImportGoldStandard gold)throws IOException{
			this.gold=gold;
			ArrayList<String> BKs=new ArrayList<String>();
			Scanner in=new Scanner(new FileReader(BKFile));
			String[] disjuncts=null;
			if(in.hasNextLine())
				disjuncts=in.nextLine().split("\t");
			for(String disjunct:disjuncts)
				BKs.add(disjunct);
			block_obj=new GenerateBlocks(BKs, null);
			in.close();
			tuples=gold.data.getTuples();
			
			build_blocks();
		}
		
		private void build_blocks(){
			
			blocks=new HashMap<String,HashSet<Integer>>();
			for(int i=0; i<tuples.size(); i++){
				block_obj.setLine(tuples.get(i),true);
				//System.out.println(tuples.get(i));
				
				for(int j=0; j<block_obj.num_clauses; j++){
					
					HashSet<String> bks=block_obj.block(j);
					if(bks==null)
						continue;
					for(String bk:bks)
						if(bk==null)
							continue;
						else if(blocks.containsKey(bk))
							blocks.get(bk).add(i);
						else{
							blocks.put(bk, new HashSet<Integer>());
							blocks.get(bk).add(i);
						}
				}
				
			}
			
			
			
			
			
			System.out.println("Num blocks: "+blocks.keySet().size());
			
		}
		
		public HashMap<Integer,HashSet<Integer>> return_pairs(){
			HashMap<Integer, HashSet<Integer>> pairs=new HashMap<Integer, HashSet<Integer>>();
			for(String bk: blocks.keySet())
				{
				if(blocks.get(bk).size()*0.5*(blocks.get(bk).size()-1)>Parameters.maxpairs)
					continue;
				
				ArrayList<Integer> list=new ArrayList<Integer>(blocks.get(bk));
				
				
				for(int i=0; i<list.size()-1; i++)
					for(int j=i+1; j<list.size(); j++)
						if(pairContains(pairs,list.get(i),list.get(j))==1){
							pairs.get(list.get(i)).add(list.get(j));
						}
						else if(pairContains(pairs,list.get(i),list.get(j))==0){
							pairs.put(list.get(i),new HashSet<Integer>());
							pairs.get(list.get(i)).add(list.get(j));
						}
			}
			System.out.println("Done generating Pairs");
			return pairs;
		}
		
		
		private void cleanPairs(HashMap<Integer,HashSet<Integer>> map){
			for(int i:map.keySet()){
				for(int j:map.get(i))
					if(map.containsKey(j))
						if(map.get(j).contains(i))
							map.get(j).remove(i);
			}
			//System.exit(-1);
		}
		
		//will print Pairs Completeness (recall) and Reduction Ratio
		//we will adopt the standard defn of reduction ratio, so pairs are only counted once
		public void print_metrics(){
			HashMap<Integer, HashSet<Integer>> pairs=return_pairs();
			cleanPairs(pairs);
			int total=gold.total_pairs;
			int truepos=gold.num_dups;
			
			int count=0;
			for(int i:pairs.keySet())
				for(int j:pairs.get(i))
					if(gold.contains(i,j))
						count++;
			System.out.println("Pairs Completeness\tReduction Ratio");
			System.out.println((double) count/truepos+"\t"+(1.0-(double) countHashMap(pairs)/total));
			
		}
		
		//will print Precision and recall
		public void print_metrics_svm(String svm_model, String output)throws IOException{
			PrintWriter out=new PrintWriter(new File(output));
			
			HashMap<Integer, HashSet<Integer>> pairs=return_pairs();
			cleanPairs(pairs);
			
			svm_model m=svm.svm_load_model(svm_model);
			
			for(int i:pairs.keySet())
				for(int j:pairs.get(i)){
					double[] prob=predictFromModel(m,
							GenerateFeaturesFile.Homo.getSVMFeatures
							(tuples.get(i), tuples.get(j)));
			out.println(i+" "+j+"\t"+prob[0]+" "+prob[1]);
					
				}
			out.close();
			
		}
		
		//will print Precision and recall
				public void print_metrics_svm_multiple(String[] svm_model, String[] output)throws IOException{
					//PrintWriter out=new PrintWriter(new File(output));
					
					HashMap<Integer, HashSet<Integer>> pairs=return_pairs();
					cleanPairs(pairs);
					
					svm_model[] m=new svm_model[svm_model.length];
					PrintWriter[] out=new PrintWriter[svm_model.length];
					
					for(int s1=0; s1<svm_model.length; s1++){
						m[s1]=svm.svm_load_model(svm_model[s1]);
						out[s1]=new PrintWriter(new File(output[s1]));
					}
					
					for(int i:pairs.keySet())
						for(int j:pairs.get(i))
							
							for(int s1=0;s1<m.length; s1++){
								double[] prob=predictFromModel(m[s1],
									GenerateFeaturesFile.Homo.getSVMFeatures
									(tuples.get(i), tuples.get(j)));
								out[s1].println(i+" "+j+"\t"+prob[0]+" "+prob[1]);
							
							}
					
					for(int s1=0; s1<svm_model.length; s1++)
						out[s1].close();
					
				}
		
		private double[] predictFromModel(svm_model m, ArrayList<Double> feat){
			svm_node[] n=new svm_node[feat.size()];
			for(int i=0; i<feat.size(); i++){
				n[i]=new svm_node();
				n[i].index=i+1;
				n[i].value=feat.get(i);
			}
			double[] prob=new double[2];
			svm.svm_predict_probability(m,n,prob);
			
			return prob;
			
		}

		public double[] return_metrics(){
			HashMap<Integer, HashSet<Integer>> pairs=return_pairs();
			int total=gold.total_pairs;
			int truepos=gold.num_dups;
			double[] res=new double[2];
			int count=0;
			for(int i:pairs.keySet())
				for(int j:pairs.get(i))
				if(gold.contains(i,j))
					count++;
			//System.out.println("Pairs Completeness\tReduction Ratio");
			//System.out.println((double) count/truepos+"\t"+(1.0-(double) countHashMap(pairs)/total));
			res[0]=(double) 1.0*count/truepos;
			res[1]=(1.0-(double) countHashMap(pairs)/total);
			return res;
		}
		
		private int countHashMap(HashMap<Integer, HashSet<Integer>> pairs){
			int count=0;
			for(int i:pairs.keySet()){
				count+=pairs.get(i).size();
			}
			return count;
		}
		
		
		//returns 2 if pair is present, 1 if only k1 is present, 0 if neither
		private int pairContains(HashMap<Integer,HashSet<Integer>> pairs, int k1, int k2){
			
			
				if(pairs.containsKey(k1))
					if(pairs.get(k1).contains(k2))
						return 2;
					else
						return 1;
			
			return 0;
		}
		
		public void randomlyPrintBlock(){
			
			ArrayList<String> tuples=null;
			
				
				tuples=gold.data.getTuples();
			
			for(String bk: blocks.keySet()){
				
				for(int tuple:blocks.get(bk))
					System.out.println(tuples.get(tuple));
				System.out.println("Key: "+bk);
				//System.exit(0);
			}
		}
		
		public static void main(String[] args)throws IOException{
				census5000_BK();
			
		}
		
		public static void restaurants_BK()throws IOException{
			String prefix="/host/heteroDatasets/cikm_experiments/restaurant/";
			String records=new String(prefix+"restaurant.csv");
			String goldfile=new String(prefix+"GoldStandard.csv");
			ImportGoldStandard gold=new ImportGoldStandard(records,goldfile,0);
			
			EvaluateBKSVMDedup p=new EvaluateBKSVMDedup(prefix+"BK",gold);
			//p.print_metrics();
			p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}
		
		public static void cora_BK()throws IOException{
			String prefix="/host/heteroDatasets/cikm_experiments/cora/";
			String records=new String(prefix+"cora.csv");
			String goldfile=new String(prefix+"GoldStandard");
			ImportGoldStandard gold=new ImportGoldStandard(records,goldfile,1);
			
			EvaluateBKSVMDedup p=new EvaluateBKSVMDedup(prefix+"BK",gold);
			p.print_metrics();
			
			//String[] models={prefix+"svm_model_1720_1000",prefix+"svm_model_8600_1000",prefix+"svm_model_8600_1000_unsup",prefix+"svm_model_7510_1000_unsup"};
			//String[] probs={prefix+"probs_1720_1000",prefix+"probs_8600_1000",prefix+"probs_8600_1000_unsup",prefix+"probs_7510_1000_unsup"};
			//p.print_metrics_svm(prefix+"svm_model_1720_1000",prefix+"probs_1720_1000");
			//p.print_metrics_svm_multiple(models,probs);
		}
		
		public static void census5000_BK()throws IOException{
			String prefix="/host/heteroDatasets/cikm_experiments/dataC5000/";
			String records=new String(prefix+"dataC5000.csv");
			String goldfile=new String(prefix+"GoldStandard");
			ImportGoldStandard gold=new ImportGoldStandard(records,goldfile,1);
			
			EvaluateBKSVMDedup p=new EvaluateBKSVMDedup(prefix+"BK",gold);
			//String[] models={prefix+"svm_model_732_1000",prefix+"svm_model_3660_1000",prefix+"svm_model_3660_unsup",prefix+"svm_model_11000_unsup"};
			//String[] probs={prefix+"probs_732_1000",prefix+"probs_3660_1000",prefix+"probs_3660_unsup",prefix+"probs_11000_unsup"};
			//p.print_metrics_svm_multiple(models,probs);
			p.print_metrics_svm(prefix+"svm_model_500_1000",prefix+"probs_AL_11000_500_1000");
			
		};
		
	}


