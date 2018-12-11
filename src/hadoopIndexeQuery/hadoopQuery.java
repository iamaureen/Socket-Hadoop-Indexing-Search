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
		private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        String query;
        
        @Override
        public void configure(JobConf job) {
            super.configure(job);
            query = job.get("query", null);

         }
        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException
        {
            String[] lineInputSplit = value.toString().split("\\s+"); // splits
            String[] wordandfile = lineInputSplit[0].split("@"); // splits the word and filename

            //https://stackoverflow.com/questions/8457183/passing-parameters-to-map-function-in-hadoop
            //get the search query 
            //System.out.println("from mapper here is the string ::" + query);

            if(wordandfile[0].equals(query)){
                    output.collect(new Text(lineInputSplit[0]), new IntWritable(Integer.parseInt(lineInputSplit[1])));
            }
            //output.collect(new Text(wordandfile[0]), one);
            //output.collect(new Text(query), one);

        }
        
	}
	
	public static class QueryReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException
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
		//user input
        //System.out.println("\nPlease enter query ");
        //Scanner sc = new Scanner(System.in);
        //String inputPath = sc.nextLine();


        //check file exists or not:
        FileSystem fs = FileSystem.get(new Configuration());
        Path indexDir = new Path(args[0]+"/part-00000");
        System.out.println(indexDir);
        if (!fs.exists(indexDir)) {
            System.out.println("Nothing indexed.");
            System.exit(1);
        }


        JobConf conf = new JobConf(WordCount.class);
        conf.setJobName("wordcount-query");

        //use configuration to pass query words to the mapper
        conf.set("query", args[2]);
        System.out.println(conf.get("query"));

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(QueryMapper.class);
        conf.setCombinerClass(QueryReducer.class);
        conf.setReducerClass(QueryReducer.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        JobClient.runJob(conf);
	}
}
