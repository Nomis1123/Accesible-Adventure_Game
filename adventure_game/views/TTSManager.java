package views;

public class TTSManager {
    private TTSProvider ttsProvider;

    public TTSManager(TTSProvider ttsProvider) {
        this.ttsProvider = ttsProvider;
    }

    public void playSpeech(String text, String voice) {
        ttsProvider.playText(text, voice);
    }
    public void stopSpeech(){
        ttsProvider.stopSpeech();
    }

}
