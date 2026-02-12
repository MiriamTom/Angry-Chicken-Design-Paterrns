# 🎮 MVC Cannon Game – Semestrální práce (NI-ADP)

## 📌 Popis projektu

Jedná sa o 2D arkádovú hru postavenú na architektúre **MVC (Model–View–Controller)**, v ktorej hráč ovláda kanón a ničí prichádzajúcich nepriateľov pomocou rakiet.
Hra využíva viacero **návrhových vzorov**, obsahuje **systém kolízií**, **počasie**, **úrovne obtiažnosti**, **undo funkcionalitu** a **zvukový systém**.

Projekt bol vypracovaný ako **semestrálna práca z predmetu NI-ADP**.

---

## ▶️ Spustenie hry

### Požiadavky

* **Java 17+**
* **JavaFX**
* IDE: *IntelliJ IDEA / Eclipse* (odporúčané)

### Spustenie

1. Naklonuj repozitár:

```bash
git clone <repo-url>
```

2. Otvor projekt v IDE
3. Spusti triedu:

```java
MvcGameJavaFxLauncher
```

---


## 🎮 Ovládanie hry

| Klávesa   | Akcia                                    |
| --------- | ---------------------------------------- |
| ↑ (UP)    | Pohyb kanónu nahor                       |
| ↓ (DOWN)  | Pohyb kanónu nadol                       |
| ← (LEFT)  | Pohyb kanónu doľava                      |
| → (RIGHT) | Pohyb kanónu doprava                     |
| **SPACE** | Streľba                                  |
| **W**     | Zväčšenie uhla kanónu                    |
| **S**     | Zmenšenie uhla kanónu                    |
| **E**     | Zvýšenie sily kanónu                     |
| **Q**     | Zníženie sily kanónu                     |
| **M**     | Prepnutie pohybovej stratégie projektilu |
| **N**     | Prepnutie módu streľby                   |
| **R**     | Reload / reset kanónu                    |
| **U**     | Uloženie snapshotu hry (Memento)         |
| **I**     | Obnovenie uloženého snapshotu            |
| **Y**     | Undo – návrat o posledný krok            |
| **P**     | Prepnutie počasia                        |
| **ESC**   | Ukončenie hry                            |

---

### ℹ️ Poznámky k ovládaniu

* **Undo (Y)** využíva návrhový vzor **Memento**
* **Počasie (P)** ovplyvňuje:
    * presnosť zásahu
    * trajektóriu strely
* **M / N** demonštrujú použitie **Strategy pattern**

---

## 🧠 Implementované funkcionality

### Povinné časti zadania

✅ Pridaní nepriatelia

✅ Kolízie (raketa × nepriateľ)

✅ GameInfo (skóre, uhol, sila, mód kanónu, počasie)

✅ Kompletná sada Commandov

✅ Vylepšený Memento systém (Undo)

✅ Visitor metódy (enemy, collision, game info)

✅ **5 unit testov** (z toho **2 s mockovaním**)

---

## 🌦️ Rozšírenie – Systém počasia (vybraná nepovinná úloha)

Implementovaný **Weather System**, ktorý ovplyvňuje hrateľnosť:

* ☀️ **Clear** – normálna viditeľnosť
* 🌧️ **Rain** – znížená presnosť
* 🌬️ **Wind** – ovplyvnenie trajektórie
* 🌫️ **Fog** – zmenšený hit radius

➡️ Počasie **nemení náhodnosť zásahu**, ale **realisticky mení fyziku hry**

Použité návrhové vzory:

* **Strategy** – správanie počasia
* **Decorator** – úprava vlastností strely
* **Observer** – notifikácia zmien

---

## 🔊 Zvukový systém

* Zvuky výstrelu, zásahu a explózie
* Dynamické zvuky počasia
* Použitý návrhový vzor **Chain of Responsibility** na kombináciu:

    * pozadie
    * počasie
    * herné udalosti

---

## 📊 Herné módy a obtiažnosť

* **Easy / Medium / Hard**
* Rôzny počet nepriateľov
* Rýchlosť a výdrž nepriateľov
* Dynamický spawn

---
## 🧪 Testovanie

* **JUnit**
* **Mockit** (mockovanie Modelu)
* Testované:

    * Commandy
    * Undo funkcionalita
    * Stav Modelu

➡️ Projekt obsahuje **5 unit testov**, z toho **min. 2 s mockovaním**




