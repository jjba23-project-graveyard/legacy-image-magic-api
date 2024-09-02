package imagemagic.storage

import imagemagic.getImageFileExtension
import imagemagic.getImageNameWithoutExtension
import imagemagic.model.*
import imagemagic.scaleImage
import zio.*

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

trait Storage:
  def listItems(path: String): Task[String]

  def storeFile(
      file: File,
      fileName: String,
      destination: Option[String],
      newFileName: Option[String],
    ): Task[File]

  def resizeImageAction(file: File): Task[Seq[String]]

object Storage:
  val local: ZLayer[Any, Nothing, Storage] = ZLayer.succeed(
    new Storage {
      def getListOfFiles(dir: String): Task[Seq[String]] =
        ZIO.attempt {
          val file = new File(dir)
          Files.createDirectories(Paths.get(dir))

          file
            .listFiles
            .map(_.getPath)
            .toList
        }

      override def listItems(path: String): ZIO[Any, Throwable, String] =
        for {
          _  <- ZIO.logInfo(s"Fetching items from directory: $path")
          ls <- getListOfFiles(path)
        } yield if (ls.length == 0) "Empty directory" else ls.mkString("\n")

      def writeFileWithData(
          path: String,
          usedFileName: String,
          data: Array[Byte],
        ): File = {
        Files.createDirectories(Paths.get(path))
        val f   = new File(s"$path/$usedFileName")
        val out = new FileOutputStream(f)
        out.write(data)
        out.close()
        f
      }

      override def storeFile(
          file: File,
          fileName: String,
          destination: Option[String],
          newFileName: Option[String],
        ): Task[File] =
        ZIO.attempt {
          val imageData = Files.readAllBytes(file.toPath)

          val path = if (destination.isDefined) {
            s"static/${destination.get}"
          }
          else {
            "static"
          }

          val usedFileName =
            if (newFileName.isDefined) s"${newFileName.get}.${getImageFileExtension(fileName)}"
            else fileName

          writeFileWithData(path, usedFileName, imageData)
        }

      override def resizeImageAction(file: File): Task[Seq[String]] =
        val bufferedImage = ImageIO.read(file)
        val ext           = getImageFileExtension(file.getName())
        val fName         = getImageNameWithoutExtension(file.getName())

        val xs = Seq(
          (
            s"$fName-${imageSizeToString(Small)}.$ext",
            scaleImage(bufferedImage, Small),
          ),
          (
            s"$fName-${imageSizeToString(Medium)}.$ext",
            scaleImage(bufferedImage, Medium),
          ),
          (
            s"$fName-${imageSizeToString(Large)}.$ext",
            scaleImage(bufferedImage, Large),
          ),
        )

        ZIO.attempt {
          xs.foreach { (imageName, image) =>
            val baos = new ByteArrayOutputStream()
            ImageIO.write(image, getImageFileExtension(file.getName()), baos)
            val data = baos.toByteArray()
            val p    = file.getPath().split('/').reverse.tail.reverse.mkString("/")
            writeFileWithData(
              p,
              imageName,
              data,
            )
          }
          xs.map(x => x._1)
        }
    }
  )
