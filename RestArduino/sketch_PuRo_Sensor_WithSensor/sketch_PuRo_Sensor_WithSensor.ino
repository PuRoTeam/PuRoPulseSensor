#include <Ethernet.h>
#include <SPI.h>
#include <sha256.h>
#include <AES.h>
#include <TrueRandom.h>
//#include <MemoryFree.h>

int uid = 1; //Questo Arduino serve paziente con uid = 1

EthernetClient client;
byte arduinoMAC[] = {0x90, 0xA2 , 0xDA, 0x0D, 0xD9, 0x35};
IPAddress serverIP(192,168,1,103);

byte* mykey=NULL;
byte* my_iv=NULL;
char* cipherText=NULL;
uint8_t* hash=NULL;

const long g = 14;
const long p = 1031;

int delayms = 20;

//PulseSensor
//IMPORTANTE!!: Se uso True Random, devo tenere il pin 0 libero. Il sensore deve essere attaccato ad un altro pin, e bisogna modificare la linea di codice qui sotto
int pulsePin = 1;                 // Pulse Sensor purple wire connected to analog pin 0 (DA MODIFICARE! PIN 1)
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
 
  randomSeed(TrueRandom.random()); 
  
  Serial.begin(115200);  
  Serial.println("Arduino");
  
  if(Ethernet.begin(arduinoMAC) == 0) 
    Serial.println("Failed to configure Ethernet using DHCP");    
  
  client.connect(serverIP, 1600);
  
  if(client.connected()){
    Serial.println("Connected to Diffie Hellman Server");    
    
    //1. Start
    client.write("Start Diffie Hellman Exchange\n");   
     
    mykey = (byte*) malloc(32*sizeof(byte));  
     
    //2. Diffie Hellman
    diffieHellman(g, p, mykey); 
        
    char* keys = (char*) byte2StringHex(mykey, 32);

    Sha256.init();
    Sha256.print(keys);
    
    free(keys);
    
    hash = Sha256.result();
    my_iv = (byte*)malloc(16*sizeof(byte));
    for(int i=0; i<16; i++)
      my_iv[i] = hash[i];
      
    //3. Initial Timest
    writeCryptoInitialTimestamp(); //deve essere richiamato dopo l'inizializazione di IV
    
    //4. End
    client.write("End Diffie Hellman Exchange\n");
    
    client.stop();
  }else
    Serial.println("Impossible connecting to Diffie Hellman Server...");

  if(client.connect(serverIP, 8080))
    Serial.println("Connected to Tomcat");
  else
    Serial.println("Connection failed");
  
  // if analog input pin 0 is unconnected, random analog
  // noise will cause the call to randomSeed() to generate
  // different seed numbers each time the sketch runs.
  // randomSeed() will then shuffle the random function.
  //randomSeed(analogRead(0));
}

void loop()
{  
  Serial.println("Loop...");
  
  int value = (int)Signal;
  long timestamp = millis();
    
  if(mykey!=NULL && client.connected()) {      
               
    //cbc "sporca" il vettore di inizializzazione ad ogni chiamata a cbc_encrypt, quindi devo reinizializzarlo ad ogni iterazione
    for(int i=0; i<16; i++)
      my_iv[i] = hash[i];  
    
    int sizeOfPlainJson = strlen("[{\"uid\":") + getNumOfDigits(uid) + strlen(",") + strlen("\"timestamp\":") 
               + getNumOfDigits(timestamp) + strlen(",") + strlen("\"value\":") + getNumOfDigits(value) + strlen("}]") + 1; //carattere terminatore
    
    char* plainjson = (char*)malloc(sizeof(char)*sizeOfPlainJson);    
    sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d}]", uid, timestamp, value);
    
    /*if (QS == true) {
      Serial.println("BPMBPMBPMBPMBPMBPM");
      sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d,\"bpm\":%d}]", uid, timestamp, value, BPM); //se non metti la formattazione giusta, arduino si incazza! (timestamp -> long -> ld)
     }
    else {
      sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d}]", uid, timestamp, value); //se non metti la formattazione giusta, arduino si incazza! (timestamp -> long -> ld)
    }
    QS = false;*/
    
    Serial.print("plainjson: ");
    Serial.println(plainjson);
      
    int plainsize = strlen(plainjson);
    int blocks = 1;
    
    if(plainsize%N_BLOCK == 0)
      blocks = plainsize/N_BLOCK;
    else
      blocks = plainsize/N_BLOCK + 1;
    
    byte* myplain = (byte*) malloc(blocks*N_BLOCK);
    byte* mycipher = (byte*) malloc(blocks*N_BLOCK);
    
    string2Bytes(plainjson, myplain);
    
    padding(myplain, plainsize, blocks*N_BLOCK);
      
    AES aes;
    
    aes.set_key(mykey, 256);
    
    aes.cbc_encrypt(myplain, mycipher, blocks, my_iv);
       
    cipherText = byte2StringHex(mycipher, blocks*N_BLOCK);
    
    Serial.print("cipherText: ");
    Serial.println(cipherText);
    
    if(client.connected())
      sendPOST();
        
    free(myplain);    
    free(mycipher);
    free(cipherText);        
    free(plainjson);
  }  
  else if(!client.connected())
  {
    client.stop(); //prima di riconnettermi devo stoppare il client!
    client.connect(serverIP, 8080); //riprova a connetterti    
  }
  
  //for(;;){}
  
  delay(delayms);
}


