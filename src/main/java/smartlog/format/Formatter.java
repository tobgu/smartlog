package smartlog.format;

public interface Formatter {
	public void dispatch(LogMessage message);
}
