import java.io.*;
import javax.sound.sampled.*;


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
		byte[] audioBytes = new byte[numBytes]; //array of audio sample bytes
		try{
			int framesread=0;
			int bytesread=0;
			while((bytesread=inp.read(audioBytes, 0, audioBytes.length)) != -1){ //read in bytes from audio input stream
				framesread=bytesread/frameSize;
				frames+=framesread;
			}
		} catch(Exception e){
			System.out.println("RIP");
		}

		return audioBytes;
	}
	
	public double[] toDouble(byte[] b){ //precond: byte array of audio data

		boolean isBig = form.isBigEndian(); //little or big endian format
		double[] dubs = new double[1024]; //arbitrary-ish length
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
	
	public static double sumofdif(double[] mags1, double[] mags2){ //precond: mags1 and mags2 same length
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
	
	public static double[] normalize(double[] mag){ //return normalized list of magnitudes of values from [0,1]
		double max=findMax(mag);
		double[] norm=new double[mag.length];
		for(int i = 0; i<mag.length;i++){
			norm[i]=mag[i]/max;
		}
		return norm;
	}
	
	
	
	public static void main(String[] args){ //args[0] sound file name 1, args[1] output file 1, args[2] sound file 2, args[3] output 2
		//make audio input objects
		audin input = new audin(args[0]);
		audin input2=new audin(args[2]);
		//get bytes from the two sound file
		byte[] b = input.getdataFromFile();
		byte[] b2 = input2.getdataFromFile();
		//convert bytes to doubles and normalize the data
		double[] d = audin.normalize(input.toDouble(b));
		double[] d2=audin.normalize(input2.toDouble(b2));
		//convert doubles to complex numbers
		Complex[] comp = Complex.makeComp(d);
		Complex[] comp2=Complex.makeComp(d2);
		//pad arrays to be powers of two for FFT
		Complex[] p = fft.pad(comp);
		Complex[] p2=fft.pad(comp2);
		//Fourier transformm
		Complex[] freq = fft.transform(p);
		Complex[]freq2=fft.transform(p2);
		//get array of magnitudes of transform
		double[] mag1=(Complex.getMagnitudes(freq));
		double[] mag2=(Complex.getMagnitudes(freq2));
		//output sum of differences of magnitudes of power spectra of two sound files
		System.out.println(audin.sumofdif(mag1, mag2));
		
		//output data to text file
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
