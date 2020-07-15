/*
 * MIT License
 *
 * Copyright (c) 2020 Thales DIS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * IMPORTANT: This source code is intended to serve training information purposes only.
 *            Please make sure to review our IdCloud documentation, including security guidelines.
 */

package pqh.vn.pliz.etoken.logic;

import androidx.annotation.NonNull;

import com.gemalto.idp.mobile.authentication.AuthInput;
import com.gemalto.idp.mobile.core.IdpCore;
import com.gemalto.idp.mobile.core.IdpException;
import com.gemalto.idp.mobile.core.root.RootDetector;
import com.gemalto.idp.mobile.core.util.SecureString;
import com.gemalto.idp.mobile.otp.OtpModule;
import com.gemalto.idp.mobile.otp.oath.DualSeedOathToken;
import com.gemalto.idp.mobile.otp.oath.OathDevice;
import com.gemalto.idp.mobile.otp.oath.OathFactory;
import com.gemalto.idp.mobile.otp.oath.OathService;
import com.gemalto.idp.mobile.otp.oath.soft.SoftOathSettings;
import com.gemalto.idp.mobile.otp.oath.soft.SoftOathToken;
import com.thalesgroup.mobileprotector.commonutils.helpers.AbstractBaseLogic;
import com.thalesgroup.mobileprotector.commonutils.helpers.OtpValue;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import pqh.vn.pliz.etoken.util.Common;

/**
 * Logic for user authentication/OTP verification.
 */
public class TransactionSignLogic extends AbstractBaseLogic {

    //region Declaration

    //region Public API

    /**
     * Generates an OTP for transaction signature.
     *
     * @param token       Token to be used for OTP generation.
     * @param pin         PIN.
     * @param beneficiary Beneficiary to be signed.
     * @return Generated OTP.
     * @throws IdpException If error during OTP generation occurs.
     */
    public static OtpValue generateOtp(@NonNull final DualSeedOathToken token,
                                       @NonNull final AuthInput pin,
                                       @NonNull String beneficiary)
            throws IdpException {

        if (IdpCore.getInstance().getRootDetector().getRootStatus() != RootDetector.RootStatus.NOT_ROOTED) { //NOPMD
            // Handle root status according to app policy.
        }

        // Create service with respective settings
        token.selectKey(1);
        if (beneficiary.length() % 2 != 0) {
            beneficiary = "0" + beneficiary;
        }
        final OathFactory oathFactory = OathService.create(OtpModule.create()).getFactory();
        final SoftOathSettings softOathSettings = oathFactory.createSoftOathSettings();
        softOathSettings.setOcraSuite(OtpConfig.INSTANCE.getOcraSuite());
        softOathSettings.setOcraOtpLength(6);

        // Create and use OATH device to calculate OTP
        final OathDevice oathDevice = oathFactory.createSoftOathDevice(token, softOathSettings);
        final SecureString otp = oathDevice.getOcraOtp(pin,
                IdpCore.getInstance().getSecureContainerFactory().fromString(beneficiary),
                null,
                null,
                null);
        return new OtpValue(otp, oathDevice.getLastOtpLifespan(), OtpConfig.INSTANCE.getOTPLifetime());
    }

    //endregion


}
