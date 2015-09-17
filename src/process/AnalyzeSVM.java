package process;

import evaluateHomo.ImportGoldStandard;

import java.util.*;
import java.io.*;


public class AnalyzeSVM {

	public static void main(String[] args)throws IOException{
		
		experiment_census5000();
	}
	
	public static void experiment_restaurant()throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/restaurant/";
		String records=new String(prefix+"restaurant.csv");
		String goldfile=new String(prefix+"GoldStandard.csv");
		ImportGoldStandard gold=new ImportGoldStandard(records,goldfile,0);
		String probs=prefix+"probs_2500_1000_unsup";
		
		
			ArrayList<Integer> q=returnSortedRatioList(probs);
				System.out.println(returnMax(gold,probs));
				//System.exit(-1);
				
				PrintWriter out=new PrintWriter(new File(prefix+"PrecRec_2500_1000_unsup"));
				ArrayList<double[]> ds=makePrecRecDS(gold,probs);
				int truepos=gold.num_dups;
				
				for(double i=1; i<=100; i+=1){
					
					System.out.println(i);
					double[] res=returnPrecRec(i,ds,truepos);
					//System.out.println(res[0]+" "+res[1]);
					out.println(res[0]+"\t"+res[1]);
					
				}
				
				for(int i:q){
					if( i<=100)
						continue;
					System.out.println(i);
					double[] res=returnPrecRec(i,ds,truepos);
					//System.out.println(res[0]+" "+res[1]);
					out.println(res[0]+"\t"+res[1]);
					
				}
				
				out.close();
	}
	
	public static void experiment_cora()throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/cora/";
		String records=new String(prefix+"cora.csv");
		String goldfile=new String(prefix+"GoldStandard");
		ImportGoldStandard gold=new ImportGoldStandard(records,goldfile,1);
		String probs=prefix+"probs_8600_1000_unsup";
		
		ArrayList<Integer> q=returnSortedRatioList(probs);
		System.out.println(returnMax(gold,probs));
		//System.exit(-1);
		
		PrintWriter out=new PrintWriter(new File(prefix+"PrecRec_8600_1000_unsup"));
		ArrayList<double[]> ds=makePrecRecDS(gold,probs);
		int truepos=gold.num_dups;
		
		for(double i=1; i<=100; i+=1){
			
			System.out.println(i);
			double[] res=returnPrecRec(i,ds,truepos);
			//System.out.println(res[0]+" "+res[1]);
			out.println(res[0]+"\t"+res[1]);
			
		}
		for(int i:q){
			if(i<=100)
				continue;
			System.out.println(i);
			double[] res=returnPrecRec(i,ds,truepos);
			//System.out.println(res[0]+" "+res[1]);
			out.println(res[0]+"\t"+res[1]);
			
		}
		out.close();
	}
	
	public static void experiment_census5000()throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/dataC5000/";
		String records=new String(prefix+"dataC5000.csv");
		String goldfile=new String(prefix+"GoldStandard");
		ImportGoldStandard gold=new ImportGoldStandard(records,goldfile,1);
		String probs=prefix+"probs_AL_11000_500_1000";
		
		ArrayList<Integer> q=returnSortedRatioList(probs);
		
		System.out.println(returnMax(gold,probs));
		
		//System.exit(-1);
		PrintWriter out=new PrintWriter(new File(prefix+"PrecRec_AL_11000_500_1000"));
		
		
		ArrayList<double[]> ds=makePrecRecDS(gold,probs);
		int truepos=gold.num_dups;
		for(double i=0.01;i<=100; i+=0.01){
			
				
			System.out.println(i);
			double[] res=returnPrecRec(i,ds,truepos);
			//System.out.println(res[0]+" "+res[1]);
			out.println(res[0]+"\t"+res[1]);
			
		}
		for(int i:q){
			if(i<=100)
				continue;
			System.out.println(i);
			double[] res=returnPrecRec(i,ds,truepos);
			//System.out.println(res[0]+" "+res[1]);
			out.println(res[0]+"\t"+res[1]);
		}
		
		
		
		out.close();
	}

	public static ArrayList<Integer> returnSortedRatioList(String probs_file)throws IOException{
		HashSet<Integer> ratios=new HashSet<Integer>();
		HashMap<Integer,Integer> hash=new HashMap<Integer,Integer>();
		Scanner in=new Scanner(new FileReader(probs_file));
		while(in.hasNextLine()){
			String[] line=in.nextLine().split("\t");
			//String[] line0=line[0].split(" ");
			double[] o_t=new double[3];
			String[] o_line1=line[1].split(" ");
			o_t[0]=Double.parseDouble(o_line1[0]);
			o_t[1]=Double.parseDouble(o_line1[1]);
			o_t[2]=o_t[0]/o_t[1];
			if(o_t[2]<10000){
				int p=((int) o_t[2]/100)*100;
				ratios.add(p);
				if(!hash.containsKey(p))
					hash.put(p, 0);
				hash.put(p,hash.get(p)+1);
			
			}
			
			else{
				int p=((int) o_t[2]/1000)*1000;
				ratios.add(p);
				if(!hash.containsKey(p))
					hash.put(p, 0);
				hash.put(p,hash.get(p)+1);
			
				
			}
			
		}
		in.close();
		ArrayList<Integer> t=new ArrayList<Integer>(ratios);
		Collections.sort(t);
		
		for(int i:t)
			System.out.println(i+" "+hash.get(i));
		
		return t;
	}
	
	public static int returnMax(ImportGoldStandard gold, String probs_file)throws IOException{
		
		
		
		ArrayList<double[]> res=new ArrayList<double[]>();
		ArrayList<double[]> overall=new ArrayList<double[]>();
		Scanner in=new Scanner(new FileReader(probs_file));
		while(in.hasNextLine()){
			String[] line=in.nextLine().split("\t");
			String[] line0=line[0].split(" ");
			double[] o_t=new double[3];
			String[] o_line1=line[1].split(" ");
			o_t[0]=Double.parseDouble(o_line1[0]);
			o_t[1]=Double.parseDouble(o_line1[1]);
			o_t[2]=o_t[0]/o_t[1];
			overall.add(o_t);
			if(gold.contains(Integer.parseInt(line0[0]),Integer.parseInt(line0[1]))){
				double[] t=new double[3];
				String[] line1=line[1].split(" ");
				t[0]=Double.parseDouble(line1[0]);
				t[1]=Double.parseDouble(line1[1]);
				t[2]=t[0]/t[1];
				res.add(t);
			}
		}
		in.close();
		double t1=addColumn(res,0);
		double t2=addColumn(res,1);
		System.out.println("gold");
		System.out.println("Aggregate "+(t1/t2));
		System.out.println("individual "+(addColumn(res,2)/res.size()));
		
		t1=addColumn(overall,0);
		 t2=addColumn(overall,1);
		System.out.println("overall");
		System.out.println("Aggregate "+(t1/t2));
		System.out.println("individual "+(addColumn(overall,2)/overall.size()));
		
		System.out.println(maxColumn(res,2));
		return (int) maxColumn(res,2);
	}
	
	private static double maxColumn(ArrayList<double[]> p, int c){
		double res=0;
		
		
		for(double[] q:p)
			if(q[c]>=res)
					res=q[c];
			
		
		
		return res;
	}
	
	private static double addColumn(ArrayList<double[]> p, int c){
		double res=0;
		
		for(double[] q:p)
			res+=q[c];
		
		return res;
	}
	
	private static void checkRecall(String probs_file, ImportGoldStandard gold)throws IOException{
		Scanner in=new Scanner(new FileReader(probs_file));
		int count=0;
		while(in.hasNextLine()){
			String[] line0=in.nextLine().split("\t");
			String[] line=line0[0].split(" ");
			int index1=Integer.parseInt(line[0]);
			int index2=Integer.parseInt(line[1]);
			if(gold.contains(index1,index2))
				count++;
		}
		System.out.println(count);
	}
	
	public static ArrayList<double[]> makePrecRecDS(ImportGoldStandard gold, String probs_file)throws IOException{
		ArrayList<double[]> DS=new ArrayList<double[]>();
		
		Scanner in=new Scanner(new FileReader(probs_file));
		while(in.hasNextLine()){
			double[] struct=new double[2];
			String[] line=in.nextLine().split("\t");
			String[] line0=line[0].split(" ");
			String[] line1=line[1].split(" ");
			struct[0]=Double.parseDouble(line1[0])/Double.parseDouble(line1[1]);
			
				
				int index1=Integer.parseInt(line0[0]);
				int index2=Integer.parseInt(line0[1]);
				if(gold.contains(index1,index2))
					struct[1]=1.0;
				else
					struct[1]=0.0;
				DS.add(struct);
			}
		
		in.close();
		return DS;
	}
	
	public static double[] returnPrecRec(double ratio, ArrayList<double[]> DS, int truepos){
		int count=0;
		int total=0;
		for(double[] d:DS){
			if(d[0]>ratio){
				total++;
				if(d[1]==1.0)
					count++;
			}
		}
		double[] res=new double[2];
		res[0]=(double) count/total;
		res[1]=(double) count/truepos;
		return res;
	}

	@Deprecated
	public static double[] returnPrecRec(double ratio, ImportGoldStandard gold, String probs_file)throws IOException{
		
		int count=0;
		int total=0;
		Scanner in=new Scanner(new FileReader(probs_file));
		while(in.hasNextLine()){
			String[] line=in.nextLine().split("\t");
			String[] line0=line[0].split(" ");
			String[] line1=line[1].split(" ");
			if(Double.parseDouble(line1[0])/Double.parseDouble(line1[1])>ratio)
			{
				total++;
				int index1=Integer.parseInt(line0[0]);
				int index2=Integer.parseInt(line0[1]);
				if(gold.contains(index1,index2))
					count++;
			}
		}
		in.close();
		double[] res=new double[2];
		res[0]=(double) count/total;
		res[1]=(double) count/gold.num_dups;
		return res;
		
	}
}
