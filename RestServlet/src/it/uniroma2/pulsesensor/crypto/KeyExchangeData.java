package it.uniroma2.pulsesensor.crypto;

public class KeyExchangeData
{
	public static final int  port = 1600;
	public static final long primitive_root = 2;
	public static final long prime = 32771;
	public static final int keyLenghtInBytes = 32; //32 -> 32*8 = 256 bit
}

//(primitive_root, prime): (2,13),(14, 1031),(2,32771)