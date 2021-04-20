package Application.GUI.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.*;

public class ImageViewerWindowController {
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    Timer timer = new Timer();

    private boolean isRunning = false;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private Label imgTitle;

    private void loadNextImage() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }


    @FXML
    private void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty()) {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
            });
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction() {
        loadNextImage();
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));

            String s = images.get(currentImageIndex).getUrl();
            imgTitle.setText(s.substring(s.lastIndexOf("/") + 1));
        }
    }

    @FXML
    private void handleBtnStartAction() {
        if (!isRunning) {
            currentImageIndex = 0;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    loadNextImage();
                }
            }, 1 * 1000, 1 * 1000);
            isRunning = true;
        }

    }

    @FXML
    private void handleBtnStopAction() {
        try {
            if (isRunning) {
                timer.cancel();
                timer.purge();
                isRunning = false;
            }
        } catch (Exception e) {

        }


    }
}