import java.util.*;

public class Players {
    private final String _nick;
    private final TreeSet<Cards> _hand = new TreeSet<>(Cards::compare);
    private final ArrayList<Integer> _comboIndexList = new ArrayList<>();
    public String getNick(){ return _nick; }
    public TreeSet<Cards> gethand(){ return _hand; }
    public ArrayList<Integer> getCombo(){ return _comboIndexList;}
    public Players(String nick) {
        this._nick = nick;
    }
    //do testowania układów rozpoznawania układów
    public void add(Cards card){
        _hand.add(card);
    } // do testowania
    public void deal(ArrayList<Cards> deck){
        _hand.add(deck.get(0));
        deck.remove(0);
    }
    public void exchange(ArrayList<Cards> deck, ArrayList<Integer> indexes){
        Iterator<Cards> value;
        for (int i = 0; i < indexes.size(); i++) {
            value = _hand.iterator();
            for (int j = 0; j < indexes.get(i) - i; j++) {
                value.next();
            }
            value.remove();
        }
        System.out.println(this);
        for (int i = 0; i < indexes.size(); i++) {
            deal(deck);
        }
    }
    public Cards getCard(int index){
        int i = 0;
        Cards card = null;
        Iterator<Cards> iterator = _hand.iterator();
        while (iterator.hasNext()) {
            card = iterator.next();
            if (i == index) {
                break;
            }
            i++;
        }
        return card;
    }
    // Poker Hand Ranking
    // 10 royal flush - 5xc  +  check color                                                                                                             +
    // 9 strait flush - 5xc  +  check value of highest figure then color exception (bicycle)                                                            +
    // 8 four of kind        +  check value of figure in four
    // 7 full house          +  check value of figure in three then check value of figure in pair then check color in three
    // 6 flush - 5xc         +  check value of highest figure then color                                                                                +
    // 5 strait              +  check value of highest figure then color exception (bicycle)                                                            +
    // 4 three of kind       +  check value of figure in three
    // 3 two pair            +  check value of figure in higher pair then check value of lower pair then chceck value of highest figure then color
    // 2 pair                +  check value of figure in pair then check value of highest figure then color
    // 1 high card           +  check value of highest figure then color                                                                                +
    public int handRanking(){
        int numofCol = 0;
        for (Cards card:_hand) {
            int local = 0;
            for (int i = 0; i < _hand.size(); i++) {
                if(card.compareCol(getCard(i)) == 0){
                    local ++;
                }
                if(local > numofCol){
                    numofCol = local;
                }
            }
        }
        if (numofCol == 5){
            if(getCard(_hand.size() - 1).getValueOfFigure() == 14 && getCard(0).getValueOfFigure() == 10){
                return 10; // Royal Flush
            } else {
                for (int i = 0; i < _hand.size() - 1; i++) {
                    // 2 != 2-1 false, 5 != 14 - 1 true
                    if (getCard(i).getValueOfFigure() != getCard(i + 1).getValueOfFigure() - 1) { // jeśli karty nie są po kolei jest zwykły flash
                        //5th == 14 && 4th == 5 true
                        if (getCard(_hand.size() - 1).getValueOfFigure() == 14 && getCard(_hand.size() - 2).getValueOfFigure() == 5) {
                            return 9; // dla układu kart 2,3,4,5,A jest strait flush A,2,3,4,5
                        }
                        return 6; // flush
                    }
                }
                return 9; // jeśli wyjde z pętli nie wywoławszy if mam 5 kart po kolei
            }
        } else {
            for (int i = 0; i < _hand.size() - 1; i++) {
                if (getCard(i).getValueOfFigure() !=
                        getCard(i + 1).getValueOfFigure() - 1) { // jeśli karty nie są po kolei przerywam i sprawdzam pozostałe układy
                    if (getCard(_hand.size() - 1).getValueOfFigure() == 14 && getCard(_hand.size() - 2).getValueOfFigure() == 5) {
                        return 5; // dla układu kart 2,3,4,5,A jest strait A,2,3,4,5 gdy nie ma 5 kart tego samego koloru
                    }
                    break;
                }
                if (i == _hand.size() - 2){
                    return 5;
                }
            }
            int maxreps = 0;
            int combo = 0;
            for (int i = 0; i < _hand.size(); i++) {
                int local = 0;
                for (int j = i; j < _hand.size(); j++) {
                    if (getCard(i).compareFig(getCard(j)) == 0){
                        local ++;
                        if (!_comboIndexList.contains(i) && local > 1) {
                            _comboIndexList.add(i);
                        }
                        if (!_comboIndexList.contains(j) && local > 1){
                            _comboIndexList.add(j);
                        }
                    }
                }
                if (local > maxreps){
                    maxreps = local;
                }
                if (local == 4){
                    break;
                }
                if (local == 2 || local == 3) {
                    combo ++; // jedna para combo = 1 max = 2, dwie pary combo = 2 max = 2, trójka combo = 2 max = 3, para i trójka combo = 3 max = 3
                }
            }
            if (maxreps == 1){
                return 1;
            } else if (maxreps == 2 && combo == 1) {
                return 2;
            } else if (maxreps == 2 && combo == 2) {
                return 3;
            } else if (maxreps == 3 && combo == 2) {
                return 4;
            } else if (maxreps == 3 && combo == 3) {
                return 7;
            } else {
                return 8;
            }
        }
    }

    @Override
    public String toString() {
        String str = _nick + " [ ";
        for (Cards card: _hand) {
            str += card.toString() + ' ';
        }
        return str + ']';
    }
}
