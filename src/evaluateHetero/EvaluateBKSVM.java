package evaluateHetero;

import java.io.*;
import java.util.*;

import features.GenerateFeaturesFile;
import features.HeteroBKSVM;
import general.CSVParser;
import general.Parameters;
import libsvm.*;

public class EvaluateBKSVM {
	
		
		ImportGoldStandard gold;	//must be explicitly for 'pairs' not 'clusters'
		GenerateBlocks block_obj;
		
		private HashMap<String, HashSet<Integer>> blocks1;
		private HashMap<String, HashSet<Integer>> blocks2;
		
		ArrayList<String> tuples1;
		ArrayList<String> tuples2;
		
		int att1;
		int att2;
		
		
		public EvaluateBKSVM(String BKFile, ArrayList<String> tuples1, ArrayList<String> tuples2)throws IOException{
			this.gold=null;
			this.tuples1=tuples1;
			this.tuples2=tuples2;
			att1=(new CSVParser()).parseLine(tuples1.get(0)).length;
			att2=(new CSVParser()).parseLine(tuples2.get(0)).length;
			
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
		
		public EvaluateBKSVM(String BKFile, ImportGoldStandard gold)throws IOException{
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
			tuples1=gold.data1.getTuples();
			tuples2=gold.data2.getTuples();
			att1=(new CSVParser()).parseLine(tuples1.get(0)).length;
			att2=(new CSVParser()).parseLine(tuples2.get(0)).length;
			build_blocks();
		}
		
		private void build_blocks(){
			
			blocks1=new HashMap<String,HashSet<Integer>>();
			for(int i=0; i<tuples1.size(); i++){
				block_obj.setLine(tuples1.get(i),true);
				//System.out.println(tuples.get(i));
				
				for(int j=0; j<block_obj.num_clauses; j++){
					
					HashSet<String> bks=block_obj.block(j);
					if(bks==null)
						continue;
					for(String bk:bks){
						if(bk==null)
							continue;
						if(!blocks1.containsKey(bk))
							blocks1.put(bk, new HashSet<Integer>());
						blocks1.get(bk).add(i);
					}
						}
				}
			
			
			
			blocks2=new HashMap<String,HashSet<Integer>>();
			for(int i=0; i<tuples2.size(); i++){
				block_obj.setLine(tuples2.get(i),false);
				//System.out.println(tuples.get(i));
				
				for(int j=0; j<block_obj.num_clauses; j++){
					
					HashSet<String> bks=block_obj.block(j);
					if(bks==null)
						continue;
					for(String bk:bks){
						if(bk==null)
							continue;
						if(!blocks2.containsKey(bk))
							blocks2.put(bk, new HashSet<Integer>());
						blocks2.get(bk).add(i);
					}
						}
				}
			
			
			
			System.out.println("Num blocks1: "+blocks1.keySet().size());
			System.out.println("Num blocks2: "+blocks2.keySet().size());
			
		}
		
		public HashMap<Integer,HashSet<Integer>> return_pairs(){
			HashMap<Integer, HashSet<Integer>> pairs=new HashMap<Integer, HashSet<Integer>>();
			for(String bk: blocks1.keySet())
				{
				if(!blocks2.containsKey(bk))
					continue;
				if(blocks1.get(bk).size()*blocks2.get(bk).size()>Parameters.maxpairs)
					continue;
				
				
				
				
				for(int i:blocks1.get(bk))
					for(int j: blocks2.get(bk))
						if(pairContains(pairs,i,j)==1){
							pairs.get(i).add(j);
						}
						else if(pairContains(pairs,i,j)==0){
							pairs.put(i,new HashSet<Integer>());
							pairs.get(i).add(j);
						}
			}
			System.out.println("Done generating Pairs: "+countPairs(pairs));
			return pairs;
		}
		
		private int countPairs(HashMap<Integer, HashSet<Integer>> pairs){
			int count=0;
			for(int i:pairs.keySet())
				count+=pairs.get(i).size();
			return count;
		}
		
		
		private HashMap<Integer,HashSet<Integer>> return_pairs_SN(int w){
			HashMap<Integer, HashSet<Integer>> pairs=new HashMap<Integer, HashSet<Integer>>();
			for(String bk: blocks1.keySet())
				{
				if(!blocks2.containsKey(bk))
					continue;
				
				
				
				ArrayList<Integer> block_1=new ArrayList<Integer>(blocks1.get(bk));
				ArrayList<Integer> block_2=new ArrayList<Integer>(blocks2.get(bk));
				ArrayList<Integer> block1=null;
				ArrayList<Integer> block2=null;
				if(block_1.size()>block_2.size()){
					block1=new ArrayList<Integer>(block_1.subList(0,block_2.size()));
					block2=block_2;
				}
				else if(block_2.size()>block_1.size()){
					block2=new ArrayList<Integer>(block_2.subList(0,block_1.size()));
					block1=block_1;
				}
				else{
					block1=block_1;
					block2=block_2;
				}
				
				for(int i=0; i<block1.size(); i++){
					int el1=block1.get(i);
					int j=(i-w)+1;
					if(j<0)
						j=0;
					int k=i+w;
					if(k>block1.size())
						k=block1.size();
					for(; j<k; j++){
						int el2=block2.get(j);
						if(pairContains(pairs,el1,el2)==1){
							pairs.get(el1).add(el2);
						}
						else if(pairContains(pairs,el1,el2)==0){
							pairs.put(el1,new HashSet<Integer>());
							pairs.get(el1).add(el2);
						}
					}
					
					
				}
				
				
						
			}
			System.out.println("Done generating Pairs");
			return pairs;
		}

		//will print Pairs Completeness (recall) and Reduction Ratio
		//we will adopt the standard defn of reduction ratio, so pairs are only counted once
		public void print_metrics_SN(int w){
			HashMap<Integer, HashSet<Integer>> pairs=return_pairs_SN(w);
			
			int total=gold.total_pairs;
			int truepos=gold.num_dups;
			
			int count=0;
			for(int i:pairs.keySet())
				for(int j:pairs.get(i))
					if(gold.contains(i,j))
						count++;
			System.out.println("Pairs Completeness\tReduction Ratio\tf-score");
			double pc=(double) count/truepos;
			double rr=(1.0-(double) countHashMap(pairs)/total);
			double fs=2*pc*rr/(pc+rr);
			System.out.println(pc+"\t"+rr+"\t"+fs);
			
		}
		
		//will print Pairs Completeness (recall) and Reduction Ratio
		//we will adopt the standard defn of reduction ratio, so pairs are only counted once
		public void print_metrics(){
			HashMap<Integer, HashSet<Integer>> pairs=return_pairs();
			
			int total=gold.total_pairs;
			int truepos=gold.num_dups;
			
			int count=0;
			for(int i:pairs.keySet())
				for(int j:pairs.get(i))
					if(gold.contains(i,j))
						count++;
			System.out.println("Pairs Completeness\tReduction Ratio\tf-score");
			double pc=(double) count/truepos;
			double rr=(1.0-(double) countHashMap(pairs)/total);
			double fs=2*pc*rr/(pc+rr);
			System.out.println(pc+"\t"+rr+"\t"+fs);
			
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
		
		public void print_metrics_svm(String schema, String candidate_set,String svm_model, String output)throws IOException{
			blocks1=null;
			blocks2=null;
			block_obj=null;
			
			HeteroBKSVM.column1=new ArrayList<Integer>();
			HeteroBKSVM.column2=new ArrayList<Integer>();
			
		
			
			Scanner sc=new Scanner(new FileReader(schema));
			while(sc.hasNextLine()){
				String[] p=sc.nextLine().split(" ");
				int index1=Integer.parseInt(p[0]);
				int index2=Integer.parseInt(p[1]);
				HeteroBKSVM.column1.add(index1);
				HeteroBKSVM.column2.add(index2);
						
				
			}
			
			sc.close();
			
			HashMap<Integer, HashSet<Integer>> pairs= new HashMap<Integer, HashSet<Integer>>();
			Scanner in=new Scanner(new FileReader(candidate_set));
			while(in.hasNextLine()){
						String[] l=in.nextLine().split(" ");
						int a=Integer.parseInt(l[0]);
						int b=Integer.parseInt(l[1]);
						if(!pairs.containsKey(a))
							pairs.put(a,new HashSet<Integer>());
						pairs.get(a).add(b);
					}
			in.close();
					
					
					
			svm_model m=svm.svm_load_model(svm_model);
			PrintWriter out=new PrintWriter(new File(output));
			for(int i:pairs.keySet())
						for(int j:pairs.get(i)){
							double[] prob=predictFromModel(m,
									GenerateFeaturesFile.Hetero.getSVMFeatures
									(tuples1.get(i), tuples2.get(j)));
							
					out.println(i+" "+j+"\t"+prob[0]+" "+prob[1]);
							
						}
					
			out.close();
					
		}
		
		public void print_metrics_svm_multiple(String schema, String candidate_set,String[] svm_model_array, String[] output_array)throws IOException{
			blocks1=null;
			blocks2=null;
			block_obj=null;	
			
			HeteroBKSVM.column1=new ArrayList<Integer>();
			HeteroBKSVM.column2=new ArrayList<Integer>();
			
		
			
			Scanner sc=new Scanner(new FileReader(schema));
			while(sc.hasNextLine()){
				String[] p=sc.nextLine().split(" ");
				int index1=Integer.parseInt(p[0]);
				int index2=Integer.parseInt(p[1]);
				HeteroBKSVM.column1.add(index1);
				HeteroBKSVM.column2.add(index2);
						
				
			}
			
			sc.close();
			
			HashMap<Integer, HashSet<Integer>> pairs= new HashMap<Integer, HashSet<Integer>>();
			Scanner in=new Scanner(new FileReader(candidate_set));
			while(in.hasNextLine()){
						String[] l=in.nextLine().split(" ");
						int a=Integer.parseInt(l[0]);
						int b=Integer.parseInt(l[1]);
						if(!pairs.containsKey(a))
							pairs.put(a,new HashSet<Integer>());
						pairs.get(a).add(b);
					}
			in.close();
					
			svm_model[] m=new svm_model[3];
			PrintWriter[] out=new PrintWriter[3];
			
			for(int i=0; i<=2; i++){
				m[i]=svm.svm_load_model(svm_model_array[i]);
				out[i]=new PrintWriter(new File(output_array[i]));
			}
			
			for(int i:pairs.keySet())
						for(int j:pairs.get(i))
						for(int k=0; k<=2; k++){
							double[] prob=predictFromModel(m[k],
									GenerateFeaturesFile.Hetero.getSVMFeatures
									(tuples1.get(i), tuples2.get(j)));
							
					out[k].println(i+" "+j+"\t"+prob[0]+" "+prob[1]);
							
						}
			for(PrintWriter out1:out)		
				out1.close();
					
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
		
		public void randomlyPrintBlock(int i){
			
			if(i==1){
				for(String bk: blocks1.keySet()){
				
				for(int tuple:blocks1.get(bk))
					System.out.println(tuples1.get(tuple));
				System.out.println("Key: "+bk);
			}
				}
			else{
				for(String bk: blocks2.keySet()){
					
					for(int tuple:blocks2.get(bk))
						System.out.println(tuples2.get(tuple));
					System.out.println("Key: "+bk);
				}
			}
			
		}
		
		//will print candidate set in goldStandard-like format
		public void print_candidate_set(String outfile)throws IOException{
			HashMap<Integer, HashSet<Integer>> pairs=return_pairs();
			PrintWriter out=new PrintWriter(new File(outfile));
			for(int i: pairs.keySet())
				for(int j: pairs.get(i))
					out.println(i+" "+j);
			out.close();
			
						
		}
		
		private static void populateArrays(String prefix, String[] svmModelArray, String[] svmOutputArray){
			svmModelArray[0]=prefix+"svmModel";
			svmModelArray[1]=prefix+"svmModelSup10";
			svmModelArray[2]=prefix+"svmModelSup50";
			
			svmOutputArray[0]=prefix+"svmOutput";
			svmOutputArray[1]=prefix+"svmOutput10";
			svmOutputArray[2]=prefix+"svmOutput50";
		}

		public static void PR_BK()throws IOException{
			String prefix="/host/heteroDatasets/icde_experiments/PR/";
			String records1=new String(prefix+"restaurant1.csv");
			String records2=new String(prefix+"restaurant2.csv");
			String goldfile=new String(prefix+"goldStandard");
			ImportGoldStandard gold=new ImportGoldStandard(records1,records2,goldfile);
			
			EvaluateBKSVM p=new EvaluateBKSVM(prefix+"BK",gold);
			p.print_metrics();
			//p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}

		public static void Persons_BK()throws IOException{
			String prefix="/host/heteroDatasets/icde_experiments/Persons/";
			String records1=new String(prefix+"person1.csv");
			String records2=new String(prefix+"person2.csv");
			String goldfile=new String(prefix+"goldStandard");
			ImportGoldStandard gold=new ImportGoldStandard(records1,records2,goldfile);
			
			EvaluateBKSVM p=new EvaluateBKSVM(prefix+"BK",gold);
			p.print_metrics();
			//p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}

		public static void game3_BK()throws IOException{
			String prefix="/host/heteroDatasets/icde_experiments/game3/";
			String records1=new String(prefix+"ibm.csv");
			String records2=new String(prefix+"vgchartz.csv");
			String goldfile=new String(prefix+"goldStandard_ibm_vgchartz");
			ImportGoldStandard gold=new ImportGoldStandard(records1,records2,goldfile);
			
			EvaluateBKSVM p=new EvaluateBKSVM(prefix+"BK",gold);
			p.print_metrics();
			//p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}

		public static void game2_BK()throws IOException{
			String prefix="/host/heteroDatasets/icde_experiments/game2/";
			String records1=new String(prefix+"ibm.csv");
			String records2=new String(prefix+"dbpedia.csv");
			String goldfile=new String(prefix+"goldStandard_ibm_dbpedia");
			ImportGoldStandard gold=new ImportGoldStandard(records1,records2,goldfile);
			
			EvaluateBKSVM p=new EvaluateBKSVM(prefix+"BK",gold);
			p.print_metrics();
			//p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}

		public static void parks1_BK()throws IOException{
			String prefix="/host/heteroDatasets/icde_experiments/parks1/";
			String records1=new String(prefix+"National_Parks_Plus.csv");
			String records2=new String(prefix+"national_park_service.csv");
			String goldfile=new String(prefix+"goldStandard_NPP_nps");
			ImportGoldStandard gold=new ImportGoldStandard(records1,records2,goldfile);
			
			EvaluateBKSVM p=new EvaluateBKSVM(prefix+"BK",gold);
			p.print_metrics();
			//p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}

		public static void libraries_BK()throws IOException{
			String prefix="/host/heteroDatasets/icde_experiments/libraries/";
			String records1=new String(prefix+"PublicLibraries.csv");
			String records2=new String(prefix+"public_libraries.csv");
			String goldfile=new String(prefix+"goldStandard_PL_pl");
			ImportGoldStandard gold=new ImportGoldStandard(records1,records2,goldfile);
			
			EvaluateBKSVM p=new EvaluateBKSVM(prefix+"BK",gold);
			p.print_metrics();
			//p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}

		public static void game1_BK()throws IOException{
			String prefix="/host/heteroDatasets/icde_experiments/game/";
			String records1=new String(prefix+"vgchartz.csv");
			String records2=new String(prefix+"dbpedia.csv");
			String goldfile=new String(prefix+"goldStandard_vgchartz_dbpedia");
			ImportGoldStandard gold=new ImportGoldStandard(records1,records2,goldfile);
			
			EvaluateBKSVM p=new EvaluateBKSVM(prefix+"BK",gold);
			p.print_metrics();
			//p.print_metrics_svm(prefix+"svm_model_2500_unsup",prefix+"probs_2500_1000_unsup");
		}

		public static void journal_BK()throws IOException{
			String prefix="/host/heteroDatasets/hyperparam-optim/d6/";
			String file1=prefix+"file1.csv";
			String file2=prefix+"file2.csv";
			String candidateSet=prefix+"candidateSet";
			String svmModel=prefix+"svmModelIter";
			String svmOutput=prefix+"svmOutputIter";
			String BK=prefix+"BK_ac"; //BK or BK_ac?
			String schemaFile=prefix+"schema_ours";
			String goldfile=new String(prefix+"goldStandard");
			ImportGoldStandard gold=new ImportGoldStandard(file1,file2,goldfile);
			String[] svmModelArray=new String[3];
			String[] svmOutputArray=new String[3];
			populateArrays(prefix, svmModelArray, svmOutputArray);
			EvaluateBKSVM p=new EvaluateBKSVM(BK,gold);
			p.print_candidate_set(candidateSet);	//make sure to generate this before calling SVM
			//p.print_metrics(); //this will generate the candidate set internally, then evaluate
			//p.print_metrics_SN(2); //parameter is window, see ESWC workshop draft for SN details
			//p.print_metrics_svm(schemaFile, candidateSet, svmModel,svmOutput);
			//p.print_metrics_svm_multiple(schemaFile, candidateSet, svmModelArray,svmOutputArray);
		}

		public static void main(String[] args)throws IOException{
				journal_BK();
			
		}
		
	}


