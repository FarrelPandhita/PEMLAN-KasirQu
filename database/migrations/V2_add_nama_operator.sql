-- Migration: Add nama_operator to transaksi table
-- Applies to: db_kasir_dev.transaksi

ALTER TABLE transaksi 
ADD COLUMN nama_operator VARCHAR(100) NULL AFTER kode_transaksi;
