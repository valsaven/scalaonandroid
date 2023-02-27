package fxcalculator

import fxcalculator.logic.expressions.{Assignment, Constant, NativeFunction}
import fxcalculator.logic.{Dictionary, Parser}
import fxcalculator.storage.Storage

import scala.util.chaining.scalaUtilChainingOps

object ParserCreator:
  import NativeFunction.{f1, f2}

  private val nativeFunctions: Set[NativeFunction] = Set(
    f1("abs", math.abs),
    f2("min", math.min),
    f2("max", math.max),
    f1("signum", math.signum),
    f1("sqrt", math.sqrt),
    f1("cbrt", math.cbrt),
    f1("exp", math.exp),
    f1("log", math.log),
    f1("log1p", math.log1p),
    f1("log10", math.log10),
    f1("sin", math.sin),
    f1("cos", math.cos),
    f1("tan", math.tan),
    f1("sinh", math.sinh),
    f1("cosh", math.cosh),
    f1("tanh", math.tanh),
    f1("asin", math.asin),
    f1("acos", math.acos),
    f1("atan", math.atan),
    f1("toRadians", math.toRadians),
    f1("toDegrees", math.toDegrees),
    f2("atan2", math.atan2),
    f2("hypot", math.hypot),
    f1("ceil", math.ceil),
    f1("floor", math.floor),
    f1("round", math.rint),
    f1("ulp", math.ulp)
  )

  private val assignments: Set[Assignment] = Set(
    Assignment("Pi", Constant(math.Pi)),
    Assignment("E", Constant(math.E)),
    Assignment("Cm", Constant(299792458.0)),
    Assignment("Ckm", Constant(299792.458))
  )

  def createParser(withNativeFunctions: Boolean = false, withConstants: Boolean = false, withStorage: Boolean = false): Parser =
    val dictionary = Dictionary().tap { dict =>
      if withNativeFunctions then nativeFunctions.foreach(f => dict.add(f.name, f))
      if withConstants then assignments.foreach { a => dict.add(a.name, a) }
    }
    Parser(dictionary = dictionary).tap { parser =>
      if withStorage then Storage.readIn(parser)
    }
