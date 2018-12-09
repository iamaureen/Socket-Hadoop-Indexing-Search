package hadoopIndexeQuery;

import java.util.Scanner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;


public class hadoopQuery {
	
	public static class QueryMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>{
		
	}
	
	public static class QueryReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{
		
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("\nPlease enter a query seprataed by space: ");
        Scanner sc = new Scanner(System.in);
        String inputPath = sc.nextLine();
        //TODO: use split and handle each keyword

        //Begin mapReduce section
        Job invertedIndex = new Job(); //JobConf used in WordCount example is part of the old API
        invertedIndex.setJarByClass(hadoopQuery.class);
        invertedIndex.setJobName("Hadoop Query");
        FileInputFormat.addInputPath(invertedIndex, new Path(inputPath));
        FileOutputFormat.setOutputPath(invertedIndex, new Path("output"));
        invertedIndex.setMapperClass(QueryMapper.class);
        invertedIndex.setReducerClass(QueryMapper.class);
        invertedIndex.setOutputKeyClass(Text.class);
        invertedIndex.setOutputValueClass(IntWritable.class);
        invertedIndex.waitForCompletion(true);
	}
}
