#include <Ethernet.h>
#include <SPI.h>
#include <MemoryFree.h>
#include <sha256.h>
#include <AES.h>
#include <string.h>

EthernetClient client;
byte arduinoMAC[] = {0x90, 0xA2 , 0xDA, 0x0D, 0xD9, 0x35};

byte* mykey=NULL;
byte* my_iv;

const long g = 2;
const long p = 13;

char* myciphers;
uint8_t* hash;

int iter = 0;
int value = 0; //SOLO PER TEST
int delayms = 200;

IPAddress serverIP(192,168,1,101);

//PulseSensor
int pulsePin = 0;                 // Pulse Sensor purple wire connected to analog pin 0
int blinkPin = 13;                // pin to blink led at each beat

volatile int BPM;                   // used to hold the pulse rate
volatile int Signal = 0;                // holds the incoming raw data
volatile int IBI = 600;             // holds the time between beats, must be seeded! 
volatile boolean Pulse = false;     // true when pulse wave is high, false when it's low
volatile boolean QS = false;        // becomes true when Arduoino finds a beat.

void setup()
{  
  interruptSetup();                 // sets up to read Pulse Sensor signal every 2mS 
  // UN-COMMENT THE NEXT LINE IF YOU ARE POWERING The Pulse Sensor AT LOW VOLTAGE, 
  // AND APPLY THAT VOLTAGE TO THE A-REF PIN
  //analogReference(EXTERNAL);  
  
  Serial.begin(115200);  
  Serial.println("Arduino");
  
  if(Ethernet.begin(arduinoMAC) == 0) 
    Serial.println("Failed to configure Ethernet using DHCP");    
  
  client.connect(serverIP, 1600);
  
  if(client.connected()){
    Serial.println("OK");
    mykey = (byte*) malloc(32*sizeof(byte));
        
    myDiffieHellman(g, p, mykey);
    
    char* keys = (char*) byte2StringHex(mykey, 32);
    
    //Sha256Class shaObj; 
    Sha256.init();
    Sha256.print(keys);
    
    hash = Sha256.result(); //GLOBALE
    //uint8_t* hash = Sha256.result(); //LOCALE
    my_iv = (byte*)malloc(16*sizeof(byte));
    for(int i=0; i<16; i++)
      my_iv[i] = hash[i];
    
    printHash(my_iv, 16);
    
    free(keys);
    
    client.stop();
  }else
    Serial.println("Impossible connecting to Diffie Hellman server...");

  if(client.connect(serverIP, 8080))
    Serial.println("Connected to Tomcat");
  else
    Serial.println("Connection failed");
  
  //SOLO PER TEST
  // if analog input pin 0 is unconnected, random analog
  // noise will cause the call to randomSeed() to generate
  // different seed numbers each time the sketch runs.
  // randomSeed() will then shuffle the random function.
  //randomSeed(analogRead(0));

}

void loop()
{  
  Serial.println("Loop...");
  
  //value = random(0, 100);
  value = (int)Signal;
  long timestamp = millis();
  int uid = 1;
    
  if(mykey!=NULL && client.connected()) {      
    
    //timestamp = (long)IBI; //FONDAMENTALE IL CAST!
    //sendDataToProcessing('S', Signal);
    // Quantified Self flag is true when arduino finds a heartbeat
    //sendDataToProcessing('B',BPM);   // send heart rate with a 'B' prefix
    //sendDataToProcessing('Q',IBI);   // send time between beats with a 'Q' prefix
           
    //perchè cbc "sporca" il vettore di inizializzazione ad ogni chiamata a cbc_encrypt
    for(int i=0; i<16; i++)
      my_iv[i] = hash[i];  
    
    int sizeOfPlainJson = strlen("[{\"uid\":") + getNumOfDigits(uid) + strlen(",") + strlen("\"timestamp\":") 
               + getNumOfDigits(timestamp) + strlen(",") + strlen("\"value\":") + getNumOfDigits(value) + strlen("}]") + 1; //carattere terminatore
    
    char* plainjson = (char*)malloc(sizeof(char)*sizeOfPlainJson);
    
    //char plainjson[] = "[{\"uid\":1,\"timestamp\":5,\"value\":1}]";
    
    sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d}]", uid, timestamp, value); //se non metti la formattazione giusta, arduino si incazza! (timestamp -> long -> ld)
    
    Serial.print("plainjson: ");
    Serial.println(plainjson);
      
    int plainsize = strlen(plainjson);
    int mount = sizeof(byte)*plainsize;
    int blocks = 1;
    
    if(plainsize%N_BLOCK == 0)
      blocks = plainsize/16;
    else
      blocks = plainsize/16 + 1;
    
    byte* myplain = (byte*) malloc(blocks*N_BLOCK);
    byte* mycipher = (byte*) malloc(blocks*N_BLOCK);
    
    string2Bytes(plainjson, myplain);
    
    padding(myplain, plainsize, blocks*N_BLOCK);
      
    AES aes;
    
    aes.set_key(mykey, 256);
    
    if (blocks == 1)
      aes.encrypt(myplain, mycipher);
    else
      aes.cbc_encrypt(myplain, mycipher, blocks, my_iv);
       
    free(myplain);  
       
    myciphers = byte2StringHex(mycipher, blocks*N_BLOCK);
    
    Serial.print("myciphers: ");
    Serial.println(myciphers);
    
    if(client.connected())
      sendPOST();
            
    free(mycipher);
    free(myciphers);        
    free(plainjson);
  }  
  else if(!client.connected())
  {
    client.stop(); //prima di riconnettermi devo stoppare il client!
    client.connect(serverIP, 8080); //riprova a connetterti    
  }     

  /*iter++;
  if(iter > 3)
  {  for(;;){} }*/
  
  delay(delayms); //se troppo piccolo riempio la finestra TCP del ricevente --> vedi wireshark
}

void sendDataToProcessing(char symbol, int data ){
  Serial.print(symbol);                // symbol prefix tells Processing what type of data is coming
  Serial.println(data);                // the data to send culminating in a carriage return
}
