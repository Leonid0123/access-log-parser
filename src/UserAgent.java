public class UserAgent {
    private final String typeOS;
    private final String browser;

    public UserAgent(String userAgent) {
        //parseOS
        if (userAgent.contains("Windows NT"))
            this.typeOS = "Windows";
        else if (userAgent.contains("Macintosh"))
            this.typeOS = "macOS";
        else if (userAgent.contains("Linux"))
            this.typeOS = "Linux";
        else
            this.typeOS = "Unknown OS";
        //parseBrowser
        if (userAgent.contains("Edg")) {
            this.browser = "Edge";//+
        } else if (userAgent.contains("Firefox")) {
            this.browser = "Firefox";//+
        } else if (userAgent.contains("Chrome") && userAgent.contains("Safari") && userAgent.contains("KHTML")
                && userAgent.contains("like Gecko") && !userAgent.contains("Edge") && !userAgent.contains("OPR")) {
            this.browser = "Chrome";//+
        } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
            this.browser = "Opera";//+
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome") && userAgent.contains("Mobile")) {
            this.browser = "Safari";//+
        } else {
            this.browser = "Other";
        }
    }

    public String getTypeOS() {
        return typeOS;
    }

    public String getBrowser() {
        return browser;
    }
}
