PuRoSensor
==========

Il progetto è centrato sulle tematiche dell’home medical equipment HME ovvero della capacità di remotizzare la capacità di monitoraggio di alcuni parametri fisici di un individuo. Per questo progetto si intende monitorare il battito cardiaco di un paziente attraverso l’utilizzo congiunto di componenti hardware dedicate all’acquisizione del dato e alla comunicazione dello stesso in remoto. I dati medici, di qualunque natura essi siano, rappresentano un’informazione che è necessario gestire in modo opportuno in termini di controllo degli accessi. L’hardware selezionato per il seguente progetto e che
verrà messo a disposizione è rappresentato da:

- Arduino Ethernet Rev3 WITH PoE Code: A000074

  (http://store.arduino.cc/it/index.php?main_page=product_info&cPath=11_12&products_id=201)
  
  Si tratta di una versione del circuito hardware programmabile Arduino dotato di un’interfaccia di rete 
  ethernet dotata della capacità Power Over Ethernet per  l’alimentazione direttamente da eternet e di una 
  shield seriale/usb che ne consente la programmazione
  
- Pulse Sensor

  (http://pulsesensor.myshopify.com/)
  
  Si tratta di un semplice hardware in grado di monitorare la forma d’onda del battito cardiaco e progettato 
  per essere integrato con il circuito arduino attraverso una connessione diretta cablata.
  
- Il progetto prevede la realizzazione di una soluzione distribuita costituita da una componente client 
  e da una componente server che sia in grado di effettuare il monitoraggio del ritmo cardiaco di un individio. 
  Di seguito viene mostrato una semplificazione grafica della soluzione richiesta con in evidenza le due 
  componenti client e server e i due principali flussi di informazioni: 
  
  1) Il primo flusso di comunicazione è interno al client e prevede la connessione fisica tra il Pulse Sensor ed il       circuito Arduino e la realizzazione di uno sketch (programma nel linguaggio proprio di Arduino) in grado di elaborare   il segnale cosi come viene catturato dal sensore.
  
  2) Il secondo flusso di comunicazione è invece tra Arduino ed il Server per la remotizzazione del dato 
  catturato dal sensore ed elaborato localmente al cliente; in questo caso sarà necessario realizzare sia uno sketch in   grado di comunicare su ethernet che una applicazione web per la ricezione delle informazioni, la memorizzazione pers
  istente delle stesse. La stessa applicazione dovrà poi esporre una semplice interfaccia utente che consenta
  la consultazione del patrimonio informativo catturato. 
  
  Riassumendo, La componente client è composta dal circuito Arduino e da un hardware specifico Pulse Sensor; 
  la principale responsabilità del client è il collezionamento dei dati ottenuti dal Pulse Sensor (e.g. frequenza
  cardiaca) e la consegna al circuito Arduino il quale dovrà provvedere ad invocare opportuni servizi per
  la persistenza remota degli stessi.
  
  L’hardware PulseSensor è stato progettato per essere compatibile con il circuito Arduino e dispone già 
  di alcune porzioni di codice esemplificativo che consentono l’interazione tra i due che possono essere
  utilizzate come base di partenza per il progetto e che se ritenuto opportuno possono essere estese.
  
  La componente server, invece, avrà il compito di gestire le informazioni ricevute dalla componente
  cliente e garantire una presentazione grafica degli stessi secondo due diverse modalità:
  
  - tempo reale: qualora ci sia un flusso di dati in arrivo dalla componente client, il server dovrà 
  consentirne la visualizzazione in tempo reale mostrando quindi i parametri vitali recuperati dal Pulse Sensor;
  
  - replay: per tutte le sessioni di registrazione ricevute precedentemente il server dovrà consentire
  di rimandare la stessa in riproduzione come se fosse in tempo reale.
  
  Il pannello di controllo di una sessione di monitoraggio dovrà rappresentare i dati vitali recuperati dal
  PulseSensor avvalendosi anche di animazioni, grafici dinamici per rendere più immediato l’accesso
  all’informazione. Si presti attenzione al trattamento del dato analogico in uscita dal sensore. Segue un
  esempio di interfaccia grafica ottenuta dall’integrazione tra Pulse Sensor e Arduino. 

  Il flusso di comunicazione tra la componente client e la componente server dovrà essere protetto in modo
  opportuno al fine di garantire le seguenti proprietà:
  
  - confidenzialità
  - integrità
  - non ripudio delle informazioni trasferite.
  
  La componente client dovrà essere certa di consegnare le informazioni ad una componente server
  autorizzata e allo stesso tempo la componente server dovrà essere in grado di identificare la 
  componente client che ha inviato le informazioni. Per raggiungere questo obiettivo il canale di 
  comunicazione dovrà essere mutualmente autenticato. E’ possibile prevedere che in una fase iniziale
  di configurazione alcune risorse (es. certificato digitale della componente server) possa essere 
  caricato all’interno della componente client prima di eseguire una sessione di monitoraggio. Si valuti
  l’opportunità si identificare una soluzione crittografica ad un livello più basso di quello applicativo per
  garantire un ridotto overhead in termini computazionali e di memoria. Per questo motivo la componente 
  server potrebbe non essere necessariamente un web server (e.g. apache) ma un server che opera ad un 
  livello differente (e.g. network server)
