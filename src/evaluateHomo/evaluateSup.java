package evaluateHomo;

import java.util.*;
import java.io.*;

import svm.svm_train;
import features.GenerateFeaturesFile.Homo;

public class evaluateSup {

	public static void main(String[] args)throws IOException{
		//generateBK_census5000();
		generateSVMAL_census5000();
		//generateSVMLightFile_sup_census5000(3660,1000);
	}
	public static void generateSVMLightFile_sup_restaurant(int dup, int nondup)throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/restaurant/";
		
		ImportGoldStandard g=new ImportGoldStandard(prefix+"restaurant.csv",prefix+"GoldStandard.csv",0);
		
		HashMap<Integer,Integer> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"restaurant.csv"));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		ArrayList<ArrayList<Double>> dupFeatures=new ArrayList<ArrayList<Double>>();
		for(int i:dups.keySet()){
			
			dupFeatures.add(Homo.getSVMFeatures(recs.get(i),recs.get(dups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Double>> nondupFeatures=new ArrayList<ArrayList<Double>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Homo.getSVMFeatures(recs.get(i),recs.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		Homo.generateSVMLightFile(prefix+"svmlight",dupFeatures,nondupFeatures);
		

		svm_train t=new svm_train();
		String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model");
		
		t.run(arg.split(" "));
	}
	
	public static void generateSVMLightFile_sup_cora(int dup, int nondup)throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/cora/";
		
		ImportGoldStandard g=new ImportGoldStandard(prefix+"cora.csv",prefix+"GoldStandard",1);
		
		ArrayList<HashSet<Integer>> dups=g.retRandomDupsCluster(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"cora.csv"));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		ArrayList<ArrayList<Double>> dupFeatures=new ArrayList<ArrayList<Double>>();
		for(HashSet<Integer> i:dups){
			ArrayList<Integer> m=new ArrayList<Integer>(i);
			Collections.sort(m);
			for(int p1=0; p1<m.size()-1; p1++)
				for(int p2=p1+1; p2<m.size(); p2++)
					dupFeatures.add(Homo.getSVMFeatures(recs.get(m.get(p1)),recs.get(m.get(p2))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		ArrayList<ArrayList<Double>> nondupFeatures=new ArrayList<ArrayList<Double>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Homo.getSVMFeatures(recs.get(i),recs.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		Homo.generateSVMLightFile(prefix+"svmlight",dupFeatures,nondupFeatures);
		
	
		svm_train t=new svm_train();
		String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model_"+dup+"_"+nondup);
		
		t.run(arg.split(" "));
	}
	
	public static void generateSVMAL_census5000()throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/dataC5000/";
		String pos=prefix+"500";
		String neg=prefix+"1000";
		Scanner in=new Scanner(new FileReader(prefix+"dataC5000.csv"));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		ArrayList<ArrayList<Double>> dupFeatures=new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> nondupFeatures=new ArrayList<ArrayList<Double>>();
		Scanner p=new Scanner(new FileReader(pos));
		while(p.hasNextLine()){
			String[] l=p.nextLine().split("\t")[0].split(" ");
			int index0=Integer.parseInt(l[0]);
			int index1=Integer.parseInt(l[1]);
			dupFeatures.add(Homo.getSVMFeatures(recs.get(index0),recs.get(index1)));
		}
		p.close();
		
		Scanner n=new Scanner(new FileReader(neg));
		while(n.hasNextLine()){
			String[] l=n.nextLine().split("\t")[0].split(" ");
			int index0=Integer.parseInt(l[0]);
			int index1=Integer.parseInt(l[1]);
			nondupFeatures.add(Homo.getSVMFeatures(recs.get(index0),recs.get(index1)));
		}
		n.close();
		
		Homo.generateSVMLightFile(prefix+"svmlight",dupFeatures,nondupFeatures);
		
		
		svm_train t=new svm_train();
		String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model_500_1000");
		
		t.run(arg.split(" "));
	}
	public static void generateSVMLightFile_sup_census5000(int dup, int nondup)throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/dataC5000/";
		
		ImportGoldStandard g=new ImportGoldStandard(prefix+"dataC5000.csv",prefix+"GoldStandard",1);
		
		ArrayList<HashSet<Integer>> dups=g.retRandomDupsCluster(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"dataC5000.csv"));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		ArrayList<ArrayList<Double>> dupFeatures=new ArrayList<ArrayList<Double>>();
		for(HashSet<Integer> i:dups){
			ArrayList<Integer> m=new ArrayList<Integer>(i);
			Collections.sort(m);
			for(int p1=0; p1<m.size()-1; p1++)
				for(int p2=p1+1; p2<m.size(); p2++)
					dupFeatures.add(Homo.getSVMFeatures(recs.get(m.get(p1)),recs.get(m.get(p2))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		
		ArrayList<ArrayList<Double>> nondupFeatures=new ArrayList<ArrayList<Double>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Homo.getSVMFeatures(recs.get(i),recs.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		Homo.generateSVMLightFile(prefix+"svmlight",dupFeatures,nondupFeatures);
		
	
		svm_train t=new svm_train();
		String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model_"+dup+"_"+nondup);
		
		t.run(arg.split(" "));
	}
	public static void generateBK_restaurant()throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/restaurant/";
		
		int dup=11;
		int nondup=1000;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"restaurant.csv",prefix+"GoldStandard.csv",0);
		HashMap<Integer,Integer> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"restaurant.csv"));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			
			dupFeatures.add(Homo.getFeatureWeightsHomo(recs.get(i),recs.get(dups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Homo.getFeatureWeightsHomo(recs.get(i),recs.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Homo.generateBKFileHomo(dupFeatures,nondupFeatures);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}

	
	public static void generateBK_cora()throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/cora/";
		
		int dup=8600;
		int nondup=1000;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"cora.csv",prefix+"GoldStandard",1);
		ArrayList<HashSet<Integer>> dups=g.retRandomDupsCluster(dup);
		System.out.println("done");
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"cora.csv"));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(HashSet<Integer> i:dups){
			ArrayList<Integer> m=new ArrayList<Integer>(i);
			Collections.sort(m);
			for(int p1=0; p1<m.size()-1; p1++)
				for(int p2=p1+1; p2<m.size(); p2++)
					dupFeatures.add(Homo.getFeatureWeightsHomo(recs.get(m.get(p1)),recs.get(m.get(p2))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Homo.getFeatureWeightsHomo(recs.get(i),recs.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Homo.generateBKFileHomo(dupFeatures,nondupFeatures);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_census5000()throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/dataC5000/";
		
		int dup=3660;
		int nondup=1000;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"dataC5000.csv",prefix+"GoldStandard",1);
		ArrayList<HashSet<Integer>> dups=g.retRandomDupsCluster(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"dataC5000.csv"));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(HashSet<Integer> i:dups){
			ArrayList<Integer> m=new ArrayList<Integer>(i);
			Collections.sort(m);
			for(int p1=0; p1<m.size()-1; p1++)
				for(int p2=p1+1; p2<m.size(); p2++)
					dupFeatures.add(Homo.getFeatureWeightsHomo(recs.get(m.get(p1)),recs.get(m.get(p2))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Homo.getFeatureWeightsHomo(recs.get(i),recs.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Homo.generateBKFileHomo(dupFeatures,nondupFeatures);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}

}
