import org.ansj.recognition.impl.StopRecognition
import org.ansj.splitWord.analysis.ToAnalysis
import org.apache.commons.lang3.StringUtils
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    //spark的相关配置
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //对结果增加过滤,支持词性过滤,和词语过滤
    val filter = new StopRecognition()
    filter.insertStopNatures("w")

    //从西游记小说中读取数据
    val rdd = sc.textFile("D://不可视之禁//大三下//大数据开发实训//准备材料//5.25//西游记.txt")
      .map { x =>
        val str = if (x.length > 0)
        //开始分词，将结果使用逗号隔开
        ToAnalysis.parse(x).recognition(filter).toStringWithOutNature(",")
        str.toString
      }.flatMap(x => x.split(","))
    val result = rdd.map((_, 1)).groupBy(_._1).mapValues(_.size).filter(x => StringUtils.isNotBlank(x._1))
    //result.foreach(println)
    //result.foreach(println)
    //统计如来出现的次数并输出结果
    val rulai = result.filter(x => x._1.equals("如来"))
    //获取如来的value
    val rulai_num = rulai.first()._2
    val wukong = result.filter(x => x._1.equals("孙悟空"))
    //获取悟空的value
    val wukong_num = wukong.first()._2
    //汇总的value
    val tot_num = rulai_num+wukong_num
    println("如来+孙悟空",tot_num)
    //val sorted = result.sortBy(_._2, false).filter(x=>x._1.length>=2)
    //输出前五个最多的词
    //sorted.collect().take(5).foreach(println)
    //输出所有结果到本文中
    //sorted.map(x=>{x._1+","+x._2}).saveAsTextFile("D:/不可视之禁/大三下/大数据开发实训/课程安排/第二波：数据湖技术体系/5.26/output"+System.nanoTime())
  }
}
