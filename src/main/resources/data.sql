insert into sijainti(version, id,paikka,postitoimipaikka,postinumero,lisatiedot) values (1, 1,'Kpojeepäiväkoti','kuopio','70100','vasemmalla ja oikealla')
insert into sijainti(version, id,paikka,postitoimipaikka,postinumero,lisatiedot) values (1, 2,'Nekalamesta','tampere','33100','älä jätä autoa oven viereen')
insert into sijainti(version, id,paikka,postitoimipaikka,postinumero,lisatiedot) values (1, 3,'Stadinriiviöt','helsinki','00100','älä mene ennen klo 13')
insert into sijainti(version, id,paikka,postitoimipaikka,postinumero,lisatiedot) values (1, 4,'Savonlinnanoopperatarha','savonlinna','57100','pelottava paikka, varovasti')
insert into sijainti(version, id,paikka,postitoimipaikka,postinumero,lisatiedot) values (1, 5,'JNSPäiväkoti','joensuu','80100','uusi kohde, ei vielä kokemuksia')
insert into asiakas(version, id,etunimi,sukunimi,puhnro,sposti,sijainti_id) values (1, 1,'Timo','Testaaja','0501234567','testi@testi.fi',1)
insert into asiakas(version, id,etunimi,sukunimi,puhnro,sposti,sijainti_id) values (1, 2,'Jorkki','Testailija','0441234567','testi@testi.com',2)
insert into asiakas(version, id,etunimi,sukunimi,puhnro,sposti,sijainti_id) values (1, 3,'Ritva','Testinen','0401234567','testi@testi.net',3)
insert into asiakas(version, id,etunimi,sukunimi,puhnro,sposti,sijainti_id) values (1, 4,'Liisa','Ihmemaa','0507654321','testi@testaaja.net',4)
insert into asiakas(version, id,etunimi,sukunimi,puhnro,sposti,sijainti_id) values (1, 5,'Keijo','Keijolainen','0447654321','keijo@testi.fi',5)
insert into pelit(version, id, pelititle, pelisubtitle, pelidesctext, pelibadgeText, peliurl) values (1, 1, 'LISKOSILMÄPELI', 'Löydä liskon silmät', 'Hyvä peli ja opettaa nönnönnöö...', 'Menossa testiin Korvatunturille ja Juupajoelle. Hyvä siihen ja tuohon.', 'images/peli1.png')
insert into pelit(version, id, pelititle, pelisubtitle, pelidesctext, pelibadgeText, peliurl) values (1, 2, 'AVARUUDENIHME', 'Avaruus sanoina', 'Tosihyvä peli ja opettaa nönnönnöö...', 'Ei ole oikein toiminut, pitää kokeilla vanhempien kanssa. Minusta tässä on potentiaalia.', 'images/peli2.png')
insert into pelit(version, id, pelititle, pelisubtitle, pelidesctext, pelibadgeText, peliurl) values (1, 3, 'SÄÄPELI', 'Sääarvailu', 'Todella opettavainen joo.', 'Ei vielä kokemuksia.', 'images/peli3.png')
insert into pelit(version, id, pelititle, pelisubtitle, pelidesctext, pelibadgeText, peliurl) values (1, 4, 'LISKOSILMÄPELI2', 'Löydä liskon silmät TAAS', 'Hyvä peli ja opettaa jeejeejee...', 'Huomattavasti parempi pienimmille asiakkaille, kuin liskosilmäpeli ykkönen!', 'images/peli4.png')
insert into pelit(version, id, pelititle, pelisubtitle, pelidesctext, pelibadgeText, peliurl) values (1, 5, 'KENEN AURINKOLASIT', 'Arvaa aurinkolasien perusteella', 'Hyvä peli mutta ei opeta mitään.', '...', 'images/peli5.png')
insert into pelit(version, id, pelititle, pelisubtitle, pelidesctext, pelibadgeText, peliurl) values (1, 6, 'YSTÄVÄPELI', 'Ystävyyden loppu', 'Brutaali tapa opettaa yhtään mitään.', 'Ei lähde ei sitten yhtään', 'images/peli6.png')
insert into peliapuri(version, id,asiakas_id,pelit_id,kaytossa,jatkoon,eijatkoon,huomiot) values (1, 1, 1, 1,false,true,false,'Huomioita huomion perään. Huomioita huomion perään. Huomioita huomion perään. Huomioita huomion perään.')
insert into peliapuri(version, id,asiakas_id,pelit_id,kaytossa,jatkoon,eijatkoon,huomiot) values (1, 2, 2, 2,true,false,true,'Huomioita huomion perään.')
insert into peliapuri(version, id,asiakas_id,pelit_id,kaytossa,jatkoon,eijatkoon,huomiot) values (1, 3, 3, 3,false,false,false,'Huomioita huomion perään. Huomioita huomion perään. Huomioita huomion perään. Huomioita huomion perään. Huomioita huomion perään. Huomioita huomion perään.')
insert into peliapuri(version, id,asiakas_id,pelit_id,kaytossa,jatkoon,eijatkoon,huomiot) values (1, 4, 4, 4,true,false,true,'Ei mitään erikoista huomiota.')
insert into peliapuri(version, id,asiakas_id,pelit_id,kaytossa,jatkoon,eijatkoon,huomiot) values (1, 5, 5, 5,false,true,false,'TODO: täydennä huomiot')
insert into kotipelit(version, id,asiakas_id,pelitkotona,pelitpvkoti,tietoja) values (1, 2, 1,'Tähtien afrikka, Shakki, Alias','Tipupeli','Timolla menossa kolmen viikon Aliassetitys kotona.')
insert into kotipelit(version, id,asiakas_id,pelitkotona,pelitpvkoti,tietoja) values (1, 3, 2,'Pasianssi, Alias','Tipupeli, matopeli','Ei kotitehtäviä toistaiseksi.')
insert into kotipelit(version, id,asiakas_id,pelitkotona,pelitpvkoti,tietoja) values (1, 4, 3,'Matopeli','-','Uusi asiakas, päivitetään kokemukset myöhemmin.')
insert into kotipelit(version, id,asiakas_id,pelitkotona,pelitpvkoti,tietoja) values (1, 5, 4,'Shakki, Alias, ArvaaKuka','Scrabble','Todella hyvää kehitystä arvaa kuka pelin kanssa, treenannut kotona sisarusten kanssa.')
insert into kayttaja(version, id,kayttajanimi,rooli,tehtava) values (1, 1, 'user','USER','peruskäyttäjä')
insert into kayttaja(version, id,kayttajanimi,rooli,tehtava) values (1, 2, 'admin','ADMIN','ylläpito')
insert into kayttaja(version, id,kayttajanimi,rooli,tehtava) values (1, 3, 'Ritva','USER','puheterapeutti1')
insert into kayttaja(version, id,kayttajanimi,rooli,tehtava) values (1, 4, 'Kutjake','USER','peruskayttaja2')