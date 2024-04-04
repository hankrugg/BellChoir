import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a Tone with associated notes and members for playing music.
 */
public class Tone {
    /** The AudioFormat for the tone. */
    public final AudioFormat af;
    /** A map to store note lengths. */
    private Map<String, NoteLength> noteLengthMap;
    /** A map to store notes. */
    private Map<String, Note> noteMap;
    /** A map to store members (players). */
    private volatile ConcurrentHashMap<String, Player> members;

    /**
     * Constructs a Tone with the given AudioFormat.
     * @param af The AudioFormat for the tone.
     */
    public Tone(AudioFormat af) {
        this.af = af;
    }

    /**
     * Loads the note length map with predefined values.
     */
    public synchronized void loadNoteLengthMap() {
        noteLengthMap = new HashMap<>();
        noteLengthMap.put("1", NoteLength.WHOLE);
        noteLengthMap.put("2", NoteLength.HALF);
        noteLengthMap.put("4", NoteLength.QUARTER);
        noteLengthMap.put("8", NoteLength.EIGTH);
    }

    /**
     * Loads the note map with predefined note values.
     */
    public synchronized void loadNoteMap() {
        noteMap = new HashMap<>();
        noteMap.put("REST", Note.REST);
        noteMap.put("A4", Note.A4);
        noteMap.put("A4S", Note.A4S);
        noteMap.put("B4", Note.B4);
        noteMap.put("C4", Note.C4);
        noteMap.put("C4S", Note.C4S);
        noteMap.put("D4", Note.D4);
        noteMap.put("D4S", Note.D4S);
        noteMap.put("E4", Note.E4);
        noteMap.put("F4", Note.F4);
        noteMap.put("F4S", Note.F4S);
        noteMap.put("G4", Note.G4);
        noteMap.put("G4S", Note.G4S);
        noteMap.put("A5", Note.A5);
    }

    /**
     * Loads the members (players) for each note.
     */
    public synchronized void loadMembers(){
        members = new ConcurrentHashMap<>();

        Player restPlayer = new Player(Note.REST, af); // Create a player for each note
        members.put("REST", restPlayer );
        Player A4Player = new Player( Note.A4, af); // Create a player for each note
        members.put("A4", A4Player);
        Player  A4SPlayer = new Player(Note.A4S , af); // Create a player for each note
        members.put("A4S",A4SPlayer);
        Player B4Player = new Player( Note.B4, af); // Create a player for each note
        members.put("B4", B4Player);
        Player C4Player = new Player( Note.C4, af); // Create a player for each note
        members.put("C4", C4Player);
        Player C4SPlayer = new Player(Note.C4S , af); // Create a player for each note
        members.put("C4S", C4SPlayer);
        Player D4Player = new Player(Note.D4 , af); // Create a player for each note
        members.put("D4", D4Player);
        Player D4SPlayer = new Player( Note.D4S, af); // Create a player for each note
        members.put("D4S", D4SPlayer);
        Player E4Player = new Player( Note.E4, af); // Create a player for each note
        members.put("E4", E4Player);
        Player F4Player  = new Player( Note.F4, af); // Create a player for each note
        members.put("F4", F4Player);
        Player F4SPlayer = new Player( Note.F4S, af); // Create a player for each note
        members.put("F4S", F4SPlayer);
        Player G4Player = new Player( Note.G4, af); // Create a player for each note
        members.put("G4", G4Player);
        Player G4SPlayer = new Player( Note.G4S, af); // Create a player for each note
        members.put("G4S", G4SPlayer);
        Player A5Player = new Player( Note.A5, af); // Create a player for each note
        members.put("A5", A5Player);

    }

    /**
     * Filters out invalid notes from the given list of BellNotes.
     * If a BellNote has a null note value, it replaces it with a rest note.
     *
     * @param song The list of BellNotes to be filtered.
     * @return A new list of BellNotes with valid notes.
     */
    private List<BellNote> validNotes(List<BellNote> song) {
        List<BellNote> newSong = new ArrayList<>();
        for (BellNote bn : song) {
            if (bn.note == null) {
                // Replace null note with a rest note
                newSong.add(new BellNote(noteMap.get("REST"), noteLengthMap.get("1")));
            } else {
                newSong.add(bn);
            }
        }
        return newSong;
    }

    /**
     * Filters out invalid note lengths from the given list of BellNotes.
     * If a BellNote has a null length value, it replaces it with a whole note length.
     *
     * @param song The list of BellNotes to be filtered.
     * @return A new list of BellNotes with valid note lengths.
     */
    private List<BellNote> validLengths(List<BellNote> song) {
        List<BellNote> newSong = new ArrayList<>();
        for (BellNote bn : song) {
            if (bn.length == null) {
                // Replace null length with a whole note length
                newSong.add(new BellNote(noteMap.get("REST"), noteLengthMap.get("1")));
            } else {
                newSong.add(bn);
            }
        }
        return newSong;
    }


    /**
     * Loads a song from a file.
     *
     * @param fileName The name of the file containing the song.
     * @return A list of BellNotes representing the song.
     */
    private synchronized List<BellNote> loadSong(String fileName) {
        // Create a list to store the loaded song
        List<BellNote> song = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            // Read each line of the file until the end
            while (scanner.hasNext()) {
                // Split each line into note and note length
                String[] values = scanner.nextLine().split(" ");

                if (values.length != 2){
                    System.err.println("The file is not in the correct format! It should be a note followed by note length!");
                    System.err.println("Example:");
                    System.err.println("A5 4");
                    System.err.println("The poorly formatted lines will try to be deciphered.");
                }

                // Retrieve the note and note length from the predefined maps
                Note note = noteMap.get(values[0]);
                NoteLength noteLength = noteLengthMap.get(values[1]);

                if (note == null){
                    System.err.println("This program does not support the note: " + values[0] + " and will be replaced with a whole note rest. ");
                }

                if (noteLength == null){
                    System.err.println("This program does not support the note length: " + values[1] + " and will be replaced with a whole note rest. ");
                }

                // Create a BellNote object and add it to the song list
                song.add(new BellNote(note, noteLength));
            }
        } catch (FileNotFoundException e) {
            // Print an error message if the file is not found
            System.err.println(fileName + " was not found");
        }
        // Return the loaded song
        return song;
    }


    /**
     * Plays a given song.
     * @param song The song to be played.
     * @throws LineUnavailableException If a line cannot be opened because it is unavailable.
     * @throws InterruptedException If the current thread is interrupted while sleeping.
     */
    private void playSong(List<BellNote> song) throws LineUnavailableException, InterruptedException {
        for (BellNote bn : song) {
            Player player = members.get(bn.note.toString());
            System.out.println(player + " is playing now!");
            player.startPlaying(bn.length);
        }
    }

    /**
     * Fires (retires) all members (players) associated with this tone.
     * This method stops all players from playing and joins their respective threads.
     */
    private synchronized void fireAllMembers(){
        for (Player player : members.values()) {
            player.retire();
        }
    }

    /**
     * Main method to load and play a song.
     *
     * @param args Command-line arguments (not used).
     * @throws Exception If an exception occurs during execution.
     */
    public static void main(String[] args) throws Exception {
        String songFileName = null;
        if (args.length > 0) {
            songFileName = args[0];
            System.err.println("This program uses arguments to run. Compile the code in the command line using 'javac Tone.java' then run java Tone <filepath-to-songfile>");
        } else {
            songFileName = "MaryLamb.txt";
        }

        // Define the audio format for the tone
        final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
        Tone t = new Tone(af);

        // Load the predefined maps
        t.loadNoteMap();
        t.loadNoteLengthMap();
        t.loadMembers();

        // Load the song from the specified file
        List<BellNote> song = t.loadSong(songFileName);

        List<BellNote> validLengthSong = t.validLengths(song);

        List<BellNote> validNoteSong = t.validNotes(validLengthSong);

        t.playSong(validNoteSong);


        // Stop all players from playing and join their respective threads to ensure they have completed their jobs
        t.fireAllMembers();
    }

}
