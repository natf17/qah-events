										
/*
 * -  -  -  -  -  -  -  -  28 -  30 -
 * -  -  -  -  -  -  -  -  -  29 -  31
 * -  -  -  -  -  -  -  -  28 -  30 -
 * 20 -  22 -  -  -  -  -  -  -  -  -
 * -  -  -  -  -  -  -  27 -  -  30 -
 * -  -  -  -  -  -  26 -  -  -  -  31
 * 
 */

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('REG', 'en', '2020-03-28', '2020-03-30');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 0);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 0);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 0);


INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('REG', 'en', '2020-03-29', '2020-03-31');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 1);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 1);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 1);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('REG', 'en', '2020-03-28', '2020-03-30');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'sp', 'Be courageous', 'Comments', 2);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'esp', 'Sea valiente', 'Comentarios', 2);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'fr', 'Sois courageux', 'Info', 2);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('REG', 'en', '2020-03-20', '2020-03-22');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 3);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 3);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 3);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('REG', 'en', '2020-03-27', '2020-03-30');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 4);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 4);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 4);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('REG', 'en', '2020-03-26', '2020-03-31');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 5);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 5);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 5);


/*
 * Same days, but different type of event
 */

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('CA-CO', 'en', '2020-03-28', '2020-03-30');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 6);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 6);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 6);


INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('CA-CO', 'en', '2020-03-29', '2020-03-31');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 7);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 7);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 7);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('CA-CO', 'en', '2020-03-28', '2020-03-30');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'sp', 'Be courageous', 'Comments', 8);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'esp', 'Sea valiente', 'Comentarios', 8);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'fr', 'Sois courageux', 'Info', 8);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('CA-CO', 'en', '2020-03-20', '2020-03-22');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 9);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 9);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 9);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('CA-CO', 'en', '2020-03-27', '2020-03-30');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 10);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 10);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 10);

INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES ('CA-CO', 'en', '2020-03-26', '2020-03-31');
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('en', 'en', 'Be courageous', 'Comments', 11);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('sp', 'ing', 'Sea valiente', 'Comentarios', 11);
INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES ('fr', 'ang', 'Sois courageux', 'Info', 11);

