# Laporan Black Box Testing
## Aplikasi: TeduhServiceApp
**Tanggal:** 19 May 2026 11:25  
**Tipe Testing:** Black Box Testing (Unit Test)  
**Framework:** JUnit4 + MockK + Kotlin Coroutines Test  

---

## Ringkasan Keseluruhan

| Metrik | Nilai |
|--------|-------|
| Total Test Case | 102 |
| ✅ Pass | 102 |
| ❌ Fail / Error | 0 |
| ⏭️ Skipped | 0 |
| Pass Rate | 100.0% |

---

## Ringkasan Per Modul

| No | Modul | Total | Pass | Fail | Status |
|----|-------|-------|------|------|--------|
| 1 | AmbilAntreanRepository | 7 | 7 | 0 | ✅ Semua Pass |
| 2 | AntreanActiveRepository | 9 | 9 | 0 | ✅ Semua Pass |
| 3 | AuthRepository | 5 | 5 | 0 | ✅ Semua Pass |
| 4 | BranchRepository | 6 | 6 | 0 | ✅ Semua Pass |
| 5 | DetailBranchRepository | 7 | 7 | 0 | ✅ Semua Pass |
| 6 | DetailNotificationsRepository | 6 | 6 | 0 | ✅ Semua Pass |
| 7 | NotificationsRepository | 6 | 6 | 0 | ✅ Semua Pass |
| 8 | ReminderRepository | 6 | 6 | 0 | ✅ Semua Pass |
| 9 | RingkasanHomeRepository | 8 | 8 | 0 | ✅ Semua Pass |
| 10 | AuthModel | 14 | 14 | 0 | ✅ Semua Pass |
| 11 | BranchModel | 12 | 12 | 0 | ✅ Semua Pass |
| 12 | FormatDateUtils | 15 | 15 | 0 | ✅ Semua Pass |
| 13 | ExampleUnitTest | 1 | 1 | 0 | ✅ Semua Pass |

---

## Detail Test Case Per Modul

### AmbilAntreanRepository
**Hasil:** 7/7 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 1 | ambilAntreanUsers - nomor antrian dan status dikembalikan dengan benar | 87 | ✅ Pass | - |
| 2 | ambilAntreanUsers - HTTP 422 Unprocessable Entity mengembalikan Result failure | 14 | ✅ Pass | - |
| 3 | ambilAntreanUsers - exception IOException mengembalikan Result failure dengan pesan | 10 | ✅ Pass | - |
| 4 | ambilAntreanUsers - API dipanggil dengan request yang benar | 14 | ✅ Pass | - |
| 5 | ambilAntreanUsers - HTTP 400 Bad Request mengembalikan Result failure | 8 | ✅ Pass | - |
| 6 | ambilAntreanUsers - sukses mengembalikan CreateAntrianResponse | 8 | ✅ Pass | - |
| 7 | ambilAntreanUsers - body null mengembalikan Result failure | 7 | ✅ Pass | - |

### AntreanActiveRepository
**Hasil:** 9/9 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 8 | batalAntrean - dipanggil dengan ID yang benar | 2929 | ✅ Pass | - |
| 9 | getAntreanActive - list kosong dikembalikan jika tidak ada antrean | 105 | ✅ Pass | - |
| 10 | batalAntrean - body null mengembalikan pesan default Berhasil | 12 | ✅ Pass | - |
| 11 | getAntreanActive - sukses mengembalikan list antrean aktif | 11 | ✅ Pass | - |
| 12 | batalAntrean - exception mengembalikan Result failure | 13 | ✅ Pass | - |
| 13 | getAntreanActive - data antrean memiliki field yang benar | 11 | ✅ Pass | - |
| 14 | batalAntrean - HTTP error 404 mengembalikan Result failure | 16 | ✅ Pass | - |
| 15 | batalAntrean - sukses mengembalikan pesan berhasil | 11 | ✅ Pass | - |
| 16 | getAntreanActive - exception mengembalikan Result failure | 11 | ✅ Pass | - |

### AuthRepository
**Hasil:** 5/5 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 17 | loginGoogle - exception jaringan mengembalikan Result failure | 17 | ✅ Pass | - |
| 18 | loginGoogle - sukses mengembalikan Result success dengan AuthResponse | 16 | ✅ Pass | - |
| 19 | loginGoogle - response HTTP error mengembalikan Result failure dengan pesan error | 8 | ✅ Pass | - |
| 20 | loginGoogle - token dalam AuthData dikembalikan dengan benar | 10 | ✅ Pass | - |
| 21 | loginGoogle - response body null mengembalikan Result failure | 15 | ✅ Pass | - |

### BranchRepository
**Hasil:** 6/6 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 22 | getBranch - sukses mengembalikan list branch | 56 | ✅ Pass | - |
| 23 | getBranch - response success false mengembalikan Result failure | 7 | ✅ Pass | - |
| 24 | getBranch - data branch pertama memiliki nama yang benar | 5 | ✅ Pass | - |
| 25 | getBranch - exception mengembalikan Result failure | 5 | ✅ Pass | - |
| 26 | getBranch - API dipanggil tepat sekali | 11 | ✅ Pass | - |
| 27 | getBranch - list kosong saat tidak ada cabang | 5 | ✅ Pass | - |

### DetailBranchRepository
**Hasil:** 7/7 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 28 | getBranchById - dipanggil dengan branchId yang tepat | 57 | ✅ Pass | - |
| 29 | getBranchById - cabang dengan koordinat null tetap bisa dikembalikan | 8 | ✅ Pass | - |
| 30 | getBranchById - sukses mengembalikan Branch | 5 | ✅ Pass | - |
| 31 | getBranchById - latitude dan longitude dikembalikan dengan benar | 7 | ✅ Pass | - |
| 32 | getBranchById - exception mengembalikan Result failure | 9 | ✅ Pass | - |
| 33 | getBranchById - data cabang memiliki field yang benar | 8 | ✅ Pass | - |
| 34 | getBranchById - response success false mengembalikan Result failure | 8 | ✅ Pass | - |

### DetailNotificationsRepository
**Hasil:** 6/6 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 35 | getDetailNotificationById - ID berbeda menghasilkan data berbeda | 66 | ✅ Pass | - |
| 36 | getDetailNotificationById - sukses mengembalikan DetailNotificationData | 11 | ✅ Pass | - |
| 37 | getDetailNotificationById - exception mengembalikan Result failure | 10 | ✅ Pass | - |
| 38 | getDetailNotificationById - dipanggil dengan ID yang benar | 17 | ✅ Pass | - |
| 39 | getDetailNotificationById - data notifikasi memiliki field yang benar | 11 | ✅ Pass | - |
| 40 | getDetailNotificationById - cabang_id bisa bernilai null | 7 | ✅ Pass | - |

### NotificationsRepository
**Hasil:** 6/6 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 41 | getAllNotifications - sukses mengembalikan list notifikasi | 51 | ✅ Pass | - |
| 42 | getAllNotifications - notifikasi pertama memiliki judul yang benar | 9 | ✅ Pass | - |
| 43 | getAllNotifications - notifikasi dengan cabang_id null tetap dikembalikan | 11 | ✅ Pass | - |
| 44 | getAllNotifications - API dipanggil tepat sekali | 14 | ✅ Pass | - |
| 45 | getAllNotifications - list kosong saat tidak ada notifikasi | 10 | ✅ Pass | - |
| 46 | getAllNotifications - exception mengembalikan Result failure | 8 | ✅ Pass | - |

### ReminderRepository
**Hasil:** 6/6 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 47 | sendWhatsappReminder - request dikirim dengan nomor WA yang benar | 49 | ✅ Pass | - |
| 48 | sendWhatsappReminder - response success false mengembalikan Result failure | 6 | ✅ Pass | - |
| 49 | sendWhatsappReminder - nomor WA tersimpan dalam response data | 7 | ✅ Pass | - |
| 50 | sendWhatsappReminder - nomor WA format internasional diterima dengan benar | 6 | ✅ Pass | - |
| 51 | sendWhatsappReminder - sukses mengembalikan ReminderModelsResponse | 6 | ✅ Pass | - |
| 52 | sendWhatsappReminder - exception jaringan mengembalikan Result failure | 8 | ✅ Pass | - |

### RingkasanHomeRepository
**Hasil:** 8/8 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 53 | getRingkasanHome - sukses mengembalikan RingkasanHomeResponse | 46 | ✅ Pass | - |
| 54 | getRingkasanHome - nomor dipanggil null pada cabang kosong | 5 | ✅ Pass | - |
| 55 | getRingkasanHome - sisa antrian cabang pertama bernilai 12 | 5 | ✅ Pass | - |
| 56 | getRingkasanHome - HttpException mengembalikan Result failure dengan kode HTTP | 394 | ✅ Pass | - |
| 57 | getRingkasanHome - total cabang dikembalikan dengan benar | 5 | ✅ Pass | - |
| 58 | getRingkasanHome - generic Exception mengembalikan pesan exception | 6 | ✅ Pass | - |
| 59 | getRingkasanHome - IOException mengembalikan pesan koneksi bermasalah | 6 | ✅ Pass | - |
| 60 | getRingkasanHome - API dipanggil tepat sekali | 10 | ✅ Pass | - |

### AuthModel
**Hasil:** 14/14 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 61 | User - dua objek identik dianggap equal | 1 | ✅ Pass | - |
| 62 | GoogleLoginRequest - token kosong tetap tersimpan | 0 | ✅ Pass | - |
| 63 | AuthData - menyimpan token dan user dengan benar | 0 | ✅ Pass | - |
| 64 | User - copy menghasilkan objek baru dengan property berbeda | 0 | ✅ Pass | - |
| 65 | GoogleLoginRequest - dua objek dengan token sama dianggap equal | 0 | ✅ Pass | - |
| 66 | User - property dapat diakses dengan benar | 0 | ✅ Pass | - |
| 67 | AuthResponse - akses nested data user berjenjang | 0 | ✅ Pass | - |
| 68 | User - role admin tersimpan dengan benar | 0 | ✅ Pass | - |
| 69 | GoogleLoginRequest - dua objek dengan token berbeda tidak equal | 1 | ✅ Pass | - |
| 70 | AuthData - token berbeda menghasilkan objek tidak equal | 0 | ✅ Pass | - |
| 71 | AuthResponse - response gagal memiliki flag success false | 1 | ✅ Pass | - |
| 72 | AuthResponse - data berisi token dan user yang benar | 0 | ✅ Pass | - |
| 73 | GoogleLoginRequest - menyimpan id_token dengan benar | 1 | ✅ Pass | - |
| 74 | AuthResponse - sukses memiliki flag success true | 1 | ✅ Pass | - |

### BranchModel
**Hasil:** 12/12 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 75 | BranchWithDistance - menyimpan cabang dan jarak dengan benar | 4 | ✅ Pass | - |
| 76 | Branch - copy menghasilkan objek baru yang tidak sama referensinya | 0 | ✅ Pass | - |
| 77 | BranchResponse - success false dengan pesan error | 1 | ✅ Pass | - |
| 78 | BranchWithDistance - jarak nol untuk lokasi yang sama | 3 | ✅ Pass | - |
| 79 | Branch - dua objek dengan data sama dianggap equal | 0 | ✅ Pass | - |
| 80 | Branch - koordinat lat dan lng tersedia | 0 | ✅ Pass | - |
| 81 | Branch - koordinat bisa bernilai null | 1 | ✅ Pass | - |
| 82 | Branch - dua objek dengan data berbeda tidak equal | 0 | ✅ Pass | - |
| 83 | BranchWithDistance - sorting berdasarkan jarak berfungsi | 2 | ✅ Pass | - |
| 84 | Branch - property dasar dapat diakses dengan benar | 0 | ✅ Pass | - |
| 85 | BranchResponse - success true dan data tidak kosong | 1 | ✅ Pass | - |
| 86 | BranchResponse - data bisa berisi banyak cabang | 13 | ✅ Pass | - |

### FormatDateUtils
**Hasil:** 15/15 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 87 | formatDate - ISO 8601 dengan offset +07 00 diformat dengan benar | 51 | ✅ Pass | - |
| 88 | formatDate - ISO 8601 dengan offset UTC Z diformat dengan benar | 1 | ✅ Pass | - |
| 89 | formatDate - string kosong mengembalikan tanda hubung | 1 | ✅ Pass | - |
| 90 | formatDate - output tidak mengandung karakter T atau Z dari ISO | 1 | ✅ Pass | - |
| 91 | formatDate - format tanggal SQL mengembalikan tanda hubung | 0 | ✅ Pass | - |
| 92 | formatDate - tanggal akhir tahun diformat dengan benar | 1 | ✅ Pass | - |
| 93 | formatDate - angka saja mengembalikan tanda hubung | 0 | ✅ Pass | - |
| 94 | formatDate - tanggal awal tahun diformat dengan benar | 1 | ✅ Pass | - |
| 95 | formatDate - dua tanggal berbeda menghasilkan output berbeda | 0 | ✅ Pass | - |
| 96 | formatDate - string null mengembalikan tanda hubung | 0 | ✅ Pass | - |
| 97 | formatDate - format tanggal tidak valid mengembalikan tanda hubung | 1 | ✅ Pass | - |
| 98 | formatDate - offset negatif diformat berdasarkan jam lokal | 0 | ✅ Pass | - |
| 99 | formatDate - teks acak mengembalikan tanda hubung | 1 | ✅ Pass | - |
| 100 | formatDate - output panjangnya konsisten untuk tanggal valid | 1 | ✅ Pass | - |
| 101 | formatDate - input identik menghasilkan output yang sama | 0 | ✅ Pass | - |

### ExampleUnitTest
**Hasil:** 1/1 Pass

| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |
|----|---------------|-----------|-------|------------|
| 102 | addition_isCorrect | 1 | ✅ Pass | - |

---

## Kesimpulan

| Status | Jumlah |
|--------|--------|
| ✅ Pass | 102 |
| ❌ Fail / Error | 0 |
| ⏭️ Skipped | 0 |
| **Total** | **102** |
| **Pass Rate** | **100.0%** |

> ✅ **Semua test case berhasil dijalankan dan lulus.**
