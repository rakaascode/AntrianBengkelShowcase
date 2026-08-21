#!/usr/bin/env python3
"""
generate_test_report.py
========================
Baca hasil test dari Gradle XML (app/build/test-results/)
lalu generate laporan Blackbox Testing dalam format Markdown.

Cara pakai:
  1. Jalankan test dulu:
       ./gradlew testDebugUnitTest
  2. Jalankan script ini:
       python3 generate_test_report.py

Output: test_report_output.md
"""

import os
import xml.etree.ElementTree as ET
from datetime import datetime
from pathlib import Path

# ─── Konfigurasi ──────────────────────────────────────────────────────────────

REPORT_DIR = Path("app/build/test-results")
OUTPUT_FILE = Path("test_report_output.md")

# Mapping nama class → nama modul yang lebih readable
MODULE_LABELS = {
    "AmbilAntreanRepositoryTest":         "AmbilAntreanRepository",
    "AntreanActiveRepositoryTest":        "AntreanActiveRepository",
    "AuthRepositoryTest":                 "AuthRepository",
    "BranchRepositoryTest":               "BranchRepository",
    "DetailBranchRepositoryTest":         "DetailBranchRepository",
    "DetailNotificationsRepositoryTest":  "DetailNotificationsRepository",
    "NotificationsRepositoryTest":        "NotificationsRepository",
    "ReminderRepositoryTest":             "ReminderRepository",
    "RingkasanHomeRepositoryTest":        "RingkasanHomeRepository",
    "AuthModelTest":                      "AuthModel",
    "BranchModelTest":                    "BranchModel",
    "FormatDateUtilsTest":                "FormatDateUtils",
}

# ─── Parse XML ────────────────────────────────────────────────────────────────

def parse_test_results(report_dir: Path):
    """
    Cari semua file TEST-*.xml di bawah report_dir,
    parse lalu kembalikan list of dict per test case.
    """
    results = []

    xml_files = list(report_dir.rglob("TEST-*.xml"))
    if not xml_files:
        print(f"[ERROR] Tidak ada file XML di {report_dir}")
        print("  → Pastikan sudah jalankan: ./gradlew testDebugUnitTest")
        return results

    print(f"[INFO] Ditemukan {len(xml_files)} file XML test result")

    for xml_file in sorted(xml_files):
        try:
            tree = ET.parse(xml_file)
            root = tree.getroot()

            for testcase in root.findall("testcase"):
                classname  = testcase.get("classname", "")
                short_class = classname.split(".")[-1]
                name       = testcase.get("name", "")
                time_sec   = float(testcase.get("time", "0"))

                failure = testcase.find("failure")
                error   = testcase.find("error")
                skipped = testcase.find("skipped")

                if skipped is not None:
                    status = "⏭️ Skip"
                    detail = ""
                elif failure is not None:
                    status = "❌ Fail"
                    detail = (failure.get("message") or failure.text or "").strip()[:200]
                elif error is not None:
                    status = "❌ Error"
                    detail = (error.get("message") or error.text or "").strip()[:200]
                else:
                    status = "✅ Pass"
                    detail = ""

                results.append({
                    "class":   short_class,
                    "module":  MODULE_LABELS.get(short_class, short_class),
                    "name":    name,
                    "status":  status,
                    "time_ms": round(time_sec * 1000),
                    "detail":  detail,
                })
        except Exception as e:
            print(f"[WARN] Gagal parse {xml_file.name}: {e}")

    return results


# ─── Generate Markdown ────────────────────────────────────────────────────────

def generate_markdown(results: list) -> str:
    if not results:
        return "# Tidak ada hasil test yang ditemukan.\n"

    total      = len(results)
    passed     = sum(1 for r in results if r["status"] == "✅ Pass")
    failed     = sum(1 for r in results if "Fail" in r["status"] or "Error" in r["status"])
    skipped    = sum(1 for r in results if "Skip" in r["status"])
    pass_rate  = round(passed / total * 100, 1) if total else 0

    now = datetime.now().strftime("%d %B %Y %H:%M")

    lines = []
    lines.append("# Laporan Black Box Testing")
    lines.append("## Aplikasi: TeduhServiceApp")
    lines.append(f"**Tanggal:** {now}  ")
    lines.append("**Tipe Testing:** Black Box Testing (Unit Test)  ")
    lines.append("**Framework:** JUnit4 + MockK + Kotlin Coroutines Test  ")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## Ringkasan Keseluruhan")
    lines.append("")
    lines.append("| Metrik | Nilai |")
    lines.append("|--------|-------|")
    lines.append(f"| Total Test Case | {total} |")
    lines.append(f"| ✅ Pass | {passed} |")
    lines.append(f"| ❌ Fail / Error | {failed} |")
    lines.append(f"| ⏭️ Skipped | {skipped} |")
    lines.append(f"| Pass Rate | {pass_rate}% |")
    lines.append("")
    lines.append("---")
    lines.append("")

    # Group by module
    from collections import defaultdict
    grouped = defaultdict(list)
    for r in results:
        grouped[r["module"]].append(r)

    # Urutan modul sesuai mapping
    module_order = list(dict.fromkeys(MODULE_LABELS.values()))
    sorted_modules = sorted(grouped.keys(), key=lambda m: module_order.index(m) if m in module_order else 999)

    lines.append("## Ringkasan Per Modul")
    lines.append("")
    lines.append("| No | Modul | Total | Pass | Fail | Status |")
    lines.append("|----|-------|-------|------|------|--------|")

    for idx, module in enumerate(sorted_modules, 1):
        items  = grouped[module]
        m_total  = len(items)
        m_pass   = sum(1 for r in items if r["status"] == "✅ Pass")
        m_fail   = sum(1 for r in items if "Fail" in r["status"] or "Error" in r["status"])
        m_status = "✅ Semua Pass" if m_fail == 0 else f"❌ {m_fail} Gagal"
        lines.append(f"| {idx} | {module} | {m_total} | {m_pass} | {m_fail} | {m_status} |")

    lines.append("")
    lines.append("---")
    lines.append("")

    # Detail per modul
    lines.append("## Detail Test Case Per Modul")
    lines.append("")

    global_no = 1
    for module in sorted_modules:
        items = grouped[module]
        m_pass = sum(1 for r in items if r["status"] == "✅ Pass")
        m_fail = sum(1 for r in items if "Fail" in r["status"] or "Error" in r["status"])

        lines.append(f"### {module}")
        lines.append(f"**Hasil:** {m_pass}/{len(items)} Pass")
        lines.append("")
        lines.append("| No | Nama Test Case | Waktu (ms) | Hasil | Keterangan |")
        lines.append("|----|---------------|-----------|-------|------------|")

        for r in items:
            name   = r["name"].replace("|", "\\|")
            detail = r["detail"].replace("\n", " ").replace("|", "\\|") if r["detail"] else "-"
            lines.append(f"| {global_no} | {name} | {r['time_ms']} | {r['status']} | {detail} |")
            global_no += 1

        lines.append("")

    # Failed summary (kalau ada)
    failed_list = [r for r in results if "Fail" in r["status"] or "Error" in r["status"]]
    if failed_list:
        lines.append("---")
        lines.append("")
        lines.append("## ⚠️ Daftar Test yang Gagal")
        lines.append("")
        lines.append("| No | Modul | Nama Test | Error |")
        lines.append("|----|-------|-----------|-------|")
        for idx, r in enumerate(failed_list, 1):
            detail = r["detail"].replace("\n", " ").replace("|", "\\|")[:150] if r["detail"] else "-"
            lines.append(f"| {idx} | {r['module']} | {r['name']} | {detail} |")
        lines.append("")

    lines.append("---")
    lines.append("")
    lines.append("## Kesimpulan")
    lines.append("")
    lines.append(f"| Status | Jumlah |")
    lines.append(f"|--------|--------|")
    lines.append(f"| ✅ Pass | {passed} |")
    lines.append(f"| ❌ Fail / Error | {failed} |")
    lines.append(f"| ⏭️ Skipped | {skipped} |")
    lines.append(f"| **Total** | **{total}** |")
    lines.append(f"| **Pass Rate** | **{pass_rate}%** |")
    lines.append("")

    if failed == 0:
        lines.append("> ✅ **Semua test case berhasil dijalankan dan lulus.**")
    else:
        lines.append(f"> ❌ **Terdapat {failed} test yang gagal. Lihat bagian 'Daftar Test yang Gagal' di atas.**")

    return "\n".join(lines) + "\n"


# ─── Main ─────────────────────────────────────────────────────────────────────

def main():
    print("=" * 60)
    print("  TeduhServiceApp - Test Report Generator")
    print("=" * 60)

    # Cari folder test results
    search_dirs = [
        REPORT_DIR / "testDebugUnitTest",
        REPORT_DIR,
    ]

    results = []
    for d in search_dirs:
        if d.exists():
            results = parse_test_results(d)
            if results:
                break

    if not results:
        print("\n[ERROR] Tidak ada hasil test ditemukan.")
        print("  Jalankan dulu: ./gradlew testDebugUnitTest\n")
        return

    print(f"[INFO] Total {len(results)} test case ditemukan")

    md_content = generate_markdown(results)

    OUTPUT_FILE.write_text(md_content, encoding="utf-8")
    print(f"\n[OK] Laporan berhasil dibuat: {OUTPUT_FILE.resolve()}")

    # Print summary ke terminal
    passed  = sum(1 for r in results if r["status"] == "✅ Pass")
    failed  = sum(1 for r in results if "Fail" in r["status"] or "Error" in r["status"])
    print(f"\n  Hasil: {passed} PASS | {failed} FAIL | {len(results)} TOTAL")
    print("=" * 60)


if __name__ == "__main__":
    main()
