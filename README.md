# Házi feladat specifikáció

Információk [itt](https://viauac00.github.io/laborok/hf)

## Mobil- és webes szoftverek
### 2022.10.23
### Napló
### Riedl Kovács Jade Yllva - (EU0IUG)
### riedlkovacsj@gmail.com
### Laborvezető: Telek Benjámin

## Bemutatás

A Napló alkalmazás egy napi bejegyzéseket lehetővé tevő alkalmazás. A szöveges bejegyzésekhez hozzáadhatóak képek és 3 hangulat közül lehet válogatni: jó, semleges, rossz. Ezekből az adatokból havonta megtekinthető egy grafikon, amely jellemzi az eltelt hónapot.

## Főbb funkciók

Az alkalmazás fő funkciója a szöveges bejegyzések létrehozása naponta. Ezek egy adatbázisban vannak tárolva (perzisztens adattárolás), a későbbi megjelenítés lehetőségét biztosítva. 3 nézettel rendelkezik: a bejegyzések listája időrendi sorrendben (recycler view), új bejegyzés hozzáadása, valamint a havi grafikon megjelenítése. Van lehetőség képek hozzáadására galériából vagy a telefon fényképezőgépén keresztül. Ha nem aznapra szeretne a felhasználó bejegyzést írni, elérhető egy dátumválasztó létrehozás előtt.

## Választott technológiák:

- UI
- fragmentek
- RecyclerView
- Perzisztens adattárolás
- Floating Action Button

