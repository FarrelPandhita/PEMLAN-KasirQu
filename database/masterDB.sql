-- =====================================================
-- DATABASE
-- =====================================================

DROP DATABASE IF EXISTS db_kasir_dev;

CREATE DATABASE db_kasir_dev
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE db_kasir_dev;

-- =====================================================
-- TABLE: kategori
-- =====================================================

CREATE TABLE kategori (
    id_kategori INT AUTO_INCREMENT PRIMARY KEY,

    nama_kategori VARCHAR(100) NOT NULL UNIQUE,
    deskripsi TEXT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    deleted_at TIMESTAMP NULL
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: barang
-- =====================================================

CREATE TABLE barang (
    id_barang INT AUTO_INCREMENT PRIMARY KEY,

    id_kategori INT NOT NULL,

    kode_barang VARCHAR(50) NOT NULL UNIQUE,
    nama_barang VARCHAR(150) NOT NULL,

    harga DECIMAL(12,2) NOT NULL,

    stok INT NOT NULL DEFAULT 0,
    minimal_stok INT NOT NULL DEFAULT 5,

    status ENUM(
        'aktif',
        'nonaktif'
    ) DEFAULT 'aktif',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_barang_kategori
    FOREIGN KEY (id_kategori)
    REFERENCES kategori(id_kategori)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_barang_kategori
ON barang(id_kategori);

CREATE INDEX idx_barang_status
ON barang(status);

-- =====================================================
-- TABLE: transaksi
-- =====================================================

CREATE TABLE transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,

    kode_transaksi VARCHAR(50)
    NOT NULL UNIQUE,

    tanggal_transaksi DATETIME
    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    subtotal DECIMAL(12,2)
    NOT NULL,

    diskon DECIMAL(12,2)
    DEFAULT 0,

    pajak DECIMAL(12,2)
    DEFAULT 0,

    grand_total DECIMAL(12,2)
    NOT NULL,

    metode_bayar ENUM(
        'cash',
        'transfer',
        'qris'
    ) DEFAULT 'cash',

    catatan TEXT NULL,

    created_at TIMESTAMP
    DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE INDEX idx_transaksi_tanggal
ON transaksi(tanggal_transaksi);

-- =====================================================
-- TABLE: detail_transaksi
-- =====================================================

CREATE TABLE detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,

    id_transaksi INT NOT NULL,
    id_barang INT NOT NULL,

    qty INT NOT NULL,

    harga_satuan DECIMAL(12,2)
    NOT NULL,

    subtotal DECIMAL(12,2)
    NOT NULL,

    created_at TIMESTAMP
    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_detail_transaksi
    FOREIGN KEY (id_transaksi)
    REFERENCES transaksi(id_transaksi)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_detail_barang
    FOREIGN KEY (id_barang)
    REFERENCES barang(id_barang)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_detail_transaksi
ON detail_transaksi(id_transaksi);

CREATE INDEX idx_detail_barang
ON detail_transaksi(id_barang);

-- =====================================================
-- TABLE: stok_log
-- =====================================================

CREATE TABLE stok_log (
    id_log INT AUTO_INCREMENT PRIMARY KEY,

    id_barang INT NOT NULL,

    jenis ENUM(
        'masuk',
        'keluar',
        'adjustment'
    ) NOT NULL,

    qty INT NOT NULL,

    stok_sebelum INT NOT NULL,
    stok_sesudah INT NOT NULL,

    keterangan TEXT NULL,

    created_at TIMESTAMP
    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_stok_barang
    FOREIGN KEY (id_barang)
    REFERENCES barang(id_barang)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_stok_barang
ON stok_log(id_barang);

-- =====================================================
-- DUMMY DATA
-- =====================================================

INSERT INTO kategori
(nama_kategori, deskripsi)
VALUES
('Minuman','Produk minuman'),
('Makanan','Produk makanan'),
('Snack','Makanan ringan'),
('Frozen Food','Frozen'),
('Sembako','Kebutuhan pokok'),
('Elektronik','Elektronik kecil'),
('ATK','Alat tulis'),
('Kebersihan','Produk kebersihan'),
('Kesehatan','Produk kesehatan'),
('Lainnya','Kategori umum');

INSERT INTO barang
(
id_kategori,
kode_barang,
nama_barang,
harga,
stok,
minimal_stok
)
VALUES
(1,'BRG001','Teh Botol',5000,50,5),
(1,'BRG002','Aqua 600ml',4000,70,5),
(1,'BRG003','Pocari Sweat',9000,30,5),
(2,'BRG004','Indomie Goreng',3500,100,10),
(2,'BRG005','Roti Tawar',15000,25,5),
(3,'BRG006','Chitato',12000,20,5),
(3,'BRG007','Qtela',9000,25,5),
(4,'BRG008','Sosis Frozen',28000,15,3),
(5,'BRG009','Beras 5kg',72000,10,2),
(5,'BRG010','Minyak Goreng',19000,30,5),
(6,'BRG011','Lampu LED',25000,18,3),
(7,'BRG012','Pulpen Joyko',4000,60,10),
(8,'BRG013','Wipol',18000,20,3),
(9,'BRG014','Masker Box',25000,12,2),
(10,'BRG015','Power Strip',45000,8,2);

-- =====================================================
-- DUMMY TRANSAKSI
-- =====================================================

INSERT INTO transaksi
(
kode_transaksi,
subtotal,
diskon,
pajak,
grand_total,
metode_bayar,
catatan
)
VALUES
('TRX001',17000,0,0,17000,'cash','Dummy'),
('TRX002',31500,1500,0,30000,'qris','Dummy');

INSERT INTO detail_transaksi
(
id_transaksi,
id_barang,
qty,
harga_satuan,
subtotal
)
VALUES
(1,1,2,5000,10000),
(1,4,2,3500,7000),

(2,3,1,9000,9000),
(2,5,1,15000,15000),
(2,6,1,12000,12000);

-- =====================================================
-- DUMMY STOK LOG
-- =====================================================

INSERT INTO stok_log
(
id_barang,
jenis,
qty,
stok_sebelum,
stok_sesudah,
keterangan
)
VALUES
(1,'keluar',2,52,50,'Dummy transaksi'),
(4,'keluar',2,102,100,'Dummy transaksi'),
(3,'keluar',1,31,30,'Dummy transaksi');

-- =====================================================
-- TEST QUERY
-- =====================================================

SELECT * FROM kategori;

SELECT * FROM barang;

SELECT
    b.kode_barang,
    b.nama_barang,
    k.nama_kategori,
    b.harga,
    b.stok
FROM barang b
JOIN kategori k
ON b.id_kategori = k.id_kategori
WHERE b.deleted_at IS NULL;

SELECT * FROM transaksi;

SELECT * FROM detail_transaksi;