The final piece missing in this quick tour of Spark is how to use it in standalone programs.
Apart from running interactively, Spark can be linked into standalone applications
in either Java, Scala, or Python. The main difference from using it in the shell
is that you need to initialize your own SparkContext. After that, the API is the same.
The process of linking to Spark varies by language. In Java and Scala, you give your
application a Maven dependency on the spark-core artifact. As of the time of writing,
the latest Spark version is 1.2.0, and the Maven coordinates for that are