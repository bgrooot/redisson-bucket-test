package redissionTest

import org.redisson.Redisson
import org.redisson.config.Config
import java.util.concurrent.Executors

fun main() {
    val config = Config.fromYAML(ClassLoader.getSystemResource("redisson.yaml"))
    val client = Redisson.create(config)

    val bucket1 = client.getBucket<String>("STRING1")
    val bucket2 = client.getBucket<String>("STRING2")

    bucket1.set("STRING1")
    bucket2.set("STRING2")

    val executor1 = Executors.newCachedThreadPool()
    val executor2 = Executors.newCachedThreadPool()

    println(executor1.toString())
    println(executor2.toString())

    while (true) {
        executor1.submit {
            val get = bucket1.get()
            if (get.equals("STRING2")) {
                println("[FATAL] EXECUTOR1: $get")
                System.exit(0)
            }
        }

        executor2.submit {
            val get = bucket2.get()
            if (get.equals("STRING1")) {
                println("[FATAL] EXECUTOR2: $get")
                System.exit(0)
            }
        }
    }
}
