import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static int[] data;
    public static boolean testRun = false;

    /**
     * Die Methode public static void main(String[] args) enthält wie immer das ausführbare Programm Ihrer Implementierung.
     * Sie sollen hier die Zahlen aus der Eingabe lesen, mehrere Instanzen der Klasse anlegen und die Liste mittels LSD- und MSD-Radixsort sortieren.
     * Messen Sie die Laufzeit der zwei Varianten von Radixsort auf zufälligen Sequenzen.
     * Welcher Algorithmus ist schneller? Vergrößern Sie oder verändern Sie die Struktur der Eingabe, bis Sie einen klaren Unterschied feststellen können.
     * Achten Sie bei der Generierung der Eingabe darauf, welche Bytes wirklich von den Eingabewerten benutzt werden.
     * Die sortierten Listen sollen dann entsprechend der Beispiele ausgegeben werden.
     * Dazu dürfen Sie Arrays.toString() aus der Bibliothek java.util.Arrays verwenden.
     * Sie dürfen davon ausgehen, dass nur positive Ganzzahlen eingegeben werden. Sie sollten
     * jedoch wie immer bei ungültigen Eingaben passende Fehlermeldungen ausgeben. Überprüfen Sie die Korrektheit des Ergebnisses mit geeigneten Assertions.
     * @param args
     */
    public static void main(String[] args) {
        //TODO: main
        int[] array_input;
        if(testRun) {
            try {
                array_input = readInput();
            } catch (NumberFormatException e) {
                System.out.println("Error: Encountered problem parsing argument, required: number");
                return;
            }
        }else{
            array_input = generateInput(256);
        }
        AufgabeB6A1 task = new AufgabeB6A1(array_input.clone());
        System.out.println("Before sorting: " + Arrays.toString(task.data));
        Instant start = Instant.now();
        task.lsdRadix();
        Instant stop = Instant.now();
        System.out.println("Sort after LSD: \t" + Arrays.toString(task.data));
        long time = Duration.between(start, stop).toMillis();
        System.out.println("LSD took: " + time);

        task = new AufgabeB6A1(array_input.clone());
        start = Instant.now();
        task.msdRadix(0, data.length - 1, 3);
        stop = Instant.now();
        System.out.println("Sort after MSD: \t" + Arrays.toString(task.data));
        if(!testRun) System.out.println("Is Sorted (Main): \t" + task.isSorted(0,task.data.length - 1));

        time = Duration.between(start, stop).toMillis();
        System.out.println("MSD took: " + time);

    }

    private static int[] generateInput(int size){
        int[] out = new int[size];
        for(int i = 0; i < out.length; i++){
            double f = Math.random()/Math.nextDown(1.0);
            out[i] = (int)(0 * (1.0 - f) + Integer.MAX_VALUE * f);
        }
        return out;
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
        //TODO: readInput
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
        this.data = data;
    }

    /**
     * Die Methode public void sortByByte(int l, int r, int b) soll das Arrayintervall
     * this.data[l, r] absteigend und stabil nach dem b-niederwertigsten Byte sortieren (für b = 0 nach dem niederwertigsten Byte, für b = 3 nach dem höchstwertigsten).
     * Dabei sollen Sie den Mechanismus von Counting-Sort verwenden (siehe Blatt 5), und ein Frequenzarray mit 256 Einträgen benutzen.
     * Sie dürfen ein Hilfsarray der Länge r − l + 1 verwenden, und davon ausgehen, dass nur positive Ganzzahlen eingegeben werden.
     * Tipp: Das b-niederwertigste Byte eines Integers a ist der Wert der b-niederwertigsten Stelle in Basis 256. Mathematisch entspricht dies (a/256b) mod 256.
     * Praktisch berechnen Sie die Division durch einen Bitshift, und den Modulo durch die Verundung mit einer Maske: (a >> (8 * b)) & 0xFF.
     * Tipp: Das Array C lässt sich im Aufruf von sortByByte leicht aus dem Frequenzarray berechnen.
     * Sie können sortByByte modifizieren, sodass C zurückgegeben wird.
     * Würden Sie statt Bytes die Dezimalstellen der Zahlen verwenden, so sähe ein Sortierschritt etwa so aus:
     * [123, 134, 234, 345, 222, 225]-> [{123, 134} rekursiv sortieren, {234, 222, 225} rekursiv sortieren, {345} rekursiv sortieren]
     * @param l
     * @param r
     * @param b
     */
    public int[] sortByByte(int l, int r, int b) {
        //TODO: SortByByte
        //Fehlerbehandlung
        if(l < 0 || r < l || data.length <= r){
            //throw new IllegalArgumentException("Bounds outside of array: " + l + ", " + r + " outside " + data.length);
            return null;
        }
        if(b < 0 || b > 3){
            //throw new IllegalArgumentException("Byte accessed was outside range: b = " + b);
            return null;
        }

        int[] counted = new int[256];
        for(int i = l; i <= r; i++){
            int key = (data[i] >> (8 * b)) & 0xFF; //key = value of b-lowest Byte; Range: [0,255]
            counted[counted.length - 1 - key]++;
        }
        int[] out = Arrays.copyOf(counted, counted.length);
        for(int i = 1; i < counted.length; i++){
            counted[i] += counted[i - 1];
        }
        int[] sorted = new int [r - l + 1];
        for(int i = r; i >= l; i--){
            int key = (data[i] >> (8 * b)) & 0xFF;
            int index = counted[counted.length - 1 - key] - 1; //Java-Arrays start @0
            counted[counted.length - 1 - key]--;
            sorted[index] = data[i];
        }
        for(int i = 0; i < sorted.length; i++){
            data[i + l] = sorted[i];
        }
        return out;
    }

    /**
     * Die Methode public void lsdRadix(), welche die Ganzzahlen in this.data absteigend sortiert.
     * Diese Methode sortiert die Zahlen zuerst nach dem niederwertigsten Byte mittels sortByByte.
     * Dann sortiert der Prozess die Zahlen nach dem zweit-niederwertisten Byte, dem dritt-niederwertigsten Byte, und schließlich nach dem höchstwertigsten (viertniederwertigsten) Byte.
     */
    public void lsdRadix() {
        //TODO: lsdRadix
        for(int i = 0; i < Integer.BYTES; i++){
            sortByByte(0, data.length - 1, i);
        }
    }

    /**
     * Die Methode public void msdRadix(int l, int r, int b) soll MSD-Radixsort umsetzen. Genauer soll die Methode wie folgt funktionieren:
     * – Wenn die Methode aufgerufen wird, dann ist this.data[l, r] bereits nach den höchstwertigsten (4 − (b + 1)) Bytes sortiert.
     * Der initiale Aufruf erfolgt mit l = 0, r = data.length − 1 und b = 3.
     * – Wenn das Intervall bereits sehr kurz ist, genauer gesagt r − l + 1 ≤ 32, dann sortieren Sie das Intervall this.data[l, r] absteigend mit dem naiven Algorithmus Insertion-Sort1.
     * – Ansonsten sortieren Sie this.data[l, r] absteigend nach dem b-niederwertigsten Byte (b = 0 entspricht dem niederwertigsten Byte). Dazu können Sie die Methode sortByByte verwenden.
     * Damit Sie rekursiv fortfahren können, müssen Sie die Grenzen der Subintervalle mit gleichem b-niederwertigsten Byte bestimmen.
     * Dazu können Sie ein Array C der Länge 257 berechnen, sodass this.data in [C[i + 1] + 1, C[i]] genau die Werte enthält,
     * deren b-tes Byte den Wert i hat (insbesondere also C[0] = r und C[256] + 1 = l).
     * Nun sortieren Sie jedes Subintervall data[C[i + 1] + 1, C[i]] rekursiv weiter.
     * @param l
     * @param r
     * @param b
     */
    public void msdRadix(int l, int r, int b) {
        //TODO: msdRadix --Part of Array is not sorted after (Last Section)--
        //Fehlerbehandlung
        if(l < 0 || r < l || data.length <= r){
            //throw new IllegalArgumentException("Bounds outside of array: " + l + ", " + r + " outside " + data.length);
            return;
        }
        if(b < 0 || b >= Integer.BYTES){
            //throw new IllegalArgumentException("Byte accessed was outside range: b = " + b);
            return;
        }
        //Rekursionsanker
        if(r - l + 1 <= 32){
            insertionSort(l, r);
            return;
        }
        int[] intervals = sortByByte(l, r, b);
        for(int i = 1; i < intervals.length; i++){
            intervals[i] += intervals[i - 1];
        }
        msdRadix(l, intervals[1], b - 1);
        for(int i = 1; i < intervals.length - 1; i++){
            msdRadix(intervals[i], intervals[i + 1], b - 1);
        }
        msdRadix(intervals[intervals.length - 1], r, b - 1);
    }

    private void insertionSort(int l, int r){
        if(r <= l || r >= data.length || l < 0){
            return;
        }
        for (int i = l + 1; i <= r; i++) {
            int insert = data[i];
            int j;
            for (j = i; j > 0 && data[j - 1] <= insert; j--) {
                data[j] = data[j - 1];
            }
            data[j] = insert;
        }
        //System.out.println(isSorted(l, r));
    }
    public boolean isSorted(int l, int r){
        int prev = data[l];
        for(int i = l + 1; i <= r; i++){
            if(prev < data[i]){
                return false;
            }
            prev = data[i];
        }
        return true;
    }
}
