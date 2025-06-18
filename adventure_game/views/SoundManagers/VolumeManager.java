package views.SoundManagers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class VolumeManager.  Helper class used to manage volume changing over time.
 */
class VolumeManager {
    private HashMap<String, Timeline> mediaTimelines; // An attribute that stores timelines based on their sound type.

    /**
     * Initializes attributes
     */
    public VolumeManager() {
        this.mediaTimelines = new HashMap<String, Timeline>();
    }


    /**
     * Sets the volume property to the target volume over a given duration.
     * If the duration is 0.0 then the volume will be set instantly with no delay.
     *
     * @param soundType type of the sound.
     * @param volumeProperty volumeProperty of the target sound.
     * @param targetVolume volume the sound should be after the given duration.
     * @param duration duration in seconds to change the volume to the target volume.
     */
    public void manageVolume(String soundType, DoubleProperty volumeProperty, double targetVolume, double duration) {
        this.killVolumeManagement(soundType);
        if (duration <= 0.0) {
            volumeProperty.setValue(targetVolume);
        }
        else {
            //TODO: Linear or ease in/out? -Half
            Interpolator interpolator = volumeProperty.getValue() > targetVolume ? Interpolator.EASE_OUT : Interpolator.EASE_IN;
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), new KeyValue(volumeProperty, targetVolume, interpolator)));
            timeline.play();
            this.mediaTimelines.put(soundType, timeline);
        }
    }

    /**
     * Returns a boolean based on if the sound corresponding to a sound type is having its volume managed.
     *
     * @param soundType type of the sound.
     * @return boolean corresponding to the management of the given sound type.
     */
    public boolean isManagingVolume(String soundType) {
        Timeline timeline = this.mediaTimelines.getOrDefault(soundType, null);
        return !Objects.equals(timeline, null);
    }

    /**
     * Kills the volume management associated with the given sound type.
     *
     * @param soundType type of the sound.
     */
    public void killVolumeManagement(String soundType) {
        if (this.isManagingVolume(soundType)) {
            this.mediaTimelines.get(soundType).stop();
            this.mediaTimelines.put(soundType, null);
        }
    }
}
