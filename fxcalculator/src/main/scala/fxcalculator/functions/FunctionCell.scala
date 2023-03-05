package fxcalculator.functions

import com.gluonhq.charm.glisten.control.{CharmListCell, ListTile}
import javafx.geometry.Insets
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii}
import javafx.scene.paint.Color

object FunctionCell:
  def apply(): FunctionCell = new FunctionCell(new ListTile(), new ImageView())

  private val background = new Background(
    new BackgroundFill(Color.BEIGE, new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0))
  )

final class FunctionCell(tile: ListTile, imageView: ImageView) extends CharmListCell[FunctionEntry]:
  import FunctionCell._
  imageView.setFitHeight(15)
  imageView.setFitWidth(25)
  tile.setPrimaryGraphic(imageView)
  setText(null)

  /* In Android, cells of a scrollable list can be re-used when they leave the screen.
     Instead of  just sitting there in the memory, waiting to show up again, or being destroyed and recreated,
     Android can change their data to something else and display them as those which show up on the screen while
     the original ones are scrolled out of the screen. It saves resources, but goodbye FP.
     (In short, that's why the country's data is set here in `updateItem` instead of the constructor - it can be changed).
  */
  override def updateItem(item: FunctionEntry, empty: Boolean): Unit =
    super.updateItem(item, empty)
    (Option(item), empty) match
      case (Some(entry: NativeEntry), false) =>
        tile.textProperty.setAll(entry.textForm)
        tile.setWrapText(true)
        setGraphic(tile)
        tile.setBackground(background)
      case (Some(entry: CustomEntry), false) =>
        tile.textProperty.setAll(entry.textForm)
        tile.setWrapText(true)
        //val trashIcon = new Image(classOf[DialogCell].getResourceAsStream("trash.png"))
        //imageView.setImage(trashIcon)
        setGraphic(tile)
        tile.setBackground(background)
      case _ =>
        setGraphic(null)
