# Peliapuriapp

This is a Java Web Programming exercise related to University of Applied Sciences (SAVONIA). The main purpose is a frontend-focused exercise using the Java Vaadin Flow. The idea of ​​the app itself is to be a tool for speech therapists who use games in their teaching. The app could also be used for other similar purposes or just as some note-taking tool. All information/data is fictional for the demo, and the project is not suitable for official use as such.

## More information about the project in Finnish
### Alustus

”Tee REST-tyylinen web-api, joka käsittelee vähintään kahden taulun tietokantaa. Sovelluksessa on backend-toiminnot ja niihin liittyvä web-frontend. Toteuta tämä käyttämä Vaadin Flow-frameworkia.
Esimerkki harjoitustyöstä:
Tietokanta sisältää henkilöiden tietoja sekä henkilöihin liittyviä mittaustuloksia kuten verenpaine, paino ym. Tietokannassa on taulujen välillä relaatio, jotta henkilöön liittyvät mittaukset ovat helposti saatavilla.”

Sain ulkopuolisen idean harjoitukseen ja alla on sitten itse muutamassa minuutissa hahmottelemani tehtävänanto yksinkertaisesti. Tästä siis lähdin rakentelemaan ja muuttelin sitä mukaa kun hahmottui paremmin mitä haluan tehdä:

- Kuvitteellinen puheterapeutin aputyökalu harjoituksissa/harjoittelussa käytettävien pelien/pelitietojen ja niihin liittyvien havaintojen ylläpitoon
- Viiden taulun tietokanta
- Taulujen välille sopivia relaatioita/yhteyksiä (Yksi peli voi olla valittu monelle asiakkaalle, asiakas on paikassa x jne. jne.)
- Jätetään todella viralliset ideat potilastietojärjestelmistä ja muista pois, täysin kuvitteellinen harjoitus kuvitteellisilla tiedoilla
- Mietintää tauluista, muokataan ohjelman mukana ja arviointikriteereiden (suodattimet, ManyToOne jne.) niin vaatiessa.

1. Henkilön/asiakkaan tiedot (asiakasnumero (auto id), nimi, paikannimi (yhteys paikkatietoihin), puh, sposti)
2. Paikkatiedot/käyntipaikat (päiväkoti tms.) (paikkatunniste tai automaattinen id, paikan nimi, postitoimipaikka, postinumero, lisätiedot kenttä)
3. Pelilista (yksilöivä pelitunniste, pelin nimi, KUVA pelistä sovellukseen???, lyhyt kuvauskenttä)
4. Jonkinlainen tietokenttä mihin yhdistyy ASIAKAS ja PELI eli havaintoja lyhyesti tänne esimerkiksi (asiakasid, mikä peli, peli käytössä asiakkaalla ruksitus, peli jatkoon ruksitus, peli ei jatkoon ruksitus, vapaa tietokenttä)
5. Asiakkaan KÄYTETTÄVISSÄ olevat pelit kotiharjoittelua varten EI liity puheterapeutin pelilistaan. Tällä terapeutti voi vilkaista mitä suositella kotiharjoitteluun. Eli taulussa olisi vain tietokentät missä lista peleistä: (Asiakasyhteys + käytössä olevat pelit kotona + käytössä olevat pelit päiväkoti)

### Ohjeet ja kommentit

Appi käynnistyy ilman sen kummempia asetuksia ja muutoksia. Jätin tarkoituksella H2 käyttöön ja muutamia rivejä valmista SQL dataa demotusta varten. Pääpointtina tässä on kuitenkin Vaadin Flow ja harjoitellessani tein kuitenkin testailuja muilla tietokantayhteyksillä. Eli käynnistettäessä siellä on valmista dataa, voi lisätä uutta, poistaa, muokata jne. Kaikki nollautuu taas mallidataan käynnistettäessä uudestaan. Ohjeistus tulee selkeästi kirjautumisruudussa. 
Kirjautuessa riippumatta user/user tai admin/admin olet samalla kotisivulla. User ei näe admin nappia eikä sinne ole oikeutta suoralla URLlä. Admin puolestaan näkee ja sinne pääsee.

Kotisivulla on vain tekstiä ja kuva, ei sen kummempaa toiminnallisuutta, demoa.

Asiakkaat: Listaus puheterapeutin asiakkaista yhteystietoineen ja missä kohteessa asiakas on. Lisäys/muokkaus/poisto/suodatukset. Etunimi, Sukunimi, Puhnro, Sposti ja Paikka (tulee sijainneista). Suodatus käyttäen mitä vaan tietoa, joko yksittäin tai montaa. Klikkaamalla dataa gridistä, sitä pääsee muokkaamaan täysin vapaasti. Kun mitään ei ole valittuna, pääsee lisäämään uuden asiakkaan. Sijaintitieto tulee totta kai valikosta, kohteet lisätään erikseen ja niitä käytetään asiakkaalle sitä kautta. Poistamiseen lisätty notificationlaatikko, koska relaatiot on pyritty tekemään virallisesti, eli jos poistat asiakkaan, poistuu myös hänen peliapuri ja kotipelitiedot. Hieman ajateltuna tietoturvaakin, jos asiakas ei ole enää asiakkaana, ei myöskään häneen liittyviä aputietoja ole enää käytössä. Toiminnallisuuslaatikkoon lisätty myös avustavaa infotekstiä näihin liittyen.

Sijainnit: Täysin sama idea kuin edellisessä. Lisätiedot tarkoituksella pois suodattimista, koska ne on vain täysin vapaata lisätietoa käytettävissä puheterapeutille liittyen kohteeseen. Tässäkin poistettaessa huomiota. Jos kokonainen kohde lähtee käytöstä, eihän sinne jää myöskään asiakkaita. Asiakkaalle aina luotava oma kohde, mikäli se hänellä muuttuu ja kohdetta ei ole vielä olemassa.

Pelit: Tämä on vain yksinkertainen erillisellä CardViewillä tehty listaus yrityksen omista apupeleistä. Halusin vain jotain TODELLA HIENOJA itse tehtyjä kuvia sivustolle…. Pelien infoja voi muokata, ei sen erikoisempia toimintoja. Nämä pelit liitetty sitten peliapuriin.
Peliapuri: Tämä nyt on sitten periaatteessa se puheterapeutin aputyökalu asiakkaiden kanssa. Asiakkaan etu- ja sukunimi tulee asiakastaulusta yhteen sarakkeeseen, pelin nimi yrityksen peleistä ja omina tietoina täällä onko peli käytössä, meneekö jatkoon vai ei. True/false ruksitukset ja sitten tärkeät lisähuomiot (mitä apua pelistä ollut, missä mennään jne jne.). Muuten toiminnallisuudet kuten aikaisemmin. Jätin poistamisen täältä pois, koska tietokentät voi tyhjentää ja tietenkin poistettaessa asiakas, myös kaikki häneen liittyvät peliapuritiedot poistuvat.

Kotipelit: Muisti-/apulista puheterapeutille. Jälleen asiakkaan etu- ja sukunimi asiakkaista ja sitten vain vapaita tietokenttiä, mitä pelejä käytössä kotona / päiväkodissa ja tietoa sitten niistä. Kaikki samaa kuin peliapurissa muuten.
Käyttäjät: Käyttäjienhallintaa. Huono ja epäkäytännöllinen toteutus, mutta halusin kuitenkin käyttäjäentiteetin luoda. Todella rajut ongelmat login/logoutin kanssa johti tähän ratkaisuun, että tämmöinen olisi edes periaatteen tasolla sivuilla. Yksinkertaista, kaikki uudet ovat pakotettu USER rooliin, sitä ei voi muuttaa. Oikeudet poistaa ja muokata ADMIN roolia blokattu, vaikka olisit kirjautuneena adminina.

Admin: Ei toimintoja, pelkkä viewi. Userilla ei asiaa tänne, ei edes näe palkissa. Admin näkee ja pääsee. Kirjautuminen toki tehty hölmösti kirjautumalla päälle, muutat userin.

### Vapaata pohdintaa

Ensimmäisenä sanottava, että minusta ei Vaadin-fania tullut, mutta sen ideasta ja käytöstä tuli todella hyvä kuva. Tykkäsin perinteisemmästä lähestymistavasta java/html kanssa ja todennäköisesti teenkin tämän projektin samantyylisenä myös niin, ihan oman harjoituksen vuoksi. Totta kai nimenomaan sivuston ulkonäöstä oli melko helppoa tehdä juuri sen näköinen kuin oli omassa päässään hahmotellut, siinä Vaadin loistaa. Yritin siis ottaa tämänkin niin hyvänä harjoituksena itselleni kuin mahdollista.  Copilottia käytetty apuna etenkin viewien kanssa, yksinkertaisesti säästääkseni aikaa, kun projektiin tuli käytettyä aivan älytön määrä tunteja muutenkin. Näin sain siis haluamiani palikoita paljon paremmin kasaan ilman, että selailin vaatimen omia componentlistauksia tuntitolkulla. Jonkinverran myös backendiin ja autentikointiin yritetty ongelmia sillä ratkoa, mutta tuntui, että copilot oli aika hakusessa ja suurimmat ongelmat tuli loppujenlopuksi ratkaistua itse.

## Running the application

Visual Studio Code

Open Folder in VS Code to open the project. VS Code automatically recognizes that this is a Maven-based Java project (requires Java Extension Pack). Project is now ready to run. Run Application.java.

IDE

Open the project in an IDE. You can download the [IntelliJ community edition](https://www.jetbrains.com/idea/download) if you do not have a suitable IDE already.
Once opened in the IDE, locate the `Application` class and run the main method using "Debug".

For more information on installing in various IDEs, see [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/getting-started/import).

If you install the Vaadin plugin for IntelliJ, you should instead launch the `Application` class using "Debug using HotswapAgent" to see updates in the Java code immediately reflected in the browser.

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `src/main/frontend` contains the client-side JavaScript views of your application.
- `themes` folder in `src/main/frontend` contains the custom CSS styles.
