package pqh.vn.pliz.etoken.fake;

import com.gemalto.idp.mobile.core.net.TlsConfiguration;

public class FakeAppData {
    public static String getProvisioningUrl() {
        //return "http://192.168.31.152:8181/provisioner/domains/vietabank_retail/provision";
        return "https://etoken.vietabank.com.vn:8181/provisioner/domains/vietabank_retail/provision";
    }

    public static byte[] getActivationcode() {
        String activationCode = "0156494554414230310000000206b43f7bf7d656163d290c08afbc2f382d46e4df691552063dbfa88b622e6b67249fe178879bd1661590413e0203f2d9eeb84bdd3580bb29b15879c3628ae520";
        return ConvertHexaStringToBytesArray(activationCode);
    }

    public static byte[] ConvertHexaStringToBytesArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Replace this string with your own EPS key ID.
     * <p>
     * This is specific to the configuration of the bank's system. Therefore
     * other values should be used here.
     */
    public static String getRsaKeyId() {
        return "C717B2834FA9C98763DABA5E85856F97A3CEA3C2";
    }

    /**
     * Replace this byte array with your own EPS key modulus unless you are
     * using the EPS 2.X default key pair.
     * <p>
     * The EPS' RSA modulus. This is specific to the configuration of the
     * bank's system.  Therefore other values should be used here.
     */

    public static byte[] getRsaKeyModulus1() {
        String modules = "00AF84FE8065E3A766095F34717D3C1DD0B225C2E703FE020F1BBAA6449A72AA8DE16D28DD18DFE4EFD1F832AE52A126C3D252AB99714FCAD99480971265E00D7D8433C0780C7A676E29B4D1BBC354798910454FC65F547159278B7DA237B8B6B818E86376D54320A0460D687EFE0EB814329AC11F2563BF90C576A63FE9AB8E64244993608A6D5734C0B39A389EE1B87B90566EF220E475A48A33BBF8522D95CCE03FA440E1C983B7C08546E7F7048B171F373B09B797250A1BA8F0E5A6D1BC4F1AB9238C1E7997DC3ABC7940DC914D425D7A2CFF306B49FA22324FDB74039A05E71BB615C173AFEE0F6BD2AE8C9DE20BE94B4A22C31E62C246EDAAB8988B711F3BA4735ED75A31AA0082C6489D7DAC81724A7FA0038C7D8546325215F315005E7294FE97F7D1D981FFF41CACA46DE50A250EA83B73416A1E589E26B8BC2CE9B03956C1B623757DCD8A2317D879C78CBF6A1ED0275AD0B60D614EDFDC984D18C99ED5C5E0650607045185308988D29EFC383E01B2C196AE2F655AABAB880B056F";
        return ConvertHexaStringToBytesArray(modules);
    }


    public static byte[] getRsaKeyModulus() {
        // Security Guideline: GEN13. Integrity of public keys
        // Since this example hard codes the key and does not load it from a
        // file, this guideline is skipped.

        // Security Guideline: GEN17. RSA key length
        // 2048 bit key
//        return new byte[]{
//                (byte) 0x00, (byte) 0xaf, (byte) 0x84, (byte) 0xfe, (byte) 0x80, (byte) 0x65,
//                (byte) 0xe3, (byte) 0xa7, (byte) 0x66, (byte) 0x09, (byte) 0x5f, (byte) 0x34,
//                (byte) 0x71, (byte) 0x7d, (byte) 0x3c, (byte) 0x1d, (byte) 0xd0, (byte) 0xb2,
//                (byte) 0x25, (byte) 0xc2, (byte) 0xe7, (byte) 0x03, (byte) 0xfe, (byte) 0x02,
//                (byte) 0x0f, (byte) 0x1b, (byte) 0xba, (byte) 0xa6, (byte) 0x44, (byte) 0x9a,
//                (byte) 0x72, (byte) 0xaa, (byte) 0x8d, (byte) 0xe1, (byte) 0x6d, (byte) 0x28,
//                (byte) 0xdd, (byte) 0x18, (byte) 0xdf, (byte) 0xe4, (byte) 0xef, (byte) 0xd1,
//                (byte) 0xf8, (byte) 0x32, (byte) 0xae, (byte) 0x52, (byte) 0xa1, (byte) 0x26,
//                (byte) 0xc3, (byte) 0xd2, (byte) 0x52, (byte) 0xab, (byte) 0x99, (byte) 0x71,
//                (byte) 0x4f, (byte) 0xca, (byte) 0xd9, (byte) 0x94, (byte) 0x80, (byte) 0x97,
//                (byte) 0x12, (byte) 0x65, (byte) 0xe0, (byte) 0x0d, (byte) 0x7d, (byte) 0x84,
//                (byte) 0x33, (byte) 0xc0, (byte) 0x78, (byte) 0x0c, (byte) 0x7a, (byte) 0x67,
//                (byte) 0x6e, (byte) 0x29, (byte) 0xb4, (byte) 0xd1, (byte) 0xbb, (byte) 0xc3,
//                (byte) 0x54, (byte) 0x79, (byte) 0x89, (byte) 0x10, (byte) 0x45, (byte) 0x4f,
//                (byte) 0xc6, (byte) 0x5f, (byte) 0x54, (byte) 0x71, (byte) 0x59, (byte) 0x27,
//                (byte) 0x8b, (byte) 0x7d, (byte) 0xa2, (byte) 0x37, (byte) 0xb8, (byte) 0xb6,
//                (byte) 0xb8, (byte) 0x18, (byte) 0xe8, (byte) 0x63, (byte) 0x76, (byte) 0xd5,
//                (byte) 0x43, (byte) 0x20, (byte) 0xa0, (byte) 0x46, (byte) 0x0d, (byte) 0x68,
//                (byte) 0x7e, (byte) 0xfe, (byte) 0x0e, (byte) 0xb8, (byte) 0x14, (byte) 0x32,
//                (byte) 0x9a, (byte) 0xc1, (byte) 0x1f, (byte) 0x25, (byte) 0x63, (byte) 0xbf,
//                (byte) 0x90, (byte) 0xc5, (byte) 0x76, (byte) 0xa6, (byte) 0x3f, (byte) 0xe9,
//                (byte) 0xab, (byte) 0x8e, (byte) 0x64, (byte) 0x24, (byte) 0x49, (byte) 0x93,
//                (byte) 0x60, (byte) 0x8a, (byte) 0x6d, (byte) 0x57, (byte) 0x34, (byte) 0xc0,
//                (byte) 0xb3, (byte) 0x9a, (byte) 0x38, (byte) 0x9e, (byte) 0xe1, (byte) 0xb8,
//                (byte) 0x7b, (byte) 0x90, (byte) 0x56, (byte) 0x6e, (byte) 0xf2, (byte) 0x20,
//                (byte) 0xe4, (byte) 0x75, (byte) 0xa4, (byte) 0x8a, (byte) 0x33, (byte) 0xbb,
//                (byte) 0xf8, (byte) 0x52, (byte) 0x2d, (byte) 0x95, (byte) 0xcc, (byte) 0xe0,
//                (byte) 0x3f, (byte) 0xa4, (byte) 0x40, (byte) 0xe1, (byte) 0xc9, (byte) 0x83,
//                (byte) 0xb7, (byte) 0xc0, (byte) 0x85, (byte) 0x46, (byte) 0xe7, (byte) 0xf7,
//                (byte) 0x04, (byte) 0x8b, (byte) 0x17, (byte) 0x1f, (byte) 0x37, (byte) 0x3b,
//                (byte) 0x09, (byte) 0xb7, (byte) 0x97, (byte) 0x25, (byte) 0x0a, (byte) 0x1b,
//                (byte) 0xa8, (byte) 0xf0, (byte) 0xe5, (byte) 0xa6, (byte) 0xd1, (byte) 0xbc,
//                (byte) 0x4f, (byte) 0x1a, (byte) 0xb9, (byte) 0x23, (byte) 0x8c, (byte) 0x1e,
//                (byte) 0x79, (byte) 0x97, (byte) 0xdc, (byte) 0x3a, (byte) 0xbc, (byte) 0x79,
//                (byte) 0x40, (byte) 0xdc, (byte) 0x91, (byte) 0x4d, (byte) 0x42, (byte) 0x5d,
//                (byte) 0x7a, (byte) 0x2c, (byte) 0xff, (byte) 0x30, (byte) 0x6b, (byte) 0x49,
//                (byte) 0xfa, (byte) 0x22, (byte) 0x32, (byte) 0x4f, (byte) 0xdb, (byte) 0x74,
//                (byte) 0x03, (byte) 0x9a, (byte) 0x05, (byte) 0xe7, (byte) 0x1b, (byte) 0xb6,
//                (byte) 0x15, (byte) 0xc1, (byte) 0x73, (byte) 0xaf, (byte) 0xee, (byte) 0x0f,
//                (byte) 0x6b, (byte) 0xd2, (byte) 0xae, (byte) 0x8c, (byte) 0x9d, (byte) 0xe2,
//                (byte) 0x0b, (byte) 0xe9, (byte) 0x4b, (byte) 0x4a, (byte) 0x22, (byte) 0xc3,
//                (byte) 0x1e, (byte) 0x62, (byte) 0xc2, (byte) 0x46, (byte) 0xed, (byte) 0xaa,
//                (byte) 0xb8, (byte) 0x98, (byte) 0x8b, (byte) 0x71, (byte) 0x1f, (byte) 0x3b,
//                (byte) 0xa4, (byte) 0x73, (byte) 0x5e, (byte) 0xd7, (byte) 0x5a, (byte) 0x31,
//                (byte) 0xaa, (byte) 0x00, (byte) 0x82, (byte) 0xc6, (byte) 0x48, (byte) 0x9d,
//                (byte) 0x7d, (byte) 0xac, (byte) 0x81, (byte) 0x72, (byte) 0x4a, (byte) 0x7f,
//                (byte) 0xa0, (byte) 0x03, (byte) 0x8c, (byte) 0x7d, (byte) 0x85, (byte) 0x46,
//                (byte) 0x32, (byte) 0x52, (byte) 0x15, (byte) 0xf3, (byte) 0x15, (byte) 0x00,
//                (byte) 0x5e, (byte) 0x72, (byte) 0x94, (byte) 0xfe, (byte) 0x97, (byte) 0xf7,
//                (byte) 0xd1, (byte) 0xd9, (byte) 0x81, (byte) 0xff, (byte) 0xf4, (byte) 0x1c,
//                (byte) 0xac, (byte) 0xa4, (byte) 0x6d, (byte) 0xe5, (byte) 0x0a, (byte) 0x25,
//                (byte) 0x0e, (byte) 0xa8, (byte) 0x3b, (byte) 0x73, (byte) 0x41, (byte) 0x6a,
//                (byte) 0x1e, (byte) 0x58, (byte) 0x9e, (byte) 0x26, (byte) 0xb8, (byte) 0xbc,
//                (byte) 0x2c, (byte) 0xe9, (byte) 0xb0, (byte) 0x39, (byte) 0x56, (byte) 0xc1,
//                (byte) 0xb6, (byte) 0x23, (byte) 0x75, (byte) 0x7d, (byte) 0xcd, (byte) 0x8a,
//                (byte) 0x23, (byte) 0x17, (byte) 0xd8, (byte) 0x79, (byte) 0xc7, (byte) 0x8c,
//                (byte) 0xbf, (byte) 0x6a, (byte) 0x1e, (byte) 0xd0, (byte) 0x27, (byte) 0x5a,
//                (byte) 0xd0, (byte) 0xb6, (byte) 0x0d, (byte) 0x61, (byte) 0x4e, (byte) 0xdf,
//                (byte) 0xdc, (byte) 0x98, (byte) 0x4d, (byte) 0x18, (byte) 0xc9, (byte) 0x9e,
//                (byte) 0xd5, (byte) 0xc5, (byte) 0xe0, (byte) 0x65, (byte) 0x06, (byte) 0x07,
//                (byte) 0x04, (byte) 0x51, (byte) 0x85, (byte) 0x30, (byte) 0x89, (byte) 0x88,
//                (byte) 0xd2, (byte) 0x9e, (byte) 0xfc, (byte) 0x38, (byte) 0x3e, (byte) 0x01,
//                (byte) 0xb2, (byte) 0xc1, (byte) 0x96, (byte) 0xae, (byte) 0x2f, (byte) 0x65,
//                (byte) 0x5a, (byte) 0xab, (byte) 0xab, (byte) 0x88, (byte) 0x0b, (byte) 0x05,
//                (byte) 0x6f
//        };
        String modules="00af84fe8065e3a766095f34717d3c" +
                "1dd0b225c2e703fe020f1bbaa6449a" +
                "72aa8de16d28dd18dfe4efd1f832ae" +
                "52a126c3d252ab99714fcad9948097" +
                "1265e00d7d8433c0780c7a676e29b4" +
                "d1bbc354798910454fc65f54715927" +
                "8b7da237b8b6b818e86376d54320a0" +
                "460d687efe0eb814329ac11f2563bf" +
                "90c576a63fe9ab8e64244993608a6d" +
                "5734c0b39a389ee1b87b90566ef220" +
                "e475a48a33bbf8522d95cce03fa440" +
                "e1c983b7c08546e7f7048b171f373b" +
                "09b797250a1ba8f0e5a6d1bc4f1ab9" +
                "238c1e7997dc3abc7940dc914d425d" +
                "7a2cff306b49fa22324fdb74039a05" +
                "e71bb615c173afee0f6bd2ae8c9de2" +
                "0be94b4a22c31e62c246edaab8988b" +
                "711f3ba4735ed75a31aa0082c6489d" +
                "7dac81724a7fa0038c7d8546325215" +
                "f315005e7294fe97f7d1d981fff41c" +
                "aca46de50a250ea83b73416a1e589e" +
                "26b8bc2ce9b03956c1b623757dcd8a" +
                "2317d879c78cbf6a1ed0275ad0b60d" +
                "614edfdc984d18c99ed5c5e0650607" +
                "045185308988d29efc383e01b2c196" +
                "ae2f655aabab880b056f";
        return ConvertHexaStringToBytesArray(modules);
    }

    /**
     * Replace this byte array with your own EPS key exponent.
     * <p>
     * The EPS' RSA exponent. This is specific to the configuration of the
     * bank's system.  Therefore other values should be used here.
     */
    public static byte[] getRsaKeyExponent() {
        // Security Guideline: GEN13. Integrity of public keys
        // Since this example hard codes the key and does not load it from a
        // file, this guideline is skipped.
        return new byte[]{
                (byte) 0x01, (byte) 0x00, (byte) 0x01
        };
    }

    public static TlsConfiguration getTlsConfiguration() {
        return new TlsConfiguration();
        //return new TlsConfiguration(TlsConfiguration.Permit.INSECURE_CONNECTIONS);
    }

    public static String getDomain() {
        return "vietabank_retail";
    }

    /**
     * The custom fingerprint data that seals all the token credentials in this
     * example.
     * <p>
     * This data does not need to be modified in order to use this example app.
     */
    public static byte[] getCustomFingerprintData() {
        // This example simply uses the package name.
        //
        // This is one example of possible data that can be used for the custom
        // data. It provides domain separation so that the data stored by the
        // Ezio Mobile SDK is different for this application than it would be
        // for another bank's application. More data can be appended to
        // further improve the fingerprinting.
        return "pqh.vn.pliz.etoken.fake".getBytes();
    }
}
