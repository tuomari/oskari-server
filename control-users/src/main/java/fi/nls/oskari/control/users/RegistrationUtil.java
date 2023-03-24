package fi.nls.oskari.control.users;

import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.control.users.model.PasswordRules;
import fi.nls.oskari.util.PropertyUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by SMAKINEN on 1.9.2016.
 */
public class RegistrationUtil {

    // From: https://owasp.org/www-community/OWASP_Validation_Regex_Repository
    private static final String EMAIL_REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    public static final String getServerAddress(ActionParameters params) {
        final String domain = PropertyUtil.get("oskari.domain", null);
        if(domain != null) {
            return domain;
        }
        final HttpServletRequest request = params.getRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.matches(EMAIL_REGEXP);
    }

    public static boolean isPasswordOk(String passwd) {
        if(passwd == null) {
            return false;
        }
        if(passwd.length() < PasswordRules.getMinLength()) {
            return false;
        }
        if(PasswordRules.getRequireCase() &&
                (passwd.toLowerCase().equals(passwd) || passwd.toUpperCase().equals(passwd))) {
            return false;
        }
        return true;
    }
    /**
     * Create timestamp for 2 days as expirytime.
     * @return
     */
    public static Timestamp createExpiryTime(){
        Calendar calender = Calendar.getInstance();
        Timestamp currentTime = new java.sql.Timestamp(calender.getTime().getTime());
        calender.setTime(currentTime);
        int expireDays = PropertyUtil.getOptional("oskari.email.link.expirytime", 2);
        calender.add(Calendar.DAY_OF_MONTH, expireDays);
        Timestamp expiryTime = new java.sql.Timestamp(calender.getTime().getTime());
        return expiryTime;
    }

    public static boolean isEnabled() {
        return PropertyUtil.getOptional("allow.registration", false);
    }
}
