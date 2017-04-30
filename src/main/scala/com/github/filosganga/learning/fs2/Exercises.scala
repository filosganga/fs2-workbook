package com.github.filosganga.learning.fs2

import fs2._
import fs2.Stream

import scala.language.higherKinds

object Exercises {

  implicit class StreamWithRepeating[F[_], O](s: Stream[F, O]) { self =>
    def repeating: Stream[F, O] = s ++ self.repeating
  }

  implicit class StreamWithDraining[F[_], O](s: Stream[F, O]) { self =>
    def draining: Stream[F, O] = s >> Stream.empty
  }


  val s: Stream[Nothing, Int] = Stream(1,0)

  val s1: Stream[Nothing, Int] = new StreamWithRepeating(s).repeating

  val s2: Stream[Nothing, Int] = s.repeating

  val s3: Stream[Nothing, Int] = s.repeating

  val s4 = s.draining

}
