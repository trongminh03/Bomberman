package uet.oop.bomberman.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.gui.MenuViewManager;


public class OptionController extends HBox {
    private ImageView speaker;
    Image speakerOn;
    Image silent;

    private Slider slider;

    public OptionController() {

    }

    public OptionController(String type) {
        speakerOn = new Image("/model/img/sound.png");
        silent = new Image("/model/img/silent.png");
        speaker = new ImageView();
        speaker.setImage(speakerOn);
//        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        setupSlider(type);
        this.getChildren().add(speaker);
        this.getChildren().add(slider);
    }

    private void setupSlider(String type) {
        slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        if (type.equals(AudioManager.BACKGROUND_MUSIC)) {
            slider.setValue(AudioManager.BGVolume * 100);
        } else {
            slider.setValue(AudioManager.GPVolume * 100);
        }
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(10);
    }

    public void changeVolume(AudioManager audio) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (slider.getValue() == 0) {
                    speaker.setImage(silent);
                    AudioManager.setSoundOn(false, audio.getType());

                } else {
                    speaker.setImage(speakerOn);
                    AudioManager.setVolume(slider.getValue() * 0.01, audio.getType());
                    AudioManager.setSoundOn(true, audio.getType());
                }
                MenuViewManager.playMenuMusic();
            }
        });
    }

    public void changeVolume(String type) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (slider.getValue() == 0) {
                    speaker.setImage(silent);
                    AudioManager.setSoundOn(false, type);

                } else {
                    speaker.setImage(speakerOn);
                    AudioManager.setVolume(slider.getValue() * 0.01, type);
                    AudioManager.setSoundOn(true, type);
                }
            }
        });
    }

    public Slider getSlider() {
        return slider;
    }
}
