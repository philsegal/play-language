/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.language

import play.api.Logger
import play.api.i18n.Lang
import play.api.mvc.{Controller, Call, Action}

trait LanguageController extends Controller {

  import play.api.Play.current

  val English = Lang("en")
  val Welsh = Lang("cy")

  private val fallbackURL = current.configuration.getString("language.fallbackUrl").getOrElse("/")

  def switchToEnglish = switchToLang(English)
  def switchToWelsh = switchToLang(Welsh)

  private def switchToLang(lang: Lang) = Action { implicit request =>
    request.headers.get(REFERER) match {
      case Some(referrer) => Redirect(referrer).withLang(lang)
      case None => {
        Logger.warn("Unable to get the referrer, so sending them to the start.")
        Redirect(Call("GET", fallbackURL)).withLang(lang)
      }
    }
  }

}

object LanguageController extends LanguageController