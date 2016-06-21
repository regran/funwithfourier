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
			//if(b2<0) b2+=0x100;
			
			if(isBig) dubs[i/2] = b1 + (b2<<8);
			else dubs[i/2]=(b1<<8) + b2;
		}
		return dubs; //postcond: samples as doubles
	}
	
	public static double sumofdif(double[] mags1, double[] mags2){ //precond: mags1 and mags 2 same length
		double sum = 0;
		for(int i=0; i<mags1.length; i++){
			sum+=mags2[i]-mags1[i];
		}
		return Math.abs(sum); //compares two sets of data by summing the differences between corresponding data points
	}
	
	public static double findMax(double[] d){ //returns largest value of a list of doubles
		double max = Double.MIN_VALUE;
		for(double dee:d){
			if(dee>max) max=dee;
		}
		return max;
	}
	
	public static double[] normalize(double[] mag){ //return normalized list of magnitudes of values from 0 to 1 (inclusive)
		double max=findMax(mag);
		double[] norm=new double[mag.length];
		for(int i = 0; i<mag.length;i++){
			norm[i]=mag[i]/max;
		}
		return norm;
	}
	
	
	
	public static void main(String[] args){ //args[0] soundfilename1, args[1] output file 1, args[2] sound file 2, args[3] output 2
		audin input = new audin(args[0]);
		byte[] b = input.getdataFromFile();
		double[] d = audin.normalize(input.toDouble(b));
		Complex[] comp = Complex.makeComp(d);
		Complex[] p = fft.pad(comp);
		Complex[] freq = fft.transform(p);
		audin input2=new audin(args[2]);
		byte[] b2 = input2.getdataFromFile();
		double[] d2=audin.normalize(input2.toDouble(b2));
		Complex[] comp2=Complex.makeComp(d2);
		Complex[] p2=fft.pad(comp2);
		Complex[]freq2=fft.transform(p2);
		double[] mag1=(Complex.getMagnitudes(freq));
		double[] mag2=(Complex.getMagnitudes(freq2));
		System.out.println(audin.sumofdif(mag1, mag2));
		try {
			PrintStream out=new PrintStream(new FileOutputStream(args[1]));
			System.setOut(out);
		}
		catch(Exception e) {
			System.out.println("nope");
		}
		
		for(int k=0; k<d.length/2; k++) {
			System.out.println(mag1[k]);
		}
		
		try {
			PrintStream out=new PrintStream(new FileOutputStream(args[3]));
			System.setOut(out);
		}
		catch(Exception e) {
			System.out.println("nope");
		}
		for(int k=0; k<d.length/2; k++) {

			System.out.println(mag2[k]);
		}
	}
}
