# A Custom Application for Managing AH Events

This Spring Boot application is a REST API that manages creating, updating, deleting, and retrieving events in various languages. Events fall under 2 categories: those that are generic, and those that are translated. Generic events contain the event and all translations associated with such event, while the translated event exists solely as a translated 'view' of the event. Due to the nature of the differences between these two events, each have differing CRUD capabilities, which can be accessed via different URIs, as shown below:



### `POST /events`

POST a list of generic events

**Ex:** `POST /events` 
... with request body:

```
{
	events = [
		{
			eventType = "REG",
   			defaultDataLang = "en", (optional)
   			eventStartDate = "2018-07-06",
   			eventEndDate = "2018-07-08", (optional)
   			eventTitle = "Be courageous",
   			eventLanguage = "sp",
   			dataLanguage = "en",
   			comments = "Caution"
   	
   			translations = [
   					{
   						eventTitle= "Sea valiente",
   						eventLanguage= "esp"
   						dataLanguage="sp",
   						comments= "Cuidado"
   					},
  					{
  						eventTitle= "Soix courageux",
   						eventLanguage= "esp"
   						dataLanguage="fr",
   						comments= "Mise en garde"
  					}
	   
   			]
   		},
   
   		{ ... }
 	   
   	]
   
}  
```
... will send the events for processing.



### `GET /events` 

GET a list of translated events that meets request params

Request parameters:
- eventLang
- eventType
- after
- before

**Ex:** `POST /events?eventLang=en&lang=sp&eventType=reg&after=2018-07-01&before=2018-08-2018` 

... returns: 
```
{
 	events = [
  		{
  			"eventLanguage": "en",
 			"dataLanguage": "sp",
  			"eventTitle": "Sea valiente!",
  			"comments": "nada",
  			"eventType": "REG",
 			"eventStartDate": "2018-07-21",
 			"eventEndDate": "2018-07-21",
  			"defaultDataLang": "sp",
	 		"id": 434
	 	},
	 	{
	 		"eventLanguage": "en",
	 		"dataLanguage": "sp",
	 		"eventTitle": "Sea valiente!",
	 		"comments": "none",
	 		"eventType": "REG",
	 		"eventStartDate": "2018-07-22",
 			"eventEndDate": "2018-07-23",
  			"defaultDataLang": "sp",
	 		"id": 434
	 	}
  	]
}
	  
```
Note: id represents id of event, does not reflect language of event information



### `DELETE /events`

DELETE all translated events that meet request params

Request parameters:
- eventLang
- eventType
- after
- before

**Ex:** `DELETE /events?eventLang=en&eventType=reg&after=2018-07-01&before=2018-08-08`

- Deletes all Regional events that are in English and are between July 1, 2018 and August 8, 2018
- Returns status code of 202 if delete is successful.



### `GET /generic/event/{id}`

GET the event with the given id as a generic event

**Ex:** `GET /generic/event/131`

... returns:
```
 {
 	id = 131,
  	eventType = "REG",
   	eventStartDate = "2018-07-06",
   	eventEndDate = "2018-07-08", (optional)
	defaultDataLang = "en",
 	eventTranslations=[
 	   		{
 	   			eventTitle= "Sea valiente",
 	   			eventLanguage= "esp"
 	   			dataLanguage="sp",
 	   			comments= "Cuidado"
 	   		},
 	   		{
 	   			eventTitle= "Soix courageux",
 	   			eventLanguage= "esp"
 	   			dataLanguage="fr",
 	   			comments= "Mise en garde"
 	  		}
 	   
 	]
 	   
 }
 ``` 

### `POST /generic/event`

POST a single generic event

**Ex:** `POST /generic/event` 
... with request body:
```
{
 	eventType = "REG",
	eventStartDate = "2018-07-06",
	eventEndDate = "2018-07-08", (optional)
   	defaultDataLang = "en",
 	eventTranslations=[
 	   		{
 	   			eventTitle= "Sea valiente",
 	   			eventLanguage= "esp"
 	   			dataLanguage="sp",
 	   			comments= "Cuidado"
 	   		},
 	   		{
 	   			eventTitle= "Soix courageux",
 	   			eventLanguage= "esp"
 	   			dataLanguage="fr",
 	   			comments= "Mise en garde"
 	  		}
 	   ]
 	   
}
```
... will send the event for processing.



### `PUT /generic/event/{id}`

PUT a single generic event

**Ex:** `PUT /generic/event/{id}` 
... with request body:
```
 {
 	eventType = "REG",
	eventStartDate = "2018-07-06",
	eventEndDate = "2018-07-08", (optional)
 	defaultDataLang = "en",
 	eventTranslations = [
 	   		{
 	   			eventTitle= "Sea valiente",
 	   			eventLanguage= "esp"
 	   			dataLanguage="sp",
 	   			comments= "Cuidado"
 	   		},
 	   		{
 	   			eventTitle= "Soix courageux",
 	   			eventLanguage= "esp"
 	   			dataLanguage="fr",
 	   			comments= "Mise en garde"
 	  		}
	]
 	   
 }
 ```  
... updates the event with the given id, completely replacing the entity in the repository with the request body



### `DELETE /generic/event/{id}`

DELETE the generic event with the given id

**Ex:** `DELETE /generic/event/233`

- Delete the event with id 233
- Returns status code of 202 if delete is successful.



###`GET /translations/{id}`
```
 	{
	 	id=2424,
 	   	eventTitle= "Sea valiente",
 	   	eventLanguage= "esp",
 	   	dataLanguage="sp",
 	   	comments= "Cuidado"
 	}
``` 

###`POST /translations/event/{id}` - id of event

```
 	{
 	  	eventTitle= "Sea valiente",
 	  	eventLanguage= "esp",
 	  	dataLanguage="sp",
 	  	comments= "Cuidado"
 	}
 ```

###`PUT /translations/{id}`

```
 	{
	 	id=2424,
 	  	eventTitle= "Sea valiente",
 	   	eventLanguage= "esp",
 	   	dataLanguage="sp",
 	   	comments= "Cuidado"
 	}
```


###`DELETE /translations/{id}`


