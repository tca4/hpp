package fr.tse.fi2.hpp.labs.queries.impl.labs4;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;

public class SHA3Util {

	/**
	 * Hash the string concatenated with the salt.
	 * @param string
	 * @param salt
	 * @param size The size of the hash
	 * @param modulo
	 * @return  a number between 0 and modulo-1
	 */
	public static int digest(String string, String salt, int size, int modulo ) {

		 DigestSHA3 md = new DigestSHA3(size);
	     String text = (string + salt);
	     System.out.println(text);
	     
	     try 
	     {
	    	 md.update(text.getBytes("UTF-8"));
	     }
	     catch (UnsupportedEncodingException ex) 
	     {
	    	 // most unlikely
	         md.update(text.getBytes());
	     }
	     
	     byte[] digest = md.digest();
	     return encode(digest, modulo);
	}

	public static int encode(byte[] bytes, int modulo) 
	{
		BigInteger bigInt = new BigInteger(1, bytes);
		BigInteger m = new BigInteger(Integer.toString(modulo));
		return bigInt.mod(m).intValue();
	}

	public static void main(String[] args) 
	{
		System.out.println(digest("Julian", "1", 512, 14378));
		System.out.println(digest("Julian", "2", 512, 14378));
		System.out.println(digest("Julian", "3", 512, 14378));
		System.out.println(digest("Julian", "4", 512, 14378));
		System.out.println(digest("Julian", "5", 512, 14378));
		System.out.println(digest("Julian", "6", 512, 14378));
		System.out.println(digest("Julian", "7", 512, 14378));
		System.out.println(digest("Julian", "8", 512, 14378));
		System.out.println(digest("Julian", "9", 512, 14378));
		
	}
}