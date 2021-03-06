/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kafka.message

import java.io.ByteArrayOutputStream

import org.junit.Assert._
import org.junit._

class MessageCompressionTest {

  @Test
  def testSimpleCompressDecompress() {

  }

  //  A quick test to ensure any growth or increase in compression size is known when upgrading libraries
  @Test
  def testCompressSize() {

  }

  def testSimpleCompressDecompress(compressionCodec: CompressionCodec) {
    val messages = List[Message](new Message("hi there".getBytes), new Message("I am fine".getBytes), new Message("I am not so well today".getBytes))
    val messageSet = new ByteBufferMessageSet(compressionCodec = compressionCodec, messages = messages:_*)
    assertEquals(compressionCodec, messageSet.shallowIterator.next().message.compressionCodec)
    val decompressed = messageSet.iterator.map(_.message).toList
    assertEquals(messages, decompressed)
  }

  def testCompressSize(compressionCodec: CompressionCodec, messages: List[Message], expectedSize: Int) {
    val messageSet = new ByteBufferMessageSet(compressionCodec = compressionCodec, messages = messages:_*)
    assertEquals(s"$compressionCodec size has changed.", expectedSize, messageSet.sizeInBytes)
  }


  def isLZ4Available: Boolean = {
    try {
      new net.jpountz.lz4.LZ4BlockOutputStream(new ByteArrayOutputStream())
      true
    } catch {
      case _: UnsatisfiedLinkError => false
    }
  }
}
