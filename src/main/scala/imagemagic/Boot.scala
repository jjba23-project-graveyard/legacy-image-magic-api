package imagemagic

import imagemagic.api.TapirServer
import imagemagic.model.ImageMagicConfig
import zio.*
import zio.logging.backend.SLF4J

object Boot extends ZIOAppDefault:

  override def run: ZIO[Any, Throwable, Unit] = (for {
    config <- ZIO.service[ImageMagicConfig]
    server <- TapirServer.run(runtime)
  } yield server).provide(Configuration.layer)

  override def runtime: zio.Runtime[Any] =
    val logging: ZLayer[Any, Nothing, Unit] = Runtime.removeDefaultLoggers >>> SLF4J.slf4j
    Unsafe.unsafe(implicit u => zio.Runtime.unsafe.fromLayer(logging))
