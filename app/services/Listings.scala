import play.api.libs.json._
import scalaj.http.Http

case class Result(title: String, price: Double, department: String, category: String, subcategory: String, url: String)

object Listings {

  def queryAndPull(str: String): (String, Double, String, String, String, String) = {
    val query = "https://poshmark.com/api/posts/" + str
    val request = Http(query).asString
    val json = Json.parse(request.body)
    (json("title").toString(),
      json("price").toString().toDouble,
      json("department")("display").toString(),
      json("category").toString(),
      json("category_v2")("display").toString(),
      json("cover_shot")("url_small").toString())
  }

  def queryAll(ids: Array[String]): Array[Result] = {
    val res = new Array[Result](ids.length)
    for (i <- ids.indices) {
      val (t, p, d, c, s, u) = queryAndPull(ids(i))
      res(i) = Result(t, p, d, c, s, u)
    }
    println(res.deep.mkString("\n"))
    res
  }

  def main(args: Array[String]): Unit = {
    val ids = Array("5d07fba48d6f1ada9372d095", "5d07fc6bfe19c75f08bec36f",
      "5d08074fbbf0765ee902cd5c", "5cec5a72c953d8eab6fc79b9",
      "5d0803f38557af4ac31ea058", "5d080649d40008eac8f18237")
    queryAll(ids)
  }
}