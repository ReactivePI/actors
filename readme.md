# Actor library for Reactive PI

Reactive PI is a library based on akka and scala that allows developers to
use the various I/O ports on their raspberry PI from Scala.

## How to use this library
There are two methods to use this library. You can use this actor library, which wraps your sensors as actors inside Akka. This is useful in cases where you want to user your sensors from other computers. 

If you don't want the overhead of [Akka](https://akka.io) you can look at the [core](https://github.com/reactivepi/core) library instead.
This library contains just the drivers to access GPIO and I2C on your Raspberry PI.

## Quickstart
### Setting up dependencies
To use this library, add a dependency to it in your `build.sbt` file:

``` sbt
libraryDependencies += "reactivepi" %% "actors" % "0.2"
```

### Accessing GPIO
You can access the GPIO bus on the raspberry PI from any actor:

``` scala
class LightsAndSwitches extends Actor {
  val input = GPIO.input(1)
  val output = GPIO.output(1)
  
  def receive = {
    case CheckSwitch => readSwitchStatus()
    case TurnOnLights => output ! GPIO.Write(1)
  }
  
  private def readSwitchStatus() {
    val switchStatus = input ? GPIO.Read
  }
}
```

In your actor, call `GPIO.input` or `GPIO.output` to configure a pin on the GPIO header as input or output.
You can get more information about which pins you can use for GPIO here: https://www.raspberrypi.org/documentation/usage/gpio/README.md

To send data to the GPIO output, use the `GPIO.Write` message and send it to an output pin actor.
To read data from a GPIO input, use the `GPIO.Read` message. This will return a `GPIO.Data` message with the value of the pin.

**Tip:** Unlike the core library, the actors library takes care of closing access to the GPIO pins automatically for you.

### Accessing I2C
To access a I2C device on your Raspberry PI, you can use the I2C actor from your own actor:

``` scala
class TemperatureSensor extends Actor {
  val sensor = I2C(1).device(0x18)
  
  def receive = {
    case GetTemperature => readSensorData()
  }
  
  private def readSensorData() {
    val output = sensor ! I2C.Read(0, 2)
    sender ! AmbientTemperature(output)
  }
}
```

I2C is called with the index of the bus you want to access. You then make a call to `device` to access a specific device.
This returns a reference to the I2C actor.

To read data from the device, you can send the `I2C.Read` message. This message accepts a register and the number of bytes to read.
You can leave out the register if the device doesn't support registers.

To write data, send the `I2C.Write` message. This message also a register address and a `Array[Byte]` instance. Again, as with the read operation you can leave out the register if your device doesn't support it.

## Development setup
The actors library has a dependency on the core library. It will automatically download the core library upon build.
Just make sure that if you're working on 0.2 of the actors library that you're also using version 0.2 of the core library.

To build and install the library on your local machine, use the following steps:

* `sbt compile`
* `sbt publishLocal`
