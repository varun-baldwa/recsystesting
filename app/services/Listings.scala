import play.api.libs.json._
import scala.util.control.Exception._
import scalaj.http.Http

case class Result(title: String, price: Double, currency: String, department: String, category: String, subcategory: String, url: String)

object Listings {

  def queryAndPull(str: String): Result = {
    val query = "https://poshmark.com/api/posts/" + str
    try {
      val request = Http(query).asString
      val json = Json.parse(request.body)
      Result(json("title").toString(),
        json("price").toString().toDouble,
        json("price_amount")("currency_code").toString(),
        json("department")("display").toString(),
        json("category").toString(),
        json("category_v2")("display").toString(),
        json("cover_shot")("url_small").toString())
    } catch {
      case e => Result("unavailable", -100.0, "unavailable", "unavailable", "unavailable", "unavailable", "unavailable")
    }
  }

  def queryAll(ids: List[String]): collection.parallel.immutable.ParSeq[Result] = {
    val res = ids.par.map(str => queryAndPull(str))
    println(res.mkString("\n"))
    res
  }

  def main(args: Array[String]): Unit = {
    val ids = List("5d07fba48d6f1ada9372d094", "5d07fba48d6f1ada9372d095", "5d07fc6bfe19c75f08bec36f",
      "5d08074fbbf0765ee902cd5c", "5cec5a72c953d8eab6fc79b9",
      "5d0803f38557af4ac31ea058", "5d080649d40008eac8f18237")
    queryAll(ids)
  }
}