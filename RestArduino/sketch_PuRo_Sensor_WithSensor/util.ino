/*
*  Esegue la procedura di creazione della chiave AES tramite DH
*  Restituisce 0 se tutto è andato a buon fine
*/
void myDiffieHellman(long g, long p, byte* key)
{    
  //Serial.print("freeMemoryA=");
  //Serial.println(freeMemory());
  
  // 2. Fase di scambio
  long temp, X;
  char* Ystring;

  X = random(2, p-1);

  temp = powMod(g, X, p);  
  Ystring = (char*)malloc(sizeof(char)*(getNumOfDigits(temp) + 1));
  sprintf(Ystring, "%d\n", temp);
  
  client.write("ciao\n"); //ciao
  for(int i=0; i<strlen(Ystring); i++){
    client.write(Ystring[i]);
  }
  client.write("fine\n"); //fine
  
  //Serial.print("Ystring: ");
  //Serial.println(Ystring);
  
  delay(500);
    
  free(Ystring);     
  Ystring = (char*)malloc(sizeof(char)*10); //la dimensione è da definire in base al primo con cui fai il modulo - p=13 in realtà bastano due cifre +1 (carattere termintore)
  
  int i = 0;
  while(client.available()){    
    Ystring[i] = (char)client.read();
    i++;
  }
  client.flush();
  
  //potrei usare una memset(Ystring, '-', getNumOfDigits(temp) + 1)) al posto di free e malloc, ma non cambierebbe occupazione di memoria
  //The string can contain additional characters after those that form the integral number, which are ignored and have no effect on the behavior of this function.    
  temp = powMod(atoi(Ystring), X, p);
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
  
  //Serial.print("freeMemoryB=");
  //Serial.println(freeMemory());

}

void writeInitialTimestamp()
{
  long initialTimestamp = millis(); //millisecondi da avvio di arduino
  int numDigits = getNumOfDigits(initialTimestamp);
  char* strInitialTimestamp = (char*)malloc(sizeof(char)*(numDigits + 1)); //+1 carattere terminatore
  sprintf(strInitialTimestamp, "%ld\n", initialTimestamp);
  //Serial.print("strInitialTimestamp ");
  //Serial.println(strInitialTimestamp);
  for(int i=0; i<strlen(strInitialTimestamp); i++){
    client.write(strInitialTimestamp[i]);
  }
  
  free(strInitialTimestamp);
}

/*void sendPOST(String* request)
{
  Serial.print("freeMemoryF=");
  Serial.println(freeMemory());
  
  Serial.println(*request);
  
    client.println("POST /RestServlet/ HTTP/1.1");           
    //Serial.println("POST /RestServlet/ HTTP/1.1");           
    client.println("Host: it.uniroma2.arduino");
    client.println("Content-Type: application/x-www-form-urlencoded");
    //client.println("Connection: close");
    client.println("User-Agent: Arduino/1.0");
    client.print("Content-Length: ");
    
    String s = "JSON=";
    //s.concat(request);
    client.println(s.length());
    client.println();
    client.print(s.concat(*request));
    Serial.println(s);
    client.println();  
}*/
void sendPOST()
{
  //PRIMO METODO
  /*
  int contentLength = strlen("JSON=") + strlen(myciphers);
  int postLength = strlen("POST /RestServlet/ HTTP/1.1\r\n") + strlen("Host: it.uniroma2.arduino\r\n") + strlen("Content-Type: application/x-www-form-urlencoded\r\n") +
                 strlen("User-Agent: Arduino/1.0\r\n") + strlen("Content-Length: ") + contentLength + strlen("\r\n") + strlen("\r\n") + +strlen("JSON=") + strlen(myciphers) + strlen("\r\n") + 1;
	
  char* post = (char*)malloc(sizeof(char)*postLength);
  sprintf(post, "POST /RestServlet/ HTTP/1.1\r\nHost: it.uniroma2.arduino\r\nContent-Type: application/x-www-form-urlencoded\r\nUser-Agent: Arduino/1.0\r\nContent-Length: %d\r\n\r\nJSON=%s\r\n", contentLength, myciphers);
  client.print(post);
  free(post);
  client.flush();
  */
  
  //SECONDO METODO  
  client.print("POST /RestServlet/ HTTP/1.1\r\n");          
  client.print("Host: it.uniroma2.arduino\r\n");
  client.print("Content-Type: application/x-www-form-urlencoded\r\n");
  //client.println("Connection: close"); così chiude la connessione al primo pacchetto inviato/ricevuto
  client.print("User-Agent: Arduino/1.0\r\n");
  
  client.print("Content-Length: ");  
  int contentLength = strlen("JSON=")+strlen(myciphers);
  char* strConLen = (char*)malloc(sizeof(char)*(getNumOfDigits(contentLength) + 1));
  sprintf(strConLen, "%d", contentLength); 
  client.print(strConLen);
  client.print("\r\n");
  free(strConLen);
  
  client.print("\r\n"); //spazio fra header e body
  client.print("JSON=");
  client.print(myciphers);
  client.print("\r\n");
  client.flush();
  
  
  //ecco cosa fà flush
  //void EthernetClient::flush() {
  //while (available())
  //  read();
  //}
  
  //TERZO METODO
  /*
  client.println("POST /RestServlet/ HTTP/1.1");          
  client.println("Host: it.uniroma2.arduino");
  client.println("Content-Type: application/x-www-form-urlencoded");
  //client.println("Connection: close"); così chiude la connessione al primo pacchetto inviato/ricevuto
  client.println("User-Agent: Arduino/1.0");
  client.print("Content-Length: ");
  client.println(strlen(myciphers)+strlen("JSON="));
  client.println();
  client.print("JSON=");
  client.println(myciphers);
  //client.print(myciphers);
  //client.println();
  client.flush();
  */
}

long powMod(long base, long e, long mod)
{
  int i;	
  long ret = 1;
  for(i = 1; i <= e; i++)	
    ret = (ret*base)%mod;
  return ret;
}

int getNumOfDigits(long num)
{
  return (int) (log10(num)) + 1;
}

/*
char* int2string(long num)
{
    char* str;
    int num_of_digits = (int) (log10(num)) + 1;
    int remainder;
    str = (char*)malloc(sizeof(char)*(num_of_digits + 1));
    str[num_of_digits] = '\0';
    for (int i = num_of_digits-1; i >= 0; --i) {
        remainder = num % 10;
        char rem_char = (char)(remainder + '0'); //ASCII value of '0' = 48
        str[i] = rem_char;
        num = num / 10;
    }
    return str;
}*/

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

void string2BytesString(String s, byte* a)
{
  for(int i=0; i<s.length(); i++){
    a[i] = (byte)s.charAt(i);
  }
}

void printHash(uint8_t* hash, int n)
{
  int i;
  for (i=0; i<n; i++){
    Serial.print("0123456789abcdef"[hash[i]>>4]);
    Serial.print("0123456789abcdef"[hash[i]&0xf]);
  }
  Serial.println();
}

void padding(byte* b, int d, int r)
{
  int c = d%16;
  c = 16-c;
  
  for(int i=0; i<c; i++)
    b[r-c+i] = (byte)c;
}
