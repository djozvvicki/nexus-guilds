# NexusGuilds (v. 0.1.0)

## Opis

Plugin dodający gildie oparte o nexus.

### Nexus - serce gildii

Nexus to blok, który powstaje przy utworzeniu gildii przez gracza.
Nexus tworzy wokół siebie region, którego wielość zależy od `poziomu` nexusa.
Aktualnie nexus pozwala się ulepszyć maksymalnie do 5 poziomu. Region początkowy
na poziomie 1 wynosi 50x50x50. Na drugim poziomie jest to 70x70x70, następnie zwiększany
jest o 10 kratek, aż do maksymalnej wielkości 100x100x100.

Koszt utworzenia nexusa (gildii):

- 8 złotych jabłek (nie zaklętych)
- 32 diamenty
- 64 marchewki
- 16 skóry

## Punkty życia nexusa, uszkadzanie go oraz leczenie

Nexus posiada punkty życia, co oznacza, że wrodzy gracze mogą go uszkodzić
(gdy jego HP spadnie do 0). Jego uszkodzenie odblokowuje obszar chroniony przez nexus,
co oznacza, że wrodzy gracze po uszkodzeniu nexusa są w stanie otwierać skrzynki,
stawiać i niszczyć bloki.

Nexus posiada mechanikę odradzania. Po 30 minutach zacznie się leczyć. Jest to 5HP, co
każde 2 minuty.

Nexus można również leczyć samemu w `menu nexusa`

## Menu nexusa

Nexus posiada menu, które otwiera się po kliknięciu prawym przyciskiem (tak jak skrzynkę).
W menu nexusa dostępne masz 3 opcje:

1. Sklep nexusa
2. Ulepszenie nexusa
3. Leczenie nexusa

## Sklep Nexusa

W sklepie nexusa można kupować przedmioty. Każdy poziom odblokowuje nowe przedmioty do kupienia.
Są to różnego rodzaju przedmioty, które pozwalają Ci poprawić swoją grę oraz otrzymać
przedmioty, których zdobycie jest niemożliwe w normalnym świecie, w tym netheryt lub elytra.

Sklep nexusa dodaje także niestandardowe przedmioty, jak `Niszczarka`.

`Niszczarka` to przedmiot, podobny do TNT, który pozwala Ci uszkadzać bloki na terenie
gildii chronionej przez wrogi nexus. Nie uszkadza bloków specjalnych, tzn.:

- Skrzynek
- Nexusa
- Stoniarek
- Piecyków
- EnderChestów
- Enchantu
- Biblioteczek

## Roadmap

Plugin będzie ulepszany, a poniżej znajduje się lista zmian na każdą następną wersję.

### 0.2.0

- PVP między członkami gildii (możliwość włączenia bądź nie)
- System punktów za zabijanie graczy
- Asysty przy zdobywaniu punktów
- Limit członków gildii
- Dodanie stoniarki / craftingu nexusa*
- Nowe przedmioty specjalne
- Aktualizacja sklepu nexusa

*- crafting nexusa pozwoli craftować przedmioty specjalne

### 0.3.0

- System schowka (więcej info pojawi się w tym pliku)
