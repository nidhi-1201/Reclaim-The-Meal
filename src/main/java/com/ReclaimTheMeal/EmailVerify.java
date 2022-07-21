package com.ReclaimTheMeal;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public class EmailVerify {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = "AC39f3f6ceedf513d892bdd5c1adb6625e";
    public static final String AUTH_TOKEN = "559cc4d297940157507311a8552a52b4";

    public void verify(String code, String email) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        VerificationCheck verificationCheck = VerificationCheck.creator(
                "VA6147f75e53820942df0f7dab1cb7cbbd").setCode(code)
            .setTo(email).create();

        System.out.println(verificationCheck.getSid());
    }
}