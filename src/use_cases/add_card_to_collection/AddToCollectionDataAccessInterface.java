package use_cases.add_card_to_collection;

import entities.Card;

public interface AddToCollectionDataAccessInterface {

    boolean cardInCollection(Card card);

    void addCard(Card card);

    void save();

}
