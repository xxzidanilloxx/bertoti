package com.fatec.padroes.facade.anti;

public class Viagem {
    public ReservaVoo reservaVoo;
    public ReservaHotel reservaHotel;
    public AluguelCarro aluguelCarro;

    public Viagem() {
        this.reservaVoo = new ReservaVoo();
        this.reservaHotel = new ReservaHotel();
        this.aluguelCarro = new AluguelCarro();
    }

    public void reservarApenasVoo() {
        reservaVoo.reservarVoo();
    }

    public void reservarApenasHotel() {
        reservaHotel.reservarHotel();
    }

    public void alugarApenasCarro() {
        aluguelCarro.alugarCarro();
    }

    public ReservaVoo getReservaVoo() {
        return reservaVoo;
    }

    public ReservaHotel getReservaHotel() {
        return reservaHotel;
    }

    public AluguelCarro getAluguelCarro() {
        return aluguelCarro;
    }

    public void reservarViagem(){
        reservaVoo.reservarVoo();
        reservaHotel.reservarHotel();
        aluguelCarro.alugarCarro();
    }
}
