import java.io.*;
import java.util.*;
import java.util.function.*;

public class fft{
	
	private static Complex[][] split(Complex[] q){ //precond: even number of elements
		Complex[] odds=new Complex[q.length/2];
		Complex[] evens=new Complex[q.length/2];
		int i = 0;
		int j=0;
		while(i<q.length){
			evens[j]=q[i];
			odds[j]=q[i+1];
			j++;
			i+=2;
		}
		Complex[][] ans = {evens, odds};
		return ans;
	} //postcond: list of two lists containing alternating elements from input lists
	
	//helper for reverse transform
	public static Complex[] revform(Complex[] p){ //precond: p.length is a power of 2, frequency coefficients of function
		int n=p.length;
		if(n<=1){	//a list of 1 is transformed, good job (end condition of recursion)
			return p;
		}
		Complex[][] eo = split(p);	//split into alternating odd and even powers
		Complex[] evens = revform(eo[0]);	//recursion omg
		Complex[] odds = revform(eo[1]);
		Complex[] trans = new Complex[n];
		Complex w = new Complex(1);	//roots of unity
		for(int i = 0; i<evens.length; i++){
			trans[i]=evens[i].add(w.mult(odds[i]));		//put all the things in a list!
			trans[i+n/2]=evens[i].sub(w.mult(odds[i]));
			w=w.mult(Complex.downwithcis(2*Math.PI/n)); //new rou
		}
		return trans; //postcond: time samples of function
	}
	
	//helper for forward transform
	public static Complex[] xform(Complex[] p){ //precond: p.length is a power of 2, frequency coefficients of function
		int n=p.length;
		if(n<=1){	//a list of 1 is transformed, good job (end condition of recursion)
			return p;
		}
		Complex[][] eo = split(p);	//split into alternating odd and even powers
		Complex[] evens = xform(eo[0]);	//recursion omg
		Complex[] odds = xform(eo[1]);
		Complex[] trans = new Complex[n];
		Complex w = new Complex(1);	//roots of unity
		for(int i = 0; i<evens.length; i++){
			trans[i]=evens[i].add(w.mult(odds[i]));		//put all the things in a list!
			trans[i+n/2]=evens[i].sub(w.mult(odds[i]));
			w=w.mult(Complex.downwithcis(-2*Math.PI/n)); //new rou
		}
		return trans; //postcond: time samples of function
	}
	
	//helper for final transform
	public static Complex[] divall(Complex[] formed){
		int i = 0;
		Complex four = new Complex((formed.length));
		while(i<formed.length){
			try{
				formed[i]=Complex.div(formed[i], four);
			} catch(divbyzero e){
				System.out.println("This list is empty");
			}
			i++;
		}
		return formed; //postcond: fast fourier transform resulting in frequency domain
	}
	
	//function used for forward transform
	public static Complex[] transform(Complex[] p){ //precond: p.length is a power of 2, time samples of function, pls no big number
		Complex[] formed=xform(p);
		return (formed); //postcond: fast fourier transform resulting in frequency domain
	}
	
	//function used for reverse transform
	public static Complex[] reverse(Complex[] p){ //precond: p.length is a power of 2, time samples of function, pls no big number
		Complex[] formed=revform(p);
		return fft.divall(formed); //postcond: fast fourier transform resulting in frequency domain
	}
	
	
	public static Complex[] pad(Complex[] notpow){ //precond: list to be padded
		int i = 0;
		while(Math.pow(2,i)<notpow.length){
			i++;
		}
		if(Math.pow(2,i)==notpow.length) return notpow;
		Complex[] padded = new Complex[(int)Math.pow(2,i)];
		int j = 0;
		while(j<notpow.length){
			padded[j]=notpow[j];
			j++;
		}
		while(j<padded.length){
			padded[j]=new Complex(0);
			j++;
		}
		return padded; //postcond: list padded with zeroes until its length is a power of 2
	}
	
	public static double[] repeat(int n, double i){ //n is desired length of array, i is repeating number
		double[] ans = new double[n];
		for(int j=0;j<n;j++){
			ans[j]=i;
		}
		return ans;
	}
	
	public static void main(String[] args){ //testing
		double[] test = {57, 3, 2, 9, 7, 6, 12, 100};
		System.out.println(Double.MAX_VALUE);
		Complex[] samples = Complex.makeComp(test);
		Complex[] frequencies=fft.transform(fft.pad(samples));
		for(int i=0; i<10; i++){
//		for(Complex c:frequencies){
			Complex.printlnComp(fft.reverse(frequencies)[i]);
		}
	}
}