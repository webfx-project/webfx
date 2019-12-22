package mongoose.frontend.activities.payment;

/**
 * A JavaScript implementation of the Secure Hash Algorithm, SHA-1, as
 * defined in FIPS PUB 180-1 Version 2.1a Copyright Paul Johnston 2000 -
 * 2002. Other contributors: Greg Holt, Andrew Kepert, Ydnar, Lostinet
 * Distributed under the BSD License See http://pajhome.org.uk/crypt/md5 for
 * details.
 */

import webfx.platform.shared.services.log.Logger;

import java.nio.charset.StandardCharsets;

/**
 * @author Bruno Salmon
 */
final class Sha1 {

    static String hash(String msg) {
        try {
            byte[] bytes = msg.getBytes(StandardCharsets.UTF_8); // convert string to UTF-8, as SHA only deals with byte-streams
/* Commented as this code compile but doesn't work with GWT (NoSuchAlgorithmException is thrown)
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(bytes);
            return new BigInteger(1, crypt.digest()).toString(16);
*/
            return hash(bytes); // alternative algorithm implementation that works with GWT
        } catch (Exception e) { // UnsupportedEncodingException or NoSuchAlgorithmException
            Logger.log("Error while computing SHA-1", e);
            return msg;
        }
    }

    private static String hash(byte[] bytes) {

        int length = bytes.length + 1; // +1 with the extra 0x80 - set get() method

        // constants [§4.2.1]
        int[] K = {0x5a827999, 0x6ed9eba1, 0x8f1bbcdc, 0xca62c1d6};

        // convert string msg into 512-bit/16-integer blocks arrays of ints [§5.2.1]
        double l = ((double) length) / 4 + 2; // length (in 32-bit integers) of msg + ‘1’ + appended length
        int N = (int) Math.ceil(l / 16);  // number of 16-integer-blocks required to hold 'l' ints
        int[][] M = new int[N][16];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 16; j++) {  // encode 4 chars per integer, big-endian encoding
                int index = i * 64 + j * 4;
                M[i][j] = (get(index++, bytes) << 24) | (get(index++, bytes) << 16) | (get(index++, bytes) << 8) | (get(index, bytes) & 0xff);
            } // note running off the end of bytes is ok 'cos get() returns 0
        }
        // add length (in bits) into final pair of 32-bit integers (big-endian) [§5.1.1]
        // note: most significant word would be (len-1)*8 >>> 32, but since JS converts
        // bitwise-op args to 32 bits, we need to simulate this by arithmetic operators
        M[N - 1][14] = (int) (((length - 1) * 8) / Math.pow(2d, 32d));
        M[N - 1][14] = (int) Math.floor(M[N - 1][14]);
        M[N - 1][15] = ((length - 1) * 8);

        // set initial hash value [§5.3.1]
        int H0 = 0x67452301;
        int H1 = 0xefcdab89;
        int H2 = 0x98badcfe;
        int H3 = 0x10325476;
        int H4 = 0xc3d2e1f0;

        // HASH COMPUTATION [§6.1.2]

        int[] W = new int[80];
        int a, b, c, d, e;
        for (int i = 0; i < N; i++) {

            // 1 - prepare message schedule 'W'
            for (int t = 0; t < 16; t++) W[t] = M[i][t];
            for (int t = 16; t < 80; t++) W[t] = rol(W[t - 3] ^ W[t - 8] ^ W[t - 14] ^ W[t - 16], 1);

            // 2 - initialise five working variables a, b, c, d, e with previous hash value
            a = H0;
            b = H1;
            c = H2;
            d = H3;
            e = H4;

            // 3 - main loop
            for (int t = 0; t < 80; t++) {
                int s = (int) Math.floor(t / 20); // seq for blocks of 'f' functions and 'K' constants
                int T = (rol(a, 5) + f(s, b, c, d) + e + K[s] + W[t]);
                e = d;
                d = c;
                c = rol(b, 30);
                b = a;
                a = T;
            }

            // 4 - compute the new intermediate hash value (note 'addition modulo 2^32')
            H0 = (H0 + a);
            H1 = (H1 + b);
            H2 = (H2 + c);
            H3 = (H3 + d);
            H4 = (H4 + e);
        }

        return toHex8(H0) + toHex8(H1) + toHex8(H2) + toHex8(H3) + toHex8(H4);
    }

    private static int get(int index, byte[] bytes) {
        if (index < bytes.length)
            return bytes[index] & 0xff;
        if (index == bytes.length)
            return 0x80;
        return 0;
    }

    /**
     * Bitwise rotate a 32-bit number to the left
     */
    private static int rol(int num, int cnt) {
        return (num << cnt) | (num >>> (32 - cnt));
    }


    /**
     * Function 'f' [§4.1.1].
     */
    private static int f(int s, int x, int y, int z) {
        switch (s) {
            case 0:
                return (x & y) ^ (~x & z);           // Ch()
            case 1:
                return x ^ y ^ z;                 // Parity()
            case 2:
                return (x & y) ^ (x & z) ^ (y & z);  // Maj()
            case 3:
                return x ^ y ^ z;                 // Parity()
        }
        return 0;
    }

    private static String toHex8(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 8)
            hex = '0' + hex;
        return hex;
    }
}