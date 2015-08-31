//  Copyright 2015 Willem Meints
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.

package nl.fizzylogic.reactivepi

import akka.actor.{Props, ActorLogging, Actor}
import nl.fizzylogic.reactivepi.gpio.{OutputPin, GPIODriver}

object GPIOOutputActor {
  def props(pinNumber: Int): Props = Props(new GPIOOutputActor(pinNumber))
}

class GPIOOutputActor(pinNumber: Int) extends Actor with ActorLogging {
  // scalastyle:off null
  // Disable null checking here, because we don't have an output pin yet during construction.
  // Only when the actor is started, we want to assign an output pin, so that when something
  // goes wrong and we restart the actor, things can be reinitialized properly.
  var outputPin: OutputPin = null
  // scalastyle:on null

  def receive: PartialFunction[Any, Unit] = {
    case GPIO.Write(data) => writeDataToPort(data)
  }

  private def writeDataToPort(data: Byte) = {
    if(data != 0) {
      outputPin.high()
    } else {
      outputPin.low()
    }
  }

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    outputPin = GPIODriver.output(pinNumber)
  }

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    outputPin.close()
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    outputPin.close()
  }
}
