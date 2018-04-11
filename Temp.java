/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homomorphicencryption;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Ashok Pundir
 */
public class Temp {
    Temp(BigInteger P,BigInteger Q)
    {
        GeneratingValues(P,Q);
        System.out.println(Encryption(new BigInteger("20")));
    
    }
    private BigInteger lambda,n,nsquare,g;
	private String pp;
	
	public void GeneratingValues(BigInteger p, BigInteger q) 
	{
		n = p.multiply(q);
		nsquare = n.multiply(n);
		g = new BigInteger("2");
		lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(
		p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
	}
	
	public BigInteger Encryption(BigInteger m) 
	{
		BigInteger r = new BigInteger(512, new Random());
		//BigInteger r = new BigInteger("87051155521627615945126071128501996209711397670633062046550150379033194418167");
		return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
	}
	
	public BigInteger Decryption(BigInteger c) 
	{
		BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
		return c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
	}
        public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		//BigInteger p=new BigInteger("87051155521627615945126071128501996209711397670633062046550150379033194418167");
		//BigInteger q=new BigInteger("76883738083136012312372139006996661766766030694975628814935127558578572504397");
                BigInteger p=new BigInteger("107378469332560560031989840066440309574379341693667637966677112802928189356871");
		BigInteger q=new BigInteger("70174965326540477229797527662312936104796140786148709413990656891900892592927");
		
		
		new Temp(p,q);
	}
}
