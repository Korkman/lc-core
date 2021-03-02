package lc

import lc.core.{Captcha, CaptchaProviders}
import lc.server.Server
import lc.background.BackgroundTask
import lc.core.Config

object LCFramework {
  def main(args: scala.Array[String]): Unit = {
    val captcha = new Captcha()
    val server = new Server(port = Config.getPort, captcha = captcha)
    val backgroundTask = new BackgroundTask(
      captcha = captcha,
      throttle = Config.getThrottle,
      timeLimit = Config.getCaptchaExpiryTimeLimit
    )
    backgroundTask.beginThread(delay = Config.getThreadDelay)
    server.start()
  }
}

object MakeSamples {
  def main(args: scala.Array[String]): Unit = {
    val samples = CaptchaProviders.generateChallengeSamples()
    samples.foreach {
      case (key, sample) =>
        val extensionMap = Map("image/png" -> "png", "image/gif" -> "gif")
        println(key + ": " + sample)

        val outStream = new java.io.FileOutputStream("samples/" + key + "." + extensionMap(sample.contentType))
        outStream.write(sample.content)
        outStream.close
    }
  }
}
