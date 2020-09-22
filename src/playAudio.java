import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class playAudio {//plays audio
	public playAudio(String file) throws Exception {

		URL url = playAudio.class.getResource(file);
		AudioClip clip = Applet.newAudioClip(url);
		clip.play();

	}
}