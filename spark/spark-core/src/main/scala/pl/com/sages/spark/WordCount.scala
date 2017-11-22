package pl.com.sages.spark

import org.apache.spark.{SparkConf, SparkContext}

object WordCount extends GlobalParameters {

  def main(args: Array[String]): Unit = {

    // prepare
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(conf)

    // run
    val booksRdd = sc.textFile(bookPath)
    val wordsRdd = booksRdd.flatMap(_.split(" "))
    val wordCount = wordsRdd.map(x => (x, 1)).reduceByKey((x, y) => x + y)

    // delete result directory and save result on HDFS
    import org.apache.hadoop.fs.{FileSystem, Path}
    FileSystem.get(sc.hadoopConfiguration).delete(new Path(resultPath), true)
    wordCount.saveAsTextFile(resultPath)

    // end
    sc.stop()
  }

}