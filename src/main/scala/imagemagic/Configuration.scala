package imagemagic

import imagemagic.model.ImageMagicConfig
import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.magnolia.DeriveConfig.*

private val source = ConfigProvider.fromEnv()

object Configuration:
  val layer: ZLayer[Any, Config.Error, ImageMagicConfig] =
    ZLayer.fromZIO(source.load(deriveConfig[ImageMagicConfig].toSnakeCase))
