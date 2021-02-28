package app.baseappsetup.data.network

enum class NetworkState(val code: Int) {
    Continue(100),
    SwitchingProtocols(101),
    Processing(102),

    OK(200),
    Created(201),
    Accepted(202),
    NonAuthoritativeInformation(203),
    NoContent(204),
    ResetContent(205),
    PartialContent(206),
    MultiStatus(207),
    AlreadyReported(208),
    IMUsed(226),

    MultipleChoices(300),
    MovedPermanently(301),
    Found(302),
    SeeOther(303),
    NotModified(304),
    UseProxy(305),
    TemporaryRedirect(307),
    PermanentRedirect(308),

    BadRequest(400),
    Unauthorized(401),
    PaymentRequired(402),
    Forbidden(403),
    NotFound(404),
    MethodNotAllowed(405),
    NotAcceptable(406),
    ProxyAuthenticationRequired(407),
    RequestTimeout(408),
    Conflict(409),
    Gone(410),
    LengthRequired(411),
    PreconditionFailed(412),
    PayloadTooLarge(413),
    URITooLong(414),
    UnsupportedMediaType(415),
    RangeNotSatisfiable(416),
    ExpectationFailed(417),
    IAmATeapot(418),
    MisdirectedRequest(421),
    UnprocessableEntity(422),
    Locked(423),
    FailedDependency(424),
    UpgradeRequired(426),
    PreconditionRequired(428),
    TooManyRequests(429),
    RequestHeaderFieldsTooLarge(431),
    UnavailableForLegalReasons(451),

    InternalServerError(500),
    NotImplemented(501),
    BadGateway(502),
    ServiceUnavailable(503),
    GatewayTimeout(504),
    HTTPVersionNotSupported(505),
    VariantAlsoNegotiates(506),
    InsufficientStorage(507),
    LoopDetected(508),
    NotExtended(510),
    NetworkAuthenticationRequired(511),

    Unknown(0),
    NoInternet(1);

    var message: String? = null
    var errorMessageCode: NetworkErrorMessageCode? = null
    var api: String? = null

    companion object {
        fun from(code: Int): NetworkState = values().find { it.code == code } ?: Unknown

        const val MESSAGE = "message"
        const val ERROR_DESCRIPTION = "error_description"
    }
}

enum class NetworkErrorMessageCode(val errorMessage: String) {
    TOKEN_EXPIRED("TOKEN_EXPIRED"),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND"),

    BAD_CREDENTIALS("BAD_CREDENTIALS"),
    INVALID_DEVICE("INVALID_DEVICE"),

    ALREADY_ACCEPTED("ALREADY_ACCEPTED"),
    ALREADY_DECLINED("ALREADY_DECLINED"),

    COURSE_NOT_FOUND("COURSE_NOT_FOUND"),

    INVALID_NAME_FORMAT("INVALID_NAME_FORMAT"),

    EMAIL_USED("EMAIL_USED"),
    INVALID_EMAIL_FORMAT("INVALID_EMAIL_FORMAT"),
    EMAIL_NOT_VERIFIED("EMAIL_NOT_VERIFIED"),

    USERNAME_USED("USERNAME_USED"),
    INVALID_USERNAME_FORMAT("INVALID_USERNAME_FORMAT"),
    TO_MANY_CHANGES("TO_MANY_CHANGES"),

    SIMILAR_PASSWORD("SIMILAR_PASSWORD"),
    INVALID_OLD_PASSWORD("INVALID_OLD_PASSWORD"),
    INVALID_NEW_PASSWORD("INVALID_NEW_PASSWORD"),
    NEW_PASSWORD_SAME_AS_OLD("NEW_PASSWORD_SAME_AS_OLD"),
    TOO_MANY_CHANGES("TO_MANY_CHANGES"),

    COURSEPROGRESS_NOT_FOUND("COURSEPROGRESS_NOT_FOUND"),

    REVIEW_NOT_FOUND("REVIEW_NOT_FOUND"),
    NOT_ALLOWED("NOT_ALLOWED");

    companion object {
        fun from(errorMessage: String): NetworkErrorMessageCode? = values().find { it.errorMessage == errorMessage }
    }
}