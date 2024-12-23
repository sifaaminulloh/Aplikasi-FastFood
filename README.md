Fast Food Dashboard

Dashboard Fast Food adalah aplikasi berbasis Java yang dirancang menggunakan Swing untuk mengelola penjualan makanan dan minuman cepat saji. Aplikasi ini memungkinkan pengguna untuk memesan makanan/minuman, melihat ringkasan pesanan, dan melakukan pembayaran. Selain itu, aplikasi ini memiliki fitur admin untuk mengelola stok dan melihat riwayat transaksi.

Fitur Utama

    Kategori Produk
        Menampilkan dua kategori utama: Makanan dan Minuman.
        Setiap produk dilengkapi dengan gambar, harga, dan informasi stok.

    Ringkasan Pesanan
        Menampilkan daftar item yang dipilih, kuantitas, dan total harga.
        Format harga menggunakan format mata uang Rupiah (IDR).

    Metode Pembayaran
        Mendukung pembayaran melalui:
            Tunai
            Transfer Bank
            QRIS (menggunakan kode QR).

    Fitur Admin
        Login Admin: Akses fitur admin menggunakan kredensial standar.
        Tambah Stok: Menambah stok untuk item tertentu.
        Riwayat Transaksi: Melihat riwayat transaksi yang telah dilakukan.

    Validasi Otomatis
        Memastikan stok mencukupi sebelum menambah pesanan.
        Mencegah pembayaran jika tidak ada barang yang dipilih.

Struktur Kode

    FastFoodDashboard (Class Utama)
        Berfungsi sebagai GUI utama aplikasi.
        Komponen Utama:
            Sidebar: Untuk memilih kategori dan akses admin.
            Main Panel: Menampilkan daftar makanan/minuman berdasarkan kategori.
            Order Summary: Menampilkan ringkasan pesanan.
            Payment Panel: Mengelola pembayaran.

    FastFoodDashboardTest (Class Pengujian)
        Menggunakan JUnit untuk menguji beberapa fungsi utama, seperti:
            Perhitungan total harga.
            Format harga dalam mata uang Rupiah.
            Validasi stok barang.

    Metode Utama:
        initializeStockAndPrices(): Mengatur data stok awal dan harga.
        initializeGUI(): Membuat elemen-elemen GUI utama.
        createItemPanel(String[] items): Membuat panel untuk kategori makanan/minuman.
        updateOrderSummary(): Menghitung total harga pesanan dan memperbarui ringkasan pesanan.
        showPaymentOptions(): Menampilkan dialog metode pembayaran.
        processOrder(String paymentMethod): Memproses pesanan dan mengurangi stok.
        showAdminMenu(): Menampilkan menu admin untuk tambah stok dan riwayat transaksi.

Cara Menggunakan

    Pengguna Umum

    Jalankan aplikasi.
    Pilih kategori Makanan atau Minuman dari menu samping.
    Tentukan jumlah item menggunakan spinner.
    Lihat ringkasan pesanan di panel sebelah kanan.
    Klik tombol Lanjutkan Pembayaran untuk memilih metode pembayaran.
    Ikuti petunjuk sesuai metode pembayaran yang dipilih.


    Admin

    Klik tombol Login Admin di sidebar.
    Masukkan username: admin dan password: password.
    Pilih salah satu opsi:
        Tambah Stok: Menambah stok item makanan/minuman.
        Lihat Riwayat Transaksi: Melihat daftar riwayat transaksi yang sudah diproses
