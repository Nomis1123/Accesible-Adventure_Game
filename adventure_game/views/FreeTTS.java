package views;

import java.util.*;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class FreeTTS implements TTSProvider{

    private Voice voice;
    private Thread speakThread;

    public FreeTTS() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    }

    @Override
    public void playText(String text, String voicename) {
        this.stopSpeech();
        this.speakThread = new Thread(() -> {
            try {
                VoiceManager vm = VoiceManager.getInstance();
                this.voice = vm.getVoice(voicename);
                voice.allocate();
                voice.speak(text);
                this.stopSpeech();
            } catch (Exception ignored) {}
        });
        this.speakThread.start();
    }


    public void stopSpeech(){
        if (!Objects.equals(this.voice, null)) {
            try {
                this.voice.deallocate();
            } catch (Exception ignored) {}
            this.voice = null;
            this.speakThread = null;
        }
    }

}
