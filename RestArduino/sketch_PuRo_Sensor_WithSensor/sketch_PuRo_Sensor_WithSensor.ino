#include <Ethernet.h>
#include <SPI.h>
#include <MemoryFree.h>
#include <sha256.h>
#include <AES.h>
#include <string.h>

byte arduinoMAC[] = {0x90, 0xA2 , 0xDA, 0x0D, 0xD9, 0x35};

/*
boolean tesisti = true;
IPAddress arduinoIP(160,80,97,135);
IPAddress dns_server(8,8,8,8);
IPAddress gateway(160,80,80,1);
IPAddress subnet(255,255,0,0);*/

EthernetClient client;
byte* mykey=NULL;
byte* my_iv;
const long g = 2;
const long p = 13;
char* myciphers;
uint8_t* hash;
int iter = 0;
int value = 0; //SOLO PER TEST
int delayms = 500;
IPAddress serverIP(192,168,1,101);
//IPAddress serverIP(160,80,97,128);

//PulseSensor
//  VARIABLES
int pulsePin = 0;                 // Pulse Sensor purple wire connected to analog pin 0
int blinkPin = 13;                // pin to blink led at each beat
//int fadePin = 5;                  // pin to do fancy classy fading blink at each beat
//int fadeRate = 0;                 // used to fade LED on with PWM on fadePin

volatile int BPM;                   // used to hold the pulse rate
volatile int Signal;                // holds the incoming raw data
volatile int IBI = 600;             // holds the time between beats, must be seeded! 
volatile boolean Pulse = false;     // true when pulse wave is high, false when it's low
volatile boolean QS = false;        // becomes true when Arduoino finds a beat.

void setup()
{  
  Serial.println("QUANDO METTI IL SENSORE RICORDATI DI LEVARE QS=TRUE ALL'INIZIO");
  //pinMode(blinkPin,OUTPUT);         // pin that will blink to your heartbeat!
  //pinMode(fadePin,OUTPUT);          // pin that will fade to your heartbeat!
  interruptSetup();                 // sets up to read Pulse Sensor signal every 2mS 
  // UN-COMMENT THE NEXT LINE IF YOU ARE POWERING The Pulse Sensor AT LOW VOLTAGE, 
  // AND APPLY THAT VOLTAGE TO THE A-REF PIN
  //analogReference(EXTERNAL);  
  
  Serial.begin(115200);  
  Serial.println("Arduino");
  
  /*if(tesisti) {
    Ethernet.begin(arduinoMAC, arduinoIP, dns_server, gateway, subnet);
  }
  else
  {
    if(Ethernet.begin(arduinoMAC) == 0) 
      Serial.println("Failed to configure Ethernet using DHCP");    
  }*/
  
  if(Ethernet.begin(arduinoMAC) == 0) 
    Serial.println("Failed to configure Ethernet using DHCP");    
  
  client.connect(serverIP, 1600);
  
  if(client.connected()){
    Serial.println("OK");
    mykey = (byte*) malloc(32*sizeof(byte));
        
    myDiffieHellman(g, p, mykey);
        
    // 7. Inizilizzazione dell'IV eseguendo l'hash della chiave e copiandolo nell'IV
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
  randomSeed(analogRead(0));

}

void loop()
{
  QS = true; //SOLO PER TEST!!!!
  
  Serial.println("Loop...");
  
  value = random(0, 100);
  long timestamp = millis();
  int uid = 1;
    
  if(mykey!=NULL && client.connected()) {
   
    sendDataToProcessing('S', Signal); 

    if(QS == true){      
      value = BPM;
      timestamp = (long)IBI; //FONDAMENTALE IL CAST!
  
      // Quantified Self flag is true when arduino finds a heartbeat
      //fadeRate = 255;                  // Set 'fadeRate' Variable to 255 to fade LED with pulse
      sendDataToProcessing('B',BPM);   // send heart rate with a 'B' prefix
      sendDataToProcessing('Q',IBI);   // send time between beats with a 'Q' prefix
      QS = false;                      // reset the Quantified Self flag for next time   
             
      //perchè cbc "sporca" il vettore di inizializzazione ad ogni chiamata a cbc_encrypt
      for(int i=0; i<16; i++)
        my_iv[i] = hash[i];  
      
      int sizeOfPlainJson = strlen("[{\"uid\":") + getNumOfDigits(uid) + strlen(",") + strlen("\"timestamp\":") 
                 + getNumOfDigits(timestamp) + strlen(",") + strlen("\"value\":") + getNumOfDigits(value) + strlen("}]") + 1; //carattere terminatore
      
      Serial.print("sizeOfPlainJson: ");
      Serial.println(sizeOfPlainJson);
      
      char* plainjson = (char*)malloc(sizeof(char)*sizeOfPlainJson);
      
      //char plainjson[] = "[{\"uid\":1,\"timestamp\":5,\"value\":1}]";
      
      Serial.println("Occhio se cambi timestamp da long a int cambia anche in sprintf");
      sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d}]", uid, timestamp, value); //se non metti la formattazione giusta, arduino si incazza! (timestamp -> long -> ld)
      
      Serial.print("strlen(plainjson): ");
      Serial.println(strlen(plainjson));    
      
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
    
    //ledFadeToBeat();
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


/*void ledFadeToBeat(){
  fadeRate -= 15;                         //  set LED fade value
  fadeRate = constrain(fadeRate,0,255);   //  keep LED fade value from going into negative numbers!
  analogWrite(fadePin,fadeRate);          //  fade LED
}*/


void sendDataToProcessing(char symbol, int data ){
  Serial.print(symbol);                // symbol prefix tells Processing what type of data is coming
  Serial.println(data);                // the data to send culminating in a carriage return
}
