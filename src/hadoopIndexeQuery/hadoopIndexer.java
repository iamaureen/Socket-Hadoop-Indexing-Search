package hadoopIndexeQuery;

import java.util.Scanner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;


public class hadoopIndexer {
	
	public static class IndexMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>{
		
	}
	
	public static class IndexReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{
		
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("\nPlease enter the HDFS path of the file(s) to index.: ");
        Scanner sc = new Scanner(System.in);
        String inputPath = sc.nextLine();

        //Begin mapReduce section
        Job invertedIndex = new Job(); //JobConf used in WordCount example is part of the old API
        invertedIndex.setJarByClass(hadoopIndexer.class);
        invertedIndex.setJobName("Hadoop Inverted Index");
        FileInputFormat.addInputPath(invertedIndex, new Path(inputPath));
        FileOutputFormat.setOutputPath(invertedIndex, new Path("output"));
        invertedIndex.setMapperClass(IndexMapper.class);
        invertedIndex.setReducerClass(IndexReducer.class);
        invertedIndex.setOutputKeyClass(Text.class);
        invertedIndex.setOutputValueClass(IntWritable.class);
        invertedIndex.waitForCompletion(true);
	}
}
