# SpyClash

เกมต่อสู้ 2 ผู้เล่นบนคีย์บอร์ดเครื่องเดียว เขียนด้วย Java Swing

## ปุ่มควบคุม

| การกระทำ | Player 1 | Player 2 |
|---|---|---|
| เดินซ้าย / ขวา | `A` / `D` | `←` / `→` |
| กระโดด | `W` | `↑` |
| โจมตี | `F` | `/` |

## โครงสร้างโปรเจกต์

| แพ็กเกจ | หน้าที่ |
|---|---|
| `src` | `Main` จุดเริ่มโปรแกรม |
| `src.util` | `GameConfig` ค่าคงที่ของเกม, `Assets` โหลดรูป |
| `src.fighter` | `Fighter` และตัวละคร `Assassin`, `Enforcer` |
| `src.weapon` | `Weapon`, `Sword`, `Gun`, `Bullet` |
| `src.item` | `Item`, `Buff`, `WeaponItem` และไอเทมแต่ละชนิด |
| `src.panels` | หน้าจอ, ปุ่ม และ HUD |
| `res` | รูปภาพทั้งหมด |
