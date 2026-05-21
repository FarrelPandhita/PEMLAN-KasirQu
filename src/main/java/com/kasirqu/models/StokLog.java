package com.kasirqu.models;

import java.sql.Timestamp;

public class StokLog {
    private int idLog;
    private int idBarang;
    private String jenis;
    private int qty;
    private int stokSebelum;
    private int stokSesudah;
    private String keterangan;
    private Timestamp createdAt;

    public StokLog() {}

    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }
    public int getIdBarang() { return idBarang; }
    public void setIdBarang(int idBarang) { this.idBarang = idBarang; }
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public int getStokSebelum() { return stokSebelum; }
    public void setStokSebelum(int stokSebelum) { this.stokSebelum = stokSebelum; }
    public int getStokSesudah() { return stokSesudah; }
    public void setStokSesudah(int stokSesudah) { this.stokSesudah = stokSesudah; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
