package com.fatec.padroes.facade.pattern;

public class ViagemFacade {
    private final ReservaVoo reservaVoo;
    private final ReservaHotel reservaHotel;
    private final AluguelCarro aluguelCarro;

    public ViagemFacade() {
        this.reservaVoo = new ReservaVoo();
        this.reservaHotel = new ReservaHotel();
        this.aluguelCarro = new AluguelCarro();
    }

    public void reservarViagem(){
        reservaVoo.reservarVoo();
        reservaHotel.reservarHotel();
        aluguelCarro.alugarCarro();
    }
}
