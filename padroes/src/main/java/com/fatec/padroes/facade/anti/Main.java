package com.fatec.padroes.facade.anti;

public class Main {
    public static void main(String[] args) {
        Viagem viagem = new Viagem();
        viagem.reservarViagem();

        viagem.reservarApenasVoo();
        viagem.reservarApenasHotel();
        viagem.alugarApenasCarro();

        viagem.getReservaVoo().reservarVoo();
        viagem.getReservaHotel().reservarHotel();
        viagem.getAluguelCarro().alugarCarro();
    }
}
