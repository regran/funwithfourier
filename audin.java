import java.io.*;
import javax.sound.sampled.*;



public class audin{
	File testFile = new File("intro.wav");
	int frames=0;
	try{
		AudioInputStream inp=AudioSystem.getAudioInputStream(testFile);
		
		AudioFileFormat form = AudioSystem.getAudioFileFormat(testFile);
		try{
			int frameSize=form.getFrameSize();
			if(framesize==AudioSystem.NOT_SPECIFIED){
				throw(new unspecifiedframe());
			}
		}catch(unspecifiedframe e){
			System.out.println("This format does not have a specified frame size.")
		}
		int numBytes=1024*frameSize;
		byte[] audioBytes = new byte[numBytes];
		try{
			int framesread=0;
			int bytesread=0;
			while((bytesread=inp.read(audioBytes)) != -1){
				framesread=bytesread/frameSize;
				frames+=framesread;
			}
		} catch(Exception e){
			System.out.println("RIP");
		}
	} catch(UnsupportedAudioFileException e){
		System.out.println("This file is unsupported. You should use waves tbh");
	} catch(Exception e){
		System.out.println("ERROR.");
	}
}