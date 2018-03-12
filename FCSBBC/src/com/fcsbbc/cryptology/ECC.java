package com.fcsbbc.cryptology;

import java.security.SecureRandom;

import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECPoint;

public class ECC {
	private static final String SECP256 = "secp256r1";
	private static final X9ECParameters SECP256R1PARAMETERS = ECNamedCurveTable.getByName(SECP256);
	public static final ECDomainParameters SECP256R1 = new ECDomainParameters(SECP256R1PARAMETERS.getCurve(), 
			SECP256R1PARAMETERS.getG(), SECP256R1PARAMETERS.getN(), 
			SECP256R1PARAMETERS.getH(), SECP256R1PARAMETERS.getSeed());
	
	public static int compare(ECPoint point_a, ECPoint point_b) {
		if (point_a == point_b) {
			return 0;
		}
		int result = point_a.getXCoord().toBigInteger().compareTo(point_b.getXCoord().toBigInteger());
		if (result != 0) {
			return result;
		}
		return point_a.getYCoord().toBigInteger().compareTo(point_b.getYCoord().toBigInteger());
	}
	
	public static byte[] generateKey() {
		byte[] key = new byte[32];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(key);
		return key;
	}
	
//	public static String toString(ECPoint p) {
//		return Helper.toHexString(p.getEncoded(true));
//	}
}
