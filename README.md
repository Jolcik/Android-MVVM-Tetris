# Tetris

**Wstęp**

Witam serdecznie w tym zaułku internetu. W tym miejscu znajduje się mój ostatni projekt na zaliczenie laboratorium z Programowania Obiektowego, czyli adaptacja Tetrisa na telefony z systemem Android. Przy okazji zastosowano tutaj także wzorzec projekt ***Model-View-ViewModel***, który całkiem nieźle spisuje się w tym przypadku. Nie jest to może najbardziej potrzebna tutaj rzecz, ale zrobiłem tak coby poćwiczyć praktyczne wykorzystywanie wzorców projektowych oraz pisania w miarę sensownego kodu na Androidzie. Całość jest napisana w jakże cudownym języku ***Kotlin*** i zawiera także testy jednostkowe.

**Ogólna architektura i struktura kodu**

Jak już wspomniałem, wykorzystuję tutaj wzorzec projektowy ***MVVM***. Na tyle na ile umiem stosuję także zasady SOLID. Dla konkretnego przykładu mogę podać zrealizowanie zasady odwrócenia zależności, w tym przypadku poprzez interfejsy wykorzystywane do komunikacji między warstwami. Są to interfejsy `AudioInterface` i `TetriminoCallbackInterface`. Pierwszy odpowiada za obsługę efektów dźwiękowych, drugi za komunikację między kontrolerem gry, a warstwą sprzętową (tutaj między Modelem, a ViewModelem). Nazwa tego interfejsu może być lekko myląca, a wynika to z tego, że na początku myślałem, że interfejs będzie tylko jedną sytuację obsługiwał. Poźniej to się rozrosło, a nazwa pozostała ta sama. Wiem, niedociągnięcie i równie dobrze w tym czasie kiedy to piszę, mógłbym to skrupulatnie naprawić. Czemu tego nie zrobię? Dla przestrogi, żebym potem zawsze o tym pamiętał, a przynajmniej tak to sobie tłumaczę. Przejdę tymczasem dalej do opisania jak zaimplementowałem elementy wzorca MVVM.

**Model**

Tutaj mamy trzy klasy - `Block`, `Tetrimino` i `GameController`. Idąc od dołu według ważności - `Block` jest klasą opisującą pojedynczy blok, `Tetrimino` jest konkretną spadającą figurą (taka jest nazwa historyczna), składa się ona właśnie z bloków (Block). Najważniejszym elementem jest `GameController`. Jest to klasa implementująca zasady gry i całą logikę biznesową, jeżeli tak to można nazwać. Jest to czysty kod w Kotlinie, **całkowicie uniezależniony** od aspektów technicznych i sprzętowych. Cykl gry jest załączany zewnętrznie przez funkcję `onTick()`, która oznacza jedno tyknięcie w grze. GameController komunikuje się z ViewModelem poprzez wcześniej wspomniany interfejs. Służy to tylko poinformowaniu niższej warstwy o specjalnych sytuacjach jak np. zakończenie gry. Dodatkowo klasa implementująca ten interfejs może być nullem, tzn. może ona w ogóle nie istnieć. Jest to możliwe dzięki wykorzystaniu właściwości Kotlina - **Null Safety**. Do interfejsu zawsze odwołuję się poprzez operator `?`, tak więc równie dobrze odbiorcy interfejsu może nie być. Jeszcze bardziej usamodzielnia to daną klasę. Super sprawa.

**ViewModel**

ViewModelem w tym przypadku jest klasa `MainViewModel`. Obsługuje ona technicznie `GameController` (wywołuje onTick() i implementuje interfejs odbiorcy zdarzeń) i całą logikę po stronie technicznej tzn. przyciski, pauza itp. Odpowiednio także przygotowuje dane do wysłanie do View dzięki zastosowaniu obserwatorów. Zajmuje się także dźwiękiem - poprzez interfejs i operator ?, czyli ta sama sytuacja co w GameControllerze. Ogólnie poza tym nie ma specjalnie o czym wspominać, tylko ewentualnie o tych właśnie obserwatorach. W ViewModelu mamy zmienne typu `LiveData`, które są obserwowane przez View. Na podstawie tego wie co wyświetlić bez ingerencji w `ViewModel`.

**View**

W tym przypadku mamy klasy `MainActivity`, `GameSurfaceView` i `NextTetriminoSurfaceView`. MainActivity przygotowuje odpowiednie elementy do użycia oraz zarządza obserwatorami. Wywołuje także odpowiednio klasy GameSurfaceView i NextTetriminoView, które wyświetlają graficznie stan gry tzn. klocki i następne tetrimino jakie się pojawi. Zrealizowałem je poprzez implementację SurfaceView i odpowiednie operowanie na rysowaniu kształtów. Klasa obrabia informacje na temat klocków i je odpowiednio wyświetla. Przy okazji można wspomnieć że jest to niezależne od kształtu planszy, można go dowolnie zmienić, a i tak wszystko będzie dobrze działało.

**Podsumowanie**

Mimo że nie jest to duży projekt, to jednak całkiem fajnie ukazuje wzorzec MVVM i dostarczył dużo rozrywki przy tworzeniu. Mam nadzieję, że nie wynudziłem przy okazji czytania tego, o ile ktoś to przeczyta. W każdym razie, w razie pytań zachęcam do kontaktu i pozdrawiam milutko.

PK
