package imagemagic.view

import scalatags.Text.all.*
import zio.*

trait View:
  def renderSubmitImageForm: URIO[Any, String]

  def renderSubmittedPage(newFileNames: Seq[String]): URIO[Any, String]

object View:
  val web: ZLayer[Any, Nothing, View] = ZLayer.succeed(
    new View {
      private val sharedHead = head(
        link(
          rel  := "stylesheet",
          href := "https://cdn.jsdelivr.net/npm/water.css@2/out/light.min.css",
        )
      )

      def renderSubmitImageForm: URIO[Any, String] = ZIO.succeed(
        html(
          sharedHead,
          body(
            h1("Image Magic - Submit Image"),
            div(
              p(
                "Please submit an image using the form below, to receive the transformed variants:"
              ),
              form(
                enctype := "multipart/form-data",
                method  := "POST",
                action  := "/submit-image",
                p(
                  label(
                    "destination: ",
                    input("", id := "destination", name := "destination", `type` := "text"),
                  )
                ),
                p(
                  label(
                    "new file name: ",
                    input("", id := "newFileName", name := "newFileName", `type` := "text"),
                  )
                ),
                p(
                  label(
                    "image file: ",
                    input(
                      "",
                      id       := "imageFile",
                      name     := "imageFile",
                      `type`   := "file",
                      required := true,
                    ),
                  )
                ),
                p(button("✅ submit", `type` := "submit")),
              ),
            ),
          ),
        ).render
      )

      def renderSubmittedPage(newFileNames: Seq[String]): URIO[Any, String] = ZIO.succeed(
        html(
          sharedHead,
          body(
            h1("Image Magic - Success"),
            div(
              p("The image has been successfully converted into variants and uploaded!"),
              pre(code(newFileNames.mkString("\n"))),
              a("< back to submit image form", href := "/submit-image"),
            ),
          ),
        ).render
      )
    }
  )
