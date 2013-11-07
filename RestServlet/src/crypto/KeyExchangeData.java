package crypto;

public class KeyExchangeData
{
	public static final int  port = 1600;
	public static final long primitive_root = 14;
	public static final long prime = 1031;
	public static final int keyLenghtInBytes = 32; //32 -> 32*8 = 256 bit
}

//(primitive_root, prime): (2,13),(14, 1031),(2,32771)