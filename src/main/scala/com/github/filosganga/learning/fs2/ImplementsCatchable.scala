package com.github.filosganga.learning.fs2

import java.io.{FileInputStream, InputStream}

import fs2.Task
import fs2.io._
import fs2.Stream
import fs2.util.{Attempt, Catchable}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object ImplementsCatchable extends App {

  implicit def OptionCatchable = new Catchable[Option] {
    override def fail[A](err: Throwable): Option[A] = None

    override def attempt[A](fa: Option[A]): Option[Attempt[A]] = {
      fa.map(a => Attempt(a))
    }

    override def flatMap[A, B](a: Option[A])(f: (A) => Option[B]): Option[B] = a.flatMap(f)

    override def pure[A](a: A): Option[A] = Some(a)
  }

  implicit def FutureCatchable(implicit ec: ExecutionContext) = new Catchable[Future] {
    override def fail[A](err: Throwable): Future[A] = Future.failed(err)

    override def attempt[A](fa: Future[A]): Future[Attempt[A]] = fa
      .map(a => Attempt(a))
      .recover {
        case err => Attempt.failure(err)
      }

    override def flatMap[A, B](a: Future[A])(f: (A) => Future[B]): Future[B] = a.flatMap(f)

    override def pure[A](a: A): Future[A] = Future.successful(a)
  }

  val eff = Stream.eval(Option(1))

//  println(eff.runLog)
//  println(eff.run)
//  println(eff.runLast)
//  println(eff.runFold(0) { (acc, y) => acc + y })
//  println(eff.runFree)

  implicit val ec = ExecutionContext.global
  val eff2: Stream[Future, Int] = Stream.eval(Future(1 + 1)).flatMap(i => Stream.eval(Future(i + 1)))

  println(Await.result(eff2.runLast, 10.seconds))

  def acquire: Future[InputStream] = Future {
    new FileInputStream("tmp")
  }

  def release(inputStream: InputStream): Future[Unit] =
    Future(inputStream.close())

  val s1 = Stream.bracket(acquire)(input => Stream.eval(Future(input.available())), release)

  println(Await.result(s1.runLast, 10.seconds))
}

