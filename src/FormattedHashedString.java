
public class FormattedHashedString {
	private final String user;					/* user0 */
	private final String MD5nSalt;				/* $1$ + salt */
	private final String hashedPassword;		/* $ + hashed value*/
	private final String expectedHashedValue;	/* $<MD5>$<salt>$<hashed value> */

	FormattedHashedString(String hashedString) {
		String[] splitted = hashedString.split("\\$", 4);

		user = splitted[0];
		MD5nSalt = new String("$1$" + splitted[2]);
		hashedPassword = new String("$" + splitted[3]);
		expectedHashedValue = new String(MD5nSalt + hashedPassword);
	}

	public String getUser() {
		return user;
	}

	public String getMD5nSalt() {
		return MD5nSalt;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public String getExpectedHashedValue() {
		return expectedHashedValue;
	}


}
