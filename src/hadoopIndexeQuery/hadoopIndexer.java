package hadoopIndexeQuery;

import java.util.Scanner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;


public class hadoopIndexer {
	
	public static class IndexMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>
	  {
	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();

	    public void map(LongWritable key, javax.xml.soap.Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException
	    {

	    		//https://stackoverflow.com/questions/19012482/how-to-get-the-input-file-name-in-the-mapper-in-a-hadoop-program
	        FileSplit fs = (FileSplit) reporter.getInputSplit();
	        String filename = fs.getPath().getName();
	        System.out.println(filename);

	        	String valuewoPunc = value.toString().replaceAll("\\p{Punct}","");
	        String line = valuewoPunc.toString();
	        StringTokenizer tokenizer = new StringTokenizer(line);
	        StringBuilder appendFile = new StringBuilder();
	        while (tokenizer.hasMoreTokens())
	        {
	            appendFile.append(tokenizer.nextToken());
	            appendFile.append('@'+filename);
	            word.set(appendFile.toString());
	            output.collect(word, one);
	            //clear appendFile for next time
	            appendFile.setLength(0);
	        }
	    }
	 }

	
	public static class IndexReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{
		public void IndexReducer(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException
        {
                int sum = 0;
                while (values.hasNext())
                {
                        sum += values.next().get();
                }
                output.collect(key, new IntWritable(sum));
        }
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
