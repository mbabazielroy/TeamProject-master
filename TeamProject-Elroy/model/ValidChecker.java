package model;
import java.util.regex.Pattern;

public class ValidChecker {

    /**Checks if a given string is a valid email address using regular expressions.
     *@param email the email address to be checked
     *@return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        //a regular expression pattern that is used to validate an email address.
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                             "[a-zA-Z0-9_+&*-]+)*@" +
                             "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                             "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    /** Checks if a given string is a valid username using regular expressions.
      *@param username the username to be checked
       *@return true if the username is valid, false otherwise
       */
      public static boolean isValidUsername(String username) {
        //a regular expression pattern that is used to validate a username.
        String usernameRegex = "^[a-zA-Z0-9_-]{3,20}$";
        Pattern pattern = Pattern.compile(usernameRegex);
        return pattern.matcher(username).matches();
    }
}
