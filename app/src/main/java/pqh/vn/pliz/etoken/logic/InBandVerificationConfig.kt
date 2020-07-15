package pqh.vn.pliz.etoken.logic

internal object InBandVerificationConfig {
    /**
     * Gets authentication URL where the generated OTP is validated.
     *
     * @return Authentication URL.
     */
    val authenticationUrl: String
        get() = "http://192.168.31.151:8080/saserver/master/api/auth/"

    /**
     * Gets the username used to authenticate on the authentication URL.
     *
     * @return Username.
     */
    val basicAuthenticationUsername: String
        get() = ""

    /**
     * Gets the password used to authenticate on the authentication URL.
     *
     * @return Password.
     */
    val basicAuthenticationPassword: String
        get() = "dmlldGFiYW5rYXBpOlZpZXRhYmFua0AyMDIw"
}