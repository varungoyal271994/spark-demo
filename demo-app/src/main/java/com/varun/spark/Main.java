package com.varun.spark;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;
public class Main {
    public static void main(String[] args) {
        String path = "demo-app/src/main/resources/";
        Path relativePath = Paths.get(path);
        System.out.println("Path is " + relativePath.toAbsolutePath() );

       // System.setProperty("hadoop.home.dir", path);
        System.setProperty("java.opts", "-Xms2g -Xmx4g"); // Increase JVM heap size
        //For above java.opts JRE ignores Xms2g and other values while jdk doesnt. IN that case in prod memeory options turning is done carefully.
        //we usually submit spark job to yarn so we dont care much
        SparkConf conf = new SparkConf()
            .setAppName("Varun-spark")
            .setMaster("local[*]")  // Use all available cores
            .set("spark.driver.memory", "4g");  // Set driver memory
            System.out.println("Java version: " + System.getProperty("java.version"));
            System.out.println("Max JVM memory: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + "MB");

        System.out.println("Hello world!" + conf.get("spark.driver.memory"));
        SparkSession sc = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> dataframe = sc.read().format("csv").option("header", true)
            .load(path + "name_and_comments.txt");
        dataframe.show();
        System.out.println("Data frames are immuatable after this still no new col");
        dataframe.withColumn("New Name", concat( dataframe.col("first_name"), lit(" "), dataframe.col("last_name")));
        dataframe.show();
        
    }
}