package imagemagic.model

import sttp.model.Part
import sttp.tapir.TapirFile

final case class ImageMagicConfig(
    apiHost: String,
    apiPort: Int,
    apiTimeout: Int,
  )

case class SubmitImageData(
    imageFile: Part[TapirFile],
    destination: Option[String],
    newFileName: Option[String],
  )

sealed trait ImageSize
case object Small  extends ImageSize
case object Medium extends ImageSize
case object Large  extends ImageSize

def imageSizeToPixels(size: ImageSize): Int = size match
  case Small  => 320
  case Medium => 640
  case Large  => 1080

// 1080px is ~ 1920 / 1.7
val aspectRatioMultiplier = 1.7

def imageSizeToString(size: ImageSize): String = size match
  case Small  => "small"
  case Medium => "medium"
  case Large  => "large"
