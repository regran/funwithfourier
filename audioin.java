import java.io.*;
import javax.sound.sampled.*;
import java.nio.ByteBuffer;

public class audioin {
	public static byte [] getWAV() {
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		byte [] abytes=new byte [100];
		byte [] audiobytes;
		try{
			File f=new File("intro.wav");
			AudioInputStream in=AudioSystem.getAudioInputStream(f);
			int read;
			byte [] buff=new byte[(int) f.length()];
			while((read=in.read(buff))>0) {
				out.write(buff, 0, read);
			}
			out.flush();
			audiobytes=out.toByteArray();
			return audiobytes;
		}
		catch(Exception e) {
			System.out.println("ERROR");
		}
		return abytes;
	}
	public static double[] toDoubleArray(byte [] byteA) {
		int t=Integer.SIZE/Byte.SIZE;
		double [] d=new double[byteA.length/t];
		for(int i=0; i<d.length; i++) {
			d[i]=(double)ByteBuffer.wrap(byteA, (i*t), t).getInteger();
		}
		return d;
	}
	public static void main(String [] args) {
		byte [] b=getWAV();
		double [] amp=toDoubleArray(b);
		Complex[] comp = Complex.makeComp(amp);
		Complex[] p = fft.pad(comp);
		Complex[] freq = fft.reverse(p);
		for(int k=0; k<p.length; k++) {
			Complex.printlnComp(freq[k]);
		}
	
	}
}