import scala.io.Source

/*
TODO Прочитайте содержимое данного файла.
В случае неудачи верните сообщение соответствующего исключения.
 */
def readThisWorksheet(): String = {
  try {
    Source.fromFile("HomeTask.sc").getLines().mkString("\n")
  } catch {
    case e: Exception => e.getMessage
  }
}

println(readThisWorksheet())