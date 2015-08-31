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

import akka.actor.{ActorRef, ActorRefFactory}

object I2C {

  case class Read(register: Byte = -1, length: Int)

  case class Data(buffer: Array[Byte])

  case class Write(register: Byte = -1, data: Array[Byte])

}

/**
 * Provides access to the I2C bus through Akka actors
 * @param bus Number of the bus to use (1 or 2 on the raspberry PI)
 * @param actorRefFactory The actor ref factory to use
 */
case class I2C(bus: Int)(implicit actorRefFactory: ActorRefFactory) {
  /**
   * Get access to a device on the specified address
   * @param address Address of the bus
   * @return        Returns the actor for the device
   */
  def device(address: Int): ActorRef = {
    actorRefFactory.actorOf(I2CDeviceActor.props(bus, address))
  }
}
