//implementing all sound effects 

import java.net.URL;
import javax.sound.sampled.*;

public class sEffects{
	static Clip clip;
	static URL soundURL[] = new URL[30];
	
	
	public static void main(String[] args){
		
	}


	public sEffects() {
		soundURL[0] = getClass().getResource("background_music.wav"); 
		soundURL[1] = getClass().getResource("ball_off_paddles.wav");
		soundURL[2] = getClass().getResource("ball_off_wall.wav");
		soundURL[3] = getClass().getResource("scoring_point.wav");
		soundURL[4] = getClass().getResource("winner_announced.wav");	
        soundURL[5] = getClass().getResource("eat.wav");		
	}


	public void setFile(int x) {
        try{
            AudioInputStream audio = AudioSystem.getAudioInputStream(soundURL[x]);
            clip = AudioSystem.getClip();
            clip.open(audio);

        } catch (Exception e) {
        }
    }


    public void Play(){
        clip.start();
    }

    public void Loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }




}