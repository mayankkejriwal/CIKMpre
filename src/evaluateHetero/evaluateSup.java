package evaluateHetero;

import java.util.*;
import java.io.*;

import features.HeteroBK;
import features.GenerateFeaturesFile.Hetero;
import general.CSVParser;

public class evaluateSup {

	public static void main(String[] args)throws IOException{
		long time1=System.currentTimeMillis();
		generateBK_PR_semisup();
		long time2=System.currentTimeMillis();
		System.out.println("Time elapsed (secs): "+((time2-time1)*1.0/1000));
		
		
	}
	public static void generateBK_parks1()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/parks1/";
		
		int dup=161;
		int nondup=161;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"National_Parks_Plus.csv",
				prefix+"national_park_service.csv",prefix+"goldStandard_NPP_nps");
		HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"National_Parks_Plus.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"national_park_service.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			for(int j:dups.get(i))
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(j)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHeteroBilenko(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_game2()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/game2/";
		
		int dup=374;
		int nondup=374;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"ibm.csv",
				prefix+"dbpedia.csv",prefix+"goldStandard_ibm_dbpedia");
		HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"ibm.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"dbpedia.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			for(int j:dups.get(i))
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(j)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHeteroBilenko(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_game1()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/game/";
		
		int dup=5000;
		int nondup=5000;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"vgchartz.csv",
				prefix+"dbpedia.csv",prefix+"goldStandard_vgchartz_dbpedia");
		HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"vgchartz.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"dbpedia.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			for(int j:dups.get(i))
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(j)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHeteroBilenko(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_PR()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/PR/";
		
		int dup=50;
		int nondup=50;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"restaurant1.csv",
				prefix+"restaurant2.csv",prefix+"goldStandard");
		HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"restaurant1.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"restaurant2.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			for(int j:dups.get(i))
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(j)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHeteroBilenko(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_libraries()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/libraries/";
		
		int dup=8395;
		int nondup=8395;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"PublicLibraries.csv",
				prefix+"public_libraries.csv",prefix+"goldStandard_PL_pl");
		HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"PublicLibraries.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"public_libraries.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			for(int j:dups.get(i))
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(j)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHeteroBilenko(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_Persons()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/Persons/";
		
		int dup=250;
		int nondup=250;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"person1.csv",
				prefix+"person2.csv",prefix+"goldStandard");
		HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"person1.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"person2.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			for(int j:dups.get(i))
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(j)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHeteroBilenko(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_game3()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/game3/";
		
		int dup=1967;
		int nondup=1967;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"ibm.csv",
				prefix+"vgchartz.csv",prefix+"goldStandard_ibm_vgchartz");
		HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"ibm.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"vgchartz.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:dups.keySet()){
			for(int j:dups.get(i))
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(j)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHeteroBilenko(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_parks1_semisup()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/parks1/";
		
		int dup=161;
		int nondup=161;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"National_Parks_Plus.csv",
				prefix+"national_park_service.csv",prefix+"goldStandard_NPP_nps");
		//HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"National_Parks_Plus.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"national_park_service.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		 in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		int[] dupInd1=new int[dup];
		int[] dupInd2=new int[dup];
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupInd1[i]=index1;
			dupInd2[i]=index2;
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_game2_semisup()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/game2/";
		
		int dup=374;
		int nondup=374;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"ibm.csv",
				prefix+"dbpedia.csv",prefix+"goldStandard_ibm_dbpedia");
		//HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"ibm.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"dbpedia.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		int[] dupInd1=new int[dup];
		int[] dupInd2=new int[dup];
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupInd1[i]=index1;
			dupInd2[i]=index2;
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_game3_semisup()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/game3/";
		
		int dup=1967;
		int nondup=1967;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"ibm.csv",
				prefix+"vgchartz.csv",prefix+"goldStandard_ibm_vgchartz");
		//HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"ibm.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"vgchartz.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		int[] dupInd1=new int[dup];
		int[] dupInd2=new int[dup];
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupInd1[i]=index1;
			dupInd2[i]=index2;
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_game1_semisup()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/game/";
		
		int dup=5000;
		int nondup=5000;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"vgchartz.csv",
				prefix+"dbpedia.csv",prefix+"goldStandard_vgchartz_dbpedia");
		//HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"vgchartz.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"dbpedia.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		int[] dupInd1=new int[dup];
		int[] dupInd2=new int[dup];
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupInd1[i]=index1;
			dupInd2[i]=index2;
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_libraries_semisup()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/libraries/";
		
		int dup=8395;
		int nondup=8395;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"PublicLibraries.csv",
				prefix+"public_libraries.csv",prefix+"goldStandard_PL_pl");
		//HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"PublicLibraries.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"public_libraries.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		int[] dupInd1=new int[dup];
		int[] dupInd2=new int[dup];
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupInd1[i]=index1;
			dupInd2[i]=index2;
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_PR_semisup()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/PR/";
		
		int dup=50;
		int nondup=50;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"restaurant1.csv",
				prefix+"restaurant2.csv",prefix+"goldStandard");
		//HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"restaurant1.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"restaurant2.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		 in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		int[] dupInd1=new int[dup];
		int[] dupInd2=new int[dup];
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupInd1[i]=index1;
			dupInd2[i]=index2;
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}
	public static void generateBK_Persons_semisup()throws IOException{
		String prefix="/host/heteroDatasets/icde_experiments/Persons/";
		
		int dup=250;
		int nondup=250;
		ImportGoldStandard g=new ImportGoldStandard(prefix+"person1.csv",
				prefix+"person2.csv",prefix+"goldStandard");
		//HashMap<Integer, HashSet<Integer>> dups=g.retRandomDups(dup);
		HashMap<Integer,Integer> nonDups=g.retRandomNonDups(nondup);
		Scanner in=new Scanner(new FileReader(prefix+"person1.csv"));
		ArrayList<String> recs1=new ArrayList<String>();
		while(in.hasNextLine())
			recs1.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(prefix+"person2.csv"));
		ArrayList<String> recs2=new ArrayList<String>();
		while(in.hasNextLine())
			recs2.add(in.nextLine());
		
		in.close();
		
		int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
		int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
		
		HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
		
		for(int i=0; i<att1; i++){
			HeteroBK.ignore.put(i,new HashSet<Integer>());
			for(int j=0; j<att2; j++)
				HeteroBK.ignore.get(i).add(j);
		}
		
		
		
		
		Scanner sc=new Scanner(new FileReader(prefix+"schema_dumas"));
		while(sc.hasNextLine()){
			String[] p=sc.nextLine().split(" ");
			int index1=Integer.parseInt(p[0]);
			int index2=Integer.parseInt(p[1]);
			if(HeteroBK.ignore.containsKey(index1))
				HeteroBK.ignore.get(index1).remove(index2);
			if(HeteroBK.ignore.get(index1)==null)
				HeteroBK.ignore.remove(index1);
					
			
		}
		//System.out.println(HeteroBK.ignore.get(2));
		sc.close();
		
		 in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		int[] dupInd1=new int[dup];
		int[] dupInd2=new int[dup];
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupInd1[i]=index1;
			dupInd2[i]=index2;
			dupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		System.out.println(dupFeatures.size());
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i:nonDups.keySet()){
			
			nondupFeatures.add(Hetero.getFeatureWeightsHetero(recs1.get(i),recs2.get(nonDups.get(i))));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		String t=Hetero.generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
		PrintWriter out=new PrintWriter(new File(prefix+"BK"));
		System.out.println(t);
		out.println(t);
		out.close();
	}

}
