/*                         Events
 * ------------------------------------------------------------------------------|
 * |  id  |  eventType  |  defaultDataLang  |  eventStartDate  |  eventEndDate   |
 * |-----------------------------------------------------------|-----------------|
 * | 4324 |     REG     |        eng        |   20180708  	   |	 20180708	 |
 * |___________________________________________________________|_________________|
 * 
 */



CREATE TABLE Events(
					id 		  			BIGINT 		  NOT NULL		IDENTITY, 
					eventType 			VARCHAR(10)   NOT NULL,
					defaultDataLang     VARCHAR(10)   NOT NULL,
					eventStartDate      date 		  NOT NULL,
					eventEndDate		date		  NOT NULL
					
					
					);
					
		
					
					
					
									
									
									
									
									
									
									
									
									
/*             				EventTranslations
 * -----------------------------------------------------------------------------------------|
 * |  id  |  dataLang  |  eventLang  |     eventTitle      |     comments      |   Events   |
 * |----------------------------------------------------------------------------------------|
 * | 284 |     "eng"   |    "eng"    |   "Be courageous"   | "This is a coment"|    4324    |    
 * |----------------------------------------------------------------------------------------|
 * | 289 |      "sp"   |    "ing"    |   "Sea valiente"    | "Esto es un comm" |    4324    | 
 * |________________________________________________________________________________________|
 * 
 */	
									
CREATE TABLE EventTranslations(
								id 					BIGINT 			NOT NULL		IDENTITY, 
								dataLang			VARCHAR(10)		NOT NULL,
								eventLang 			VARCHAR(10) 	NOT NULL,
								eventTitle 			VARCHAR(40) 	NOT NULL,
								comments 			VARCHAR(100),
								Events				BIGINT			NOT NULL,
								
								FOREIGN KEY(Events) 			REFERENCES Events(id) 					ON DELETE CASCADE
								);
								
								
								
								
								
								


								
								
								
								
								
								