//import breeze.linalg.DenseVector
import org.apache.spark.ml.classification.{BinaryLogisticRegressionSummary, LogisticRegression}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.linalg.DenseVector
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession



/*
 * https://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+(Original)
 * Array(1000025,5,1,1,1,2,1,3,1,1,2)
   0. Sample code number            id number
   1. Clump Thickness               1 - 10
   2. Uniformity of Cell Size       1 - 10
   3. Uniformity of Cell Shape      1 - 10
   4. Marginal Adhesion             1 - 10
   5. Single Epithelial Cell Size   1 - 10
   6. Bare Nuclei                   1 - 10
   7. Bland Chromatin               1 - 10
   8. Normal Nucleoli               1 - 10
   9. Mitoses                       1 - 10
  10. Class:                        (2 for benign, 4 for malignant)
 */

object Cancer {

  case class Obs(clas: Double, thickness: Double, size: Double, shape: Double, madh: Double, epsize: Double, bnuc: Double, bchrom: Double, nNuc: Double, mit: Double)

  def parseObs(line: Array[Double]): Obs = {
    Obs(
      if (line(9) == 4.0) 1 else 0, line(0), line(1), line(2), line(3), line(4), line(5), line(6), line(7), line(8)
    )
  }

  def parseRDD(rdd: RDD[String]): RDD[Array[Double]] = {
    rdd.map(_.split(",")).filter(_(6) != "?").map(_.drop(1)).map(_.map(_.toDouble))
  }

  def main(args: Array[String]) {
/*    */
    val spark : SparkSession = SparkSession.builder
      .master("local")
      .appName("stock")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR") // TO remove vervose INFO message when run the program.
    val rdd =spark.sparkContext.textFile("C:\\scala_coursera\\data\\wbcd.csv")

    /*before Spark 2.x using:  val conf = new SparkConf() , val sc = SparkContext(conf)
     * , val sqlContext = new SQLContext(sc) */

//    //  val rdd = sc.textFile("wdbc.data")
//    val rdd = sc.textFile("data/wbcd.csv")
    val obsRDD = parseRDD(rdd).map(parseObs)
       println("* obsRDD.count() :"+obsRDD.count())
    /*convert RDD to DataFrame : spark is SparkSession builder */
    val obsDF = spark.createDataFrame(obsRDD)

/*********** DataFrae manipulation test ******************************/
    obsDF.printSchema()
    obsDF.describe("size").show(5)
    obsDF.select("thickness").show(5)
    /* select all shape , but increment th nNuc by 1  */
    import spark.implicits._ //to use $ notation
    obsDF.select($"shape", $"nNuc" + 1).show(5)
  /* show elements with epsize is bigger than 6 */
    obsDF.select($"epsize" > 6).show(5)
    /* count element by size, shape, thickness respectively using groupBy */
    println("* groupBy(\"size\") :"+ obsDF.groupBy("size").count().sort("count").show())
    println("* groupBy(\"shape\") :" + obsDF.groupBy("shape").count().sort("count").show())
    println("* groupBy(\"thickness\") :" + obsDF.groupBy("thickness").count().sort("count").show())
    /*compute the average thickness, size,shape grouped by clas[malignant( 1) or not(0)] */
    //Instead sqlContext in Spark1.6, "catalog" interface is accessible from SparkSession
    println(" groupBy(\"clas\") using sqlContext.sql(\"Select clas, avg(thickness) as avgthickness,...From obs GROUP BY clas\").show()")
//    sqlContext.sql("Select clas, avg(thickness) as avgthickness From obs GROUP BY clas").show
    /* Instaed of Spark1.6 sqlContex.sql, in Spark2.x use SparkSession in this case spark:
       so, spark.sql
     */
//    spark.sql("Select clas, avg(thickness) as avgthickness From obs GROUP BY clas").show  //not working
    /*     case class Emplyee(id: Int, fname: String, lname: String, age: Int, city: Srting)
           val testDF = spark.sql(""" SELECT id, lname
     *                                   FROM employees
      *                                   WHERE city = "Sydney"
      *                                   ORDER BY id""" )*/
    // Now create an SQL table and issue SQL queries against it without
    // using the sqlContext but through the SparkSession object.
    // Creates a temporary view of the DataFrame
   obsDF.createOrReplaceTempView("clasTable")
   obsDF.cache()
    val resultsDF = spark.sql(
      "SELECT clas, AVG(thickness) AS avgthickness, AVG(size) AS avgsize, AVG(shape) AS avgshape FROM clasTable GROUP BY clas" )
    println("** clasTable : ")
    resultsDF.show() //it shows only 2 record of clas of 0.0 and 1.0

/*Saving and Reading from Hive table with SparkSession */
//create a Hive table and issue queries against it using SparkSession object
    // as you would with a HiveContext.
    //drop table if exits to prevent error
//    spark.sql("DROP TABLE IF EXISTS clas_hive_table2")
//    //save as a hive table
//    spark.table("clasTable").write.saveAsTable("clas_hive_table2")
//    // query againt cals_hive_table
//    val malDF = spark.sql("SELECT clas, avgthickness, avgshape FROM clas_hive_table2 WHERE clas == 1.0")
//    malDF.show() ; println(" malDF **")

    //    val obsDF = obsRDD.toDF().cache()  //in this case .toDF() not activated, based on
    val featureCols = Array("thickness", "size", "shape", "madh", "epsize", "bnuc", "bchrom", "nNuc", "mit")
    //set the input and output colum name
    val assembler = new VectorAssembler().setInputCols(featureCols).setOutputCol("features")
    println("* assembler : "+ assembler, assembler.getInputCols, assembler.getOutputCol)
    assembler.getInputCols.foreach(print)
    println()
    //return a dataframe with all of the feature columns in a vector column
    val df2 = assembler.transform(obsDF)
    df2.show(5); println("DF with features**")  //only show 5 rows
/************ end of test **************************************************************/

    val labelIndexer = new StringIndexer().setInputCol("clas").setOutputCol("label")
    val df3 = labelIndexer.fit(df2).transform(df2)
    df3.show(5); println("DF with label,which was clas**")

    val splitSeed = 5043
    val Array(trainingData, testData) = df3.randomSplit(Array(0.7, 0.3), splitSeed)
//create the classifier, set parameters for training
    val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
    //training using training data
    val model = lr.fit(trainingData)  //training
//run the model on test features to get predictions
    val predictions = model.transform(testData)
    println(s"coefficints: ${model.coefficients} Intercept: ${model.intercept}")
    //previous model transform produce a new clumns : rawPrediction, probability and prediction
    predictions.show(5); println("predictions **")
//    predictions.select("clas", "label", "prediction").show(5) ; println("with prediction **")

// Extract the summary from the returned LogisticRegressionModel instance trained in the earlier
    val trainingSummary = model.summary
    val objectiveHistory = trainingSummary.objectiveHistory
    objectiveHistory.foreach(loss => println(loss)); println(" objectiveHistory **")
// Obtain the metrics useful to judge performance on test data.
// We cast the summary to a BinaryLogisticRegressionSummary since the problem is a
// binary classification problem.
    val binarySummary = trainingSummary.asInstanceOf[BinaryLogisticRegressionSummary]

    // Obtain the receiver-operating characteristic as a dataframe and areaUnderROC.
    val roc = binarySummary.roc
    roc.show(); println(" binarySummary.roc.show() **" )
    println(" binarySummary.areaUnderROC :" +binarySummary.areaUnderROC)

    // Set the model threshold to maximize F-Measure
    val fMeasure = binarySummary.fMeasureByThreshold
//    binarySummary.fMeasureByThreshold.select
    fMeasure.show(); println("fMeasure **")
    fMeasure.printSchema()
//    val maxFMeasure = fMeasure.select("fmeasureV").head().getDouble(0)
/* change column name in DataFrame
//    val newNames = Seq("id", "x1", "x2", "x3")
//    val dfRenamed = df.toDF(newNames: _*)
*/
    val newNames = Seq("threshV", "fmeasureV")
    val fMRenamed = fMeasure.toDF(newNames: _*)
    fMRenamed.show(5)

    val fm = fMRenamed.col("fmeasureV")  //fm type is column
//    val maxFMeasure = fMeasure.select(max("fmeasureV")).head().getDouble(0)  //select not work : how to make select work !
    println("fm : "+fm)  //fm is F-Measure

    fMRenamed.createOrReplaceTempView("fmeasureVTable")
    fMRenamed.cache()
    val maxFMeasure = spark.sql("SELECT MAX(fmeasureV) FROM fmeasureVTable").head().getDouble(0)
    println("maxFMeasure: " + maxFMeasure)



    val bestThreshold = fMeasure.where($"F-Measure" === maxFMeasure).select("threshold").head().getDouble(0)
    model.setThreshold(bestThreshold)

    val evaluator = new BinaryClassificationEvaluator().setLabelCol("label")

    val accuracy = evaluator.evaluate(predictions)
    println("accuray : "+accuracy)
    /******************
      * common matric used for logistic regression is area under the ROC curve(AUC).
      * We can use BinaryClassificationEvaluator to obtain AUC.
      * Create an Evaluator for BinaryClassification, which expects two input columns: rawPrediction and label.
      *

    *******************/
    val evaluator_tow = new BinaryClassificationEvaluator().setLabelCol("label")
      .setRawPredictionCol("rawPrediction")
      .setMetricName("areaUnderROC")
    val accuracy_two = evaluator_tow.evaluate(predictions)
    println("accuracy with rawPrediction and label : "+accuracy_two)
    /*****************
      * true positive :
      * lp.filter($"prediction" === 0.0).filter($"label" === $"prediction").count()
      * true negative :
      * False positive : actual No, predict Yes :
      * lp.filter($"prediction" === 1.0).filter(not($"label" === $"prediction")).count()
      * False negative             : actual Yes , predict No :
      * lp.filter($"prediction" === 0.0).filter(not($"label" === $"prediction")).count()
      * ********************* */

    val lp = predictions.select( "label", "prediction")
    val counttotal = predictions.count()
    val correct = lp.filter($"label" === $"prediction").count()
    val wrong = lp.filter(!($"label" === $"prediction")).count()
    val truep = lp.filter($"prediction" === 0.0).filter($"label" === $"prediction").count()
    val falseN = lp.filter($"prediction" === 0.0).filter(!($"label" === $"prediction")).count()
    val falseP = lp.filter($"prediction" === 1.0).filter(!($"label" === $"prediction")).count()
    val ratioWrong=wrong.toDouble/counttotal.toDouble
    val ratioCorrect=correct.toDouble/counttotal.toDouble
    println(s"correct : $correct , wrong: $wrong, truePosive: $truep , falseN : $falseN" )
    println(s"ratioWrong : $ratioWrong, ratioCorrect : $ratioCorrect")

    // use MLlib to evaluate, convert DF to RDD
/*  A Precision-Recall curve plots (precision, recall) points for different threshold values,
 while a receiver operating characteristic, or ROC, curve plots (recall, false positive rate) points.
  The closer  the area Under ROC is to 1, the better the model is making predictions.
  */

    val  predictionAndLabels =predictions.select("rawPrediction", "label").rdd.map(x => (x(0).asInstanceOf[DenseVector](1), x(1).asInstanceOf[Double]))
    val metrics = new BinaryClassificationMetrics(predictionAndLabels)
    println("area under the precision-recall curve: " + metrics.areaUnderPR)
    println("area under the receiver operating characteristic (ROC) curve : " + metrics.areaUnderROC)
  }//end of main
}

