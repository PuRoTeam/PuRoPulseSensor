#include <Ethernet.h>
#include <SPI.h>
#include <MemoryFree.h>
#include <sha256.h>
#include <AES.h>
#include <string.h>

byte arduinoMAC[] = {0x90, 0xA2 , 0xDA, 0x0D, 0xD9, 0x35};
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
IPAddress local(192,168,1,100);



void setup()
{  
  Serial.println("Se non funziona, controlla l'indirizzo del Server!");
  //IPAddress arduinoIP(192,168,1,177); 
  
  Serial.begin(115200);
  
  Serial.println("Arduino");
  
  if(Ethernet.begin(arduinoMAC) == 0){
    Serial.println("Failed to configure Ethernet using DHCP");
    //Ethernet.begin(arduinoMAC, arduinoIP);
  }
  
  client.connect(local, 1600);
  
  if(client.connected()){
    Serial.println("OK");
    mykey = (byte*) malloc(32*sizeof(byte));
    
    //Serial.print("freeMemoryI=");
    //Serial.println(freeMemory());
    
    myDiffieHellman(g, p, mykey);
    
    //Serial.print("freeMemoryC=");
    //Serial.println(freeMemory());
    
    // 7. Inizilizzazione dell'IV eseguendo l'hash della chiave e copiandolo nell'IV
    char* keys = (char*) byte2StringHex(mykey, 32);
    
    //Serial.print("keys: ");
    //Serial.println(keys);
    
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
    
    //Serial.print("freeMemoryD=");
    //Serial.println(freeMemory());
    
    client.stop();
  }else
    Serial.println("Impossible connecting to DH server...");

  if(client.connect(local, 8080))
    Serial.println("Connected to Tomcat");
  else
    Serial.println("Connection failed");

  Serial.print("freeMemoryE=");
  Serial.println(freeMemory());
  
  //SOLO PER TEST
  // if analog input pin 0 is unconnected, random analog
  // noise will cause the call to randomSeed() to generate
  // different seed numbers each time the sketch runs.
  // randomSeed() will then shuffle the random function.
  randomSeed(analogRead(0));

}

void loop()
{
  Serial.println("Loop...");

  Serial.print("freeMemoryA=");
  Serial.println(freeMemory());
  
  //if(client.connected())
  //  do_stuff();
  //else
  //  client.connect(local, 8080); --> riprova a connetterti
    
    if(mykey!=NULL && client.connected()){
    //if(mykey!=NULL){
    
    //Nel caso puntatori    
    value = random(0, 100);
    long timestamp = millis();
    int uid = 1;

    /*if(value <100)
      value = value+10;
    else
      value = value-100;*/
      
    //perchè cbc "sporca" il vettore di inizializzazione ad ogni chiamata a cbc_encrypt
    for(int i=0; i<16; i++)
      my_iv[i] = hash[i];  

   
    //char plainjson[100];
    int sizeOfPlainJson = strlen("[{\"uid\":") + getNumOfDigits(uid) + strlen(",") + strlen("\"timestamp\":") 
               + getNumOfDigits(timestamp) + strlen(",") + strlen("\"value\":") + getNumOfDigits(value) + strlen("}]") + 1; //carattere terminatore
    
    Serial.print("sizeOfPlainJson: ");
    Serial.println(sizeOfPlainJson);
    
    char* plainjson = (char*)malloc(sizeof(char)*sizeOfPlainJson);
    
    //char plainjson[] = "[{\"uid\":1,\"timestamp\":5,\"value\":1}]";
    //String plainjson = "[{\"uid\":1,\"timestamp\":5,\"value\":1}]"; //////////////////
                     //[{\"uid\":1,\"timestamp\":1,\"value\":1}]
  
    Serial.print("value: ");
    Serial.println(value);
    
    sprintf(plainjson, "[{\"uid\":%d,\"timestamp\":%ld,\"value\":%d}]", uid, timestamp, value); //se non metti la formattazione giusta, arduino si incazza! (timestamp -> long -> ld)
  
    Serial.print("strlen(plainjson): ");
    Serial.println(strlen(plainjson));
  
    Serial.print("freeMemoryD=");
    Serial.println(freeMemory());      
  
    Serial.print("plainjson: ");
    Serial.println(plainjson);
  
    //String myplains = "1234567890123456789012345678901234567890123456";
    
    int plainsize = strlen(plainjson);
    //int plainsize = plainjson.length(); ///////////////////
    int mount = sizeof(byte)*plainsize;
    int blocks = 1;
    
    if(plainsize%N_BLOCK == 0)
      blocks = plainsize/16;
    else
      blocks = plainsize/16 + 1;
  
    byte* myplain = (byte*) malloc(blocks*N_BLOCK);
    byte* mycipher = (byte*) malloc(blocks*N_BLOCK);
  
    string2Bytes(plainjson, myplain);
    //string2BytesString(plainjson, myplain); //////////////////////
  
    padding(myplain, plainsize, blocks*N_BLOCK);
    
    //Serial.print("freeMemoryB=");
    //Serial.println(freeMemory());
  
    AES aes;
  
    aes.set_key(mykey, 256);
    
    if (blocks == 1)
      aes.encrypt(myplain, mycipher);
    else
      aes.cbc_encrypt(myplain, mycipher, blocks, my_iv);

    //Serial.print("my_iv: ");
    //Serial.println(byte2StringHex(my_iv, 16));
    
    /*Serial.print("mycipher: ");
    Serial.println(byte2StringHex(mycipher, blocks*N_BLOCK));
    
    Serial.print("myplain: ");
    Serial.println(byte2StringHex(myplain, blocks*N_BLOCK));
    
    Serial.print("plainjson: ");
    Serial.println(plainjson);*/
       
    free(myplain);  
       
    myciphers = byte2StringHex(mycipher, blocks*N_BLOCK);
    Serial.print("myciphers: ");
    Serial.println(myciphers);
    
    if(client.connected())
      sendPOST();
        
    //Serial.print("freeMemoryC=");
    //Serial.println(freeMemory());
    
    free(mycipher);
    free(myciphers);
    
    //Serial.print("freeMemoryD=");
    //Serial.println(freeMemory());
    
    free(plainjson);
  
    
  }
  else if(!client.connected())
  {
    client.stop(); //prima di riconnettermi devo stoppare il client!
    client.connect(local, 8080); //riprova a connetterti
  
  }  
  /*iter++;
  if(iter > 3)
  {  for(;;){} }*/
    
  delay(delayms); //se troppo piccolo riempio la finestra TCP del ricevente --> vedi wireshark
}
