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

import akka.actor.{Actor, ActorLogging, Props}
import nl.fizzylogic.reactivepi.gpio.{InputPin, GPIODriver}

object GPIOInputActor {
  def props(pinNumber: Int): Props = Props(new GPIOInputActor(pinNumber))
}

class GPIOInputActor(pinNumber: Int) extends Actor with ActorLogging {
  // scalastyle:off null
  // Disable null checking here, because we don't have an input pin yet during construction.
  // Only when the actor is started, we want to assign an input pin, so that when something
  // goes wrong and we restart the actor, things can be reinitialized properly.
  var inputPin : InputPin = null
  // scalastyle:on null

  def receive: PartialFunction[Any, Unit] = {
    case GPIO.Read => readDataFromPort()
  }

  private def readDataFromPort(): Unit = {
    val data = inputPin.read()
    sender ! GPIO.Data(data)
  }

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    inputPin = GPIODriver.input(pinNumber)
  }

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    inputPin.close()
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    inputPin.close()
  }
}
