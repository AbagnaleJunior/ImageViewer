package Application.GUI.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.*;

public class ImageViewerWindowController {
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    Timer timer;


    private boolean isRunning = false;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private Label imgTitle;

    @FXML
    private Label pixelCounterLbl;

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
            Image newImage = images.get(currentImageIndex);
            imageView.setImage(newImage);

            Platform.runLater(() -> {

                String s = images.get(currentImageIndex).getUrl();

                pixelCounterLbl.setText(countPixels(newImage));
                imgTitle.setText(s.substring(s.lastIndexOf("/") + 1));
            });
        }
    }

    private String countPixels(Image image) {
        int r = 0;
        int g = 0;
        int b = 0;
        int mixed = 0;

        final PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            throw new IllegalStateException();
        }
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                if (red > blue && red > green) {
                    r = r + 1;
                } else if (blue > red && blue > green) {
                    b = b + 1;
                } else if (green > blue && green > red) {
                    g = g + 1;
                } else {
                    mixed = mixed + 1;
                }
            }
        }

        return "Pixels: R:" + r + " G:" + g + " B:" + b + "";
    }

    @FXML
    private void handleBtnStartAction() {
        if (!isRunning) {
            // double sliderBarValue =

            timer = new Timer();
            currentImageIndex = 0;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    loadNextImage();
                }
            }, 1 * 1000, 1 * 1000);
            // }, sliderBarValue, sliderBarValue);

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