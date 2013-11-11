//Esegue la procedura di creazione della chiave AES tramite DH
void diffieHellman(long g, long p, byte* key)
{  
  //Fase di scambio
  long temp, X;
  char* Ystring;

  X = random(2, p-1);

  temp = modpowFast(g, X, p);  
  Ystring = (char*)malloc(sizeof(char)*(getNumOfDigits(temp) + 1));
  sprintf(Ystring, "%d\n", temp); //include il carattere "newline", quindi non devo aggiungere println dopo il for
        
  for(int i=0; i<strlen(Ystring); i++){
    client.write(Ystring[i]);
  }
  
  delay(500); //attendo per essere sicuro di avere dati da leggere
    
  free(Ystring);     
  Ystring = (char*)malloc(sizeof(char)*(getNumOfDigits(p) + 1)); //la dimensione è da definire in base al primo con cui fai il modulo - p=13 in realtà bastano due cifre +1 (carattere terminatore)
  
  int i = 0;
  while(client.available()){    
    Ystring[i] = (char)client.read();
    i++;
  }
  client.flush();
  
  //potrei usare una memset(Ystring, '-', getNumOfDigits(temp) + 1)) al posto di free e malloc, ma non cambierebbe occupazione di memoria
  //The string can contain additional characters after those that form the integral number, which are ignored and have no effect on the behavior of this function.    
  temp = modpowFast(atoi(Ystring), X, p);
  free(Ystring);
 
  Ystring = (char*)malloc(sizeof(char)*(getNumOfDigits(temp) + 1));
  sprintf(Ystring, "%d", temp);
   
  //Esegue lo SHA256 del risultato dello scambio DH
  //e lo inserisce nella chiave di AES    
  uint8_t* hash;
  //Sha256Class shaObj; 
  Sha256.init();
  Sha256.print(Ystring);
  hash = Sha256.result(); //variabile locale

  for(int i = 0; i < 32; i++)
    key[i] = hash[i];
    
  free(Ystring);
}

/*
//lasciare commentata
char* encrypt(char* plainTxt)
{
  Serial.print("plainTxt: ");
  Serial.println(plainTxt);

  //cbc "sporca" il vettore di inizializzazione ad ogni chiamata a cbc_encrypt, quindi devo reinizializzarlo ad ogni iterazione
  for(int i=0; i<16; i++)
    my_iv[i] = hash[i];

  int plainsize = strlen(plainTxt);
  int blocks = 1;
  
  if(plainsize%N_BLOCK == 0)
    blocks = plainsize/N_BLOCK;
  else
    blocks = plainsize/N_BLOCK + 1;  
   
  byte* myplain = (byte*) malloc(blocks*N_BLOCK);
  byte* mycipher = (byte*) malloc(blocks*N_BLOCK);
  
  string2Bytes(plainTxt, myplain);
  
  padding(myplain, plainsize, blocks*N_BLOCK);
    
  AES aes;  
  aes.set_key(mykey, 256);
  aes.cbc_encrypt(myplain, mycipher, blocks, my_iv);
  
  char* cipherTxt = byte2StringHex(mycipher, blocks*N_BLOCK); //non include "newline" quindi lo devo scrivere al client

  Serial.print("cipherTxt: ");
  Serial.println(cipherTxt);

  free(myplain);
  free(mycipher);
  
  return cipherTxt;
}
*/
void writeCryptoInitialTimestamp()
{  
  long initialTimestamp = millis(); //millisecondi da avvio di arduino
  int numDigits = getNumOfDigits(initialTimestamp);
  char* plainInitialTimestamp = (char*)malloc(sizeof(char)*(numDigits + 1)); //+1 carattere terminatore
  sprintf(plainInitialTimestamp, "%ld", initialTimestamp);

  Serial.print("plainInitialTimestamp: ");
  Serial.println(plainInitialTimestamp);
    
  int plainsize = strlen(plainInitialTimestamp);
  int blocks = 1; //è sempre un solo blocco, perchè è praticamente impossibile superare le 16 cifre con i millisecondi
  
  if(plainsize%N_BLOCK == 0)
    blocks = plainsize/N_BLOCK;
  else
    blocks = plainsize/N_BLOCK + 1;
   
  byte* myplain = (byte*) malloc(blocks*N_BLOCK);
  byte* mycipher = (byte*) malloc(blocks*N_BLOCK);
  
  string2Bytes(plainInitialTimestamp, myplain);
  free(plainInitialTimestamp); 
  
  padding(myplain, plainsize, blocks*N_BLOCK);
    
  AES aes;  
  aes.set_key(mykey, 256);
  aes.cbc_encrypt(myplain, mycipher, blocks, my_iv);
  free(myplain);
  
  char* cryptoInitialTimestamp = byte2StringHex(mycipher, blocks*N_BLOCK); //non include "newline" quindi lo devo scrivere al client
  free(mycipher);
  
  Serial.print("cryptoInitialTimestamp: ");
  Serial.println(cryptoInitialTimestamp);  
    
  for(int i=0; i<strlen(cryptoInitialTimestamp); i++){
    client.write(cryptoInitialTimestamp[i]);
  }
  client.println();
  client.flush(); 
   
  free(cryptoInitialTimestamp);
}

void sendPOST()
{ 
  int beatPerMinute = BPM;
  char* beatPerMinuteStr = (char*)malloc(sizeof(char)*(getNumOfDigits(beatPerMinute) + 1));  
  sprintf(beatPerMinuteStr, "%d", beatPerMinute);  
  
  /*for(int i=0; i<16; i++)
      my_iv[i] = hash[i];
  char* cryptoBPM;
  encrypt(mykey, beatPerMinuteStr, cryptoBPM);
  free(cryptoBPM);*/
  
  client.print("POST /RestServlet/ HTTP/1.1\r\n");          
  client.print("Host: it.uniroma2.arduino\r\n");
  client.print("Content-Type: application/x-www-form-urlencoded\r\n");
  client.print("User-Agent: Arduino/1.0\r\n");
  
  client.print("Content-Length: ");  
  int contentLength = strlen("JSON=") + strlen(cipherText) + strlen("&BPM=") + strlen(beatPerMinuteStr);
  char* strConLen = (char*)malloc(sizeof(char)*(getNumOfDigits(contentLength) + 1));
  sprintf(strConLen, "%d", contentLength);
  client.print(strConLen);
  client.print("\r\n");
  free(strConLen);
  
  client.print("\r\n"); //spazio fra header e body
  
  client.print("JSON=");
  client.print(cipherText);
  
  client.print("&BPM=");
  client.print(beatPerMinuteStr);
  free(beatPerMinuteStr);

  client.print("\r\n");
}

/*long powMod(long base, long e, long mod)
{
  int i;	
  long ret = 1;
  for(i = 1; i <= e; i++)	
    ret = (ret*base)%mod;
  return ret;
}*/

long modpowFast(long a,long b,long mod)
{
    long product,pseq;
    product=1;
    pseq=a%mod;
    while(b>0)
    {
        if(b&1)
            product=modmult(product,pseq,mod);
        pseq=modmult(pseq,pseq,mod);
        b>>=1;
    }
    return product;
}

long modmult(long a,long b,long mod)
{
    if (a == 0 || b < mod / a)
        return (a*b)%mod;
    long sum;
    sum = 0;
    while(b>0)
    {
        if(b&1)
            sum = (sum + a) % mod;
        a = (2*a) % mod;
        b>>=1;
    }
    return sum;
}

int getNumOfDigits(long num)
{
  return (int) (log10(num)) + 1;
}

char* byte2StringHex(byte* b, int n)
{  
  char* hexstr = (char*)malloc(n*2 + 1);
  int i=0;
  for(i=0; i<n; i++) {
    sprintf(hexstr+i*2, "%02x", b[i]);
  }
  hexstr[i*2] = 0;
 
  return hexstr;
}

void string2Bytes(char* s, byte* a)
{
  for(int i=0; i<strlen(s); i++){
    a[i] = (byte)s[i];
  }
}
/*
//lasciare commentata
void printHash(uint8_t* hash, int n)
{
  int i;
  for (i=0; i<n; i++){
    Serial.print("0123456789abcdef"[hash[i]>>4]);
    Serial.print("0123456789abcdef"[hash[i]&0xf]);
  }
  Serial.println();
}
*/
void padding(byte* b, int d, int r)
{  
  int c = d%16;
  c = 16-c;
  
  for(int i=0; i<c; i++)
    b[r-c+i] = (byte)c;
}
