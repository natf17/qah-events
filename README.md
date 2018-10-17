





POST /events
-- save a list of generic events



A. With translations

	 *  {
	 * 	events = [
	 *  		{
	 *  			eventType = "REG",
 	 *  			defaultDataLang = "en", (optional)
	 *  			eventStartDate = "2018-07-06",
	 *  			eventEndDate = "2018-07-08", (optional)
	 *  			eventTitle = "Be courageous",
 	 *  			eventLanguage = "sp",
 	 *  			dataLanguage = "en",
 	 *  			comments = "Caution"
 	 *  	
 	 *  			translations=[
 	 *  					{
 	 *  						eventTitle= "Sea valiente",
 	 *  						eventLanguage= "esp"
 	 *  						dataLanguage="sp",
 	 *  						comments= "Cuidado"
 	 *  					},
 	 *  					{
 	 *  						eventTitle= "Soix courageux",
 	 *  						eventLanguage= "esp"
 	 *  						dataLanguage="fr",
 	 *  						comments= "Mise en garde"
 	 * 					}
 	 *  
 	 *  			]
 	 *  		},
 	 *  
 	 *  		{ ... }
 	 *  
 	 *  	]
 	 *  
 	 *  }
 	 *  




GET /events
Params: eventLang
eventType
after
before
-- get a list of translated events that meets request params



/events?eventLang=en&lang=sp&eventType=reg&after=2018-07-01&before=2018-08-2018

Note: id represents id of event, does not take into consideration language it is in
	 * {
	 * 	events = [
	 * 		{
     	 * 			"eventLanguage": "en",
    	 * 			"dataLanguage": "sp",
     	 * 			"eventTitle": "Sea valiente!",
     	 * 			"comments": "nada",
     	 * 			"eventType": "REG",
    	 * 			"eventStartDate": "2018-07-21",
    	 * 			"eventEndDate": "2018-07-21",
	 * 			"defaultDataLang": "sp",
 	 *			"id": 434
  	 *		},
  	 *		{
  	 *			"eventLanguage": "en",
  	 *			"dataLanguage": "sp",
  	 *			"eventTitle": "Sea valiente!",
  	 *			"comments": "none",
  	 *			"eventType": "REG",
  	 *			"eventStartDate": "2018-07-22",
    	 * 			"eventEndDate": "2018-07-23",
	 * 			"defaultDataLang": "sp",
 	 *			"id": 434
  	 *		}
	 * 	]
	 * }
	 * 








DELETE /events
Params: eventLang
eventType
after
before

-- delete a range of events







GET /generic/event/{id}
 	 *  {
	 *	id = 131,
	 * 	eventType = "REG",
	 *  	eventStartDate = "2018-07-06",
	 *  	eventEndDate = "2018-07-08", (optional)
 	 *  	defaultDataLang = "en",
 	 *  	eventTranslations=[
 	 *  			{
 	 *  				eventTitle= "Sea valiente",
 	 *  				eventLanguage= "esp"
 	 *  				dataLanguage="sp",
 	 *  				comments= "Cuidado"
 	 *  			},
 	 *  			{
 	 *  				eventTitle= "Soix courageux",
 	 *  				eventLanguage= "esp"
 	 *  				dataLanguage="fr",
 	 *  				comments= "Mise en garde"
 	 * 			}
 	 *  
 	 *  	]
 	 *  
 	 *  }
 	 *  

POST /generic/event
 	 *  {
	 * 	eventType = "REG",
	 *  	eventStartDate = "2018-07-06",
	 *  	eventEndDate = "2018-07-08", (optional)
 	 *  	defaultDataLang = "en",
 	 *  	eventTranslations=[
 	 *  			{
 	 *  				eventTitle= "Sea valiente",
 	 *  				eventLanguage= "esp"
 	 *  				dataLanguage="sp",
 	 *  				comments= "Cuidado"
 	 *  			},
 	 *  			{
 	 *  				eventTitle= "Soix courageux",
 	 *  				eventLanguage= "esp"
 	 *  				dataLanguage="fr",
 	 *  				comments= "Mise en garde"
 	 * 			}
 	 *  
 	 *  	]
 	 *  
 	 *  }
 	 *  


PUT /generic/event/{id}
 	 *  {
	 * 	eventType = "REG",
	 *  	eventStartDate = "2018-07-06",
	 *  	eventEndDate = "2018-07-08", (optional)
 	 *  	defaultDataLang = "en",
 	 *  	eventTranslations=[
 	 *  			{
 	 *  				eventTitle= "Sea valiente",
 	 *  				eventLanguage= "esp"
 	 *  				dataLanguage="sp",
 	 *  				comments= "Cuidado"
 	 *  			},
 	 *  			{
 	 *  				eventTitle= "Soix courageux",
 	 *  				eventLanguage= "esp"
 	 *  				dataLanguage="fr",
 	 *  				comments= "Mise en garde"
 	 * 			}
 	 *  
 	 *  	]
 	 *  
 	 *  }
 	 *  


DELETE /generic/event/{id}


GET /translations/{id}
 	 *  {
	 *	id=2424,
 	 *  	eventTitle= "Sea valiente",
 	 *  	eventLanguage= "esp"
 	 *  	dataLanguage="sp",
 	 *  	comments= "Cuidado"
 	 *  }
 

POST /translations/event/{id} - id of event
 	 *  {
 	 *  	eventTitle= "Sea valiente",
 	 *  	eventLanguage= "esp"
 	 *  	dataLanguage="sp",
 	 *  	comments= "Cuidado"
 	 *  }
 

PUT /translations/{id}
 	 *  {
	 *	id=2424,
 	 *  	eventTitle= "Sea valiente",
 	 *  	eventLanguage= "esp"
 	 *  	dataLanguage="sp",
 	 *  	comments= "Cuidado"
 	 *  }



DELETE /translations/{id}


