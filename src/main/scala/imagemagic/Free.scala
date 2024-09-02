package imagemagic

import imagemagic.model.*
import imagemagic.storage.Storage
import imagemagic.view.View
import zio.*

import java.awt.image.BufferedImage
import java.io.File

object Free:
  def listItems: URIO[Storage, Either[String, String]] =
    ZIO.serviceWithZIO[Storage] { storage =>
      storage
        .listItems("static")
        .either
        .map(itemList => itemList.left.map(_.toString))
    }

  def healthCheck: URIO[Any, Either[String, String]] = ZIO.right("UP")

  def renderSubmitImageForm: URIO[View, String] =
    ZIO.serviceWithZIO[View](_.renderSubmitImageForm)

  def submitImageAction(submitImageRequest: SubmitImageData): URIO[Storage & View, String] =
    (for {
      storage      <- ZIO.service[Storage]
      view         <- ZIO.service[View]
      _            <- ZIO.logInfo(s"input: ${submitImageRequest.imageFile.headers}")
      f            <- storage
                        .storeFile(
                          file = submitImageRequest.imageFile.body,
                          fileName = submitImageRequest
                            .imageFile
                            .fileName
                            .getOrElse(""),
                          destination = submitImageRequest.destination,
                          newFileName = submitImageRequest.newFileName,
                        )
      newFileNames <- storage.resizeImageAction(f)
      resultPage   <- view.renderSubmittedPage(newFileNames)
    } yield resultPage).orDie
