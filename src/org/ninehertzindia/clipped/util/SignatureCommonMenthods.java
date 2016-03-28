package org.ninehertzindia.clipped.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * class is used for using static methods for generating signatures for the
 * registered activities
 * 
 * @author Pramit chaturvedi
 * 
 */
public class SignatureCommonMenthods {

	/**
	 * This method is used for convert the byte received after conversion of
	 * SHA1 to HEX
	 * 
	 * @param data
	 * @return
	 */
	private static String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (byte b : data) {
			int halfbyte = (b >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte)
						: (char) ('a' + (halfbyte - 10)));
				halfbyte = b & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * This method is used to convert the given text to SHA1 string
	 * 
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private static String SHA1(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		byte[] sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	/**
	 * This method is used for create the signature for All APIs
	 * 
	 * @return the complete encrypted signature for APIs
	 */
	public static String getSignatureForAPI(
			ArrayList<String> al_signature_strings) {

		Collections.sort(al_signature_strings);

		StringBuffer str_sig_string = new StringBuffer();
		for (String string : al_signature_strings) {
			str_sig_string.append(string);
		}
		str_sig_string.append(ApplicationConstants.BP_SIGNATURE_SUFFIX);

		System.out.println("pc the complete signature before encryption : "
				+ str_sig_string.toString());
		
		String str_signature = null;
		try {
			str_signature = SHA1(str_sig_string.toString());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str_signature;

	}
}