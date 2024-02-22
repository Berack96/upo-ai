# UPO AI
Il progetto è stato caricato su GitHub al seguente [link](https://github.com/Berack96/upo-ai). \
In esso sono contenuti 3 esercizi risolti con relativi tests e 2 esercizi di Genie aventi interfaccia.\
Ogni lavoro svolto ha una propria interfaccia grafica usata per interagire con il problema e mostrare la soluzione di esso.\
Di seguito una breve descrizione di ogni lavoro svolto con le classi e i metodi principali usati.

## 8-Puzzle
package: [net.berack.upo.ai.problem1](https://github.com/Berack96/upo-ai/tree/main/src/main/java/net/berack/upo/ai/problem1)

#### AStar
La visita A* è implementata in una classe generica con due parametri (Stato e Azione) in modo che si possa utilizzare per qualunque oggetto che abbia degli stati e che per muoversi da uno stato all'altro serva compiere una azione.

Quando viene istanziata la classe A* vengono richieste varie funzioni:
- Transizione: funzione di transizione da uno stato ad un altro usando una azione
- Azioni: dato uno stato deve restituire tutte le azioni disponibili
- Costo: che indica quanto "spendo" per passare da uno stato ad un altro (opzionale dato che di default è impostata alla funzione costante ad 1)
- Euristica: stima per difetto quanto costa passare da uno stato a quello goal (opzionale dato che di default è impostata alla funzione costante a 0).

#### Puzzle8
Per la risoluzione del gioco è stata prima implementata la classe Puzzle8 che contiene i valori e le regole di esso.\
Tra i metodi della classe ci sono:\
*shuffle* che serve a generare un nuovo puzzle a caso ma risolvibile.\
*move* che serve per muovere lo spazio vuoto in una delle direzioni disponibili.\
*availableMoves* che restituisce le azioni disponibili.\
*solve* che restituisce la sequenza di azioni da fare per risolvere il gioco.

Quest'ultima utilizza la classe A* per avere la lista di azioni.
Nel caso del Puzzle non viene impostata la funzione di costo dato che il costo, per passare da uno stato ad un altro eseguendo un'azione, è sempre di 1.
Mentre per la funzione euristica è stata utilizzata la distanza di Manhattan per stimare il costo di uno stato rispetto allo stato goal.

## Tris
package: [net.berack.upo.ai.problem2](https://github.com/Berack96/upo-ai/tree/main/src/main/java/net/berack/upo/ai/problem2)

#### MiniMax
Questa classe implementa l'algoritmo MiniMax per la soluzione di problemi a due giocatori.
La classe è stata resa generica e richiede tre parametri (Stato, Azione, Giocatore) in questo modo si può utilizzare qualunque oggetto che abbia stati, azioni per muoversi tra stati e dei giocatori.\
In essa è stata introdotta una piccola modifica per rendere la scelta della mossa migliore casuale nel caso ci siano più azioni migliori.

Questa classe richiede tre funzioni quando viene istanziata:
- Transizione: funzione di transizione da uno stato ad un altro usando una azione
- Azioni: dato uno stato deve restituire tutte le azioni disponibili
- Guadagno: funzione che deve restituire il guadagno del giocatore nel dato stato

#### Tris
Per il gioco del tris è stata introdotta la classe tris che contiene i valori e le regole del gioco. In essa si possono notare alcuni metodi utili per il gioco:\
*play* che data la coordinata fa giocare il prossimo giocatore.\
*availablePlays* che restituisce tutte le azioni disponibili.\
*isFinished* che indica se il gioco è finito (ha un vincitore o non si possono fare più mosse)

#### TrisAI
Questa classe usa MiniMax per la generazione della mossa migliore per Tris.\
In essa è implementata la funzione di GAIN (Guadagno) in modo da restituire correttamente i valori in base allo stato del gioco e al giocatore passato in input.\
Oltre a questo ha due metodi *playNext* che, dato un lookahead, permettono di fare la mossa migliore.

## LikelihoodWeighting
package: [net.berack.upo.ai.problem3](https://github.com/Berack96/upo-ai/tree/main/src/main/java/net/berack/upo/ai/problem3)

#### LikelihoodWeighting
Questa classe risolve il problema del calcolo delle probabilità di una rete bayesiana.\
Per fare ciò si utilizzano le reti create tramite la libreria jSmile. Una volta caricata la rete l'algoritmo sceglie un valore per ogni nodo in base alla probabilità che esso possa verificarsi; nel caso dei nodi con evidenza, non viene fatto un *sample* ma viene preso il valore della probabilità che possa verificarsi quella evidenza e viene moltiplicata con gli altri valori estratti dei nodi evidenza.\
Questo procedimento viene fatto molteplici volte (indicate da una variabile apposta *totalRuns*) contando quante volte un determinato stato viene "estratto" per ogni nodo o sommando le probabilità totali dei nodi evidenza.\
Infine viene calcolata la probabilità di ogni stato di ogni nodo di verificarsi prendendo la conta dello stato e dividendola per la somma delle probabilità dei nodi evidenza.

#### SmileLib
Classe statica che serve per il caricamento della libreria jSmile.
In questo file viene creata una variabile di ambiente per Java in cui viene indicato dove la JVM può trovare il file binario della libreria jSmile.\
Oltre a questo ha dei metodi utili per la lettura di un Network sia come risorsa che come file.

#### NetworkNode
Classe di appoggio usata per il calcolo delle probabilità dell'algoritmo LW. Usata principalmente per evitare di ricreare i dati dei nodi ogni volta che viene richiamata la libreria e per avere dei metodi per la ricerca dei valori corretti da prendere nella CPT in base alle evidenze o sample impostati nei nodi padre.

## Prototipo
file: [src/main/resources/Prototipo.xdsl](https://github.com/Berack96/upo-ai/blob/main/src/main/resources/Prototipo.xdsl)

Esercizio in cui bisogna creare una rete bayesiana per il supporto alla decisione di una azienda per la produzione di un prodotto.

In essa vengono richieste tre decisioni si/no: Effettuare una ricerca di mercato, Migliorare la qualità del prototipo, Mandare in produzione il prodotto; ognuna di esse ha collegato un valore di costo (e quindi con valore negativo) che viene preso in considerazione solamente in caso di decisione positiva.\
L'unico nodo che può far aumentare l'utilità è "Valore ricavo" che viene assegnato in base alla probabilità di profitto e il fatto che si abbia fatto partire la produzione.\
È stato creato un altro nodo utilità denominato "Valore profitto" che prende in input tutti i nodi utilità per farne una somma; esso viene usato solamente nella rappresentazione della rete per semplicità nel vedere il risultato.\
La rete ha un nodo di cui si può inserire il valore che è "Ricerca di mercato" ed esso viene contato nel calcolo solamente se si è scelto di effettuare la ricerca.\
Il nodo "Profitto" è stato dichiarato NoisyMax dato che in questo modo risulta più semplice inserire i valori, dando più profitto nel caso in cui la domanda sia alta o in caso in cui la qualità sia ottima.

La rete ha una sua rappresentazione grafica nel progetto, nella quale è possibile interagire inserendo le decisioni (quella migliore per la rete viene indicata con il font in grassetto) per poi avere il valore dell'utilità finale indicato in rosso.

## Veicolo
file: [src/main/resources/Veicolo.xdsl](https://github.com/Berack96/upo-ai/blob/main/src/main/resources/Veicolo.xdsl) \
file: [src/main/resources/Veicolo_unrolled.xdsl](https://github.com/Berack96/upo-ai/blob/main/src/main/resources/Veicolo%20unrolled.xdsl)

Esercizio in cui creare una rete bayesiana per il supporto alla decisione di un veicolo autonomo in modo che rimanga al centro della corsia anche quando il sensore è guasto o restituisce valori scorretti.

La prima cosa che si nota della rete è che si trova su un Temporal Plate di 5 time steps. In essa si possono notare due gruppi di nodi: uno riguardante l'accuratezza del sensore e uno riguardante la posizione del veicolo. \
Nel gruppo dell'accuratezza ci sono dei nodi per il controllo dell'ambiente (stato del terreno e meteo) che influenzano l'accuratezza e il fatto che il sensore si possa guastare. I nodi Guasto e Accuratezza sono stati dichiarati come NoisyMax sempre per il fatto che è più semplice l'inserimento dei valori di probabilità. \
La posizione del Veicolo non è conosciuta e per questo viene rappresentata come un nodo di probabilità; essa viene legata ad un nodo di utilità dato che il nostro obiettivo è di rimanere al centro. \
Le decisioni che devono essere prese sono rappresentate dal nodo Comando, che va ad influenzare la posizione del veicolo. Essendo il nodo su un piano temporale di 5 steps, verranno richieste 5 decisioni. \
Oltre ai nodi del piano temporale sono stati introdotti dei nodi come Term Conditions; questo perché altrimenti l'ultima decisione non avrebbe avuto conseguenze per il nostro modello.\
Tutti gli altri nodi inseriti come utilità vengono usati solamente per il calcolo totale dell'utilità e nella rappresentazione della rete per vedere il risultato.
