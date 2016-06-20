import java.io.*;
import javax.sound.sampled.*;

/* 		some stuff for reference	
				int sampleSize=form.getSampleSizeInBits(); //bits in a sample
				float sampleRate=form.getSampleRate(); //samples per second, Hz
				int timeInterval = audioLength/samples; //time between samples */

public class audin{
	
	private File f;
	private AudioFormat form;
	private AudioInputStream inp;
	
	public audin(String s){ //construct input with format info using file name
		f=new File(s);
		try{
			inp=AudioSystem.getAudioInputStream(f);
			form=inp.getFormat();
		} catch(Exception e){
			System.out.println("Something went wrong");
		}
		
	}
	
	public AudioFormat getForm(){
		return form;
	}
	
	public AudioInputStream getStream(){
		return inp;
	}
	
	public byte[] getdataFromFile(){
		int frames=0;
		int frameSize=form.getFrameSize(); //number of samples in a frame
		int numBytes=1024*frameSize; //1024 samples in array
		byte[] audioBytes = new byte[numBytes];
		try{
			int framesread=0;
			int bytesread=0;
			while((bytesread=inp.read(audioBytes, 0, audioBytes.length)) != -1){
				framesread=bytesread/frameSize;
				frames+=framesread;
			}
		} catch(Exception e){
			System.out.println("RIP");
		}

		return audioBytes;
	}
	
	public double[] toDouble(byte[] b){ //precond: byte array of audio data
		//semi-redundant references
		float frameLength=inp.getFrameLength(); //frames in sample
		float frameRate=form.getFrameRate(); //frames per second
		float audioLength=frameLength/frameRate; //length of sample in seconds
		float sampleRate=form.getSampleRate(); //samples per second, Hz
		int samples = (int)(audioLength*sampleRate/2);  //n

		boolean isBig = form.isBigEndian(); //little or big endian format
		double[] dubs = new double[1024]; //arbitrary-ish length for now
		for(int i=0; i<dubs.length*2; i+=2){ 	//convert pairs of bytes to Endian values
			int b1=b[i];
			int b2=b[i+1];
		//	if(b1<0) b1+= 0x100;
		//	if(b2<0) b2+=0x100;
			
			if(isBig) dubs[i/2] = b1 + (b2<<8);
			else dubs[i/2]=(b1<<8) + b2;
		}
		return dubs; //postcond: samples as doubles
	}
	public static void main(String[] args){
		audin input = new audin("c1.wav");
		byte[] b = input.getdataFromFile();
		double[] d = input.toDouble(b);
		Complex[] comp = Complex.makeComp(d);
		Complex[] p = fft.pad(comp);
		Complex[] freq = fft.transform(p);
		for(int k=0; k<d.length; k++) {
			//output frequency domain or time domain
//			Complex.printlnComp(freq[k]);
			System.out.println(d[k]);
		}
	}
}
