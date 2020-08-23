package Wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Line_Count {
	
	//Mapper Class
	public static class MapForLineCount extends Mapper<Object, Text, Text, IntWritable>{
		
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text("Total Lines");
		public void map(Object key, Text value, Context con) throws IOException, InterruptedException{
			
			con.write(word, one);
		}
	}
	
	public static class ReduceForLineCount extends Reducer<Text, IntWritable, Text, IntWritable>{
		
		public void reduce(Text word, Iterable<IntWritable> values, Context con) throws IOException, InterruptedException{
			
			int sum = 0;
			for(IntWritable value: values){
				sum = sum + value.get();
			}
			
			con.write(word, new IntWritable(sum));
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		
		Configuration c= new Configuration();
		Job j = Job.getInstance(c, "wordcount:");
		j.setJarByClass(Word_Count.class);
		j.setMapperClass(MapForLineCount.class);
		j.setReducerClass(ReduceForLineCount.class);
		j.setOutputKeyClass(Text.class);
		j.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(j, new Path(args[0]));
		FileOutputFormat.setOutputPath(j, new Path(args[1]));
		System.exit(j.waitForCompletion(true)?0:1);
	}

}
