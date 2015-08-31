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

import akka.actor.{Props, Actor}
import nl.fizzylogic.reactivepi.i2c.{I2CDevice}

object I2CDeviceActor {
  def props(bus: Int, address: Int): Props = Props(new I2CDeviceActor(bus,address))
}

class I2CDeviceActor(bus: Int, address: Int) extends Actor {
  private val driver = I2CDevice.open(bus,address)

  def receive: PartialFunction[Any, Unit] = {
    case I2C.Read(register, length) => readFromDevice(register, length)
    case I2C.Write(register,data) => writeToDevice(register,data)
  }

  private def writeToDevice(register: Byte, data: Array[Byte]) = {
    if(register == -1) {
      driver.write(data)
    } else {
      driver.write(register, data)
    }
  }

  private def readFromDevice(register: Byte, length: Int) = {
    if(register == -1) {
      sender ! I2C.Data(driver.read(length))
    } else {
      sender ! I2C.Data(driver.read(register, length))
    }
  }
}
