/*
 * Copyright (c) 2016, 2020, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *   * Neither the name of Gluon, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Based on https://github.com/gluonhq/gluon-samples/tree/master/fiftystates
 */

package io.makingthematrix.europeanunion.model

import com.gluonhq.attach.cache.CacheService
import io.makingthematrix.europeanunion.ChangeListener4S
import javafx.scene.image.Image

import scala.jdk.OptionConverters._

final case class Country(name: String, abbr: String, capital: String, population: Int, area: Int, flag: String) {
  lazy val density: Double = if (area > 0) population.toDouble / area.toDouble else 0.0

  override def toString: String =
    s"""
      $name ($abbr),
      Capital: $capital,
      Population (M): ${String.format("%.2f", population / 1_000_000d)},
      Area (km^2): $area,
      Density (pop/km^2): ${String.format("%.1f", density)}
    """.stripMargin
}

object Country {
  private val cache = CacheService.create().toScala.map(_.getCache[String, Image]("images"))

  private final val wikiPath = "https://upload.wikimedia.org/wikipedia/commons/thumb/"

  val countriesList: Seq[Country] = Seq(
    Country("Austria", "AT", "Vienna", 8858775, 83855, "4/41/Flag_of_Austria.svg/23px-Flag_of_Austria.svg.png"),
    Country("Belgium", "BE", "Brussels", 11467923, 30528, "9/92/Flag_of_Belgium_%28civil%29.svg/23px-Flag_of_Belgium_%28civil%29.svg.png"),
    Country("Bulgaria", "BG", "Sofia", 7000039, 110934, "9/9a/Flag_of_Bulgaria.svg/23px-Flag_of_Bulgaria.svg.png"),
    Country("Croatia", "HR", "Zagreb", 4076246, 56594, "1/1b/Flag_of_Croatia.svg/23px-Flag_of_Croatia.svg.png"),
    Country("Cyprus", "CY", "Nicosia", 875898, 9251, "d/d4/Flag_of_Cyprus.svg/23px-Flag_of_Cyprus.svg.png"),
    Country("Czechia", "CZ", "Prague", 10649800, 78866, "c/cb/Flag_of_the_Czech_Republic.svg/23px-Flag_of_the_Czech_Republic.svg.png"),
    Country("Denmark", "DK", "Copenhagen", 5806081, 43085, "9/9c/Flag_of_Denmark.svg/20px-Flag_of_Denmark.svg.png"),
    Country("Estonia", "EE", "Tallin", 1324820, 45227, "8/8f/Flag_of_Estonia.svg/23px-Flag_of_Estonia.svg.png"),
    Country("Finland", "FI", "Helsinki", 5517919, 338424, "b/bc/Flag_of_Finland.svg/23px-Flag_of_Finland.svg.png"),
    Country("France", "FR", "Paris", 67028048, 640679, "c/c3/Flag_of_France.svg/23px-Flag_of_France.svg.png"),
    Country("Germany", "DE", "Berlin", 83019214, 357021, "b/ba/Flag_of_Germany.svg/23px-Flag_of_Germany.svg.png"),
    Country("Greece", "GR", "Athens", 10722287, 113990, "5/5c/Flag_of_Greece.svg/23px-Flag_of_Greece.svg.png"),
    Country("Hungary", "HU", "Budapest", 9797561, 93030, "c/c1/Flag_of_Hungary.svg/23px-Flag_of_Hungary.svg.png"),
    Country("Ireland", "IR", "Dublin", 1415872, 28314, "4/45/Flag_of_Ireland.svg/23px-Flag_of_Ireland.svg.png"),
    Country("Italy", "IT", "Rome", 60359546, 301338, "0/03/Flag_of_Italy.svg/23px-Flag_of_Italy.svg.png"),
    Country("Latvia", "LV", "Riga", 1919968, 64589, "8/84/Flag_of_Latvia.svg/23px-Flag_of_Latvia.svg.png"),
    Country("Lithuania", "LT", "Vilnius", 2794184, 65200, "1/11/Flag_of_Lithuania.svg/23px-Flag_of_Lithuania.svg.png"),
    Country("Luxembourg", "LU", "Luxembourg", 613894, 2586, "d/da/Flag_of_Luxembourg.svg/23px-Flag_of_Luxembourg.svg.png"),
    Country("Malta", "MT", "Valletta", 493559, 316, "7/73/Flag_of_Malta.svg/23px-Flag_of_Malta.svg.png"),
    Country("Netherlands", "NL", "Amsterdam", 17282163, 41543, "2/20/Flag_of_the_Netherlands.svg/23px-Flag_of_the_Netherlands.svg.png"),
    Country("Poland", "PL", "Warsaw", 37972812, 312685, "1/12/Flag_of_Poland.svg/23px-Flag_of_Poland.svg.png"),
    Country("Portugal", "PT", "Lisbon", 10276617, 92390, "5/5c/Flag_of_Portugal.svg/23px-Flag_of_Portugal.svg.png"),
    Country("Romania", "RO", "Bucharest", 19401658, 238391, "7/73/Flag_of_Romania.svg/23px-Flag_of_Romania.svg.png"),
    Country("Slovakia", "SK", "Bratislava", 5450421, 49035, "e/e6/Flag_of_Slovakia.svg/23px-Flag_of_Slovakia.svg.png"),
    Country("Slovenia", "SL", "Ljubljana", 2080908, 20273, "f/f0/Flag_of_Slovenia.svg/23px-Flag_of_Slovenia.svg.png"),
    Country("Spain", "ES", "Madrid", 46934632, 504030, "9/9a/Flag_of_Spain.svg/23px-Flag_of_Spain.svg.png"),
    Country("Sweden", "SE", "Stockholm", 10230185, 449964, "4/4c/Flag_of_Sweden.svg/23px-Flag_of_Sweden.svg.png")
  )

  lazy val EuropeanUnion: Country = Country(
    "European Union", "EU", "Brussels",
    countriesList.map(_.population).sum,
    countriesList.map(_.area).sum,
    "b/b7/Flag_of_Europe.svg/23px-Flag_of_Europe.svg.png"
  )

  lazy val Scotland:Country = Country("Scotland", "SCO", "Edinburgh", 5463300, 77933, "1/10/Flag_of_Scotland.svg/23px-Flag_of_Scotland.svg.png")

  def getImage(imageUrl: String): Option[Image] = (cache, wikiPath + imageUrl) match {
    case (Some(c), url) if url.nonEmpty =>
      Option(c.get(url)).orElse {
        val cachedImage = new Image(url, true)
        c.put(url, cachedImage)

        // Working with JavaFX listeners proved to be a bit tricky. I needed to create a wrapper to simplify dealing with
        // generic types. But still, take note that we use `java.lang.Boolean` here, which we convert to Scala's Boolean,
        // and because of the implicit conversion the line can't be shortened to `ChangeListener4S { if (_) ... }`.
        // TODO: Try another library for pictures, e.g. Glide.
        cachedImage.errorProperty.addListener(
          ChangeListener4S[java.lang.Boolean]{ newValue => if (newValue) cache.foreach(_.remove(imageUrl)) }
        )

        Some(cachedImage)
      }
    case _ => None
  }
}
