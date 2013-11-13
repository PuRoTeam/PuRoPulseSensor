package it.uniroma2.pulsesensor.servlet;

public class ShareTime {
	
	private long now;
	private long timestampArduino;
	
	public ShareTime(long n, long tA){
		this.now = n;
		this.timestampArduino = tA;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public long getTimestampArduino() {
		return timestampArduino;
	}

	public void setTimestampArduino(long timestampArduino) {
		this.timestampArduino = timestampArduino;
	}

}
