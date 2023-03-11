package fxcalculator

import fxcalculator.logic.{Dictionary, EvaluationResults, Evaluator, Parser, Preprocessor}
import fxcalculator.logic.expressions.{Constant, ConstantAssignment, Error, Expression, FunctionAssignment, NativeFunction}
import fxcalculator.functions.Storage
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, OverrunStyle}

import scala.math.round
import scala.util.chaining.scalaUtilChainingOps

object MainController:
  private val idsToSigns = Map(
    "b1" -> '1', "b2" -> '2', "b3" -> '3', "b4" -> '4', "b5" -> '5', "b6" -> '6', "b7" -> '7', "b8" -> '8', "b9" -> '9', "b0" -> '0',
    "bPoint" -> '.', "bAdd" -> '+', "bSubstract" -> '-', "bMultiply" -> '*', "bDivide" -> '/',
    "lParens" -> '(', "rParens" -> ')', "bPower" -> '^'
  )
  private val operators = Set('+', '-', '*', '/', '^', '(', ')')
  private val numbers = Set('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

final class MainController:
  import MainController.*

  @FXML private var expression: Label = _

  private var clearExpression = true
  private var memory: Option[String] = None

  private lazy val parser = ParserCreator.createParser(withNativeFunctions = true, withConstants = true, withStorage = true)

  def initialize(): Unit =
    expression.setText("0.0")
    expression.setTextOverrun(OverrunStyle.CLIP)

  def onEvaluate(event: ActionEvent): Unit =
    evaluate(expression.getText) match
      case Right(text) =>
        expression.setText(text)
      case Left(text) =>
        expression.setText(text)
        clearExpression = true

  def onMemoryPlus(event: ActionEvent): Unit =
    evaluate(expression.getText) match
      case Right(result) =>
        memory = Some(result)
      case Left(error) =>
        expression.setText(error)
        clearExpression = true

  def onMemoryReveal(event: ActionEvent): Unit =
    memory.foreach(updateExpression(_, true))

  def onFx(event: ActionEvent): Unit =
    val text = AdvancedEditor.showDialog(parser.dictionary)
    if text.nonEmpty then
      evaluate(text) match
        case Right(result) =>
          updateExpression(result, true)
        case Left(error) =>
          expression.setText(error)
          clearExpression = true
  
  def onClear(event: ActionEvent): Unit =
    expression.setText("0.0")

  def onBackspace(event: ActionEvent): Unit = expression.getText match
    case ""   =>
    case text => expression.setText(text.init)

  def onOperator(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId) match
      case Some(op) if isOperatorAllowed(op) => updateExpression(op)
      case _ =>

  def onNumber(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId).foreach(updateExpression)

  def onPoint(event: ActionEvent): Unit =
    if isPointAllowed then updateExpression('.')

  private def evaluate(text: String): Either[String, String] =
    val result = Evaluator(parser).evaluate(text)
    result match
      case EvaluationResults(_, Some(err), _) =>
        Left(err.toString)
      case EvaluationResults(None, _, assignments) if assignments.nonEmpty =>
        assignments.map(_.textForm).foreach(Storage.append)
        Left(assignments.head.textForm)
      case EvaluationResults(Some(res), _, assignments) =>
        assignments.map(_.textForm).foreach(Storage.append)
        Right(res.toString)
      case _ =>
        Right("")

  private def updateExpression(newSign: Char): Unit = updateExpression(newSign.toString)

  private def updateExpression(newStr: String, onlyAfterOperator: Boolean = false): Unit =
    expression.getText match
      case currentExpr if currentExpr.isEmpty || clearExpression || currentExpr == "0.0" =>
        expression.setText(newStr)
        clearExpression = false
      case currentExpr if onlyAfterOperator && operators.contains(currentExpr.last) =>
        expression.setText(s"$currentExpr$newStr")
      case currentExpr if !onlyAfterOperator =>
        expression.setText(s"$currentExpr$newStr")
      case _ =>

  private def isPointAllowed: Boolean =
    val currentExpr = expression.getText
    val commaIndex = currentExpr.lastIndexOf('.')
    if commaIndex > -1 then
      operators.map(currentExpr.lastIndexOf(_)).max > commaIndex
    else
      currentExpr.lastOption.forall(numbers.contains)

  private def isOperatorAllowed(operator: Char): Boolean = (expression.getText, operator) match
    case (".", _)                                               => false
    case (currentExpr, _) if currentExpr == Double.NaN.toString => false
    case (currentExpr, '-')                                     => !currentExpr.lastOption.contains('-')
    case ("0", _)                                               => false
    case (currentExpr, '(')                                     => currentExpr.lastOption.forall(operators.contains)
    case (currentExpr, _)                                       => !currentExpr.lastOption.forall(operators.contains)
