package com.petrilli.sandro.malasiabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MalasiaBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getHandPoints() == 12)
            return -1;

        if(MaoGiga(intel)) {
            return 1;
        }

        if (MaoZapOuCopasEasAtres(intel)) {
            return 1;
        }

        if(MaoZapOuCopasEfiguras(intel)) {
            return 1;
        }

        if(MaoEspadasOuOurosEfiguras(intel)) {
            return 0;
        }

        if(MaoLixo(intel)){
            return -1;
        }

        if (MaoMediaSemBoasCartas(intel)){
            return -1;
        }

        if (MaoMediaComUmaBoaCarta(intel)){
            return 0;
        }

        if (MaoRuimComManilha(intel)){
            return -1;
        }
        if (MaoComDuasBoasSemManilha(intel)){
            return 0;
        }

        return 0;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        CardToPlay cardToPlay = null;

        if (intel.getOpponentCard().isPresent()) {
            cardToPlay = CardToPlay.of(DeMenorQuePodeGanhar(intel));
        }

        return cardToPlay;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    //retorna a menor carta da mão
    private TrucoCard DeMenor(GameIntel intel) {
        TrucoCard deMenor = null;
        for (TrucoCard card : intel.getCards()) {
            if (deMenor == null || card.relativeValue(intel.getVira()) < deMenor.relativeValue(intel.getVira())) {
                deMenor = card;
            }
        }
        return deMenor;
    }

    //retorna menor carta na mão que ganha da carta que o oponente jogou
    private TrucoCard DeMenorQuePodeGanhar(GameIntel intel) {
        TrucoCard DeMenorQuePodeGanhar = null;
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isPresent()) {
            TrucoCard opponentCardValue = opponentCard.get();
            for (TrucoCard card : intel.getCards()) {
                int cardValue = card.relativeValue(vira);
                int opponentCardValueRelative = opponentCardValue.relativeValue(vira);
                if (cardValue > opponentCardValueRelative &&
                        (DeMenorQuePodeGanhar == null || cardValue < DeMenorQuePodeGanhar.relativeValue(vira))) {
                    DeMenorQuePodeGanhar = card;
                }
            }
        }
        return DeMenorQuePodeGanhar;
    }

    private boolean MaoGiga(GameIntel intel) {

            TrucoCard vira = intel.getVira();
            List<TrucoCard> cards = intel.getCards();

            int manilhasCount = 0;
            for (TrucoCard card : cards) {
                if (card.relativeValue(vira) > 9) {
                    manilhasCount++;
                    if(manilhasCount >= 2) {
                        return true;
                    }
                }
            }
            return false;
    }

    private boolean MaoZapOuCopasEfiguras(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        int figurasCount = 0;
        int manilhasCount = 0;

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if(cardValue == 13 || cardValue == 12) {
                manilhasCount++;
            }
            if(cardValue >= 4 && cardValue <= 6) {
                figurasCount++;
            }
        }
        if(manilhasCount == 1 && figurasCount == 1) {
            return true;
        }
        return false;
    }

    private boolean MaoEspadasOuOurosEfiguras(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        int figurasCount = 0;
        int manilhasCount = 0;

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if(cardValue == 11 || cardValue == 10) {
                manilhasCount++;
            }
            if(cardValue >= 4 && cardValue <= 6) {
                figurasCount++;
            }
        }
        if(manilhasCount == 1 && figurasCount == 1) {
            return true;
        }
        return false;
    }

    private boolean MaoZapOuCopasEasAtres(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        int figurasCount = 0;
        int manilhasCount = 0;

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if(cardValue == 13 || cardValue == 12) {
                manilhasCount++;
            }
            if(cardValue >= 7 && cardValue <= 9) {
                figurasCount++;
            }
        }
        if(manilhasCount == 1 && figurasCount == 1) {
            return true;
        }
        return false;
    }


    private boolean MaoMediaSemBoasCartas(GameIntel intel) {

        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if (cardValue < 4 ){
                return false;
            }
            if (cardValue > 7){
                return false;
            }
        }
        return true;
    }

    private boolean MaoMediaComUmaBoaCarta(GameIntel intel) {

        int boaCartaCount = 0;

        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if (cardValue < 4 ){
                return false;
            }
            if (cardValue > 7){
                boaCartaCount++;
            }
        }
        return boaCartaCount == 1;
    }

    private boolean MaoComDuasBoasSemManilha(GameIntel intel) {

        int boaCartaCount = 0;

        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if (cardValue > 7 && cardValue < 10){
                boaCartaCount++;
            }
        }
        return boaCartaCount == 2;
    }

    private boolean MaoRuimComManilha(GameIntel intel) {

        int manilhaCount = 0;

        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if (cardValue > 4 && cardValue < 10){
                return false;
            }
            if (cardValue >= 10){
                manilhaCount++;
            }
        }
        return manilhaCount == 1;
    }


    private boolean MaoLixo(GameIntel intel) {

        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(vira);
            if (cardValue > 5){
                return false;
            }
        }
        return true;
    }

}
