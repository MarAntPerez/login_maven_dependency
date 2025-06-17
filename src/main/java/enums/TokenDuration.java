package enums;

public enum TokenDuration {

	THIRTY_SECONDS(30), ONE_MINUTE(60), TWO_MINUTES(120), FIVE_MINUTES(300);

	private final long seconds;

	TokenDuration(long seconds) {
		this.seconds = seconds;
	}

	public long getSeconds() {
		return seconds;
	}

}
