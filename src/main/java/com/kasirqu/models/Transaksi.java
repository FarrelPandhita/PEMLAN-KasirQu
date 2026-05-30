package com.kasirqu.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaksi {
    private int idTransaksi;
    private String kodeTransaksi;
    private String namaOperator; // Note: added via migration V2
    private Timestamp tanggalTransaksi;
    private BigDecimal subtotal;
    private BigDecimal diskon;
    private BigDecimal pajak;
    private BigDecimal grandTotal;
    private String metodeBayar;
    private String catatan;
    private Timestamp createdAt;

    public Transaksi() {}

    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }
    public String getKodeTransaksi() { return kodeTransaksi; }
    public void setKodeTransaksi(String kodeTransaksi) { this.kodeTransaksi = kodeTransaksi; }
    public String getNamaOperator() { return namaOperator; }
    public void setNamaOperator(String namaOperator) { this.namaOperator = namaOperator; }
    public Timestamp getTanggalTransaksi() { return tanggalTransaksi; }
    public void setTanggalTransaksi(Timestamp tanggalTransaksi) { this.tanggalTransaksi = tanggalTransaksi; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDiskon() { return diskon; }
    public void setDiskon(BigDecimal diskon) { this.diskon = diskon; }
    public BigDecimal getPajak() { return pajak; }
    public void setPajak(BigDecimal pajak) { this.pajak = pajak; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }
    public String getMetodeBayar() { return metodeBayar; }
    public void setMetodeBayar(String metodeBayar) { this.metodeBayar = metodeBayar; }
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
