package com.github.filosganga.learning.fs2

import fs2._
import scala.language.higherKinds
import scala.annotation.tailrec
import scala.concurrent.duration._

/**
 * Is interesting to note that this function are not stack-safe but because the
 * Types are stack-safe everything is ok.
 *
 * More to read on "Functional Programming in Scala" and the "trampolines paper"
 */
object Pipes {


  def echo[F[_],O](h: Handle[F,O]): Pull[F,O,Nothing] =
    for {
      // Pull is monad on R. and awaitLimit return an R that is tuple2
      (x, h1) <- h.await1
      tl <- Pull.output(Chunk.indexedSeq(Vector(x,x))) >> echo(h1)
    } yield tl


  def take[F[_], A](n: Long)(h: Handle[F,A]): Pull[F,A,Handle[F,A]] =
    if (n <= 0) {
      Pull.done
    } else {
      h.awaitLimit(if (n <= Int.MaxValue) n.toInt else Int.MaxValue).flatMap {
        case (chunk, h) => Pull.output(chunk) >> h.take(n - chunk.size.toLong)
      }
    }

  def takeWhile[F[_], O](p: O => Boolean)(h: Handle[F,O]): Pull[F,O,Nothing] = for {
    (x, h1) <- h.await1
    tl <- if(p(x)) Pull.output(Chunk.singleton(x)) >> takeWhile(p)(h1) else Pull.done
  } yield tl


  def index[F[_], O](h: Handle[F,O]): Pull[F, (Long,O), Nothing] = {
    def loop(index: Long)(h1: Handle[F,O]): Pull[F, (Long,O), Nothing] = {
      h1.await1.flatMap { case (x, h2) =>
        Pull.output(Chunk.singleton(index->x)) >> loop(index + 1)(h2)
      }
    }

    loop(0)(h)
  }

  def takeWithin[F[_], O](d: FiniteDuration)(h: Handle[F,O]): Pull[F, (Long,O), Nothing] = {
    ???
  }

}
