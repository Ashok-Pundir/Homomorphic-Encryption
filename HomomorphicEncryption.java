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
public class HomomorphicEncryption {

    /**
     * @param args the command line arguments
     */
    /**
     * Creates new form SimpleInterest
     */
    private BigInteger p,q,lambda;
    
    /**  
     * n = p*q, where p and q are two large primes.  
     */  
    
    public BigInteger n; 
    
    /**  
     * nsq = n*n  
     */  
    
    public BigInteger nsq; 
    
    /**  
     * a random integer in Z*_{n^2} where gcd (L(g^lambda mod n^2), n) = 1.  
     */  
    
    private BigInteger g; 
    
    /**  
     * number of bits of modulus  
     */  
    
    private int bitLength;
    
    /**  
     * Constructs an instance of the Paillier cryptosystem.  
     * @param bitLengthVal number of bits of modulus 
     * @param certainty The probability that the new BigInteger represents a prime number will exceed (1 - 2^(-certainty)). 
     * The execution time of this constructor is proportional to the value of this parameter. 
 */
    
      public HomomorphicEncryption(int bitLengthVal,int certainty)
      {   
          KeyGeneration(bitLengthVal,certainty); 
      }
      
      /**  
       * Constructs an instance of the Paillier cryptosystem with 512 bits of modulus and at least 1-2^(-64) certainty of primes generation. 
       */  
      
      public HomomorphicEncryption() 
      {  
          KeyGeneration(512,64);  
      }
      
      /**  
       * Sets up the public key and private key.  
       * @param bitLengthVal number of bits of modulus.  
       * @param certainty The probability that the new BigInteger represents a prime number will exceed (1 - 2^(-certainty)). 
       * The execution time of this constructor is proportional to the value of this parameter.  
       */  
      
      public void KeyGeneration(int bitLengthVal,int certainty)
      {   
          bitLength = bitLengthVal;
      
      
      /*Constructs two positive BigIntegers that are prime, with the specified bitLength and certainty.*/ 
          //p = new BigInteger("94720926170568469358616877915285968728115783389284836675164991620451950827361");
         // q = new BigInteger("69565178715217190050383662239126710733033104161195639007628994512 475007939377");
        
          p = new BigInteger(bitLength / 2, certainty, new Random());   
          q = new BigInteger(bitLength / 2, certainty, new Random()); 
          n = p.multiply(q);   
          nsq = n.multiply(n); 
          g = new BigInteger("2"); 
          
          // lambda = lcm(p-1, q-1) = (p-1)*(q-1)/gcd(p-1, q-1)
          lambda=p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE))); 
          /* check whether g is good.*/
          
         //gcd(pq,(p-1)(q-1))==1
          if(g.modPow(lambda, nsq).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) 
          {   
              System.out.println("g is not good. Choose g again.");   
              System.exit(1);   
          }  
        }
      /**  
       * Encrypts plaintext m. ciphertext c = g^m * r^n mod n^2. This function explicitly requires random input r to help with encryption.  
       * @param m plaintext as a BigInteger  
       * @param r random plaintext to help with encryption  
       * @return ciphertext as a BigInteger  
       */
        public BigInteger Encryption(BigInteger m, BigInteger r) 
        {     
            return g.modPow(m,nsq).multiply(r.modPow(n,nsq)).mod(nsq);  
        }
          /** 
           * Encrypts plaintext m. ciphertext c = g^m * r^n mod n^2. This function automatically generates random input r (to help with encryption). 
           * @param m plaintext as a BigInteger  * @return ciphertext as a BigInteger  
           */  
        public BigInteger Encryption(BigInteger m) 
        {   
            BigInteger r = new BigInteger(bitLength, new Random());   
            return g.modPow(m,nsq).multiply(r.modPow(n,nsq)).mod(nsq); 
        }
        /**  
         * Decrypts ciphertext c. plaintext m = L(c^lambda mod n^2) 
         * u mod n, where u = (L(g^lambda mod n^2))^(-1) mod n.  
         * @param c ciphertext as a BigInteger 
         * @return plaintext as a BigInteger  
         */  
        public BigInteger Decryption(BigInteger c) 
        {   
            BigInteger u = g.modPow(lambda,nsq).subtract(BigInteger.ONE).divide(n).modInverse(n);   
            return c.modPow(lambda,nsq).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);  
        }
        
        private BigInteger pr,t,r;
        public BigInteger em1,em2,em3,SI,E_SI;
        
        /*Passing the values*/
        public void EnterValues(String e1, String e2, String e3)
        {      
            pr = new BigInteger(e1);   
            r = new BigInteger(e2);   
            t = new BigInteger(e3);    
        }
        
        public void EncryptValues()
        {  
            em1 = Encryption(pr);   
            em2 = Encryption(t);   
            em3 = Encryption(r);
            
        }
    public void CalculateSI()
    { 
        BigInteger p_r = em1.modPow(r,nsq);   
        BigInteger p_r_t= p_r.modPow(t,nsq);
        SI=pr.multiply(r).multiply(t).divide(new BigInteger("100"));
        E_SI = Encryption(Decryption(p_r_t).divide(new BigInteger("100")));
       
    }
        public static void main(String[] args) {
        // TODO code application logic here
            new HomomorphicEncryption();
       }
    
}
