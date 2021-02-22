package io.makingthematrix.scalaonandroid.comments.views

import com.gluonhq.charm.glisten.afterburner.GluonPresenter
import com.gluonhq.charm.glisten.control.FloatingActionButton
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon
import io.makingthematrix.scalaonandroid.comments.Comments
import io.makingthematrix.scalaonandroid.comments.model.Comment
import io.makingthematrix.scalaonandroid.comments.cloud.Service
import javafx.fxml.FXML
import javafx.scene.control.{Label, ListView}

import javax.inject.Inject
import scala.beans.BeanProperty

class CommentsPresenter extends GluonPresenter[Comments] {
  println("Comments presenter constructor")

  @Inject
  @FXML
  var service: Service = _

  @FXML
  @BeanProperty
  var comments: View = _

  @FXML
  @BeanProperty
  var commentsList: ListView[Comment] = _

  def initialize(): Unit = {
    println("CommentsPresenter initialize")
    comments.showingProperty.addListener((_, _, newValue) => {
      if (newValue) {
        val appBar = getApp.getAppBar
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(_ => getApp.getDrawer.open()))
        appBar.setTitleText("Comments")
      }
    })
    val floatingActionButton = new FloatingActionButton(
      MaterialDesignIcon.ADD.text,
      _ => AppViewManager.commentsView.switchView
    )
    floatingActionButton.showOn(comments)

    commentsList.setCellFactory(_ => CommentListCell())
    commentsList.setPlaceholder(new Label("There are no comments"))
    commentsList.setItems(service.commentsList)

    service.retrieveComments()
  }
}
