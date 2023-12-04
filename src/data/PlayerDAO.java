package data;

import entities.*;
import view.Observer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerDAO {
    private static final String CSV_FILE_PATH = "./player_data.csv";

    public static void update(Player player) {
        savePlayer(player);
    }

    public static boolean playerDataExists(){
        File file = new File(CSV_FILE_PATH);
        return file.exists() && file.length() > 0;
    }

    public static void savePlayer(Player player) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write the player data to the file
            writer.println(player.getName());
            writer.println(player.getCurrentDeck().getDeckName());

            // Save the player's collection
            writer.println("COLLECTION_START");
            for (Card card : player.getCollection().getCards()) {
                writer.println(card.getId());
                writer.println(card.getName());
            }
            writer.println("COLLECTION_END");

            // Save the player's current deck
            writer.println("DECK_START");
            Deck currentDeck = player.getCurrentDeck();
                // Write other deck attributes...
            for (Card card : currentDeck.getCards()) {
                writer.println(card.getId());
            }
            writer.println("DECK_END");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Player loadPlayer() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            // Read the player data from the file
            String playerName = reader.readLine();
            String currentDeckName = reader.readLine();

            // Load and build the player's collection
            ArrayList<Card> cards = new ArrayList<Card>();
            CommonCardFactory factory = new CommonCardFactory();
            String cardId;
            String cardName;

            // Skip to the start of the collection
            String line;
            while ((line = reader.readLine()) != null && !line.equals("COLLECTION_START")) {
            }

            while ((cardId = reader.readLine()) != null && !cardId.equals("COLLECTION_END") && (cardName = reader.readLine()) != null) {
                Card card = factory.create(cardId, cardName);
                cards.add(card);
            }
            Collection collection = new CommonCollection(cards);

            // Skip to the start of the decks
            while ((line = reader.readLine()) != null && !line.equals("DECK_START")) {
            }

            // prepare to get currentDeck
            ArrayList<Card> deckCards = new ArrayList<Card>();
            String deckCardID;
            Deck current_deck = null;

            while ((line = reader.readLine()) != null && !line.equals("DECK_END")) {
                deckCardID = line;
                while (deckCardID != null && !deckCardID.equals("DECK_END")) {
                    Card card = collection.getCard(deckCardID);
                    deckCards.add(card);
                    deckCardID = reader.readLine();
                }
            }

            current_deck = new CommonDeck(currentDeckName, deckCards.get(0), deckCards.get(1), deckCards.get(2),
                    deckCards.get(3), deckCards.get(4), deckCards.get(5));

            HashMap<String, Deck> decks = new HashMap<String, Deck>();

            // Create and return the player
            Player player = new CommonPlayer(playerName, current_deck, collection, decks);
            player.addDeck(currentDeckName, current_deck);
            return player;

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }



}