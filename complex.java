import java.io.*;

public class Complex{
	private double real; //real part
	private double imag; //imaginary part
	public Complex(double r){ //make complex number with only a real part
		real=r;
		imag=0;
	}
	public Complex(double r, double i){ //make complex number with real and imaginary parts
		real=r;
		imag=i;
	}
	public double getReal(){ //give real part of complex number
		return real;
	}
	
	public double getImag(){ //give imaginary part of complex number
		return imag;
	}
	
	public Complex add(Complex b){ //add two complex numbers
		return new Complex(real+b.real, imag+b.imag);
	}
	public Complex sub(Complex b){ //subtract
		return new Complex(real-b.real, imag-b.imag);
	}
	public Complex mult(Complex b){ //multiply
		return new Complex(real*b.real-imag*b.imag, real*b.imag+imag*b.real);
	}
	public static Complex div(Complex a, Complex b) throws divbyzero{ //divide
		if(b.real==0 && b.imag==0) throw(new divbyzero());
		Complex conj = new Complex(b.real, -b.imag);        //conjugate for division
		return new Complex((a.mult(conj)).real/(b.mult(conj)).real, (a.mult(conj)).imag/(b.mult(conj)).real);
	} 
	
	public double abs(){ //find absolute value of a complex number
		return Math.sqrt(Math.pow(real,2)+Math.pow(imag,2));
	}
	
	public double magnitude(){
		return real*real+imag*imag;
	}
	
	public double[] getMagnitudes(Complex[] c){
		double[] d = new double[c.length];
		for(int i=0;i<c.length;i++){
			d[i]=c[i].magnitude();
		}
		return d;
	}
	
	public static Complex downwithcis(double a){ //cosa+isina, i.e. e^ia
		return new Complex(Math.cos(a), Math.sin(a));
	}
	
	public static Complex[] makeComp(double[] v){ //make an array of complex numbers from an array of doubles
		int len = v.length;
		int n = 0;
		Complex[] comp = new Complex[len];
		while(n<len){
			comp[n]=new Complex(v[n]);
			n++;
		}
		return comp;
	}
	
	public static void printComp(Complex a){ //output a complex number
		if (a.real==0 && a.imag == 0){
			System.out.print(0);
			return;
		}
		if (a.real!=0){
			System.out.print(a.real);
		}
		if (a.imag == 0){
			return;
		}
		if(a.imag > 0&&a.real!=0){
			System.out.print("+");
		}
		if (a.imag!=1 && a.imag!= -1){
			System.out.print(a.imag);
		}
		if(a.imag==-1){
			System.out.print("-");
		}
		System.out.print("i");
		return;
	}
	public static void printlnComp(Complex a){ //output complex number with line breaks
		if (a.real==0 && a.imag == 0){
			System.out.print(0);
			System.out.println();
			return;
		}
		if (a.real!=0){
			System.out.print(a.real);
		}
		if (a.imag == 0){
			System.out.println();
			return;
		}
		if(a.imag > 0&&a.real!=0){
			System.out.print("+");
		}

		if (a.imag!=1 && a.imag!= -1){
			System.out.print(a.imag);
		}
		if(a.imag==-1){
			System.out.print("-");
		}
		System.out.print("i");
		System.out.println();
		return;
	}
	public static void main(String[] args){  //testing
		double a=Double.parseDouble(args[0]);
		double b=Double.parseDouble(args[1]);
		double c=Double.parseDouble(args[2]);
		double d=Double.parseDouble(args[3]);
		double[] ys = {a, b, c, d};
		Complex u = new Complex(a, b);
		Complex w = new Complex(c, d);
		printlnComp(u.add(w));
		printlnComp(u.sub(w));
		printlnComp(u.mult(w));

		try{ printlnComp(div(u,w));
		} catch(divbyzero e){
			System.out.println("Cannot divide by zero.");
		}
		System.out.println(u.abs());
		printlnComp(downwithcis(Math.PI/2));
		Complex[] xs=makeComp(ys);
		for(Complex x:xs){
			printlnComp(x);
		}
	}
}
