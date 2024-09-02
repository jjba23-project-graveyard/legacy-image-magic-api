package imagemagic

import imagemagic.model.*

import java.awt.Image
import java.awt.image.BufferedImage

def scaleImage(bufferedImage: BufferedImage, imageSize: ImageSize): BufferedImage =
  convertToBufferedImage(
    bufferedImage.getScaledInstance(
      imageSizeToPixels(imageSize),
      (imageSizeToPixels(imageSize) / aspectRatioMultiplier).toInt,
      Image.SCALE_SMOOTH,
    )
  )

def convertToBufferedImage(img: Image): BufferedImage =
  val bi         =
    new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)
  val graphics2D = bi.createGraphics
  graphics2D.drawImage(img, 0, 0, null)
  graphics2D.dispose()
  bi

def getImageFileExtension(fileName: String): String =
  fileName.split('.').reverse.head

def getImageNameWithoutExtension(fileName: String): String =
  fileName.split('.').head
