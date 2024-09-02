package imagemagic

import imagemagic.api.Endpoints
import imagemagic.api.Endpoints.Storage.*
import imagemagic.api.Endpoints.Utility.*
import imagemagic.api.Endpoints.Web.*
import imagemagic.api.Endpoints.{ Storage as _, * }
import imagemagic.storage.*
import imagemagic.view.View
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.docs.openapi.OpenAPIDocsOptions
import sttp.tapir.swagger.SwaggerUIOptions
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.*
import zio.*

object Wiring:

  private val storage: ULayer[Storage] = Storage.local
  private val view: ULayer[View]       = View.web

  val wiredSwaggerEndpoints: Set[ZServerEndpoint[Any, ZioStreams]] = SwaggerInterpreter(
    openAPIInterpreterOptions = OpenAPIDocsOptions.default,
    customiseDocsModel = identity,
    swaggerUIOptions = SwaggerUIOptions.default,
    addServerWhenContextPathPresent = true,
  )
    .fromEndpoints[Task](
      documentedEndpoints.toList,
      "image-magic-api",
      "",
    )
    .toSet

  val wiredImageMagicEndpoints: Set[ZServerEndpoint[Any, ZioStreams]] = Set(
    listItemsEndpoint
      .zServerLogic(_ => Free.listItems.provide(storage)),
    submitImageFormEndpoint.zServerLogic(_ => Free.renderSubmitImageForm.provide(view)),
    submitImageActionEndpoint
      .zServerLogic(i => Free.submitImageAction(i).provide(storage, view)),
  )
  val wiredHealthEndpoints: Set[ZServerEndpoint[Any, ZioStreams]]     = Set(
    healthEndpoint.zServerLogic(_ => Free.healthCheck)
  )
