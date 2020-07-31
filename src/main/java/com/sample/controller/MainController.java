package com.sample.controller;

import com.sample.model.CloneableCircle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    private static final int CIRCLE_RADIUS = 3;
    private static final int CHANGE_CIRCLE_X_TEXTFIELD_INDEX = 2;
    private static final int CHANGE_CIRCLE_Y_TEXTFIELD_INDEX = 4;
    //Used when any element from the given list will be correct
    private static final int UNRELEVANT_INDEX = 0;
    private static int pointIdx = 0;

    private Random rand = new Random();
    //It's the controller variable - needed to make changes to the inboxes during draging points.
    private Map<Integer, Pane> pointListMap = new HashMap<>();
    //It's the view variable - used only to attach next circles to the list in the view
    @FXML
    private VBox pointListView;

    private List<ImagePanelController> controllers;

    @FXML
    private ImagePanelController imagePanel1Controller;

    @FXML
    private ImagePanelController imagePanel2Controller;

    @FXML
    private ImagePanelController imagePanel3Controller;

    @FXML
    private ImagePanelController imagePanel4Controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controllers = getImagePanelControllersAsList(imagePanel1Controller, imagePanel2Controller, imagePanel3Controller,
                imagePanel4Controller);

        for (ImagePanelController controller : controllers) {
            controller.getImageView()
                      .setOnMouseClicked(this::makeCircle);
        }
    }

    private void makeCircle(MouseEvent mouseEvent) {
        Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f);
        try {
            List<Circle> circles = getXSizeListOfSameCircles(controllers.size(),
                    new CloneableCircle(mouseEvent.getX(), mouseEvent.getY(), CIRCLE_RADIUS, color));

            setUpOnMouseDragBehaviour(circles);

            Pane circleListElement = getCircleListElement(circles, "Point" + pointIdx++);
            for (int i = 0; i < controllers.size(); i++) {
                controllers.get(i)
                           .getAnchorPane()
                           .getChildren()
                           .add(circles.get(i));
            }

            pointListView.getChildren()
                         .addAll(circleListElement);
            pointListMap.put(circles.hashCode(), circleListElement);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /*
    Following method sets correct behaviour for each circle during drag gesture - while any circle is dragged all circles from given List
    also starts to move and values of X and Y are changing in corresponding inboxes.
    */
    private void setUpOnMouseDragBehaviour(List<Circle> circles) {
        for (Circle circle : circles) {
            circle.setOnMouseDragged(mouseEvent1 -> {
                Pane currentlyDragged = pointListMap.get(circles.hashCode());
                TextField xField = (TextField) currentlyDragged.getChildren()
                                                               .get(CHANGE_CIRCLE_X_TEXTFIELD_INDEX);
                TextField yField = (TextField) currentlyDragged.getChildren()
                                                               .get(CHANGE_CIRCLE_Y_TEXTFIELD_INDEX);
                double mouseX = mouseEvent1.getX();
                double mouseY = mouseEvent1.getY();

                if (controllers.get(UNRELEVANT_INDEX)
                               .isXInBounds(mouseX) && controllers.get(UNRELEVANT_INDEX)
                                                                  .isYInBounds(mouseY)) {
                    xField.setText("" + (int) mouseX);
                    yField.setText("" + (int) mouseY);

                    for (Circle value : circles) {
                        value.setCenterX(mouseX);
                        value.setCenterY(mouseY);
                    }
                }
            });
        }
    }

    //Method return HBox representing point and its properties
    private Pane getCircleListElement(List<Circle> circleList, String circleName) {
        HBox hBox = new HBox();
        hBox.setSpacing(3.0);
        hBox.setPadding(new Insets(3, 3, 3, 3));

        Text pointName = new Text(circleName);
        pointName.setFill(circleList.get(UNRELEVANT_INDEX)
                                    .getFill());

        Text xLabel = new Text("x: ");
        TextField xField = new TextField("" + (int) circleList.get(UNRELEVANT_INDEX)
                                                              .getCenterY());
        xField.textProperty()
              .addListener((observable, oldValue, newValue) -> {
                  try {
                      double newValueParsed = Double.parseDouble(newValue);

                      if (controllers.get(UNRELEVANT_INDEX)
                                     .isXInBounds(newValueParsed)) {
                          for (Circle circle : circleList) {
                              circle.setCenterX(newValueParsed);
                          }
                      }
                  } catch (NumberFormatException e) {
                  }
              });

        Text yLabel = new Text("y: ");
        TextField yField = new TextField("" + (int) circleList.get(UNRELEVANT_INDEX)
                                                              .getCenterY());
        yField.textProperty()
              .addListener((observable, oldValue, newValue) -> {
                  try {
                      double newValueParsed = Double.parseDouble(newValue);

                      if (controllers.get(UNRELEVANT_INDEX)
                                     .isYInBounds(newValueParsed)) {
                          for (Circle circle : circleList) {
                              circle.setCenterY(newValueParsed);
                          }
                      }
                  } catch (NumberFormatException e) {
                  }
              });

        hBox.getChildren()
            .addAll(pointName, xLabel, xField, yLabel, yField);
        return hBox;
    }

    private List<ImagePanelController> getImagePanelControllersAsList(ImagePanelController... imagePanelControllers) {
        return Arrays.asList(imagePanelControllers);
    }

    private List<Circle> getXSizeListOfSameCircles(int x, CloneableCircle circleToCopy) throws CloneNotSupportedException {
        List<Circle> result = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            result.add((Circle) circleToCopy.clone());
        }
        return result;
    }
}
