package process;
import java.util.*;
import java.io.*;

public class preprocessDedup {

	
	//also attach "0"...
	public static void deleteFirstLastColumn(String input, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(new File(input)));
		PrintWriter out=new PrintWriter(new File(output));
		int count=0;
		
		while(in.hasNextLine()){
			String line=in.nextLine();
			String total=new String("");
			String[] q=line.split(",");
			for(int i=1; i<q.length-1; i++)
				total+=q[i]+",";
			total=total.substring(0,total.length()-1);
			out.println(total);
			//out.println("\""+count+"\", "+total);
			//count++;
		}
		
		out.close();
		in.close();
	}
	
	public preprocessDedup(String input, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(new File(input)));
		PrintWriter out=new PrintWriter(new File(output));
		int count=0;
		
		while(in.hasNextLine()){
			String line=in.nextLine();
			out.println("\""+count+"\", "+line);
			count++;
		}
		
		out.close();
		in.close();
	}
	
	public static void preprocessGold(String input, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(input));
		PrintWriter out=new PrintWriter(new File(output));
		
		while(in.hasNextLine()){
			String[] line=in.nextLine().split(" ");
			for(int i=0; i<line.length-1; i++)
				for(int j=i+1; j<line.length; j++)
					out.println(line[i]+" "+line[j]);
		}
		
		out.close();
		in.close();
	}
	
	public static void main(String[] args)throws IOException{
		//preprocessGold("/host/heteroDatasets/cikm_experiments/dataC5000/GoldStandard.csv",
			//	"/host/heteroDatasets/cikm_experiments/dataC5000/GoldStandard");
		deleteFirstLastColumn("/host/heteroDatasets/cikm_experiments/scalability/without_fcolumn/data25000.csv",
				"/host/heteroDatasets/cikm_experiments/scalability/without_fcolumn/dataC25000.csv");
	}
}
