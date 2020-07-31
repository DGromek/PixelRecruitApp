package com.sample.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ImagePanelController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView imageView;

    AnchorPane getAnchorPane() {
        return anchorPane;
    }

    ImageView getImageView() {
        return imageView;
    }

    boolean isXInBounds(double xValue) {
        return xValue >= 0 && xValue <= imageView.boundsInParentProperty()
                                                 .get()
                                                 .getWidth();
    }

    boolean isYInBounds(double yValue) {
        return yValue >= 0 && yValue <= imageView.boundsInParentProperty()
                                                 .get()
                                                 .getHeight();
    }

}
