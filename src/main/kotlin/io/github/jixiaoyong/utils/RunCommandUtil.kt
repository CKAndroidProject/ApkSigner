import io.github.jixiaoyong.beans.CommandResult
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author : jixiaoyong
 * @description ：运行本机脚本
 * @email : jixiaoyong1995@gmail.com
 * @date : 3/10/2023
 */
object RunCommandUtil {

    /**
     * 执行bash命令
     * @return 成功返回null，失败返回Exception类型错误原因
     */
    fun runCommand(
        command: String,
        tag: String = "",
        printLog: Boolean = true,
        printError: Boolean = true
    ): Exception? {
        val logTag = if (tag.isBlank()) "" else "$tag: "

        Logger.info("$tag command: $command")

        return try {
            val process = Runtime.getRuntime().exec(command)
            val logBuffer = StringBuffer()
            val errBuffer = StringBuffer()

            if (printLog) {
                BufferedReader(InputStreamReader(process.inputStream)).useLines {
                    it.forEach { line ->
                        Logger.info("${logTag}$line")
                        logBuffer.append(line).append("\n")
                    }
                }
            }

            if (printError) {
                BufferedReader(InputStreamReader(process.errorStream)).useLines {
                    it.forEach { line ->
                        Logger.error("${logTag}$line")
                        errBuffer.append(line).append("\n")
                    }
                }
            }

            val result = process.waitFor()
            if (result == 0) {
                null
            } else {
                Exception("${logTag}exit code: $result\n${logBuffer}\nerr:${errBuffer}")
            }

        } catch (e: Exception) {
            if (printError) Logger.error("$tag error: $e")
            e
        }
    }

    fun runCommandWithResult(
        command: String,
        tag: String = "",
        printLog: Boolean = true,
        printError: Boolean = true
    ): CommandResult {
        val logTag = if (tag.isBlank()) "" else "$tag: "

        Logger.info("$tag command: $command")

        return try {
            val process = Runtime.getRuntime().exec(command)
            val logBuffer = StringBuffer()
            val errBuffer = StringBuffer()

            if (printLog) {
                BufferedReader(InputStreamReader(process.inputStream)).useLines {
                    it.forEach { line ->
                        Logger.info("${logTag}$line")
                        logBuffer.append(line).append("\n")
                    }
                }
            }

            if (printError) {
                BufferedReader(InputStreamReader(process.errorStream)).useLines {
                    it.forEach { line ->
                        Logger.error("${logTag}$line")
                        errBuffer.append(line).append("\n")
                    }
                }
            }

            val result = process.waitFor()
            if (result == 0) {
                CommandResult.Success(logBuffer.toString())
            } else {
                CommandResult.Error("${logTag}exit code: $result\n${logBuffer}\nerr:${errBuffer}")
            }

        } catch (e: Exception) {
            if (printError) Logger.error("$tag error: $e")
            CommandResult.Error("$tag error: $e", e)
        }
    }

}