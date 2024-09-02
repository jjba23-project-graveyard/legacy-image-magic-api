package imagemagic.api

import imagemagic.api.Endpoints.Storage.*
import imagemagic.api.Endpoints.Utility.*
import imagemagic.api.Endpoints.Web.*
import imagemagic.model.SubmitImageData
import sttp.tapir.Endpoint
import sttp.tapir.PublicEndpoint
import sttp.tapir.generic.auto.*
import sttp.tapir.ztapir.*

object Endpoints:
  val documentedEndpoints =
    Set(
      healthEndpoint,
      listItemsEndpoint,
      submitImageFormEndpoint,
      // submitImageActionEndpoint,
    )

  object Utility:
    val healthEndpoint: PublicEndpoint[Unit, Unit, Either[String, String], Any] =
      endpoint.get.in("health").out(plainBody[Either[String, String]])

  object Storage:
    val listItemsEndpoint: PublicEndpoint[Unit, Unit, Either[String, String], Any] =
      endpoint.get.in("items").out(plainBody[Either[String, String]])

  object Web:
    val submitImageFormEndpoint: PublicEndpoint[Unit, Unit, String, Any] =
      endpoint.get.in("submit-image").out(htmlBodyUtf8)

    val submitImageActionEndpoint: Endpoint[Unit, SubmitImageData, Unit, String, Any] =
      endpoint
        .post
        .in("submit-image")
        .in(multipartBody[SubmitImageData])
        .out(htmlBodyUtf8)
