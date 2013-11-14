#include <Ethernet.h>
#include <SPI.h>
#include <sha256.h>
#include <AES.h>
#include <TrueRandom.h>

int uid = 1; //Questo Arduino serve paziente con uid = 1

EthernetClient client;
byte arduinoMAC[] = {0x90, 0xA2 , 0xDA, 0x0D, 0xD9, 0x35};
IPAddress serverIP(192,168,1,102);

byte* mykey=NULL;
byte* my_iv=NULL;
char* cipherText=NULL;
uint8_t* hash=NULL;

const long g = 2;
const long p = 32771;

int delayms = 20;

//PulseSensor
//IMPORTANTE!!: Se uso True Random, devo tenere il pin 0 libero. Il sensore deve essere attaccato ad un altro pin, e bisogna modificare la linea di codice qui sotto
int pulsePin = 1;                 // Pulse Sensor purple wire connected to analog pin 0 (DA MODIFICARE! PIN 1)
//int blinkPin = 13;                // pin to blink led at each beat

volatile int BPM = 0;               // used to hold the pulse rate L'HO IMPOSTATO IO A ZERO, PRIMA ERA NON INIZIALIZZATO
volatile int Signal = 0;            // holds the incoming raw data
volatile int IBI = 600;             // holds the time between beats, must be seeded! 
volatile boolean Pulse = false;     // true when pulse wave is high, false when it's low
//volatile boolean QS = false;      // becomes true when Arduoino finds a beat.

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
    client.write("Start\n");   
     
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
    client.write("End\n");
    
    client.stop();
  }else
    Serial.println("Impossible connecting to Diffie Hellman Server...");

  if(client.connect(serverIP, 8080))
    Serial.println("Connected to Tomcat");
  else
    Serial.println("Connection to Tomcat failed");
}

void loop()
{
  Serial.println("Loop...");
  
  int value = Signal;
  long timestamp = millis();  
    
  if(mykey!=NULL && client.connected()) {      
               
    //cbc "sporca" il vettore di inizializzazione ad ogni chiamata a cbc_encrypt, quindi devo reinizializzarlo ad ogni iterazione
    for(int i=0; i<16; i++)
      my_iv[i] = hash[i];
   
    /*int beatPerMinute = BPM;    
    int sizeOfPlainJson = strlen("[{\"uid\":") + getNumOfDigits(uid) + strlen(",") + strlen("\"timestamp\":") 
             + getNumOfDigits(timestamp) + strlen(",") + strlen("\"value\":") + getNumOfDigits(value) + strlen(",")
             + strlen("\"bpm\":") + getNumOfDigits(BPM) + strlen("}]") + 1; //carattere terminatore //CON BPM*/
             
    int sizeOfPlainJson = strlen("[{\"uid\":") + getNumOfDigits(uid) + strlen(",") + strlen("\"timestamp\":") 
            + getNumOfDigits(timestamp) + strlen(",") + strlen("\"value\":") + getNumOfDigits(value) + strlen("}]") + 1; //carattere terminatore
    
    char* plainjson = (char*)malloc(sizeof(char)*sizeOfPlainJson);
    
    //sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d,\"BPM\":%d}]", uid, timestamp, value, BPM); //CON BPM    
    
    sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d}]", uid, timestamp, value);
        
    Serial.print("plainjson: ");
    Serial.println(plainjson);
       
    cipherText = encrypt(plainjson);
    free(plainjson);
    
    Serial.print("cipherText: ");
    Serial.println(cipherText);
    
    if(client.connected())
      sendPOST();        

    free(cipherText);
  }  
  else if(!client.connected())
  {
    client.stop(); //prima di riconnettermi devo stoppare il client!
    client.connect(serverIP, 8080); //riprova a connetterti    
  }
  
  //for(;;){}
  
  delay(delayms);
}


