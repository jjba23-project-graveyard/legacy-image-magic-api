package imagemagic.api

import com.linecorp.armeria.common.SessionProtocol
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.{ HttpServiceWithRoutes, Server as ArmeriaServer }
import imagemagic.*
import imagemagic.Wiring.*
import imagemagic.model.ImageMagicConfig
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter
import zio.*
import zio.aws.core.config.AwsConfig
import zio.aws.netty.NettyHttpClient
import zio.aws.s3.S3

object TapirServer:

  def run(implicit r: zio.Runtime[Any]): ZIO[ImageMagicConfig, Throwable, Unit] = for
    config    <- ZIO.service[ImageMagicConfig]
    httpClient = NettyHttpClient.default
    awsConfig  = httpClient >>> AwsConfig.default
    awss       = awsConfig >>> S3.live

    wiredEndpoints =
      (wiredHealthEndpoints | wiredImageMagicEndpoints | wiredSwaggerEndpoints).toList

    tapirService = ArmeriaZioServerInterpreter().toService(wiredEndpoints)

    runResult <- ZIO.acquireReleaseWith(acquire(tapirService))(release(_).orDie)(_ => ZIO.never)
  yield runResult

  private def acquire(endpoints: HttpServiceWithRoutes)
      : ZIO[ImageMagicConfig, Throwable, ArmeriaServer] =
    for
      config     <- ZIO.service[ImageMagicConfig]
      server      = ArmeriaServer
                      .builder()
                      .requestTimeoutMillis(config.apiTimeout)
                      .port(config.apiPort, SessionProtocol.HTTP)
                      .service(endpoints)
                      .accessLogWriter(AccessLogWriter.common(), true)
                      .build()
      liveServer <- ZIO.fromFutureJava(server.start()).as(server)
    yield liveServer

  private def release(server: ArmeriaServer): Task[Unit] =
    ZIO.fromFutureJava(server.stop()) *> ZIO.logInfo("Server Stopped")
