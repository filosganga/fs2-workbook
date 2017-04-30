package com.github.filosganga.learning.fs2

import org.scalatest._
import fs2._

class PipesSpec extends WordSpec with Matchers {
  import Pipes._

  "The echo" should {
    "duplicate each item" in {
      Stream(1,2).pull(echo).toList should contain theSameElementsInOrderAs List(1,1,2,2)
    }

    "produce a chunk every pair" in {
      Stream(1,2).pull(echo).chunks.toList should contain theSameElementsInOrderAs List(Chunk.seq(Seq(1,1)), Chunk.seq(Seq(2,2)))
    }
  }

  "takeWhile" when {
    "the first item does not fatisfy the predicate" should {
      "return empty" in {
        Stream(5,10,15,20).pull(takeWhile(_ > 5)).toList should be(empty)
      }
    }

    "the first n elements do satisfy the predicate" should {
      "return all the n elements" in {
        Stream(5,10,15,20, 25).pull(takeWhile(_ < 20)).toList should contain theSameElementsInOrderAs List(5,10,15)
      }
    }
  }

  "index" should {
    "return element with index" in {
      Stream("A", "B", "C").pull(index).toList should contain theSameElementsInOrderAs List(0L->"A", 1L->"B", 2L->"C")
    }
  }

}
