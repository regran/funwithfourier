import java.io.*;
public class Samples{
	private double[] timedomain;
	private Complex[] freqdomain;
	private float sampleRate;
	public Samples(byte[] audioBytes, boolean isBigEndian, int numSamples, float sr){
		timedomain=new double[numSamples];
		for(int i=0; i<numSamples*2; i+=2){ 	//convert pairs of bytes to Endian values
			int b1=audioBytes[i];
			int b2=audioBytes[i+1];
//			if(b1<0) b1+= 0x100;
//			if(b2<0) b2+=0x100;
			
			if(isBigEndian) timedomain[i/2] = b1 + (b2<<8);
			else timedomain[i/2]=(b1<<8) + b2;
		}
		freqdomain=fft.transform(fft.pad(Complex.makeComp(timedomain)));
		sampleRate=sr;
	}
	public Samples(double[] d, float sr){
		timedomain=d;
		freqdomain=fft.transform(fft.pad(Complex.makeComp(timedomain)));
		sampleRate = sr;
	}
	
	public double[] getSamples(){
		return timedomain;
	}
	
	public float getRate(){
		return sampleRate;
	}
	
	public Complex[] getTransform(){
		return freqdomain;
	}
}