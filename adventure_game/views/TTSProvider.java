package views;
public interface TTSProvider {

    void playText(String speak, String voice);
    void stopSpeech();

}
