import java.util.ArrayList;
import java.util.Scanner;

/**
 * In der Vorlesung haben Sie den Algorithmus Radixsort kennengelernt, der oft damit motiviert wird,
 * dass eine Liste von mehrdimensionalen Schlüsseln sortiert werden soll.
 * Tatsächlich ist Radixsort aber auch gut dazu geeignet, eine Liste von Ganzzahlen zu sortieren.
 * Dabei wird jeder int-Wert als ein Schlüssel mit vier Komponenten interpretiert, wobei jede Komponente eines
 * der vier Bytes ist, welche den int-Wert bilden. Das höchstwertigste Byte (Most-SignificantDigit, MSD) ist die erste Komponente, und das niederwertigste Byte (Least-Significant-Digit,
 * LSD) ist die letzte Komponente. Beispiel:
 * Wert in Basis-10-Darstellung: 1661914940
 * Wert in 32-Bit-Binärdarstellung: 01100011 00001110 11001111 00111100
 * Wert in Byte-Komponenten: 99
 * Komp. 1 14 > Komp. 2 207 > Komp. 3 >60 > Komp. 4
 * Jedes Byte entspricht dabei einer Stelle in der Basis-256-Darstellung des Wertes:
 * 1661914940 = 99 · 256^3 + 14 · 256^2 + 207 · 256^1 + 60 · 256^0.
 * Die Kernaufgabe diese Blattes ist es, zwei Varianten von Radixsort für Ganzzahlen implementieren: Least-Significant-Digit-First (LSD) und Most-Significant-Digit-First (MSD). Für beide
 * Varianten ist es wichtig, dass Sie ein Array nach dem Wert eines bestimmten Bytes sortieren können.
 */
public class AufgabeB6A1 {
    public int[] data;

    /**
     * Die Methode public static void main(String[] args) enthält wie immer das ausführbare Programm Ihrer Implementierung. Sie sollen hier die Zahlen aus der Eingabe lesen, mehrere Instanzen der Klasse anlegen und die Liste mittels LSD- und MSD-Radixsort
     * sortieren. Messen Sie die Laufzeit der zwei Varianten von Radixsort auf zufälligen Sequenzen. Welcher Algorithmus ist schneller? Vergrößern Sie oder verändern Sie die Struktur
     * der Eingabe, bis Sie einen klaren Unterschied feststellen können. Achten Sie bei der Generierung der Eingabe darauf, welche Bytes wirklich von den Eingabewerten benutzt
     * werden. Die sortierten Listen sollen dann entsprechend der Beispiele ausgegeben werden.
     * Dazu dürfen Sie Arrays.toString() aus der Bibliothek java.util.Arrays verwenden.
     * Sie dürfen davon ausgehen, dass nur positive Ganzzahlen eingegeben werden. Sie sollten
     * jedoch wie immer bei ungültigen Eingaben passende Fehlermeldungen ausgeben. Überprüfen Sie die Korrektheit des Ergebnisses mit geeigneten Assertions.
     * @param args
     */
    public static void main(String[] args) {
        //TODO: main
    }

    /**
     * Die Methode public static int[] readInput() soll alle Ganzzahlen aus Standard-In
     * einlesen. Wie in den vorigen Wochen sollen nur Eingaben akzeptiert werden, die vollständig aus Ganzzahlen bestehen. Die eingelesenen Ganzzahlen sollen als int[] zurückgegeben
     * werden. Entstehende NumberFormatException sollen von dieser Methode weitergeleitet
     * werden.
     * @return
     * @throws NumberFormatException
     */
    public static int[] readInput() throws NumberFormatException {
        //TODO: readInput --Drafted--
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> list = new ArrayList<>();
        try {
            while (in.hasNextLine()) {
                int current = Integer.parseInt(in.nextLine());
                list.add(current);
            }
        }catch(NumberFormatException e){
            System.err.println("Error: Encountered problem parsing the input.");
            throw e;
        }
        int[] arr = new int[list.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * Der Konstruktor public AufgabeB6A1(int[] data) soll eine neue Instanz dieser Klasse
     * erstellen und sich das übergebene data in einem Attribut mit dem selben Namen speichern
     * @param data
     */
    public AufgabeB6A1(int[] data) {
        //TODO: Konstruktor
    }

    /**
     * Die Methode public void sortByByte(int l, int r, int b) soll das Arrayintervall
     * this.data[l, r] absteigend und stabil nach dem b-niederwertigsten Byte sortieren (für
     * b = 0 nach dem niederwertigsten Byte, für b = 3 nach dem höchstwertigsten). Dabei sollen
     * Sie den Mechanismus von Counting-Sort verwenden (siehe Blatt 5), und ein Frequenzarray
     * mit 256 Einträgen benutzen. Sie dürfen ein Hilfsarray der Länge r − l + 1 verwenden,
     * und davon ausgehen, dass nur positive Ganzzahlen eingegeben werden.
     * Tipp: Das b-niederwertigste Byte eines Integers a ist der Wert der b-niederwertigsten
     * Stelle in Basis 256. Mathematisch entspricht dies (a/256b
     * ) mod 256. Praktisch berechnen
     * Sie die Division durch einen Bitshift, und den Modulo durch die Verundung mit einer
     * Maske: (a >> (8 * b)) & 0xFF.
     * Tipp: Das Array C lässt sich im Aufruf von sortByByte leicht aus dem Frequenzarray berechnen.
     * Sie können sortByByte modifizieren, sodass C zurückgegeben wird.
     * Würden Sie statt Bytes die Dezimalstellen der Zahlen verwenden, so sähe ein Sortierschritt etwa so aus:
     * [123, 134, 234, 345, 222, 225]-> [{123, 134} rekursiv sortieren, {234, 222, 225} rekursiv sortieren, {345} rekursiv sortieren]
     * @param l
     * @param r
     * @param b
     */
    public void sortByByte(int l, int r, int b) {
        //TODO: SortByByte --Drafted--
        int[] counted = new int[256];
        for(int i = 0; i < data.length; i++){
            int key = (data[i] >> (8 * b)) & 0xFF;
            counted[key]++;
        }
        //Keys counted, add up
        for(int i = 1; i < counted.length; i++){
            counted[i] += counted[i - 1];
        }
        //Indices calculated, next up: Sort
        int[] out = new int [data.length];
        for(int i = data.length - 1; i >= 0; i--){
            int key = (data[i] >> (8 * b)) & 0xFF;
            int index = counted[key];
            out[index] = data[i];
        }
        for(int i = 0; i < data.length; i++){
            data[i] = out[i];
        }
    }

    /**
     * Die Methode public void lsdRadix(), welche die Ganzzahlen in this.data absteigend
     * sortiert. Diese Methode sortiert die Zahlen zuerst nach dem niederwertigsten Byte mittels sortByByte. Dann sortiert der Prozess die Zahlen nach dem zweit-niederwertisten
     * Byte, dem dritt-niederwertigsten Byte, und schließlich nach dem höchstwertigsten (viertniederwertigsten) Byte.
     */
    public void lsdRadix() {
        //TODO: lsdRadix
    }

    /**
     * Die Methode public void msdRadix(int l, int r, int b) soll MSD-Radixsort umsetzen. Der Algorithmus sortiert die Zahlen zunächst nach dem höchstwertigsten Byte.
     * Dadurch wird das Array in Intervalle partitioniert, von denen jedes Intervall genau die
     * Werte enthält, die das gleiche höchstwertigste Byte haben. Nun wird jedes Intervall rekursiv nach dem nächsten Byte sortiert, und rekursiv fortgefahren, bis entweder nach allen
     * vier Bytes sortiert wurde, oder das betrachtete Intervall klein genug ist, um es mit einem
     * naiven Algorithmus zu sortieren. Genauer soll die Methode wie folgt funktionieren:
     * – Wenn die Methode aufgerufen wird, dann ist this.data[l, r] bereits nach den
     * höchstwertigsten (4 − (b + 1)) Bytes sortiert. Der initiale Aufruf erfolgt mit l = 0,
     * r = data.length − 1 und b = 3.
     * – Wenn das Intervall bereits sehr kurz ist, genauer gesagt r − l + 1 ≤ 32, dann
     * sortieren Sie das Intervall this.data[l, r] absteigend mit dem naiven Algorithmus Insertion-Sort1.
     * – Ansonsten sortieren Sie this.data[l, r] absteigend nach dem b-niederwertigsten
     * Byte (b = 0 entspricht dem niederwertigsten Byte). Dazu können Sie die Methode
     * sortByByte verwenden. Damit Sie rekursiv fortfahren können, müssen Sie die Grenzen der Subintervalle mit gleichem b-niederwertigsten Byte bestimmen.
     * Dazu können Sie ein Array C der Länge 257 berechnen, sodass self.data[C[i+ 1] + 1, C[i]] genau
     * die Werte enthält, deren b-tes Byte den Wert i hat (insbesondere also C[0] = r und C[256] + 1 = l).
     * Nun sortieren Sie jedes Subintervall data[C[i + 1] + 1, C[i]] rekursiv
     * weiter.
     * @param l
     * @param r
     * @param b
     */
    public void msdRadix(int l, int r, int b) {
        //TODO: msdRadix
    }
}
